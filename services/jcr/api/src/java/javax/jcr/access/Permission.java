/*
 * $Id: Permission.java,v 1.2 2004/07/24 00:16:22 benjmestrallet Exp $
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

/**
 * The permissions used by <code>{@link javax.jcr.access.AccessManager}</code>.
 * <p/>
 * <b>Level 2 only</b>
 * <p/>
 * <p>This interface defines the following permissions:
 * <UL>
 * <LI>ADD_NODE
 * <LI>SET_PROPERTY
 * <LI>REMOVE_ITEM
 * <LI>READ_ITEM
 * </UL>
 * <b>Level 2 only</b></code>
 * <p/>
 * Given a particular ticket, a particular <code>Permission</code> is either
 * <code>true</code> or <code>false</code> of a particular node or property.
 * The possible permissions and their meanings are:
 * <dl>
 * <dt><code>Permission.ADD_NODE</code></dt>
 * <dd>
 * If <code>true</code> of node <code>N</code> then <code>N.addNode</code>
 * is allowed. If <code>false</code> then it will throw an
 * <code>AccessDeniedException</code> on <code>save</code>. Always
 * <code>false</code> of properties.
 * </dd>
 * <dt><code>Permission.SET_PROPERTY</code></dt>
 * <dd>
 * If <code>true</code> of node <code>N</code> then <code>N.setProperty</code>
 * is allowed. If <code>true</code> of property <code>P</code> then
 * <code>P.setValue</code> is allowed. If <code>false</code> then these methods
 * will throw an <code>AccessDeniedException</code> on <code>save</code>.
 * </dd>
 * <dt><code>Permission.REMOVE_ITEM</code></dt>
 * <dd>
 * If <code>true</code> of item <code>I</code>, where the path of <code>I</code>
 * relative to <code>N</code> is <code>T</code>, then <code>N.remove(T)</code>
 * is allowed. If <code>false</code>, then it will cause an
 * <code>AccessControlException</code> on <code>save</code>.
 * </dd>
 * <dt><code>Permission.READ_ITEM</code></dt>
 * <dd>
 * If <code>true</code> of property <code>P</code>, where the path of
 * <code>P</code> relative to <code>N</code> is <code>T</code>, then
 * <code>N.getProperty(T)</code> is allowed. If <code>false</code> then it
 * throws an <code>PathNotFoundException</code>.<p>
 * If <code>true</code> of node <code>M</code>, where the path of
 * <code>M</code> relative to <code>N</code> is <code>T</code>, then
 * <code>N.getNode(T)</code> is allowed. If <code>false</code> then it
 * throws an <code>PathNotFoundException</code>.
 * </dd>
 * </dl>
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Permission {

  /**
   * The permissions defined by the JCR standard. Each constant is a
   * power of 2 (i.e. sets of permissions can be encoded as a bitmask
   * in a <code>long</code> value).
   */
  public static final long ADD_NODE = 1;
  public static final long SET_PROPERTY = 2;
  public static final long REMOVE_ITEM = 4;
  public static final long READ_ITEM = 8;

  /**
   * Returns the numerical constant identifying this permission. This value
   * is always a power of 2 ((i.e. sets of permissions can be encoded as
   * a bitmask in a <code>long</code> value).
   *
   * @return the numerical value
   * @see AccessManager#getSupportedPermissions
   */
  public long getValue();

  /**
   * Returns the descriptive name of this permission.
   *
   * @return the name
   * @see AccessManager#getSupportedPermissions
   */
  public String getName();
}

