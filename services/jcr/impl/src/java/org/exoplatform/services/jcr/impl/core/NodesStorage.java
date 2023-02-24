/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodesStorage.java,v 1.6 2004/11/02 18:36:33 geaz Exp $
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;
import org.exoplatform.services.jcr.storage.RepositoryManager;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.jcr.util.PathUtil;
import org.exoplatform.services.log.LogUtil;

public class NodesStorage {

    // path -> NodeChange
    private Map nodeChanges;
    // UUID -> NodeChange (reference to nodeChanges' item)
    private Map nodeReferences;
    private RepositoryManager repositoryManager;
    private WorkspaceContainer workspaceContainer;
    protected Log log;

    NodesStorage(RepositoryManager repositoryManager, WorkspaceContainer workspaceContainer) {
      nodeChanges = new HashMap();
      nodeReferences = new HashMap();
      this.repositoryManager = repositoryManager;
      this.workspaceContainer = workspaceContainer;
      log = LogUtil.getLog("org.exoplatform.services.jcr");
    }

    void add(NodeImpl node) {

        NodeChange change = new NodeChangeImpl(node, NodeChangeState.ADDED, node.getPath());
        if(node != null)
          nodeChanges.put(change.getPath(), change);

       // If there is UUID add change to the nodeReferences as well
       String uuid;
       try {
          uuid = node.getUUID();
          nodeReferences.put(uuid, change);
       } catch (Exception e) {
       }

    }

    void addReference(String relatedUUID, String refPath) {
       log.debug("NodesStorage.addReference() "+refPath+" added to "+relatedUUID);
       // Is there such node?
       NodeChange change = (NodeChange)nodeReferences.get(relatedUUID);
       if(change == null) {

          try {            
             ReferenceableNodeLocation loc = repositoryManager.getLocationByUUID(workspaceContainer.getName(), relatedUUID);
             loc.addReferencedPath(refPath);
             // Find node from workspaceContainer by realPath
             // Unchanged or we need REF_ADDED?
             change = new NodeChangeImpl(workspaceContainer.getNodeByPath(loc.getRealPath()), NodeChangeState.REF_ADDED, refPath);
          } catch (PathNotFoundException e) {
            log.error("NodesStorage.addReference node for "+relatedUUID+"('"+refPath+"') not found "+change);
            return;
          } catch (RepositoryException e) {
            e.printStackTrace();
            throw new RuntimeException("NodesStorage.addReference node for "+relatedUUID+" FAILED "+e);
          }
       }
       nodeChanges.put(refPath, change);
       nodeReferences.put(relatedUUID, change);

    }

    NodeChange getNodeChangeByPath(String path) {
        return (NodeChange)nodeChanges.get(path);
    }

    NodeChange getNodeChangeByUUID(String UUID) {
        return (NodeChange)nodeReferences.get(UUID);
    }

    void remove(String path) {
        // try to remove from references as well
        NodeChange change = (NodeChange)nodeChanges.get(path);
        log.debug("NodesStorage.remove "+change);
        if(change != null) {
          try {
            String uuid = change.getNode().getUUID();
            nodeReferences.remove(uuid);
          } catch (Exception e) {
            // just not referenceable node
          }
        }
        nodeChanges.remove(path);
    }

    void setState(String path, int state) {
        NodeChange change = (NodeChange)nodeChanges.get(path);
        if(change != null)
         change.setState(state);
    }

    Set getKeys() {
        return nodeChanges.keySet();
    }


  NodeData getNodeByUUID(String UUID) {

    // try to get node from storage's ref by UUID
    NodeChange nc = (NodeChange)nodeReferences.get(UUID);
    if(nc != null) {
//        if(nc.getState() == NodeChangeState.DELETED)
//           return null;
        if(nc.getState() != NodeChangeState.UNCHANGED)
           return nc.getNode();
        else {  // UNCHANGED
           try {
             NodeData temp = workspaceContainer.getNodeByPath(nc.getPath());
             return nc.refreshNode(temp);
           } catch (Exception e) {
             e.printStackTrace();
             log.error("NodesStorage.getNodeByUUID can't refresh "+nc.getNode()+" NULL is returned!!!! Reason: "+e.getMessage());
             return null;
           }
        }
    }

    // try to get node from workspace container
    // try to get node location from repository ref by UUID
    ReferenceableNodeLocation loc;
    try {
        loc = repositoryManager.getLocationByUUID(workspaceContainer.getName(), UUID);
    } catch (PathNotFoundException e) {
        log.debug("NodesStorage.getNodeByUUID: UUID '"+UUID+"' not found in RepositoryManager");
        return null;
    }

    // find in workspace container by realPath
    NodeData temp = null;
    try {
       temp = workspaceContainer.getNodeByPath(loc.getRealPath());
    } catch (RepositoryException e) {
       e.printStackTrace();
       throw new RuntimeException("NodesStorage.getNodeByPath() for "+loc.getRealPath()+" FAILED "+e);
    }

    if (temp == null) {
      log.error("NodesStorage.getNodeByUUID: UUID '"+UUID+"' not found in "+workspaceContainer.getName()+"probably error in container/repository impl!!!");
      return null;
    }
    // Should we add to nodeChanges as well ??
    nc = new NodeChangeImpl(temp, NodeChangeState.UNCHANGED, temp.getPath());
    nodeChanges.put(loc.getRealPath(), nc);
    nodeReferences.put(UUID, nc);
    return nc.getNode();

  }

