/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.jcr.ActionVetoedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NamespaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.jcr.Ticket;
import javax.jcr.Workspace;
import javax.jcr.access.AccessDeniedException;
import javax.jcr.nodetype.ConstraintViolationException;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.core.WorkspaceRegistry;
import org.exoplatform.services.jcr.impl.util.DocNodeImporter;
import org.exoplatform.services.jcr.impl.util.NodeTypeRecognizer;
import org.exoplatform.services.jcr.impl.util.SysNodeImporter;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SARL        .
 * <p/>
 * The ticket represents a kind of key to the repository and
 * encapsulates the identity of the user and all access-related
 * restrictions that flow from that identity.
 * In level 1, access control is beyond the scope of this specification
 * (thought level 1 repositories may still implement it, of course). The
 * only requirement is that the identity of the user, and hence their
 * access privileges are bound to the ticket.
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: TicketImpl.java,v 1.23 2004/09/05 17:14:28 geaz Exp $
 */

public class TicketImpl implements Ticket {

  private static final String ANONYMOUS = "anonymous";
//  private static final String DEFAULT = "default";

  private RepositoryImpl repository;
  private Credentials credentials;
  private Workspace workspace;
  private NodesModificationManager nodesManager;
  private String workspaceName;
  private Map namespaces;


  public TicketImpl(RepositoryImpl repository, Credentials credentials, String workspaceName)
    throws RepositoryException  {

    WorkspaceRegistry workspaceRepository = (WorkspaceRegistry) PortalContainer.getInstance().
        getComponentInstanceOfType(WorkspaceRegistry.class);
    this.repository = repository;
    this.credentials = credentials;
    if (credentials == null) {
      this.credentials = new SimpleCredentials(ANONYMOUS, ANONYMOUS.toCharArray());
    }
    this.workspaceName = workspaceName;
    if (workspaceName == null) {
        throw new RuntimeException("Workspace name could not be NULL!");
    }
    this.workspace = workspaceRepository.getWorkspace(repository.getName(), workspaceName, this);
    this.nodesManager = new NodesModificationManager(this);
    namespaces = new HashMap();
  }

  /**
   * 6.1.3
   * Gets the Repository that issued this ticket.
   */
  public Repository getRepository() {
    return repository;
  }

  /**
   * 6.1.3
   * Return the Credentials object that was used to
   * authorize the issue of this ticket.
   */
  public Credentials getCredentials() {
    return credentials;
  }

  /**
   * 6.1.3
   * Returns a new ticket in accordance with the
   * specified (new) credentials. In effect allows the
   * current user to "impersonate" another use
   * (assuming that their original ticket gives them that
   * right).
   */
  public Ticket impersonate(Credentials c) throws LoginException {
    try {
      return new TicketImpl(repository, c, workspaceName);
    } catch (Exception e) {
      throw new LoginException(e.getMessage());
    }
  }


  /**
   * Returns the <code>Workspace</code> attached to this <code>Ticket</code>.
   *
   * @return a <code>{@link Workspace}</code> object.
   */
  public Workspace getWorkspace() {
    return workspace;
  }

  /**
   * Returns the root node of the workspace.
   * The root node, "/", is the main access point to the content of the
   * workspace.
   *
   * @return The root node of the workspace: a <code>Node</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Node getRootNode() throws RepositoryException {
    NodeImpl node = (NodeImpl) nodesManager.getNodeByPath("/");
    if (node != null) {
      node.setTicket(this);
    } else
      throw new RepositoryException("Ticket.getRootNode fatal error! RootNode could not be NULL");

    return node;

  }

  /**
   * Returns the node specifed by the given UUID. Only applies to nodes that
   * expose a UUID, in other words, those of mixin node type
   * <code>mix:referenceable</code>
   *
   * @param uuid A universally unique identifier.
   * @return A <code>Node</code>.
   * @throws ItemNotFoundException if the specified UUID is not found.
   * @throws RepositoryException   if another error occurs.
   */
  public Node getNodeByUUID(String uuid) throws ItemNotFoundException, RepositoryException {
    NodeImpl node = (NodeImpl) nodesManager.getNodeByUUID(uuid);
    if (node != null) {
      node.setTicket(this);
    } else
      throw new ItemNotFoundException("Ticket.getNodeByUUID failed. Node with UUID='" + uuid + "' not found.");

    return node;
  }

  /**
   * Returns the node at the specified absolute path.
   *
   * @param absPath An absolute path.
   * @return A <code>Node</code>.
   * @throws PathNotFoundException if the specified path cannot be found.
   * @throws RepositoryException   if another error occurs.
   */
  public Node getNodeByAbsPath(String absPath) throws PathNotFoundException, RepositoryException {
    NodeImpl node = (NodeImpl) nodesManager.getNodeByPath(absPath);
    if (node != null) {
      node.setTicket(this);
    } else
      throw new PathNotFoundException("Ticket.getNodeByAbsPath failed. Path'" + absPath + "' not found.");

    return node;
  }


