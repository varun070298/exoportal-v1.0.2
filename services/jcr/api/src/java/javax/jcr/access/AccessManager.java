/*
 * $Id: AccessManager.java,v 1.2 2004/07/24 00:16:22 benjmestrallet Exp $
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
package javax.jcr.access;

import javax.jcr.RepositoryException;
import javax.jcr.PathNotFoundException;

/**
 * The access manager object.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * Acquired via <code>{@link javax.jcr.Workspace#getAccessManager()}</code>.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface AccessManager {

  /**
   * Returns the status of the item at <code>absPath</code> regarding
   * the specified <code>permissions</code> (under the rights held by current
   * ticket). See <code>{@link Permission}</code> for more details.
   *
   * @param absPath     an absolute path to an item.
   * @param permissions A combination of one or more of the following constants
   *                    encoded as a bitmask value:
   *                    <ul>
   *                    <li><code>Permission.ADD_NODE</code></li>
   *                    <li><code>Permission.SET_PROPERTY</code></li>
   *                    <li><code>Permission.REMOVE_ITEM</code></li>
   *                    <li><code>Permission.READ_ITEM</code></li>
   *                    </ul>
   *                    See <code>{@link Permission}</code>.
   * @return <code>true</code> if <code>permissions</code> are true for the
   *         item at <code>absPath</code>; <code>false</code> otherwise.
   * @throws PathNotFoundException    if no <code>Item</code> exists at
   *                                  <code>absPath</code>.
   * @throws RepositoryException      if an unexpected error occurs.
   * @throws IllegalArgumentException if <code>permissions</code> specifies
   *                                  invalid permissions.
   */
  public boolean isGranted(String absPath, long permissions) throws PathNotFoundException, RepositoryException;

  /**
   * Returns the set of permissions that are <code>true</code> for the item
   * at <code>absPath</code>.
   *
   * @param absPath an absolute path to an item.
   * @return A set of permissions encoded in a bitmask of permission constants
   *         defined in <code>{@link Permission}</code>
   * @throws PathNotFoundException if no <code>Item</code> exists at
   *                               <code>absPath</code>.
   * @throws RepositoryException   if an unexpected error occurs.
   */
  public long getPermissions(String absPath) throws PathNotFoundException, RepositoryException;

  /**
   * Returns an array of <code>{@link Permission}</code> objects describing
   * all permissions supported by this repository.
   *
   * @return An array of <code>Permission</code>s.
   */
  public Permission[] getSupportedPermissions();
}
