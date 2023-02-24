/*
 * $Id: Item.java,v 1.2 2004/07/24 00:16:21 benjmestrallet Exp $
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
import javax.jcr.lock.Lock;

/**
 * The <code>Item</code> is the base interface of <code>{@link Node}</code>
 * and <code>{@link Property}</code>.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Item {

  /**
   * Returns the path to this <code>Item</code>.
   * If this item or an ancestor has multiple parents then this item will
   * have more than one path.  In that case, this method returns one of those
   * paths. The mechanism by which the implementation chooses which path to
   * return is left unspecified, the only requirement being that for a given
   * <code>Item</code> this method always returns the same path.  To get all
   * paths to this <code>Item</code> use <code>{@link #getPaths()}</code>.
   *
   * @return the path (or one of the paths) of this <code>Item</code>.
   */
  public String getPath();

  /**
   * Returns all paths to this <code>Item</code>.
   *
   * @return An array of strings: all paths that lead to this
   *         <code>Item</code>.
   */
  public StringIterator getPaths();

  /**
   * Returns the name of this <code>Item</code>.
   * The name is the last item in the path. If this <code>Item</code>
   * is the root node of the repository (i.e. if
   * <code>this.getDepth() == 0</code>), an empty string will be returned.
   * <p/>
   * If this item has multple paths then the name returned
   * is the last item in the path returned by <code>{@link #getPath()}</code>.
   *
   * @return the (or a) name of this <code>Item</code> or an empty string
   *         if this <code>Item</code> is the root node.
   */
  public String getName();

  /**
   * Returns the ancestor of the specified absolute degree.
   * An ancestor of
   * absolute degree <i>x</i> is the <code>Item</code> that is <i>x</i>
   * levels down along the path from the root node to <i>this</i>
   * <code>Item</code>.
   * <ul>
   * <li><i>degree</i> = 0 returns the root node.
   * <li><i>degree</i> = 1 returns the child of the root node along the path
   * to <i>this</i> <code>Item</code>.
   * <li><i>degree</i> = 2 returns the grandchild of the root node along the
   * path to <i>this</i> <code>Item</code>.
   * <li>And so on to <i>degree</i> = <i>n</i>, where <i>n</i> is the depth
   * of <i>this</i> <code>Item</code>, which returns <i>this</i>
   * <code>Item</code> itself.
   * </ul>
   * If <i>degree</i> &gt; <i>n</i> is specified then a
   * <code>ItemNotFoundException</code> is thrown.
   * <p/>
   * If multiple paths exist to this
   * <code>Item</code> then the path used is the same one that is returned
   * by <code>{@link #getPath()}</code>.
   *
   * @param degree An integer, 0 &lt;= <i>degree</i> &lt;= <i>n</i> where <i>n</i> is the depth
   *               of <i>this</i> <code>Item</code> along the path returned by
   *               <code>{@link #getPath()}</code>.
   * @return The ancestor of the specified absolute degree of this
   *         <code>Item</code> along the path returned by
   *         <code>{@link #getPath()}</code>.
   * @throws ItemNotFoundException if <i>degree</i> &lt; 0 or
   *                               <i>degree</i> &gt; <i>n</i> where <i>n</i> is the is the depth of
   *                               <i>this</i> <code>Item</code> along the path returned by
   *                               <code>{@link #getPath()}</code>.
   * @throws AccessDeniedException if the current ticket does not have
   *                               sufficient access rights to complete the operation.
   * @throws RepositoryException   if another error occurs.
   */
  public Item getAncestor(int degree) throws ItemNotFoundException, AccessDeniedException, RepositoryException;

  /**
   * Returns the parent of this <code>Item</code>.
   * If this <code>Item</code> has multiple paths then this method returns
   * the parent along the path returned by <code>{@link #getPath()}</code>.
   *
   * @return The parent of this <code>Item</code> along the path returned
   *         by <code>{@link #getPath()}</code>.
   * @throws ItemNotFoundException if there is no parent.  This only happens
   *                               if <i>this</i> <code>Item</code> is the root node.
   * @throws AccessDeniedException if the current ticket does not have
   *                               sufficient access rights to complete the operation.
   * @throws RepositoryException   if another error occurs.
   */
  public Node getParent() throws ItemNotFoundException, AccessDeniedException, RepositoryException;

  /**
   * Returns all parents of this <code>Item</code>.
   *
   * @return An iterator over the parents of this <code>Item</code>.
   * @throws ItemNotFoundException if there is no parent. This only happens
   *                               if <i>this</i> <code>Item</code> is the root.
   * @throws AccessDeniedException if the current ticket does not have
   *                               sufficient access rights to complete the operation.
   * @throws RepositoryException   if another error occurs.
   */
  public NodeIterator getParents() throws ItemNotFoundException, AccessDeniedException, RepositoryException;

  /**
   * Returns the <code>Ticket</code> through which this <code>Item</code>
   * was acquired.
   * Every <code>Item</code> can ultimately be traced back through a series
   * of method calls to the call <code>{@link Ticket#getRootNode}</code>,
   * <code>{@link Ticket#getNodeByAbsPath}</code> or
   * <code>{@link Ticket#getNodeByUUID}</code>. This method returns that
   * <code>Ticket</code> object.
   *
   * @return the <code>Tickete</code> through which this <code>Item</code> was
   *         acquired.
   */
  public Ticket getTicket();

  /**
   * Accepts an <code>ItemVistor</code>.
   * Calls the appropriate <code>ItemVistor</code>
   * <code>visit</code> method of the according to whether <i>this</i>
   * <code>Item</code> is a <code>Node</code> or a <code>Property</code>.
   *
   * @param visitor The ItemVisitor to be accepted.
   * @throws RepositoryException if an error occurs.
   */
  public void accept(ItemVisitor visitor) throws RepositoryException;

  /**
   * Indicates whether this <code>Item</code> is a <code>Node</code> or a
   * <code>Property</code>.
   * Returns <code>true</code> if this <code>Item</code> is a <code>Node</code>;
   * Returns <code>false</code> if this <code>Item</code> is a <code>Property</code>.
   *
   * @return <code>true</code> if this <code>Item</code> is a
   *         <code>Node</code>, <code>false</code> if it is a <code>Property</code>.
   */
  public boolean isNode();

  /**
   * Returns the status of this <code>Item</code> regarding
   * the specified permissions.
   * Returns the status of this <code>Item</code> regarding
   * the specified <code>permissions</code> (under the rights held by current
   * ticket). See <code>{@link javax.jcr.access.Permission}</code> and
   * <code>{@link javax.jcr.access.AccessManager}</code> for more details. Equivalent to
   * <code>AccessManager.isGranted(<b>this</b>, permissions)</code>.
   *
   * @param permissions A combination of one or more of the following constants
   *                    encoded as a bitmask value:
   *                    <ul>
   *                    <li><code>Permission.ADD_NODE</code></li>
   *                    <li><code>Permission.SET_PROPERTY</code></li>
   *                    <li><code>Permission.REMOVE_ITEM</code></li>
   *                    <li><code>Permission.READ_ITEM</code></li>
   *                    </ul>
   *                    See <code>{@link javax.jcr.access.Permission}</code>.
   * @return <code>true</code> if all requested permissions are true for this
   *         <code>Item</code>; <code>false</code> otherwise.
   * @throws UnsupportedRepositoryOperationException
   *                             if access control
   *                             discovery is not supported.
   * @throws RepositoryException if another error occurs.
   */
  public boolean isGranted(long permissions) throws UnsupportedRepositoryOperationException, RepositoryException;

  /**
   * Returns the depth of this <code>Item</code> in the repository tree.
   * Returns the depth below the root node of <i>this</i> <code>Item</code>
   * (counting <i>this</i> <code>Item</code> itself).
   * <ul>
   * <li>The root node returns 0.
   * <li>A property or child node of the root node returns 1.
   * <li>A property or child node of a child node of the root returns 2.
   * <li>And so on to <i>this</i> <code>Item</code>.
   * </ul>
   * If multiple paths exist to this
   * <code>Item</code> then the path used to determine the depth is the
   * same one that is returned by <code>{@link #getPath()}</code>.
   *
   * @return The depth of this <code>Item</code> in the repository hierarchy.
   */
  public int getDepth();

  /**
   * Places a lock on this node, or throws an
   * <code>UnsupportedRepositoryOperationException</code> if locking is
   * not supported.
   * <p/>
   * Puts a lock on this item.
   * <p/>
   * If <code>recurse</code> is <code>true</code> then a lock is also placed
   * on all descendants of this item. If <code>false</code> then only this
   * item is locked.
   * <p/>
   * If <code>shared</code> is <code>true</code> then this lock is
   * a shared lock. If <code>false</code> then it is an exclusive lock.
   * <p/>
   * The lock is of the specified <code>lockType</code>. This standard
   * defines only one type, <code>LockType.WRITE_LOCK</code>, though more can
   * be added by the implementation, see <code>{@link javax.jcr.lock.LockType LockType}</code> for the
   * semantics specific to this lock type.
   * <p/>
   * <b>General points about locking</b>
   * <p/>
   * Locking is an optional part of the specification. If locking is not
   * supported then this method will throw an
   * <code>UnsupportedRepositoryOperationException</code>, even in a level 2
   * implementation.
   * <p/>
   * Even if locking is supported, the features of a particular
   * implementation can vary along the following dimensions:
   * <dl>
   * <dt>Lock Type</dt>
   * <dd>Only WRITE_LOCK is defined by this standard. An implementation may
   * define other lock types.</dd>
   * <dt>Lock Scope</dt>
   * <dd>An implementation may choose to implement exclusive locks, shared
   * locks or both.</dd>
   * <dt>Lock Level</dt>
   * <dd>An implementation may choose to implement locking on
   * nodes only or on both nodes and properties. If the implementation locks
   * only on the node level this is equivalent to a
   * "both nodes and properties" locking implementaion in which locking or
   * unlocking a node automatically performs the same operation on all its
   * properties.</dd>
   * </dl>
   * Information about what locking support is provided is available via
   * the method <code>{@link Workspace#getLockCapabilities()}</code> which returns
   * a <code>{@link javax.jcr.lock.LockCapabilities LockCapabilities}</code> object which provides methods for
   * querying the locking support provided by the implementation.
   *
   * @param recurse  if <code>true</code> set locks on all descendants also.
   * @param shared   Whether the lock is shared or exclusive
   * @param lockType The type of the lock
   * @return The Lock object.
   * @throws AccessDeniedException If the user
   *                               ticket does not have the right to lock this user <i>or</i> does have
   *                               the right to lock this node but an exclusive lock is already held by
   *                               another user.
   * @throws UnsupportedRepositoryOperationException
   *                               If the lock type or
   *                               <code>shared</code> status is not supported in this
   *                               implementation or locking in general is not supported.
   * @throws RepositoryException   if another error occurs.
   */
  public Lock lock(boolean recurse, boolean shared, int lockType) throws UnsupportedRepositoryOperationException, AccessDeniedException, RepositoryException;

  /**
   * Removes the specified lock from this node or throws an
   * <code>UnsupportedRepositoryOperationException</code> if locking is not
   * supported.
   * <p/>
   * If this node is not locked, then this method has no effect.
   * If this node is locked and the user of this ticket is a user of the
   * lock then the lock is removed.  If this node is locked but the user of
   * this ticket does not have the power to unlock it, then an
   * <code>AccessDeniedException</code> is thrown.
   *
   * @throws AccessDeniedException In level 1: Never. In level 2: If the user
   *                               of this ticket does not have the right to unlock this node.
   * @throws UnsupportedRepositoryOperationException
   *                               If locking in general is not supported.
   */
  public void unlock(Lock lock) throws UnsupportedRepositoryOperationException, AccessDeniedException;

  /**
   * Returns all locks on this node or throws an
   * <code>UnsupportedRepositoryOperationException</code> if locking is not
   * supported.
   * <p/>
   * Returns all locks on this node, if there are any, otherwise returns
   * <code>null</code>.
   *
   * @return The locks on this node
   * @throws UnsupportedRepositoryOperationException
   *          If locking in general is not supported.
   */
  public Lock[] getLocks() throws UnsupportedRepositoryOperationException;

  /**
   * Returns <code>true</code> if there is a lock on this node, false
   * otherwise.
   *
   * @return a boolean.
   */
  public boolean hasLocks();

  /**
   * Returns <code>true</code> if <code>this</code> <code>Item</code>
   * (the Java object) represents the same repository item as the Java
   * object <code>otherItem</code>.
   * <p/>
   * In some implementations the results of <code>Item.isIdentical</code>
   * and <code>Object.equals</code> will not always be the same. Therefore,
   * in general, clients of the API cannot rely on <code>Object.equals</code>
   * to test for identity of repository items and must use this method.
   *
   * @param otherItem the item to be tested for identity with
   *                  <code>this</code>.
   * @return a <code>boolean</code>.
   */
  public boolean isIdentical(Item otherItem);
}