  /**
   * Validates and (if validation succeeds) persists all changes made through
   * this Ticket since the last <code>save</code> or </code>revert</code>.
   * <p/>
   * Constraints mandated by node types are validated on save. If validation
   * fails a <code>ConstraintViolationException</code> is thrown and the
   * state of the transient storage is left unaffected. If validation succeeds
   * then all pending changes are persited and the transient change cache is
   * flushed.
   * <p/>
   * The save method allows a node to be in a temporarily invalid state while
   * it is being "built", that is, while it is having properties and child
   * nodes added to it. Once it is in a consistent state it can be saved.
   * <p/>
   * An <code>AccessDeniedException</code> will be thrown if an attempt is
   * made to save changes for which the current user does not have sufficient
   * access rights.
   * <p/>
   * An <code>ActionVetoedException</code> will be thrown if a
   * <code>VetoableEventListener</code> vetoes one of the changes being saved.
   * <p/>
   * If save succeeds then the all changes are removed from the cache
   * of pending changes in the ticket.
   *
   * @throws ConstraintViolationException If any of the changes would
   *                                      violate a constraint as defined by the node type of the respective node.
   * @throws AccessDeniedException        if the current ticket does not have
   *                                      sufficient access rights to complete the operation.
   * @throws RepositoryException          If another error occurs.
   * @throws ActionVetoedException        If a VetoableEventListener vetoes one of
   *                                      the changes being saved.
   */
  public void save() throws AccessDeniedException, ConstraintViolationException,
      ActionVetoedException, RepositoryException {
    getRootNode().save(false);
  }

  /**
   * Discards all pending changes made through this ticket, that is, all
   * changes made since the last sucessful <code>save</code>.
   *
   * @throws RepositoryException If an unexpected error occurs.
   */
  public void revert() throws RepositoryException {
    nodesManager.rollback((NodeImpl) getRootNode());
  }

  /**
   * Deserializes an XML document (in <b>system view</b> form or <b>document
   * view</b> form) and adds the resulting item subtree as a child of the
   * node at <code>parentAbsPath</code>. Requires a <code>save</code> to
   * persist the changes.
   * <p/>
   * The deserialization mechanism must take into account the
   * property <code>jcr:uuid</code> in order to reconstruct hard links
   * across serialization/deserialization cycles.
   * <p/>
   * If node type restrictions prevent the addition of the subtree to the
   * node at <code>parentAbsPath</code>, a <code>ConstraintViolationException</code>
   * is thrown.
   * <p/>
   * If the XML stream provided is not a valid JCR <b>system view</b> XML
   * document then an <code>InvalidSerializedDataException</code> is thrown.
   * <p/>
   * If the user does not have sufficient access rights to write the
   * deserialized nodes and properties, then an
   * <code>AccessDeniedException</code> is thrown on <code>save</code>.
   * <p/>
   *
   * @param parentAbsPath the absolute path of the node below which the deserialized
   *                      subtree is added.
   * @param in            The <code>Inputstream</code> from which the XML to be deserilaized
   *                      is read.
   * @throws java.io.IOException            if an error during an I/O operation occurs.
   * @throws PathNotFoundException          if no node exists at <code>parentAbsPath</code>.
   * @throws ItemExistsException            if an item by the same name as the newly
   *                                        imported root node already exists.
   * @throws ConstraintViolationException   if node type restrictions prevent
   *                                        the addition of the subtree to the node at <code>parentAbsPath</code>
   * @throws InvalidSerializedDataException if the serialized data being input is
   *                                        not a valid JCR <b>system view</b> XML document.
   * @throws RepositoryException            is another error occurs.
   */
  public void importXML(String parentAbsPath, InputStream in) throws IOException, PathNotFoundException,
      ItemExistsException, ConstraintViolationException, InvalidSerializedDataException, RepositoryException {
    int type;
    byte[] buffer = new byte[in.available()];
    ByteArrayInputStream str;
    try {
      in.read(buffer);
      str = new ByteArrayInputStream(buffer);
      type = NodeTypeRecognizer.recognize(str);
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }

    str = new ByteArrayInputStream(buffer);
    if (type == NodeTypeRecognizer.SYS){
      importSysView(parentAbsPath, str);
    } else {
      importDocView(parentAbsPath, str);
    }
  }

