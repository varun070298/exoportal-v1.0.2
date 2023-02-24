/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.jcr.ActionVetoedException;
import javax.jcr.ItemExistsException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Ticket;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Workspace;
import javax.jcr.access.AccessDeniedException;
import javax.jcr.access.AccessManager;
import javax.jcr.lock.LockCapabilities;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.QueryManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeManagerImpl;
import org.exoplatform.services.jcr.impl.core.query.QueryManagerImpl;
import org.exoplatform.services.jcr.impl.util.StringConverter;
import org.exoplatform.services.jcr.impl.util.XMLWriter;
import org.exoplatform.services.jcr.storage.RepositoryManager;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.jcr.util.PathUtil;
import org.exoplatform.services.log.LogUtil;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Created by The eXo Platform SARL        .
 * <p/>
 * 6.1.4
 * For the time One workspace for each ticket - simple impl
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: WorkspaceImpl.java,v 1.31 2004/11/02 18:36:33 geaz Exp $
 */

public class WorkspaceImpl implements Workspace {

  public static final String ROOT_PATH = "/";

  protected Log log;

  private Ticket ticket;
  private WorkspaceContainer serverContainer;
  private String workspaceName;

  public WorkspaceImpl(String workspaceName, Ticket ticket) throws RepositoryException {
    log = LogUtil.getLog("org.exoplatform.services.jcr");
    this.workspaceName = workspaceName;
    this.ticket = ticket;
    this.serverContainer = ((RepositoryImpl) ticket.getRepository()).getContainer(workspaceName);
  }

  /**
   * 6.1.4
   * Gets the ticket that was used to get this Workspace
   * object.
   */
  public Ticket getTicket() {
    return ticket;
  }


  /**
   * Copies the subtree (or node and its properties if <code>shallow</code> is
   * <code>true</code>) at <code>srcAbsPath</code> to the new location at
   * <code>destAbsPath</code> in the specified workspace.
   *
   * @param srcAbsPath  the path of the node to be copied.
   * @param destAbsPath the location to which the node at <code>srcAbsPath</code>
   *                    is to be copied.
   * @param shallow     if true only the node at <code>srcAbsPath</code> and its
   *                    properties are copied, otherwise the source node and its entire subtree is copied.
   * @throws ConstraintViolationException if the copy would violate a
   *                                      node type or other constraint.
   * @throws AccessDeniedException        if the current ticket (i.e. the ticket that
   *                                      was used to aqcuire this <code>Workspace</code> object) does not have
   *                                      sufficient access rights to complete the operation.
   * @throws PathNotFoundException        if the node at <code>srcAbsPath</code> or
   *                                      the parent of the new node at <code>destAbsPath</code> does not exist.
   * @throws ItemExistsException          if a node or property already exists at
   *                                      <code>destAbsPath</code>.
   * @throws ActionVetoedException        If a <code>VetoableEventListener</code>
   *                                      vetoes one of the changes being saved.
   * @throws RepositoryException          if another error occurs.
   */
  public void clone(String srcAbsPath, String destAbsPath, String destWorkspace, boolean shallow)
      throws ConstraintViolationException, AccessDeniedException, PathNotFoundException,
      ItemExistsException, ActionVetoedException, RepositoryException {

    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(srcAbsPath);

    if (node == null)
      throw new PathNotFoundException("Path not found : " + srcAbsPath);

    ItemLocation destLocation = new ItemLocation(destAbsPath, node.getName());

    WorkspaceContainer destContainer = ((RepositoryImpl)ticket.getRepository()).getContainer(destWorkspace);

    log.debug("Clone "+node+" from "+serverContainer.getName()+" to "+destContainer.getName());

    if (destContainer.getNodeByPath(destLocation.getPath()) != null)
      throw new ItemExistsException("Workspace.clone() failed: destination node'" + destAbsPath +
          "'already exists in workspace : " + destWorkspace);

    Node newParent = destContainer.getNodeByPath(new ItemLocation(destAbsPath).getPath());
//        getAncestorPath(1));

    if (newParent == null)
      throw new PathNotFoundException("Parent for '" + destAbsPath + "' not found.");
    if (!newParent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName()))
      throw new ConstraintViolationException("Workspace.clone(): Adding node " + node +
          " to " + newParent + " is not allowed!");

