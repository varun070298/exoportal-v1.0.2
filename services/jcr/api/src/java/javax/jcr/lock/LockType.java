/*
 * $Id: LockType.java,v 1.2 2004/07/24 00:16:22 benjmestrallet Exp $
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

/**
 * The lock types defined by the JCR standard.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * This interface defines the following lock type:
 * <UL>
 * <LI>WRITE_LOCK
 * </UL>
 * Implementations of the JCR standard may support additional lock types.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface LockType {

  /**
   * The lock type <code>WRITE_LOCK</code>.
   * If a lock of this type is put on an item then any attempt to
   * write to the item (i.e., add a child node or property to a node, or
   * set the value of a property) will throw an <code>AccessDeniedException</code>
   * if the user of the ticket attempting to perform the operation is not an owner of
   * the lock.
   */
  public static final int WRITE_LOCK = 1;

  /**
   * Returns the numerical constant identifying this lock type.
   *
   * @return the numerical value
   * @see LockCapabilities#getSupportedLockTypes
   */
  public int getValue();

  /**
   * Returns the descriptive name of this lock type.
   *
   * @return the name
   * @see LockCapabilities#getSupportedLockTypes
   */
  public String getName();
}