  NodeData getNodeByPath(String absPath) {
    // try  get From changes
    NodeChange nc = (NodeChange)nodeChanges.get(absPath);
    log.debug("NodesStorage.getNodeByPath(" + absPath + ") NodeChange :"+nc);

    if (nc != null) {
        // The Same codes as in getByUUID!!!
//        if(nc.getState() == NodeChangeState.DELETED)
//           return null;

        if(nc.getState() != NodeChangeState.UNCHANGED)
           return nc.getNode();
        else {  // UNCHANGED
           try {

              NodeData temp = workspaceContainer.getNodeByPath(nc.getPath());
              return nc.refreshNode(temp);
           } catch (Exception e) {
             e.printStackTrace();
             log.error("NodesStorage.getNodeByPath can't refresh "+nc.getNode()+" NULL is returned!!!! Reason: "+e.getMessage());
             return null;
           }
        }
    }

    //try from workspaceManager by absPath
    NodeImpl temp = null;

    try {
       temp = (NodeImpl)workspaceContainer.getNodeByPath(absPath);

    } catch (RepositoryException e) {
       e.printStackTrace();
       throw new RuntimeException("NodesStorage.getNodeByPath() for "+absPath+" FAILED "+e);
    }

    if(temp != null) {
       nc = new NodeChangeImpl(temp, NodeChangeState.UNCHANGED, temp.getPath());
       log.debug("Node "+temp+" added to changes from container" );
       nodeChanges.put(absPath, nc);
       try {
          String uuid = nc.getNode().getUUID();
          nodeReferences.put(uuid, nc);
       } catch (Exception e) {
          // just not referenceable node
       }
       return nc.getNode();
    }


    // try from repository references
    ReferenceableNodeLocation loc = null;
    try {
        loc = repositoryManager.getLocationByPath(workspaceContainer.getName(), absPath);
    } catch (PathNotFoundException e) {
        log.debug("NodesStorage.getNodeByPath: Path '"+absPath+"' not found in RepositoryManager");
//        return null;
    }


    // find in workspace container by realPath
    if(loc != null) {
       try {
          temp = (NodeImpl)workspaceContainer.getNodeByPath(loc.getRealPath());
       } catch (RepositoryException e) {
          e.printStackTrace();
          throw new RuntimeException("NodesStorage.getNodeByPath() for "+absPath+" FAILED "+e);
       }
       if (temp == null) {
         log.debug("NodesStorage.getNodeByPath: Path '"+loc.getRealPath()+"' not found in "+workspaceContainer.getName());
         return null;
       }

       nc = new NodeChangeImpl(temp, NodeChangeState.UNCHANGED, temp.getPath());
       log.debug("Node "+temp+" added to changes from container" );
       nodeChanges.put(absPath, nc);
       try {
          String uuid = nc.getNode().getUUID();
          nodeReferences.put(uuid, nc);
       } catch (Exception e) {
          // just not referenceable node
       }
       return nc.getNode();
    } else
       return null;


  }

  Set getChildren(String absPath) {

    Set children = new HashSet();
    List changes = getChildrenChanges(absPath, true);
    for(int i=0; i<changes.size(); i++) {
       NodeImpl node =(NodeImpl)((NodeChange)changes.get(i)).getNode();
       String path = node.getPath();
//       log.debug("NodesStorage getChild paths of " + absPath + "  from changes. ="+path);
       children.add(path);
    }

    try {
       List nodes = workspaceContainer.getChildren(absPath);
       for(int i=0; i<nodes.size(); i++) {
//      log.debug("NodesStorage getChild paths of " + absPath + "  from workspace container ="+nodes.get(i));
       children.add((String)nodes.get(i));
       }

    } catch (RepositoryException e) {
       e.printStackTrace();
       throw new RuntimeException("NodesStorage.getChildren for "+absPath+" FAILED "+e);
    }

    return children;
  }

/////////////////////////////////////////////

  List getChildrenChanges(String path, boolean shallow) {
    ArrayList list = new ArrayList();
    Iterator entries = nodeChanges.keySet().iterator();
    while (entries.hasNext()) {
      String testPath = (String) entries.next();
      if (PathUtil.isDescendant(testPath, path, shallow)) {
        NodeImpl node = (NodeImpl)((NodeChange)nodeChanges.get(testPath)).getNode();
//        if(
        log.debug("NodesStorage.getChildrenChanges for " +path+ "=" + testPath+" "+node);
        list.add(nodeChanges.get(testPath));
      }
    }
    return list;
  }

  // Returns nodes
  Set getChangedReferencedPaths(String uuid) {
    Set list = new HashSet();
    if(!nodeReferences.containsKey(uuid))
       return list;

    NodeImpl node = (NodeImpl)((NodeChange)nodeReferences.get(uuid)).getNode();
    Iterator entries = nodeChanges.values().iterator();
    while (entries.hasNext()) {
      NodeImpl testNode = (NodeImpl)((NodeChange)entries.next()).getNode();
      if(testNode.equals(node)) {
         list.add(testNode.getPath());
      }
    }
    return list;
  }

}
