/*
 * $Id: LockCapabilities.java,v 1.2 2004/07/24 00:16:22 benjmestrallet Exp $
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
 * Allows discovery of locking support.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * Returned by <code>{@link javax.jcr.Workspace#getLockCapabilities()}</code>. Provides information
 * about what locking features are supported by this implementation.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface LockCapabilities {

  /**
   * Reports whether exclusive locks are supported by this implementation.
   *
   * @return <code>true</code> if exclusive locks are supported;
   *         <code>false</code> otherwise.
   */
  public boolean isExclusiveLockSupported();

  /**
   * Reports whether shared locks are supported by this implementation.
   *
   * @return <code>true</code> if shared locks are supported;
   *         <code>false</code> otherwise.
   */
  public boolean isSharedLockSupported();

  /**
   * Reports whether locking at the the level of individual properties is
   * supported by this implementation.
   *
   * @return <code>true</code> if property locking is supported;
   *         <code>false</code> otherwise.
   */
  public boolean isPropertyLockSupported();

  /**
   * Reports the supported lock types. If locking is supported at all, this
   * will include at least <code>{@link LockType#WRITE_LOCK}</code>. If
   * other lock types are also supported then these are returned as well.
   *
   * @return An array of <code>LockType</code> objects representing all
   *         supported lock types.
   */
  public LockType[] getSupportedLockTypes();
}
