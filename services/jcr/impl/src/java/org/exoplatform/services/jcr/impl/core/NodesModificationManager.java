/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.core;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.DateValue;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeDef;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.core.NodeChange;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;
import org.exoplatform.services.jcr.storage.RepositoryManager;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.jcr.util.PathUtil;
import org.exoplatform.services.log.LogUtil;



/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodesModificationManager.java,v 1.48 2004/11/02 18:36:33 geaz Exp $
 */
public class NodesModificationManager {

    NodesStorage nodesStorage;
    private Map validationErrors;
    protected Log log;
    private TicketImpl ticket;

    public NodesModificationManager(TicketImpl ticket) throws RepositoryException {
        nodesStorage = new NodesStorage(((RepositoryImpl)ticket.getRepository()).getRepositoryManager(),
            ticket.getContainer());
        this.ticket = ticket;
        log = LogUtil.getLog("org.exoplatform.services.jcr");
        validationErrors = new HashMap();
    }

    synchronized public void delete(NodeImpl item) {
        NodeImpl node = (NodeImpl)item;
        // i.e. not in Changes yet
        if (getState(node) == NodeChangeState.UNDEFINED) {
            node = (NodeImpl)nodesStorage.getNodeByPath(item.getPath());
        }
        log.debug("NodesModificationManager is marking to DELETE " + node + " with old state " +
            NodeChangeState.getStateName(getState(node)));
        nodesStorage.setState(node.getPath(), NodeChangeState.DELETED);
    }

    synchronized public void add(NodeImpl node) {
        log.debug("NodesModificationManager is ADDING " + node + " to the change log.");
        nodesStorage.add(node);
    }

    synchronized public void addReference(String relatedUUID, String refPath) throws PathNotFoundException, RepositoryException {
        log.debug("NodesModificationManager is ADDING REFERENCE " + refPath + " to the "+relatedUUID);
        nodesStorage.addReference(relatedUUID, refPath);
    }

    synchronized public void update(NodeImpl node) {
        log.debug("NodesModificationManager is marking to UPDATE " + node + " with old state " +
            NodeChangeState.getStateName(getState(node)));

        if (getState(node) == NodeChangeState.UNCHANGED) {
            log.debug("Node modification manager update UNCHANGED node: " + node);
            nodesStorage.setState(node.getPath(), NodeChangeState.UPDATED);
        }
    }

    synchronized public void commit(NodeData node, boolean shallow) throws RepositoryException {

        NodeChange entry = nodesStorage.getNodeChangeByPath(node.getPath());

        log.debug("NodesModificationManager is COMMITTING (shallow = " + shallow + ") " + entry );
        if (entry != null) {
            if (entry.getState() == NodeChangeState.DELETED) {
                commitDeletedNode(entry);
            } else if (entry.getState() == NodeChangeState.ADDED) {
                commitAddedNode(entry);
            } else if (entry.getState() == NodeChangeState.UPDATED) {
                commitUpdatedNode(entry);
            } else if (entry.getState() == NodeChangeState.REF_ADDED) {
                commitRefAddedNode(entry);
            }
        }
 
        List children = nodesStorage.getChildrenChanges(node.getPath(), shallow);
        Collections.sort(children, new PathComparator());
        if (!shallow) {
            for (int i = 0; i < children.size(); i++) {
                NodeChange childChange = (NodeChange)children.get(i);
                Node childNode = childChange.getNode();
                log.debug("NodesModificationManager is COMMITTING child " + childNode + " current state " +
                    NodeChangeState.getStateName(childChange.getState()));
                if (childChange.getState() == NodeChangeState.DELETED) {
                    commitDeletedNode(childChange);
                } else if (childChange.getState() == NodeChangeState.ADDED) {
                    commitAddedNode(childChange);
                } else if (childChange.getState() == NodeChangeState.UPDATED) {
                    commitUpdatedNode(childChange);
                } else if (childChange.getState() == NodeChangeState.REF_ADDED) {
                    commitRefAddedNode(childChange);
                }
            }
        } else {

            for (int i = 0; i < children.size(); i++) {
                NodeChange childChange = (NodeChange)children.get(i);
                Node childNode = childChange.getNode();
                log.debug("NodesModificationManager is COMMITTING child " + childNode + " current state " +
                    NodeChangeState.getStateName(childChange.getState()));
                if (childChange.getState() == NodeChangeState.DELETED) {
                    commitDeletedNode(childChange);

            }
          }
       }
    }

