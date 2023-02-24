/*
 * $Id: Ticket.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
 *
 * Copyright 2002-2004 Day Management AG, Switzerland.
 *
 * Licensed under the Day RI License, Version 2.0 (the "License"),
 * as a reference implementation of the following specification:
 *
 *   Content Repository API for Java Technology, revision 0.12
 *        <http://www.jcp.org/en/jsr/detail?id=170>
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License files at
 *
 *     http://www.day.com/content/en/licenses/day-ri-license-2.0
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.jcr;

import org.xml.sax.*;

import javax.jcr.nodetype.*;
import javax.jcr.access.*;
import java.io.*;

/**
 * Through this interface all access to the content repository occurs.
 * Given upon <code>login</code> to a <code>Repository</code>.
 * Both level 1 and 2 implementations must implement this interface, though
 * in level 1 some methods will throw an
 * <code>UnsupportedRepositoryOperationException</code> (see the
 * method-by-method descriptions).
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 * @see javax.jcr.xa.XATicket
 */
public interface Ticket {

  /**
   * Returns the <code>Repository</code> object through which this ticket was
   * acquired.
   *
   * @return a <code>{@link Repository}</code> object.
   */
  public Repository getRepository();

  /**
   * Returns the Credentials object that was used to authorize the issue of
   * this ticket.
   *
   * @return the Credentials of this ticket.
   */
  public Credentials getCredentials();

  /**
   * Returns the <code>Workspace</code> attached to this <code>Ticket</code>.
   *
   * @return a <code>{@link Workspace}</code> object.
   */
  public Workspace getWorkspace();

  /**
   * Returns new ticket in accordance with the specified (new) Credentials.
   * Allows the current user to "impersonate" another using incomplete
   * credentials (perhaps including a user name but no password, for example),
   * assuming that their original ticket gives them that right. Throws a
   * <code>LoginException</code> if the current ticket does not have
   * sufficient rights.
   *
   * @param credentials A <code>Credentials</code> object
   * @return a <code>Ticket</code> object
   * @throws LoginException if the current ticket does not have
   *                        sufficient rights.
   */
  public Ticket impersonate(Credentials credentials) throws LoginException;

  /**
   * Returns the root node of the workspace.
   * The root node, "/", is the main access point to the content of the
   * workspace.
   *
   * @return The root node of the workspace: a <code>Node</code> object.
   * @throws RepositoryException if an error occurs.
   */
  public Node getRootNode() throws RepositoryException;

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
  public Node getNodeByUUID(String uuid) throws ItemNotFoundException, RepositoryException;

  /**
   * Returns the node at the specified absolute path.
   *
   * @param absPath An absolute path.
   * @return A <code>Node</code>.
   * @throws PathNotFoundException if the specified path cannot be found.
   * @throws RepositoryException   if another error occurs.
   */
  public Node getNodeByAbsPath(String absPath) throws PathNotFoundException, RepositoryException;

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
  public void save() throws AccessDeniedException, ConstraintViolationException, ActionVetoedException, RepositoryException;

  /**
   * Discards all pending changes made through this ticket, that is, all
   * changes made since the last sucessful <code>save</code>.
   *
   * @throws RepositoryException If an unexpected error occurs.
   */
  public void revert() throws RepositoryException;

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
  public void importXML(String parentAbsPath, InputStream in) throws IOException, PathNotFoundException, ItemExistsException, ConstraintViolationException, InvalidSerializedDataException, RepositoryException;

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
  public ContentHandler importXML(String parentAbsPath) throws PathNotFoundException, ItemExistsException, RepositoryException;

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
  public void setPrefix(String prefix, String uri) throws NamespaceException;

  /**
   * Returns all prefixes currently set for this ticket. This includes all
   * those registered in the <code>NamespaceRegistry</code> but <i>not
   * over-ridden</i> by a <code>Ticket.setPrefix</code>, plus those currently set
   * locally by <code>Ticket.setPrefix</code>.
   *
   * @return a string array
   */
  public String[] getPrefixes();

  /**
   * For a given prefix, returns the URI to which it is mapped as currently
   * set in this ticket. If the prefix is unknown a NamespaceException is thrown.
   *
   * @param prefix a string
   * @return a string
   * @throws NamespaceException if the prefix is unknown.
   */
  public String getURI(String prefix) throws NamespaceException;

  /**
   * Releases all resources associate with this ticket. Calling this method
   * is not mandatory since a good implementaion should automatically timeout
   * anyway. However, when more precise control over resource allocation is
   * need this method can be used.
   */
  public void logout();
}