  /**
   * Deserializes the stream of SAX events (from either system view or
   * document view XML source) defined by the series of calls of
   * the methods of the <code>org.xml.ContentHandler</code> into a subtree of
   * items immediately below the node at <code>parentAbsPath</code>. If the
   * incoming XML stream (in the form of SAX events) is not a valid JCR
   * system view or document view XML document then the
   * <code>ContentHandler</code> will throw a <code>SAXException</code> in
   * one of its methods. After the document has been fed in <code>save</code>
   * must be called to persist the changes to the repository.
   *
   * @param parentAbsPath the absolute path of a node under which (as child) the imported
   *                      subtree will be built.
   * @return an org.xml.ContentHandler whose methods may be called to feed SAX
   *         events into the deserializer.
   * @throws ItemExistsException   if an item alread exists at <code>parentAbsPath</code>.
   * @throws PathNotFoundException if no node exists at <code>parentPath</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public ContentHandler importXML(String parentAbsPath) throws PathNotFoundException, ItemExistsException, RepositoryException {
    throw new RuntimeException("TODO implement importXML()!");
  }

  /**
   * Within the scope of this ticket, rename a persistently registered
   * namespace URI to the new prefix.  The renaming only affects operations
   * done through this ticket. To clear all renamings the client must acquire
   * a new ticket.
   * <p/>
   * If the specified <code>uri</code> is not among those registered in the
   * <code>NamespaceRegistry</code> then a <code>NamespaceException</code> is
   * thrown.
   *
   * @param prefix a string
   * @param uri    a string
   * @throws NamespaceException if specified uri is not registered.
   */
  public void setPrefix(String prefix, String uri) throws NamespaceException {
    try {
      workspace.getNamespaceRegistry().getPrefix(uri); // throws exception if not found
      namespaces.put(prefix, uri);
    } catch (Exception e) {
      throw new NamespaceException(e.getMessage());
    }
  }

  /**
   * Returns all prefixes currently set for this ticket. This includes all
   * those registered in the <code>NamespaceRegistry</code> but <i>not
   * over-ridden</i> by a <code>Ticket.setPrefix</code>, plus those currently set
   * locally by <code>Ticket.setPrefix</code>.
   *
   * @return a string array
   */
  public String[] getPrefixes() {
    Collection allPrefixes = new LinkedList();
    allPrefixes.addAll(namespaces.keySet());
    String[] permanentPrefixes = workspace.getNamespaceRegistry().getPrefixes();
    for (int i = 0; i < permanentPrefixes.length; i++) {
      String permanentPrefix = permanentPrefixes[i];
      String uri = null;
      try {
        uri = workspace.getNamespaceRegistry().getURI(permanentPrefix);
        if (!allPrefixes.contains(permanentPrefix) && !(namespaces.values().contains(uri))) {
          allPrefixes.add(permanentPrefix);
        }
      } catch (RepositoryException e) {
        e.printStackTrace();
      }
    }
    return (String[]) allPrefixes.toArray(new String[allPrefixes.size()]);
  }

  /**
   * For a given prefix, returns the URI to which it is mapped as currently
   * set in this ticket. If the prefix is unknown a NamespaceException is thrown.
   *
   * @param prefix a string
   * @return a string
   * @throws NamespaceException if the prefix is unknown.
   */
  public String getURI(String prefix) throws NamespaceException {
    String uri = null;
    try {
      //look in ticket first
      uri = (String) namespaces.get(prefix);
      if (uri != null)
        return uri;
      uri = workspace.getNamespaceRegistry().getURI(prefix);
      if (namespaces.values().contains(uri))
        return null;
    } catch (Exception e) {
      throw new NamespaceException("Can not lookup the URI", e);
    }
    if (uri == null)
      throw new NamespaceException("No such " + uri + " uri in the NamespaceRepository");

    return uri;
  }

  /**
   * Releases all resources associate with this ticket. Calling this method
   * is not mandatory since a good implementaion should automatically timeout
   * anyway. However, when more precise control over resource allocation is
   * need this method can be used.
   */
  public void logout() {
  }


  private void importSysView(String parentPath, InputStream in) throws PathNotFoundException,
      InvalidSerializedDataException, ConstraintViolationException, RepositoryException {
    ArrayList items;
    try {
      SysNodeImporter.fillItems(parentPath, in, this);
    } catch (IOException e) {
      throw new InvalidSerializedDataException(e.getMessage(),e);
    } catch (SAXException e) {
      throw new InvalidSerializedDataException(e.getMessage(),e);
    }
  }

  private ContentHandler importSysView(String parentPath)
      throws InvalidSerializedDataException, ConstraintViolationException {
    throw new ConstraintViolationException("Does Not implemented temporary!");
  }

  private void importDocView(String parentPath, InputStream in)
      throws PathNotFoundException, InvalidSerializedDataException,
      ConstraintViolationException, RepositoryException {
    try {
      NodeImpl node = (NodeImpl) this.getNodeByAbsPath(parentPath);
      node.setTicket(this);
      DocNodeImporter.fillNode(node, in, new String[0]);
    } catch (IOException e) {
      throw new InvalidSerializedDataException("importDocView failed", e);
    } catch (SAXException e) {
      throw new InvalidSerializedDataException("importDocView failed", e);
    }
  }

  private ContentHandler importDocView(String parentPath)
      throws InvalidSerializedDataException, ConstraintViolationException {
    throw new ConstraintViolationException("Does Not implemented temporary!");
  }

  NodesModificationManager getNodesManager() {
    return this.nodesManager;
  }

  // for tesing
  public WorkspaceContainer getContainer() throws RepositoryException {
    return repository.getContainer(workspaceName);
  }

  public String getWorkspaceName() {
    return workspaceName;
  }

}