    List items = new ArrayList();
    if (shallow)
      items.add(node);
    else
      getRecursively(node, items, serverContainer);

    for (int i = 0; i < items.size(); i++) {
      NodeImpl item = (NodeImpl) items.get(i);
      String newPath = destAbsPath+item.getPath();
      NodeImpl newNode = new NodeImpl(newPath, item.getPermanentProperties());
      log.debug("Workspace.clone() new node = " + newNode);
      destContainer.add(newNode);

      Property uuid = newNode.getPermanentProperty("jcr:uuid");
      if (uuid != null)
          getRepositoryManager().addLocation(destContainer.getName(), uuid.getString(), newNode.getPath(), true);

//          getRepositoryManager().addPath(destContainer.getName(), uuid.getString(), newNode.getPath());

    }


  }

  /**
   * Moves the node at <code>srcAbsPath</code> (and its entire subtree) to the
   * new location at <code>destAbsPath</code>.
   * Throws an exception if
   * there is already an item at <code>destAbsPath</code>, or if some other
   * constraint (access, node type, system-level) is violated. If succesful,
   * the parent of the new node at <code>destAbsPath</code> is "saved" (i.e. the
   * change is persisted immediately as is the parent of the moved node at its
   * original location, there is no need to call <code>Node.save()</code>).
   *
   * @param srcAbsPath  the path of the node to be moved.
   * @param destAbsPath the location to which the node at <code>srcAbsPath</code>
   *                    is to be moved.
   * @throws ConstraintViolationException if the move would violate a
   *                                      node type or other constraint.
   * @throws AccessDeniedException        if the current ticket (i.e. the ticket that
   *                                      was used to aqcuire this <code>Workspace</code> object) does not have
   *                                      sufficient access rights to complete the operation.
   * @throws PathNotFoundException        if the node at <code>srcAbsPath</code> or
   *                                      the parent of the new node at <code>destAbsPath</code> does not exist.
   * @throws ItemExistsException          if a node or property already exists at
   *                                      <code>destAbsPath</code>.
   * @throws ActionVetoedException        If a <code>VetoableEventListener</code>
   *                                      vetoes one of the changes being saved.
   * @throws RepositoryException          if another error occurs.
   */
  public void move(String srcAbsPath, String destAbsPath) throws ConstraintViolationException,
      AccessDeniedException, PathNotFoundException, ItemExistsException,
      ActionVetoedException, RepositoryException {
    copy(srcAbsPath, destAbsPath);
    List items = new ArrayList();
    NodeImpl node = (NodeImpl)serverContainer.getNodeByPath(srcAbsPath);
    getRecursively(node, items, serverContainer);
    for(int i=0; i<items.size(); i++) {
       NodeImpl childNode = (NodeImpl)items.get(i);
       serverContainer.delete(childNode.getPath()); //serverContainer.getNodeByPath(srcAbsPath));

       Property uuid = childNode.getPermanentProperty("jcr:uuid");
       if (uuid != null)
           getRepositoryManager().deleteLocationByUUID(serverContainer.getName(), uuid.getString());
//           getRepositoryManager().deletePath(serverContainer.getName(), uuid.getString());

    }
    
  }

  /**
   * 6.1.4
   * Returns the QueryManager, which is used to search
   * the repository.
   */
  public QueryManager getQueryManager() {
    QueryManagerImpl qm = QueryManagerImpl.getInstance();
    qm.init(this);
    return qm;
  }

  /**
   * 6.1.4
   * Returns the NodeTypeManager, which is used to
   * access information about which node types are
   * available in the repository.
   */
  public NodeTypeManager getNodeTypeManager() {
    return NodeTypeManagerImpl.getInstance();
  }


  /**
   * 6.1.4
   * Returns the NamespaceRegistry object, which is
   * used to set the mapping between namespace
   * prefixes and URIs.
   */
  public NamespaceRegistry getNamespaceRegistry() {
    return (NamespaceRegistry) PortalContainer.getInstance().
        getComponentInstanceOfType(NamespaceRegistry.class);
  }

  /**
   * In level 2 gets the <code>AccessManager</code>, in level 1 throws
   * an <code>UnsupportedRepositoryOperationException</code>.
   * <p/>
   * <b>Level 1:</b>
   * <p/>
   * Always throws an <code>UnsupportedRepositoryOperationException</code> since
   * access control is not supported in level 1.
   * <p/>
   * <b>Level 2:</b>
   * <p/>
   * Gets the <code>AccessManager</code> through which access control
   * information is queried.
   *
   * @return an <code>AccessManager</code> object.
   * @throws UnsupportedRepositoryOperationException
   *          In level 1: Always.
   *          In level 2: Never.
   */
  public AccessManager getAccessManager() throws UnsupportedRepositoryOperationException {
    throw new UnsupportedRepositoryOperationException("Workspace.getAccessManager() is not supported by Level 1 of JCR.");
  }


  /**
   * <i>If locking is supported</i> returns the
   * <code>LockCapabilities</code> object, otherwise throws an
   * <code>UnsupportedRepositoryOperationException</code>.
   * <p/>
   * <b>Level 1:</b>
   * <p/>
   * Always throws an <code>UnsupportedRepositoryOperationException</code>
   * since locking is not supported in level 1.
   * <p/>
   * <b>Level 2:</b>
   * <p/>
   * If locking is supported, returns the <code>LockCapabilities</code>
   * object through which dynamic discovery of supported locking functionality
   * is provided. If locking is not supported then this method throws an
   * <code>UnsupportedRepositoryOperationException</code>.
   *
   * @return A <code>LockCapabilities</code> object.
   * @throws UnsupportedRepositoryOperationException
   *          In level 1: Always.
   *          In level 2: if implementation does not support locking.
   */
  public LockCapabilities getLockCapabilities() throws UnsupportedRepositoryOperationException {
    throw new UnsupportedRepositoryOperationException("Workspace.getLockCapabilities() is not supported by Level 1 of JCR.");
  }

  /**
   * If observation is supproted gets the <code>ObservationManager</code>,
   * otherwisein throws an <code>UnsupportedRepositoryOperationException</code>.
   * <p/>
   * <b>Level 1:</b>
   * <p/>
   * Always throws an <code>UnsupportedRepositoryOperationException</code> since
   * observation is not in supported in level 1.
   * <p/>
   * <b>Level 2:</b>
   * <p/>
   * If observation is supported (it is optional, even in level 2), gets the
   * <code>ObservationManager</code> object through which event observation
   * is managed. Otherwise throws an
   * <code>UnsupportedRepositoryOperationException</code>.
   *
   * @return an <code>ObservationManager</code> object.
   * @throws UnsupportedRepositoryOperationException
   *          In level 1: Always.
   *          In level 2: if implementation doesnot support observation.
   */
  public ObservationManager getObservationManager() throws UnsupportedRepositoryOperationException {
    throw new UnsupportedRepositoryOperationException("Workspace.getObservationManager() is not supported by Level 1 of JCR.");
  }


  /**
   * 6.1.4
   * Serializes the node at absPath .
   */
  public void exportSysView(String absPath, ContentHandler handler, boolean binaryAsLink, boolean noRecurse)
      throws PathNotFoundException, SAXException, RepositoryException {
    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(absPath);
    XMLWriter writer = new XMLWriter(((NamespaceRegistryImpl) getNamespaceRegistry()).getURIMap());
    initNodeAsSysView(node, writer, binaryAsLink, noRecurse);

    invokeHandler(writer.getBytes(), handler);
  }

  /**
   * 6.1.4
   * Serializes the node at absPath .
   */
  public void exportSysView(String absPath, OutputStream out, boolean binaryAsLink, boolean noRecurse)
      throws IOException, PathNotFoundException, RepositoryException {
    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(absPath);
    if (node == null)
      throw new PathNotFoundException("exportSysView error: node not found at the path '" + absPath + "'");

    XMLWriter writer = new XMLWriter(((NamespaceRegistryImpl) getNamespaceRegistry()).getURIMap());
    initNodeAsSysView(node, writer, binaryAsLink, noRecurse);

    try {
      out.write(writer.getBytes());
    } catch (IOException e) {
      throw new RepositoryException("Write Sys View failed. Reason: " + e);
    }

  }

  /**
   * 6.1.4
   * Serializes the node at absPath .
   */
  public void exportDocView(String absPath, ContentHandler handler, boolean binaryAsLink, boolean noRecurse)
      throws PathNotFoundException, SAXException, RepositoryException {
    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(absPath);
    XMLWriter writer = new XMLWriter(((NamespaceRegistryImpl) getNamespaceRegistry()).getURIMap());
    initNodeAsDocView(node, writer, binaryAsLink, noRecurse);

    invokeHandler(writer.getBytes(), handler);
  }

  private void invokeHandler(byte[] input, ContentHandler contentHandler)
      throws SAXException, RepositoryException {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      XMLReader reader = parser.getXMLReader();
      reader.setContentHandler(contentHandler);
      reader.parse(new InputSource(new ByteArrayInputStream(input)));
    } catch (SAXException e) {
      throw e;
    } catch (Exception e) {
      throw new RepositoryException("Can not invoke content handler", e);
    }
  }

  /**
   * 6.1.4
   * Serializes the node at absPath .
   */
  public void exportDocView(String absPath, OutputStream out, boolean binaryAsLink, boolean noRecurse)
      throws IOException, PathNotFoundException, RepositoryException {
    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(absPath);
    if (node == null)
      throw new PathNotFoundException("exportDocView error: node not found at the path '" + absPath + "'");
    XMLWriter writer = new XMLWriter(((NamespaceRegistryImpl) getNamespaceRegistry()).getURIMap());
    initNodeAsDocView(node, writer, binaryAsLink, noRecurse);

    try {
      out.write(writer.getBytes());
    } catch (IOException e) {
      throw new RepositoryException("Write Doc View failed. Reason: " + e);
    }

  }


  public void copy(String srcPath, String destPath, boolean shallow) throws ItemExistsException,
      AccessDeniedException, ConstraintViolationException, RepositoryException {

    NodeImpl node = (NodeImpl) serverContainer.getNodeByPath(srcPath);

    if (node == null)
      throw new PathNotFoundException("Path not found : " + srcPath);

    if (serverContainer.getNodeByPath(destPath) != null)
      throw new ItemExistsException("Workspace.copy() failed: destination node'" + destPath +
          "'already exists!");

    Node newParent = serverContainer.getNodeByPath(new ItemLocation(destPath).getAncestorPath(1));
    if (newParent == null)
      throw new PathNotFoundException("Parent for '" + destPath + "' not found.");
    if (!newParent.getPrimaryNodeType().canAddChildNode(node.getName(), node.getPrimaryNodeType().getName()))
      throw new ConstraintViolationException("Workspace.copy(): Adding node " + node + " to " + newParent + " is not allowed!");

    List items = new ArrayList();
    if (shallow)
      items.add(node);
    else
      getRecursively(node, items, serverContainer);

    for (int i = 0; i < items.size(); i++) {
      NodeImpl item = (NodeImpl) items.get(i);
//      item.setTicket(new DummyTicket(serverContainer));
      String _path = PathUtil.rewriteSuffix(item.getPath(), srcPath, destPath);
      List props = new ArrayList();
      Iterator propertyIterator = item.getPermanentProperties().iterator();

      while (propertyIterator.hasNext()) {
        PropertyImpl property = (PropertyImpl) propertyIterator.next();
        String propPath = PathUtil.rewriteSuffix(property.getPath(), srcPath, destPath);
        log.debug("Workspace.copy() new prop = " + propPath);
        props.add(new PropertyImpl(propPath, property.getValue(), property.getType()));
      }

      NodeImpl newNode = new NodeImpl(_path, props);
//      newNode.setTicket(new DummyTicket(serverContainer));
      log.debug("Workspace.copy() new node = " + newNode);
      serverContainer.add(newNode);
    }

  }


  /**
   * Equivalent to copy(srcPath, destPath, false).
   * No more in interface!
   */
  public void copy(String srcPath, String destPath) throws ItemExistsException, AccessDeniedException, RepositoryException {
    copy(srcPath, destPath, false);
  }