    private void commitDeletedNode(NodeChange nodeChange) throws RepositoryException {
        NodeData node = nodeChange.getNode();
        List children = new ArrayList();
        getAllDescendants((NodeImpl)node, children);
        for (int i = 0; i < children.size(); i++) {
            NodeData childNode = (NodeData)children.get(i);
            log.debug("NodesModificationManager is Committing deleted child " + childNode);
            getWorkspaceContainer().delete(childNode.getPath());
            getRepositoryManager().deleteLocationByPath(getWorkspaceContainer().getName(), childNode.getPath());
            nodesStorage.remove(childNode.getPath());
        }

        log.debug("NodesModificationManager COMMITTED (recursively) deleted " + node + " to persist. ");
    }

    private void commitAddedNode(NodeChange nodeChange) throws RepositoryException {
        NodeData node = nodeChange.getNode();
        addNode(nodeChange);
        Property uuid = node.getPermanentProperty("jcr:uuid");
        if (uuid != null) {
            getRepositoryManager().addLocation(getWorkspaceContainer().getName(), uuid.getString(), node.getPath(), true);
            // Add this path as referenceable as well
            ReferenceableNodeLocation loc = getRepositoryManager().getLocationByUUID(getWorkspaceContainer().getName(), uuid.getString());
            loc.addReferencedPath(node.getPath());
        }
        nodesStorage.setState(node.getPath(), NodeChangeState.UNCHANGED);
        log.debug("NodesModificationManager COMMITTED added " + node + " to persist. ");
    }

    private void commitRefAddedNode(NodeChange nodeChange) throws RepositoryException {

        NodeData node = nodeChange.getNode();

        try {
            String uuid = node.getUUID();
            getRepositoryManager().addLocation(getWorkspaceContainer().getName(), uuid, nodeChange.getPath(), false);
        } catch (Exception e) {
            nodesStorage.remove(node.getPath());
            throw new RepositoryException("NodesModificationManager.commitRefAddedNode failed. Reason: "+e);
        }

        nodesStorage.setState(node.getPath(), NodeChangeState.UNCHANGED);
        log.debug("NodesModificationManager COMMITTED reference added " + node + " to persist. ");
    }


    private void commitUpdatedNode(NodeChange nodeChange) throws RepositoryException {
        NodeData node = nodeChange.getNode();
        if (node.getPermanentProperty("jcr:lastModified") != null) {
            updateTime(node, "jcr:lastModified");
        }
        log.debug("Commit updated node " + node);
        getWorkspaceContainer().update(node);
        nodesStorage.setState(node.getPath(), NodeChangeState.UNCHANGED);
        log.debug("NodesModificationManager COMMITTED updated " + node + " to persist. ");
    }

    synchronized public void rollback(NodeImpl node) {
        validationErrors.clear();
        NodeChange entry = nodesStorage.getNodeChangeByPath(node.getPath());
        if (entry == null || entry.getState() == NodeChangeState.UNCHANGED) {
            List children = nodesStorage.getChildrenChanges(node.getPath(), false);
            for (int i = 0; i < children.size(); i++) {
                NodeChange childChange = (NodeChange)children.get(i);
                NodeData childNode = childChange.getNode();
                if (childChange.getState() == NodeChangeState.ADDED) {
                    nodesStorage.remove(childNode.getPath());
                } else {
                    nodesStorage.setState(childNode.getPath(), NodeChangeState.UNCHANGED);
                }
            }
            log.debug("NodesModificationManager ROLLED BACK children of unchanged " + node + " to persist. ");
            return;
        }
        if (entry.getState() == NodeChangeState.DELETED) {
            List children = nodesStorage.getChildrenChanges(node.getPath(), false);
            for (int i = 0; i < children.size(); i++) {
                NodeData childNode = ((NodeChange)children.get(i)).getNode();
                nodesStorage.remove(childNode.getPath());
            }
            nodesStorage.remove(node.getPath());
            log.debug("NodesModificationManager ROLLED BACK deleted " + node + " to persist. ");
        } else if (entry.getState() == NodeChangeState.ADDED) {
            //      log.debug("Rollback added node " + node);
            List children = nodesStorage.getChildrenChanges(node.getPath(), false);
            Collections.reverse(children);
            nodesStorage.remove(node.getPath());
            for (int i = 0; i < children.size(); i++) {
                NodeData childNode = ((NodeChange)children.get(i)).getNode();
                nodesStorage.remove(childNode.getPath());
            }
            log.debug("NodesModificationManager ROLLED BACK added " + node + " to persist. ");
        } else if (entry.getState() == NodeChangeState.UPDATED) {
            nodesStorage.setState(node.getPath(), NodeChangeState.UNCHANGED);
            log.debug("NodesModificationManager ROLLED BACK updated " + node + " to persist. ");
        }

    }


