/**
 **************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.storage.filesystem;


import org.apache.commons.logging.Log;

import java.io.File;


import java.util.List;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Query;
import java.util.ArrayList;


import java.io.IOException;


import javax.jcr.*;

import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeManagerImpl;
import org.exoplatform.services.jcr.impl.storage.filesystem.nodedata.NodeContainer;
import org.exoplatform.services.jcr.impl.storage.filesystem.nodedata.NodeContainerResolver;
import org.exoplatform.services.jcr.storage.Container;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.log.LogUtil;


/**
 * Created by The eXo Platform SARL        .
 * 1. Dir is node of name=folder_name, path=relative path
 *    type nt:folder lastModified=created=File.lastModified() jcr:UUID=canonical_file_path
 * 2. File is node of name=file_name, path=relative_path
 *    jcr:primaryType=nt:file jcr:lastModified=jcr:created=File.lastModified()
 *    jcr:content=file_content jcr:UUID=canonical_file_path
 * 3. File CAN contain node of other type if:
 * - it is xml file and has extension .xml and xml def (starts with <?xml....
 * - it has format of serialized node ( <sv:node ...) Othervice content of file is jcr:content Note:
 * ???? by default (2) file extension is ignored ????
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SimpleFsContainerImpl.java,v 1.23 2004/11/02 18:34:39 geaz Exp $
 */
public class SimpleFsContainerImpl implements WorkspaceContainer {

  private static long id = System.currentTimeMillis();
  private static NodeContainerResolver resolver = new NodeContainerResolver();
  private Log log;
  private File rootFile;
  private NodeImpl rootNode;
  private String name;


  public SimpleFsContainerImpl(String name, String rootPath) throws RepositoryException {
    log = LogUtil.getLog("org.exoplatform.services.jcr");
    rootFile = new File(rootPath);
    if (!rootFile.exists())
      rootFile.mkdirs();

    String rootType = "nt:folder";
    if (!rootFile.isDirectory())
      rootType = "nt:file";
    if (!rootFile.exists())
      throw new RepositoryException("SimpleFsContainerImpl could not be created. File or folder '" + rootFile + "' not found.");
    ArrayList props = new ArrayList();
    props.add(new PropertyImpl("/jcr:primaryType", new StringValue(rootType), PropertyType.STRING));
    rootNode = new NodeImpl(ROOT_PATH, props);
    //log.debug("FS ContainerImpl() Root "+rootNode.getPrimaryNodeType());
  }


   public String getName() {
    return name;
  }

  public File getRootFile() {
    return rootFile;
  }

  public NodeData getRootNode() {
    return rootNode;
  }

  public NodeData getNodeByPath(String absPath) {
    try {
      NodeContainer container = resolver.resolve(rootFile, absPath);

//      System.out.println("Container ---"+container+" "+rootFile.getCanonicalPath()+" "+absPath);
      return container.getNodeByPath(container.parseRelPath(absPath));
    } catch (Exception e) {
      log.debug("GetNodeByPath throws exception " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
/*
  public NodeData getNodeById(String UUID) {

    log.debug("FS Container getByUUID path --- "+new String(Base64.decodeBase64(UUID.getBytes()))+"/jcr:content");
    return getNodeByPath( new String(Base64.decodeBase64(UUID.getBytes()))+"/jcr:content");
//    throw new RuntimeException("getNodeById() is not supported by the container implementation!");
  }
*/
  public List getChildren(String path) {
    try {
      NodeContainer container = resolver.resolve(rootFile, path);
      return container.getChildren(container.parseRelPath(path));
    } catch (Exception e) {
      log.debug("SimpleFSContainer.GetChildren() throws exception " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public QueryResult getQueryResult(Query query) {
    return null;
  }

  synchronized public void add(Node node) throws ItemExistsException, RepositoryException {
    try {
      NodeContainer container = resolver.create(rootFile, (NodeData)node);
      log.debug("SFS Container add -->" + container);
      container.add(node);
    } catch (IOException e) {
      throw new RepositoryException("SimpleFsContainerImpl.add() failed. Reason:" + e);
    } catch (PathNotFoundException e) {
      throw new RepositoryException("SimpleFsContainerImpl.add() failed. Reason:" + e);
    }
  }

  synchronized public void update(Node node) throws RepositoryException {
    try {      
      NodeContainer container = resolver.resolve(rootFile, node.getPath());
      container.update(node);
    } catch (Exception e) {
      log.debug("Update Node throws exception " + e.getMessage());
    }
  }

  synchronized public void delete(String absPath) {
    try {
      NodeContainer container = resolver.resolve(rootFile, absPath);
      log.debug("FS Container delete node:" + absPath);
      container.delete(absPath);
    } catch (Exception e) {
      log.debug("DelNode throws exception " + e.getMessage());
    }
  }


}
