/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.inmemory;


import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.jcr.util.PathUtil;
import org.exoplatform.services.log.LogUtil;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import java.util.*;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: ContainerImpl.java,v 1.27 2004/09/16 15:26:54 geaz Exp $
 */

public class ContainerImpl implements WorkspaceContainer {

  private Log log;

  private Map workspace;
  private String name;

  public ContainerImpl(String name) throws RepositoryException {
    this(name, "nt:default");
  }

  public ContainerImpl(String name, String rootNodeType) throws RepositoryException {
    log = LogUtil.getLog("org.exoplatform.services.jcr");
    log.debug("inmemory ContainerImpl(), root " + rootNodeType);
    this.name = name;
    workspace = WorkspaceContainerRegistry.getInstance().getWorkspaceContainer(name, rootNodeType);

    workspace.put(ROOT_PATH, new NodeImpl(ROOT_PATH, rootNodeType));

  }

  public String getName() {
    return name;
  }


  public NodeData getRootNode() {

    // Copy of permanent node
    try {
       return (NodeData) new NodeImpl( (NodeImpl) workspace.get(ROOT_PATH) );
    } catch (Exception e) {
       log.error("getRootNode() failed: "+ e.getMessage());
       return null;
    }

  }


  public NodeData getNodeByPath(String absPath) {

    log.debug("Inmemory Container looks up node : " + absPath + " in workspace container: "+name);
    Object obj = workspace.get(absPath);

    if(obj == null)
        return null;

     // Copy of permanent node
     try {
        return new NodeImpl((NodeImpl) obj);
     } catch (Exception e) {
        log.error("getNodeByPath() failed: <" + absPath + "> " + e.getMessage());
     }

    return null;
  }


  public List getChildren(String path) {
    Collection keys = workspace.keySet();
    Iterator it = keys.iterator();
    ArrayList children = new ArrayList();
    while (it.hasNext()) {
      String curPath = (String) it.next();
      if (PathUtil.isDescendant(curPath, path, true)) {
          children.add(curPath);
      }
    }
    return children;
  }

  public QueryResult getQueryResult(Query query) {
    return null;
  }

  synchronized public void add(Node node) throws RepositoryException {

    if (node.getPath().equals("/"))
      return;

    if (workspace.get(node.getPath()) != null)
      throw new ItemExistsException("Container.add(NodeImpl node) item <" + node.getPath() + "> already exists!");

    NodeImpl newNode = new NodeImpl((NodeImpl) node);
    workspace.put(newNode.getPath(), newNode);

    log.debug("InmemoryContainer added " + node +" to workspace container: "+name);
  }

  synchronized public void delete(String absPath) {
    workspace.remove(absPath);
    log.debug("InmemoryContainer removed " + absPath);
  }


  synchronized public void update(Node node) throws RepositoryException {

    if (workspace.get(node.getPath()) == null)
      return;

    NodeImpl newNode = new NodeImpl((NodeImpl) node);
    workspace.put(node.getPath(), newNode);
    log.debug("InmemoryContainer updated " + newNode);
  }

}