/////////////////////////////////////////////

  private void getRecursively(NodeImpl node, List items, WorkspaceContainer c) {
    log.debug("GET Recursively " + node);
    items.add(node);

    try {

      List children = c.getChildren(node.getPath());
      for (int i = 0; i < children.size(); i++) {
        String path = (String) children.get(i);
        NodeImpl item = (NodeImpl)c.getNodeByPath(path);
        getRecursively(item, items, c);
      }
    } catch (RepositoryException e) {
       e.printStackTrace();
       throw new RuntimeException("NodesStorage.getRecursively() for "+node.getPath()+" FAILED "+e);
    }
  }

  // Helper for for node export
  private void initNodeAsSysView(NodeImpl node, XMLWriter writer, boolean binaryAsLink, boolean noRecurse)
      throws RepositoryException {

    log.debug("Sys --" + node + " writer: " + writer);

    String name = node.getName();

    if (name.length() == 0) // root node
      name = "sv:root";

    Properties attrs = new Properties();
    attrs.setProperty("sv:name", name);
    writer.startElement("sv:node", attrs);

    PropertyIterator props = node.getProperties();
    while (props.hasNext()) {
      PropertyImpl prop = (PropertyImpl) props.next();
      String strPropVal = getStrPropValue(prop, binaryAsLink);
      String strPropType;
      if (prop.getType() == PropertyType.BINARY && binaryAsLink)
        strPropType = PropertyType.nameFromValue(PropertyType.SOFTLINK).toLowerCase();
      else
        strPropType = PropertyType.nameFromValue(prop.getType()).toLowerCase();

      attrs = new Properties();
      attrs.setProperty("sv:name", prop.getName());
      attrs.setProperty("sv:type", "pt:" + strPropType);
      writer.startElement("sv:property", attrs);
      writer.writeText(strPropVal);
      writer.endElement();
    }

    List nodes = serverContainer.getChildren(node.getPath());
    for (int i = 0; i < nodes.size(); i++) {
//      NodeImpl child = (NodeImpl) nodes.get(i);
      String path = (String) nodes.get(i);
      NodeImpl child = (NodeImpl) serverContainer.getNodeByPath(path);      
      if (!noRecurse)
        initNodeAsSysView(child, writer, binaryAsLink, noRecurse);
    }
    writer.endElement();
  }

  // Helper for for node export
  private void initNodeAsDocView(NodeImpl node, XMLWriter writer, boolean binaryAsLink, boolean noRecurse) throws RepositoryException {

    String name = node.getName();
    if (name.length() == 0) // root node
      name = "root";

//        buffer.append("<"+name);
    Properties attrs = new Properties();

    PropertyIterator props = node.getProperties();
    while (props.hasNext()) {
      PropertyImpl prop = (PropertyImpl) props.next();
      String strPropVal = getStrPropValue(prop, binaryAsLink);
      attrs.setProperty(prop.getName(), strPropVal);
    }
    writer.startElement(name, attrs);

    List nodes = serverContainer.getChildren(node.getPath());
    for (int i = 0; i < nodes.size(); i++) {
      String path = (String) nodes.get(i);
      NodeImpl child = (NodeImpl) serverContainer.getNodeByPath(path);      
//      NodeImpl child = (NodeImpl) nodes.get(i);
      if (!noRecurse)
        initNodeAsDocView(child, writer, binaryAsLink, noRecurse);
    }

    writer.endElement();
  }

  private String getStrPropValue(PropertyImpl prop, boolean binaryAsLink) {
    if (prop.getType() == PropertyType.BINARY) {
      if (binaryAsLink)
        return prop.getPath();
      else {
        String str = new String(Base64.encodeBase64(prop.getString().getBytes()));
        return str;
      }
    } else
      return StringConverter.normalizeString(prop.getString(), false);
  }


  //
  public WorkspaceContainer getContainer() {
    return serverContainer;
  }

  public String getWorkspaceName() {
    return workspaceName;
  }

  private RepositoryManager getRepositoryManager() {
     return ((RepositoryImpl)ticket.getRepository()).getRepositoryManager();
  }

}