    // for testing only
    NodeChange getNodeChange(String path) {
        return nodesStorage.getNodeChangeByPath(path);
    }

    private int getState(NodeImpl node) {
        NodeChange entry = nodesStorage.getNodeChangeByPath(node.getPath());
        if (entry == null)
            return NodeChangeState.UNDEFINED;
        else
            return entry.getState();
    }

    private void addNode(NodeChange nodeChange) throws RepositoryException, ItemExistsException {
        try {
            NodeData node = nodeChange.getNode();
            ItemLocation loc = null;
            Property property = null;
            if (node.getPermanentProperty("jcr:uuid") != null) {

                updateUUID(node);
            }
            if (node.getPermanentProperty("jcr:created") != null) {

                updateTime(node, "jcr:created");
            }
            if (node.getPermanentProperty("jcr:lastModified") != null) {

                updateTime(node, "jcr:lastModified");
            }
            getWorkspaceContainer().add(node);
        } catch (ItemExistsException e) {
            log.error("The node already exists ", e);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            throw new RepositoryException("AddNode failed Reason: " + e.getMessage());
        }
    }

    // TODO REfactor this method
    public NodeData getNodeByUUID(String UUID) {
        return nodesStorage.getNodeByUUID(UUID);
    }

    // TODO REfactor this method
    public NodeData getNodeByPath(String absPath) {
      return nodesStorage.getNodeByPath(absPath);
    }

    public Set getChildren(String absPath) {
        return nodesStorage.getChildren(absPath);
    }

    public Set getPaths(String uuid) {

        Set paths = new HashSet();
        try {

           paths.addAll(nodesStorage.getChangedReferencedPaths(uuid));
//           System.out.println("PATHS from changes for "+uuid+"---"+paths);
           paths.addAll(getRepositoryManager().getLocationByUUID(getWorkspaceContainer().getName(), uuid).getReferencedPaths());
           System.out.println("ALL PATHS for "+uuid+"---"+paths);
           return paths;

        } catch (Exception e) {
            log.debug("getPaths failed "+e);
            e.printStackTrace();
            return paths;
        }
    }


    public void addValidationError(String path, Exception error) {
        validationErrors.put(path, error);
    }

    public void validate(Node node, boolean shallow) throws ConstraintViolationException, ItemExistsException {
        log.debug("NodesModificationManager Validates "+node+" to persist");
        processErrors(node, validationErrors);
        processErrors(node, validateMandatory(node));
        for (Iterator iterator = validationErrors.keySet().iterator(); iterator.hasNext(); ) {
            String key = (String)iterator.next();
            if (key.equals(node.getPath()) || PathUtil.isDescendant(key, node.getPath(), false)) {
                validationErrors.remove(key);
            }
        }
    }

