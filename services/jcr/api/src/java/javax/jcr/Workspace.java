/*
 * $Id: Workspace.java,v 1.5 2004/08/12 18:13:43 benjmestrallet Exp $
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

import javax.jcr.access.AccessDeniedException;
import javax.jcr.access.AccessManager;
import javax.jcr.lock.LockCapabilities;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.QueryManager;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Represents a view onto the the content repository
 * associated with a particular <code>Ticket</code>.
 * Acquired by calling <code>{@link Ticket#getWorkspace()}</code>.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Workspace {

  /**
   * Returns the <code>Ticket</code> object through which this Workspace
   * object was acquired.
   *
   * @return a <code>{@link Ticket}</code> object.
   */
  public Ticket getTicket();

  /**
   * Copies the node at <code>srcAbsPath</code> to the new location at
   * <code>destAbsPath</code>.
   * If <code>shallow</code> is true then only the node at <code>srcAbsPath</code>
   * and its properties (but not its child nodes) is copied to the new
   * location, otherwise its entire subtree is copied. Throws an exception if
   * there is already an item at <code>destAbsPath</code>, or if some other
   * constraint (access, node type, system-level) is violated. If succesful,
   * the parent of the new node at <code>destAbsPath</code> is "saved" (i.e. the
   * change is persisted immediately, there is no need to call
   * <code>Node.save()</code>).
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
  public void copy(String srcPath, String destPath, boolean shallow)
      throws ConstraintViolationException, ItemExistsException, AccessDeniedException, RepositoryException;

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
  public void clone(String srcAbsPath, String destAbsPath, String destWorkspace, boolean shallow) throws ConstraintViolationException, AccessDeniedException, PathNotFoundException, ItemExistsException, ActionVetoedException, RepositoryException;




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
  public void move(String srcAbsPath, String destAbsPath) throws ConstraintViolationException, AccessDeniedException, PathNotFoundException, ItemExistsException, ActionVetoedException, RepositoryException;

  /**
   * Gets the <code>QueryManager</code>.
   * Gets the <code>QueryManager</code> through which searching is done.
   *
   * @return the <code>QueryManager</code> object.
   */
  public QueryManager getQueryManager();

  /**
   * Returns the NamespaceRegistry object, which is used to set the mapping
   * between namespace prefixes and URIs.
   *
   * @return the <code>NamespaceRegistry</code>.
   */
  public NamespaceRegistry getNamespaceRegistry();

  /**
   * Gets the <code>NodeTypeManager</code> object.
   * Returns the <code>NodeTypeManager</code> through which node type
   * information can be queried.
   *
   * @return a <code>NodeTypeManager</code> object.
   */
  public NodeTypeManager getNodeTypeManager();

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
  public AccessManager getAccessManager() throws UnsupportedRepositoryOperationException;

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
  public LockCapabilities getLockCapabilities() throws UnsupportedRepositoryOperationException;

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
  public ObservationManager getObservationManager() throws UnsupportedRepositoryOperationException;

  /**
   * Serializes the subtree rooted at the node at <code>absPath</code>
   * (it must be a node, not a property) as a stream of SAX
   * events to the supplied <code>org.xml.sax.ContentHandler</code>.
   * The XML represented by these SAX events will be in the
   * <b>system view</b> form.
   * <p/>
   * If <code>binaryAsLink</code> is <code>true</code> then any properties
   * of <code>PropertyType.BINARY</code> will be replaced in the serialized
   * output by properties of <code>PropertyType.SOFTLINK</code> recording
   * their own absolute path.
   * <p/>
   * If <code>binaryAsLink</code> is <code>false</code> then the actual value
   * of each <code>BINARY</code> property is recorded using Base64 encoding.
   * <p/>
   * If <code>noRecurse</code> is true then only the node at
   * <code>absPath</code> and its properties, but not its child nodes, is
   * serialized.
   * <p/>
   * If <code>noRecurse</code> is <code>false</code> then the entire subtree
   * rooted at <code>absPath</code> is serialized.
   * <p/>
   * If the user lacks read access to some subsection of the specified tree,
   * that section simply does not get serialized, since, from the user's
   * point of view, it is not there.
   *
   * @param absPath        The path of the root of the subtree to be serialized.
   *                       This must be the path to a node, not a property
   * @param contentHandler The  <code>org.xml.ContentHandler</code> to
   *                       which the SAX events representing the XML serialization of the subtree
   *                       will be output.
   * @param binaryAsLink   A <code>boolean</code> governing whether binary
   *                       properties are to converted to soft links.
   * @param noRecurse      A <code>boolean</code> governing whether the subtree at
   *                       absPath is to be recursed.
   * @throws PathNotFoundException if no node exists at <code>absPath</code>.
   * @throws SAXException          if an error occurs while feeding events to the
   *                               <code>org.xml.ContentHandler</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public void exportSysView(String absPath, ContentHandler contentHandler, boolean binaryAsLink, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException;

  /**
   * Serializes the subtree rooted at the node (it must be a node, not a
   * property)at <code>absPath</code> as an  XML document and streams it to
   * the supplied <code>OutputStream</code>. The resulting XML is in the
   * <b>system view</b> form.
   * <p/>
   * If <code>binaryAsLink</code> is <code>true</code> then any properties
   * of <code>PropertyType.BINARY</code> will be replaced in the serialized
   * output by properties of <code>PropertyType.SOFTLINK</code> recording
   * their own absolute path.
   * <p/>
   * If <code>binaryAsLink</code> is <code>false</code> then the actual value
   * of each <code>BINARY</code> property is recorded using Base64 encoding.
   * <p/>
   * If <code>noRecurse</code> is true then only the node at
   * <code>absPath</code> and its properties, but not its child nodes, is
   * serialized.
   * <p/>
   * If <code>noRecurse</code> is <code>false</code> then the entire subtree
   * rooted at <code>absPath</code> is serialized.
   * <p/>
   * If the user lacks read access to some subsection of the specified tree,
   * that section simply does not get serialized, since, from the user's
   * point of view, it is not there.
   *
   * @param absPath      The path of the root of the subtree to be serialized.
   * @param out          The <code>OutputStream</code> to which the XML
   *                     serialization of the subtree will be output.
   * @param binaryAsLink A <code>boolean</code> governing whether binary
   *                     properties are to converted to soft links.
   * @param noRecurse    A <code>boolean</code> governing whether the subtree at
   *                     <code>absPath</code> is to be recursed.
   * @throws IOException           if an error during an I/O operation occurs.
   * @throws PathNotFoundException if no node exists at <code>absPath</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public void exportSysView(String absPath, OutputStream out, boolean binaryAsLink, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException;

  /**
   * Same as <code>exportSysView(String absPath, ContentHandler contentHandler,
   * boolean binaryAsLink, boolean noRecurse)</code> except that the resulting
   * XML is in the <b>document view</b> form.
   *
   * @param absPath        The path of the root of the subtree to be serialized.
   * @param contentHandler The <code>org.xml.ContentHandler</code> to
   *                       which the SAX events representing the XML serialization of the subtree
   *                       will be output.
   * @param binaryAsLink   A <code>boolean</code> governing whether binary
   *                       properties are to converted to soft links.
   * @param noRecurse      A <code>boolean</code> governing whether the subtree at
   *                       <code>absPath</code> is to be recursed.
   * @throws PathNotFoundException if no node exists at <code>absPath</code>.
   * @throws SAXException          if an error occurs while feeding events to the
   *                               <code>org.xml.ContentHandler</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public void exportDocView(String absPath, ContentHandler contentHandler, boolean binaryAsLink, boolean noRecurse) throws PathNotFoundException, SAXException, RepositoryException;

  /**
   * Same as <code>exportSysView(String absPath, OutputStream out,
   * boolean binaryAsLink, boolean noRecurse</code> except that the resulting
   * XML is in the <b>document view</b> form.
   *
   * @param absPath      The path of the root of the subtree to be serialized.
   * @param out          The <code>OutputStream</code> to which the XML
   *                     serialization of the subtree will be output.
   * @param binaryAsLink A <code>boolean</code> governing whether binary
   *                     properties are to converted to soft links.
   * @param noRecurse    A <code>boolean</code> governing whether the subtree at
   *                     <code>absPath</code> is to be recursed.
   * @throws IOException           if an error during an I/O operation occurs.
   * @throws PathNotFoundException if no node exists at <code>absPath</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public void exportDocView(String absPath, OutputStream out, boolean binaryAsLink, boolean noRecurse) throws IOException, PathNotFoundException, RepositoryException;
}
