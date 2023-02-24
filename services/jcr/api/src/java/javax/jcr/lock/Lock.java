/*
 * $Id: Lock.java,v 1.2 2004/07/24 00:16:22 benjmestrallet Exp $
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
package javax.jcr.lock;

import javax.jcr.Item;


/**
 * Represents a lock placed on an item.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * A lock is associated with an item and a user (not a ticket)
 *
 * @author Peeter Piegaze
 */
public interface Lock {

  /**
   * Returns the user ID of the user who has this lock.
   *
   * @return a user ID.
   */
  public String getOwner();

  /**
   * Returns the type of this lock. This standard specifies only one
   * type of lock: <code>LockType.WRITE_LOCK</code> though more can be
   * supported by a specific JCR implementation.
   *
   * @return the type of this lock.
   * @see LockCapabilities#getSupportedLockTypes
   * @see LockType
   */
  public int getType();

  /**
   * Returns <code>true</code> if this lock is a shared lock.
   *
   * @return <code>true</code> if this is a shered lock, <code>false</code>
   *         otherwise.
   */
  public boolean isShared();

  /**
   * Returns the locked item.
   *
   * @return an <code>Item</code>.
   */
  public Item getItem();
}