    private void processErrors(Node node, Map errors) throws ConstraintViolationException, ItemExistsException {
        if (errors.size() > 0) {
            StringBuffer sB = new StringBuffer();
            Set keys = nodesStorage.getKeys();
            boolean throwException = false;
            boolean throwItemExistsException = false;
            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                String key = (String)iterator.next();
                if (key.equals(node.getPath()) || PathUtil.isDescendant(key, node.getPath(), false)) {
                    if (errors.containsKey(key)) {
                        Exception error = (Exception)errors.get(key);
                        if (error != null) {
                            sB.append("Validation error : " + error + "\n");
                            throwException = true;
                            if (error instanceof ItemExistsException) {
                                throwItemExistsException = true;
                            }
                        }
                    }
                }
            }
            if (throwException) {
                if (throwItemExistsException)
                    throw new ItemExistsException(sB.toString());
                throw new ConstraintViolationException(sB.toString());
            }
        }
    }

    private Map validateMandatory(Node node) {
        List nodes = new ArrayList();
        Map errors = new HashMap();
        //    nodes.add(node);
        getAllDescendants((NodeImpl)node, nodes);
        for (int j = 0; j < nodes.size(); j++) {
            NodeImpl curNode = (NodeImpl)nodes.get(j);
            curNode.setTicket(ticket);
            NodeDef[] reqChildNodes = curNode.getPrimaryNodeType().getDeclaredChildNodeDefs();
            if (reqChildNodes != null) {
                for (int i = 0; i < reqChildNodes.length; i++) {
                    try {
                        // log.debug("validate CHILD Node ---- " + curNode.getPrimaryNodeType() + " " + reqChildNodes[i] + " " +
                        //    reqChildNodes[i].getName());
                        if (reqChildNodes[i].getName() != null && reqChildNodes[i].isMandatory() &&
                            !curNode.hasNode(reqChildNodes[i].getName())) {
                                errors.put(curNode.getPath(),
                                    new ConstraintViolationException("Mandatory node  " +
                                    reqChildNodes[i].getName() + " is required."));
                        }
                    } catch (RepositoryException e) {
                        /// TEMPORARY
                        log.error("ERRORR!!!!" + e);
                    }
                }
            }
        }
        return errors;
    }

    private void getAllDescendants(NodeImpl node, List items) {
        log.debug("Recursively (getAllDescendants): " + node);
        items.add(node);
        Iterator children = nodesStorage.getChildren(node.getPath()).iterator();
        while(children.hasNext()) {
            String path = (String)children.next();
//            log.debug("Recursively child path " + path);
            NodeImpl item = (NodeImpl)getNodeByPath(path);
            log.debug("Recursively child " + item);
            getAllDescendants(item, items);
        }
    }

    public boolean hasPersistedParent(NodeImpl node) throws RepositoryException {
        NodeData inContainerParent = getWorkspaceContainer().getNodeByPath(node.getParent().getPath());
        if (inContainerParent != null)
            return true;
        else
            return false;
    }

    private RepositoryManager getRepositoryManager() {
        return ((RepositoryImpl)ticket.getRepository()).getRepositoryManager();
    }

    private WorkspaceContainer getWorkspaceContainer() throws RepositoryException {
        return ticket.getContainer();
    }

    private String getWorkspaceName() {
        return ticket.getWorkspaceName();
    }

    private void updateUUID(NodeData node) throws PathNotFoundException, RepositoryException {

        ItemLocation loc = new ItemLocation(node.getPath(), "jcr:uuid");
        String uuidVal = getRepositoryManager().generateUUID(node);
        Property property = new PropertyImpl(loc.getPath(), new StringValue(uuidVal), PropertyType.STRING);
        node.addPermanentProperty(property);
//        addReference(uuidVal, node.getPath());

    }

    private void updateTime(NodeData node, String propertyName) throws PathNotFoundException, RepositoryException {

        ItemLocation loc = new ItemLocation(node.getPath(), propertyName);
        Property property = new PropertyImpl(loc.getPath(),
        new DateValue(getRepositoryManager().getCurrentTime()), PropertyType.DATE);
        node.addPermanentProperty(property);
    }


    private class PathComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            NodeChange nc1 = (NodeChange)o1;
            NodeChange nc2 = (NodeChange)o2;
            String path1 = nc1.getNode().getPath();
            String path2 = nc2.getNode().getPath();
            return path1.compareTo(path2);
        }
    }
}
