/*
 * $Id: Node.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.*;
import java.io.InputStream;
import java.util.Calendar;

/**
 * The <code>Node</code> interface represents a node in the hierarchy that
 * makes up the repository.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Node extends Item {

  /**
   * Creates a new node at <code>relPath</code>. The new node will only be
   * persisted in the workspace when <code>save()</code> and if the structure
   * of the new node (its child nodes and properties) meets the constraint
   * criteria of the parent node's node type.
   * <p/>
   * If <code>relPath</code> implies intermediary nodes that do not
   * exist then a <code>PathNotFoundException</code> is thrown.
   * <p/>
   * If an item already exists at <code>relPath</code> then an
   * <code>ItemExistsException</code> is thrown.
   * <p/>
   * If an attempt is made to add a node as a child of a
   * property then a <code>ConstraintViolationException</code> is
   * thrown immediately (not on <code>save</code>).
   * <p/>
   * Since this signature does not allow explicit node type assignment, the
   * new node's node types (primary and mixin, if applicable) will be
   * determined immediately (not on save) by the <code>NodeDef</code>s
   * in the node types of its parent. If there is no <code>NodeDef</code>
   * corresponding to the name specified for this new node, then a
   * <code>ConstraintViolationException</code> is thrown immediately (not on
   * <code>save</code>).
   *
   * @param relPath The path of the new node to be created.
   * @return The node that was added.
   * @throws ItemExistsException          If an item at the specified path already exists.
   * @throws PathNotFoundException        If the specified path implies intermediary
   *                                      nodes that do not exist.
   * @throws ConstraintViolationException if If there is no NodeDef
   *                                      corresponding to the name specified for this new node in the parent
   *                                      node's node type, or if an attempt is made to add a node as a child of a
   *                                      property.
   * @throws RepositoryException          If another error occurs.
   */
  public Node addNode(String relPath) throws ItemExistsException, PathNotFoundException, ConstraintViolationException, RepositoryException;

  /**
   * Creates a new node at <code>relPath</code> of the specified node type.
   * The same as <code>{@link #addNode(String relPath)}</code> except that the primary
   * node type type of the new node is explictly specified.
   * <p/>
   * If <code>primaryNodeTypeName</code> is not
   * recognized, then a <code>NoSuchNodeTypeException</code> is thrown.
   *
   * @param relPath             The path of the new <code>Node</code> that is to be created,
   *                            the last item of this pathwill be the name of the new <code>Node</code>.
   * @param primaryNodeTypeName The name of the primary node type of the new node.
   * @return The node that was added.
   * @throws ItemExistsException          If an item at the
   *                                      specified path already exists.
   * @throws PathNotFoundException        If specified path implies intermediary
   *                                      <code>Node</code>s that do not exist.
   * @throws NoSuchNodeTypeException      If the specified <code>nodeTypeName</code>
   *                                      is not recognized.
   * @throws ConstraintViolationException If an attempt is made to add a node as the
   *                                      child of a <code>Property</code>
   * @throws RepositoryException          if another error occurs.
   */
  public Node addNode(String relPath, String primaryNodeTypeName) throws ItemExistsException, PathNotFoundException, NoSuchNodeTypeException, ConstraintViolationException, RepositoryException;

  /**
   * Adds the existing node at <code>absPath</code> as child of this node, thus adding
   * <code>this</code> node as an addional parent of the node at <code>absPath</code>.
   * <p/>
   * This change will be persisted (if valid) on <code>save</code>.
   * <p/>
   * If the node at <code>absPath</code> is not of mixin type
   * <code>mix:referenceable</code>, a <code>ConstraintViolationException</code>
   * will be thrown on <code>save</code>.
   * <p/>
   * The name of the new child node as accessed from <code>this</code> node
   * will be the same as its current name in <code>absPath</code> (that is, the last path
   * segment in that <code>absPath</code>).
   *
   * @param absPath The absolute path of the new child node.
   * @return The new child node.
   * @throws PathNotFoundException If no node exists at <code>absPath</code>.
   * @throws RepositoryException   In level 2: If another error occurs.
   */
  public Node addExistingNode(String absPath) throws PathNotFoundException, RepositoryException;

  /**
   * The same as <code>addExistingNode(String absPath)</code> except that the
   * node at <code>absPath</code> adopts <code>newName</code> in the path
   * where this node is its parent.
   *
   * @param absPath The absolute path of the new child node.
   * @param newName The new name for this node when referenced as a child of this node.
   * @return The new child node.
   * @throws PathNotFoundException If no node exists at <code>absPath</code>.
   * @throws RepositoryException   If another error occurs.
   */
  public Node addExistingNode(String absPath, String newName) throws PathNotFoundException, RepositoryException;

  /**
   * Sets the specified property to the specified value. If the property does
   * not yet exist, it is created. The property type of the property will be
   * that specified by the node type of <code>this</code> node (the one on
   * which this method is being called). If the </code>PropertyType</code>
   * of the supplied <code>Value</code> object (recall that a
   * <code>{@link Value}</code> object records the property type of its
   * contained value) is different from that required, a best-effort
   * conversion is attempted.
   * <p/>
   * If the node type of the parent node does not specify a
   * specific property type for the property being set, then
   * the property type of the supplied <code>Value</code> object
   * is used.
   * <p/>
   * If the property already exists (has previously been set) it assumes
   * the new value. If the node type of the parent node does not specify a
   * specific property type for the property being set, then the property
   * will also assume the new type (if different).
   * <p/>
   * To erase a property, use <code>{@link #remove(String relPath)}</code>.
   * <p/>
   * To persist the addition or change of a property to the workspace
   * <code>{@link #save}</code> must be called on
   * this node (the parent of the property being set) or a higher-order
   * ancestor of the property.
   *
   * @param name  The name of a property of this node
   * @param value The value to be assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, Value value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value and the specified type.
   * If the property does not yet exist, it is created.
   * <p/>
   * If the node type of the parent node does not specify a
   * specific property type for the property being set, then the property type
   * specified by the <code>type</code> parameter is used.
   * <p/>
   * If the property already exists (has previously been set) it assumes both
   * the new value and new type.
   * <p/>
   * To erase a property, use
   * <code>{@link #remove(String relPath)}</code>.
   * <p/>
   * To persist the addition or change of a property to the workspace
   * <code>save()</code> must be called on
   * <code>this</code> node (the parent of the property being set) or a higher-order
   * ancestor of the property.
   *
   * @param name  The name of a property of this node
   * @param value The value to be assigned
   * @param type  The type of the property
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if the type or format of <code>value</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>value</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code>.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, Value value, int type) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified array of values (assuming it
   * is a multi-value property) and the specified type. Same as
   * <code>{@link #setProperty(String name, Value value, int type)}<code>
   * except that an array of <code>Value</code> objects is assigned instead
   * of a single <code>Value</code>. If an array of more than one element
   * is passed and the property is not  multi-valued then a
   * <code>ValueFormatException</code> is thrown.
   *
   * @param name   the name of the property to be set.
   * @param values an array of <code>Value</code> objects.
   * @param type   a <code>PropertyType</code> constant.
   * @return the updated <code>Property</code> object.
   * @throws ValueFormatException if the type or format of <code>values</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>values</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code> or if an array of more than one value is being passed
   *                              and the property is not multi-valued.
   * @throws RepositoryException  if another error occurs.
   */
  public Property setProperty(String name, Value[] values, int type) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value and the specified type.
   * If the property does not yet exist, it is created. Same as
   * {@link #setProperty(String name, Value value, int type)} except the value
   * is passed as a <code>String</code> instead of a <code>Value</code> object.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @param type  The type of the property
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if the type or format of <code>value</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>value</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code>.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, String value, int type) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified array of values (assuming it
   * is a multi-value property) and the specified type. Same as
   * <code>{@link #setProperty(String name, String[] value, int type)}<code>
   * except that an array of <code>String</code> objects is assigned instead
   * of a single <code>String</code>. If an array of more than one element
   * is passed and the property is not multi-valued then a
   * <code>ValueFormatException</code> is thrown.
   *
   * @param name   the name of the property to be set.
   * @param values an array of <code>String</code> objects.
   * @param type   a <code>PropertyType</code> constant.
   * @return the updated <code>Property</code> object.
   * @throws ValueFormatException if the type or format of <code>values</code>
   *                              is incompatible with the type of the specified property or if
   *                              <code>values</code> is incompatible with (i.e. can not be converted to)
   *                              <code>type</code> or if an array of more than one value is being passed
   *                              and the property is not multi-valued.
   * @throws RepositoryException  if another error occurs.
   */
  public Property setProperty(String name, String[] values, int type) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * The property type of the property being set is determined
   * by the node type of <code>this</code> node (the one on which
   * this method is being called). If this is something other than
   * <code>PropertyType.STRING</code>, a best-effort conversion is attempted.
   * If the conversion fails, a <code>ValueFormatException</code> is
   * thrown.
   * <p/>
   * If the node type of <code>this</code> node does not
   * specify a particular property type for the property being set
   * then <code>PropertyType.STRING</code> is used.
   * <p/>
   * If the property
   * already exists (has previously been set) it assumes both the new value
   * and new type (if different). To erase a property, use
   * <code>{@link #remove(String relPath)}</code>.
   * <p/>
   * To persist the addition or change of a property to the workspace
   * <code>save</code> must be called.
   * <p/>
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, String value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * Same as <code>{@link #setProperty(String name, String value)}</code>
   * except that the value passed is an <code>InputStream</code> and therefore the
   * implied property type is <code>PropertyType.BINARY</code>.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, InputStream value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * Same as <code>{@link #setProperty(String name, String value)}</code>
   * except that the value passed is a <code>boolean</code> and therefore the
   * implied property type is <code>PropertyType.BOOLEAN</code>.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, boolean value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * Same as <code>{@link #setProperty(String name, String value)}</code>
   * except that the value passed is a <code>double</code> and therefore the implied
   * property type is <code>PropertyType.DOUBLE</code>.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, double value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * Same as <code>{@link #setProperty(String name, String value)}</code>
   * except that the value passed is a <code>long</code> and therefore the implied
   * property type is <code>PropertyType.LONG</code>.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, long value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the specified property to the specified value.
   * If the property does not yet exist, it is created.
   * Same as <code>{@link #setProperty(String name, String value)}</code>
   * except that the value passed is a <code>Calendar</code> and therefore the implied
   * property type is <code>PropertyType.DATE</code>.
   *
   * @param name  The name of a property of this node
   * @param value The value to assigned
   * @return The updated <code>Property</code> object
   * @throws ValueFormatException if <code>value</code> is incompatible with
   *                              (i.e. can not be converted to) the type of the specified property.
   * @throws RepositoryException  If another error occurs.
   */
  public Property setProperty(String name, Calendar value) throws ValueFormatException, RepositoryException;

  /**
   * Removes the parent-child binding along relPath to the item. If this item
   * is a node that has other references to it (due to multiple parents),
   * then all of these other references to the node are preserved. If the
   * item at path has no other references to it then it is actually erased,
   * along with any child items that also have no other references and so on
   * down the subtree. A call to save (either on Ticket or on an ancestor of
   * the newly removed node) is required to persist the removal.
   *
   * @param relPath The path of the item to be removed.
   * @throws PathNotFoundException if no <code>Item</code> exists at
   *                               <code>relPath</code>.
   * @throws RepositoryException   if another error occurs.
   */
  public void remove(String relPath) throws PathNotFoundException, RepositoryException;

  /**
   * Validates and (if validation succeeds) persists all changes to this node
   * made via the ticket through which this <code>Node</code> object was
   * acquired.
   * <p/>
   * If <code>shallow</code> is <code>false</code> then all pending changes
   * to this node and all its descendants are validated and (if validation
   * succeeds) persisted.
   * <p/>
   * If <code>shallow</code> is <code>true</code> then pending changes to
   * this node only are validated and (if validation succeeds) persisted.
   * The changes persisted are:
   * <ul>
   * <li>New properties added to this node.
   * <li>Changes to values of existing properties of this node
   * <li>Removal of properties from this node
   * <li>Removal of child nodes from this node
   * </ul>
   * <p/>
   * Constraints mandated by node types are validated on save. If validation
   * fails a <code>ConstraintViolationException</code> is thrown and the
   * state of the transient storage is left unaffected.
   * <p/>
   * An <code>AccessDeniedException</code> will be thrown if an attempt is
   * made to save changes for which the current ticket does not have sufficient
   * access rights.
   * <p/>
   * Throws an <code>ActionVetoedException</code> If a <code>VetoableEventListener</code>
   * vetoes one of the changes being saved.
   * <p/>
   * If save succeeds then the changes persisted are removed from the cache
   * of pending changes in the ticket.
   *
   * @param shallow a boolean. If <code>false</code> then this node and its
   *                subtrree is saved. If <code>true</code> then only this node is saved.
   * @throws ConstraintViolationException If any of the changes would
   *                                      violate a constraint as defined by the node type of the respective node.
   * @throws AccessDeniedException        If the current ticket does not have
   *                                      sufficient access rights to complete the operation.
   * @throws ActionVetoedException        If a <code>VetoableEventListener</code>
   *                                      vetoes one of the changes being saved.
   * @throws RepositoryException          If another error occurs.
   */
  public void save(boolean shallow) throws AccessDeniedException, ConstraintViolationException, ActionVetoedException, RepositoryException;

  /**
   * Returns the node at <code>relPath</code> relative to <code>this</code> node.
   * The properties and child nodes of the returned node can then be read
   * (and if permissions allow) changed and written. However, any changes
   * made to this node, its properties or its child nodes
   * (and their properties and child nodes, etc.) will only be persisted to
   * the repository upon calling save. Within the scope of a single
   * <code>Ticket</code> object, if a node has been acquired with
   * <code>getNode</code>, any subsequent call of <code>getNode</code>
   * re-acquiring the same node will return a reference to same object,
   * not a new copy.
   *
   * @param relPath The relative path of the node to retrieve.
   * @return The node at <code>relPath</code>.
   * @throws PathNotFoundException If no node exists at the
   *                               specified path.
   * @throws RepositoryException   If another error occurs.
   */
  public Node getNode(String relPath) throws PathNotFoundException, RepositoryException;

  /**
   * Returns a <code>NodeIterator</code> over all child <code>Node</code>s of
   * this <code>Node</code>. Does <i>not</i> include properties of this
   * <code>Node</code>. The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @return A <code>NodeIterator</code> over all child <code>Node</code>s of
   *         this <code>Node</code>.
   * @throws RepositoryException If an unexpected error occurs.
   */
  public NodeIterator getNodes() throws RepositoryException;

  /**
   * Gets all child nodes of this node that match <code>namePattern</code>.
   * The pattern may be a full name or a partial name with one or more
   * wildcard characters ("<code>*</code>"), or a disjunction (using the
   * "<code>|</code>" character to represent logical <code>OR</code>) of
   * these. For example,
   * <p/>
   * <code>N.getNodes("jcr:* | myapp:report")</code>
   * <p/>
   * would return a <code>NodeIterator</code> holding all child nodes of
   * <code>N</code> that are either called <code>myapp:report</code> or begin
   * with the prefix <code>jcr:</code>. The pattern does not represent paths,
   * that is, it may not contain <code>/</code> characters.
   * <p/>
   * The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param namePattern a name pattern
   * @return a <code>NodeIterator</code>
   * @throws RepositoryException If an unexpected error occurs.
   */
  public NodeIterator getNodes(String namePattern) throws RepositoryException;

  /**
   * Returns the property at <code>relPath</code> relative to <code>this</code>
   * node. The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param relPath The relative path of the property to retrieve.
   * @return The property at <code>relPath</code>.
   * @throws PathNotFoundException If no property exists at the
   *                               specified path.
   * @throws RepositoryException   If another error occurs.
   */
  public Property getProperty(String relPath) throws PathNotFoundException, RepositoryException;

  /**
   * Returns all properties of this node.
   * Returns a <code>PropertyIterator</code> over all properties
   * of this node. Does <i>not</i> include child <i>nodes</i> of this
   * node. The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @return A <code>PropertyIterator</code>.
   * @throws RepositoryException If an error occurs.
   */
  public PropertyIterator getProperties() throws RepositoryException;

  /**
   * Gets all properties of this node that match <code>namePattern</code>.
   * The pattern may be a full name or a partial name with one or more
   * wildcard characters ("<code>*</code>"), or a disjunction (using the
   * "<code>|</code>" character to represent logical <code>OR</code>) of
   * these. For example,
   * <p/>
   * <code>N.getProperties("jcr:* | myapp:name")</code>
   * <p/>
   * would return a <code>PropertyIterator</code> holding all properties of
   * <code>N</code> that are either called <code>myapp:name</code> or begin
   * with the prefix <code>jcr:</code>. The pattern does not represent paths,
   * that is, it may not contain <code>/</code> characters.
   * <p/>
   * The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param namePattern a name pattern
   * @return a <code>PropertyIterator</code>
   * @throws RepositoryException If an unexpected error occurs.
   */
  public PropertyIterator getProperties(String namePattern) throws RepositoryException;

  /**
   * Returns the first property of <i>this</i> node found with the specified value.
   * What makes a particular property "first" (that is, the search order) is
   * implementaion dependent. If the specified value and the value of a
   * property are of different types then a conversion is attempted before the
   * equality test is made. Returns <code>null</code> if no such property is
   * found. In the case of multivalue properties, a property qualifies as
   * having the specified value if and only if at least one of its values matches.
   * The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param value A <code>Value</code> object.
   * @return The first property of <i>this</i> node found with the specified value.
   * @throws RepositoryException If an unexpected error occurs.
   */
  public Property findProperty(Value value) throws RepositoryException;

  /**
   * Returns all properties of <i>this</i> node with the specified value.
   * If the spedified value and the value of a property are of different types
   * then a conversion is attempted before the equality test is made. Returns
   * an empty iterator if no such property could be found. In the case of
   * multivalue properties, a property qualifies as having the specified value
   * if and only if at least one of its values matches.
   * The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @param value A <code>Value</code> object.
   * @return A PropertyIterator holding all properties of this node with the
   *         specified value. Returns an empty iterator if no such property could be found.
   * @throws RepositoryException If an unexpected error occurs.
   */
  public PropertyIterator findProperties(Value value) throws RepositoryException;

  /**
   * Returns the deepest primary child item accessible via a chain of
   * primary child items from this node.
   * A node's type can specifiy a maximum of one of
   * its child items (child node or property) as its <i>primary child item</i>.
   * This method traverses the chain of primary child items of this node
   * until it either encounters a property or encounters a node that does not
   * have a primary child item. It then returns that property or node. If
   * this node itself (the one that this method is being called on) has no
   * primary child item then this method throws a
   * <code>ItemNotFoundException</code>. The same <code>save</code> and re-acquisition
   * semantics apply as with <code>{@link #getNode(String)}</code>.
   *
   * @return the deepest primary child item accessible from this node via
   *         a chain of primary child items.
   * @throws ItemNotFoundException if this node does not have a primary
   *                               child item.
   * @throws RepositoryException   If another error occurs.
   */
  public Item getPrimaryItem() throws ItemNotFoundException, RepositoryException;

  /**
   * Returns the UUID of this node as recorded in the node's jcr:UUID
   * property. This method only works on nodes of mixin node type
   * <code>mix:referenceable</code>. On nonreferenceable nodes, this method
   * throws an <code>UnsupportedRepositoryOperationException</code>.
   *
   * @return the UUID of this node
   * @throws UnsupportedRepositoryOperationException
   *                             If this node nonreferenceable.
   * @throws RepositoryException If another error occurs.
   */
  public String getUUID() throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Indicates whether a node exists at <code>relPath</code>
   * Returns <code>true</code> if a node exists at <code>relPath</code> and
   * <code>false</code> otherwise.
   *
   * @param relPath The path of a (possible) node.
   * @return <code>true</code> if a node exists at <code>relPath</code>;
   *         <code>false</code> otherwise.
   * @throws RepositoryException If an unspecified error occurs.
   */
  public boolean hasNode(String relPath) throws RepositoryException;

  /**
   * Indicates whether a property exists at <code>relPath</code>
   * Returns <code>true</code> if a property exists at <code>relPath</code> and
   * <code>false</code> otherwise.
   *
   * @param relPath The path of a (possible) property.
   * @return <code>true</code> if a property exists at <code>relPath</code>;
   *         <code>false</code> otherwise.
   * @throws RepositoryException If an unspecified error occurs.
   */
  public boolean hasProperty(String relPath) throws RepositoryException;

  /**
   * Indicates whether this node has child nodes.
   * Returns <code>true</code> if this node has one or more child nodes;
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if this node has one or more child nodes;
   *         <code>false</code> otherwise.
   * @throws RepositoryException If an unspecified error occurs.
   */
  public boolean hasNodes() throws RepositoryException;

  /**
   * Indicates whether this node has properties.
   * Returns <code>true</code> if this node has one or more properties;
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if this node has one or more properties;
   *         <code>false</code> otherwise.
   * @throws RepositoryException If an unspecified error occurs.
   */
  public boolean hasProperties() throws RepositoryException;

  /**
   * Returns the primary node type of this node.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType getPrimaryNodeType();

  /**
   * Returns an array of NodeType objects representing the mixin node types
   * assigned to this node.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType[] getMixinNodeTypes();

  /**
   * Indicates whether this node is of the specified node type.
   * Returns <code>true</code> if this node is of the specified node type
   * or a subtype of the specified node type. Returns <code>false</code> otherwise.
   * This method provides a quick method for determining whether
   * a particular node is of a particular type without having to
   * manually search the inheritance hierarchy (which, in some implementations
   * may be a multiple-inhertiance hierarchy, making a manual search even
   * more complex). This method works for both perimary node types and mixin
   * node types.
   *
   * @param nodeTypeName the name of a node type.
   * @return true if this node is of the specified node type
   *         or a subtype of the specified node type; returns false otherwise.
   * @throws RepositoryException If an unspecified error occurs.
   */
  public boolean isNodeType(String nodeTypeName) throws RepositoryException;

  /**
   * Adds the specified mixin node type to this node. If a conflict with
   * another assigned mixin or the main node type results, then an exception
   * is thrown on save. Adding a mixin node type to a node immediately adds
   * the name of that type to the list held in that node’s
   * <code>jcr:mixinTypes</code> property.
   *
   * @param mixinName
   */
  public void addMixin(String mixinName);

  /**
   * Returns the definition of <i>this</i> <code>Node</code>. This method is
   * actually a shortcut to searching through this node's parent's node type
   * (and its supertypes) for the child node definition applicable to this
   * node.
   *
   * @return a <code>NodeDef</code> object.
   * @see NodeType#getChildNodeDefs
   */
  public NodeDef getDefinition();

  /**
   * Creates a new version with a system generated version name and returns that version.
   *
   * @return a <code>Version</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public Version checkin() throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Sets this versionable node to checked-out status by setting its
   * <code>jcr:isCheckedOut</code> property to true.
   *
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void checkout() throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Updates this node to reflect the state (i.e., have the same properties
   * and child nodes) of this node's corresponding node in srcWorkspace (that
   * is, the node in srcWorkspace with the same UUID as this node). If
   * shallow is set to false, then only this node is updated. If shallow is
   * set to true then every node with a UUID in the subtree rooted at this
   * node is updated. If the current ticket does not have sufficient rights
   * to perform the update or the specified workspace does not exist, a
   * <code>NoSuchWorkspaceException</code> is thrown. If another error occurs
   * then a RepositoryException is thrown. If the update succeeds the changes
   * made to this node are persisted immediately, there is no need to call
   * save.
   *
   * @param srcWorkspaceName the name of the source workspace.
   * @param shallow          a boolean
   * @throws RepositoryException If another error occurs.
   */
  public void update(String srcWorkspaceName, boolean shallow) throws NoSuchWorkspaceException, RepositoryException;

  /**
   * Performs the same function as update (above) with one restriction:
   * merge only succeeds if the base version of the corresponding node in
   * <code>srcWorkspace</code> is a successor (or a successor of a successor, etc., to
   * any degree of separation) of the base version of this node. Otherwise,
   * the operation throws a <code>MergeException</code>. In repositories that
   * do not support versioning, <code>merge</code> throws an
   * <code>UnsupportedRepositoryOperationException</code>. If the current
   * ticket does not have sufficient rights to perform the <code>merge</code> or the
   * specified workspace does not exist, a <code>NoSuchWorkspaceException</code> is thrown.
   * If the <code>merge</code> succeeds, the changes made to this node are
   * persisted immediately, there is no need to call <code>save</code>.
   *
   * @param srcWorkspace the name of the source workspace.
   * @param shallow      a boolean
   * @throws UnsupportedRepositoryOperationException
   *                                  if versioning is not supported.
   * @throws MergeException           succeeds if the base version of the corresponding
   *                                  node in srcWorkspace is not a successor of the base version of this node.
   * @throws NoSuchWorkspaceException If the current
   *                                  ticket does not have sufficient rights to perform the <code>merge</code> or the
   *                                  specified workspace does not exist.
   * @throws RepositoryException      If another error occurs.
   */
  public void merge(String srcWorkspace, boolean shallow) throws UnsupportedRepositoryOperationException, NoSuchWorkspaceException, MergeException, RepositoryException;

  /**
   * Returns <code>true</code> if this node is currently checked-out and <code>false</code> otherwise.
   *
   * @return a boolean
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public boolean isCheckedOut() throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Restores this node to the state recorded in the specified version.
   *
   * @param versionName
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(String versionName) throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Restores this node to the state recorded in the specified version.
   *
   * @param version a <code>Version</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(Version version) throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Restores this node to the state recorded in the version corresponding to the specified date.
   *
   * @param date a <codeCalendar</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restore(Calendar date) throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Restores this node to the state recorded in the version specified by
   * <code>versionLabel</code>.
   *
   * @param versionLabel a String
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public void restoreByLabel(String versionLabel) throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Returns the <code>VersionHistory</code> object of this node. This object
   * is simply a wrapper for the <code>nt:versionHistory</code> node holding
   * this node's versions.
   *
   * @return a <code>VersionHistory</code> object
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public VersionHistory getVersionHistory() throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Returns the current base version of this versionable node.
   *
   * @return a <code>Version</code> object.
   * @throws UnsupportedRepositoryOperationException
   *                             if versioning is not supported.
   * @throws RepositoryException If another error occurs.
   */
  public Version getBaseVersion() throws UnsupportedRepositoryOperationException, RepositoryException;
}