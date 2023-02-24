/*
 * $Id: NodeType.java,v 1.3 2004/08/01 22:14:03 benjmestrallet Exp $
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
package javax.jcr.nodetype;

import javax.jcr.Value;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

/**
 * Represents a node type.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface NodeType {

  /**
   * Returns the name of the node type.
   *
   * @return the name of the node type
   */
  public String getName();

  /**
   * Returns <code>true</code> if this node type is a mixin node type.
   * Returns <code>false</code> if this node type is a primary node type.
   *
   * @return a boolean
   */
  public boolean isMixin();

  /**
   * Returns all supertypes of this node type including both those directly
   * declared and those inherited. For primary types, this list will always
   * include at least <code>nt:base</code>. For mixin types, there is no
   * required base type.
   *
   * @return an array of <code>NodeType</code> objects.
   * @see #getDeclaredSupertypes
   */
  public NodeType[] getSupertypes();

  /**
   * Returns all <i>direct</i> supertypes as specified in the declaration of
   * <i>this</i> node type. In single inheritance systems this will always be
   * an array of size 0 or 1. In systems that support multiple inheritance of
   * node types this array may be of size greater than 1.
   *
   * @return an array of <code>NodeType</code> objects.
   * @see #getSupertypes
   */
  public NodeType[] getDeclaredSupertypes();

  /**
   * Returns an array containing the property definitions of this node type,
   * including the property definitions inherited from supertypes of this node
   * type.
   *
   * @return an array containing the property definitions.
   * @see #getDeclaredPropertyDefs
   */
  public PropertyDef[] getPropertyDefs();

  /**
   * Returns an array containing the property definitions explicitly specified
   * in the declaration of <i>this</i> node type. This does <i>not</i> include
   * property definitions inherited from supertypes of this node type.
   *
   * @return an array containing the property definitions.
   * @see #getPropertyDefs
   */
  public PropertyDef[] getDeclaredPropertyDefs();

  /**
   * Returns an array containing the child node definitions of this node type,
   * including the child node definitions inherited from supertypes of this
   * node type.
   *
   * @return an array containing the child node definitions.
   * @see #getDeclaredChildNodeDefs
   */
  public NodeDef[] getChildNodeDefs();

  /**
   * Returns an array containing the child node definitions explicitly
   * specified in the declaration of <i>this</i> node type. This does
   * <i>not</i> include child node definitions inherited from supertypes of
   * this node type.
   *
   * @return an array containing the child node definitions.
   * @see #getChildNodeDefs
   */
  public NodeDef[] getDeclaredChildNodeDefs();

  /**
   * Returns <code>true</code> if setting <code>propertyName</code> to
   * <code>value</code> is allowed by this node type. Otherwise returns
   * <code>false</code>.
   *
   * @param propertyName The name of the property
   * @param value        A <code>Value</code> object.
   */
  public boolean canSetProperty(String propertyName, Value value);

  /**
   * Returns <code>true</code> Checks if adding a child node called
   * <code>childNodeName</code> is allowed by <i>this</i> node type.
   * <p/>
   *
   * @param childNodeName The name of the child node.
   */
  public boolean canAddChildNode(String childNodeName);

  /**
   * Returns <code>true</code> if adding a child node called
   * <code>childNodeName</code> of node type <code>nodeTypeName</code> is
   * allowed by <i>this</i> node type. Otherwise returns <code>false</code>.
   * The same as <code>{@link #canAddChildNode(String childNodeName)}</code>
   * except that the type of the child node is explictly specified.
   *
   * @param childNodeName The name of the child node.
   * @param nodeTypeName  The name of the node type of the child node.
   */
  public boolean canAddChildNode(String childNodeName, String nodeTypeName);

  /**
   * Returns true if removing <code>itemName</code> is allowed by this node type.
   * Otherwise returns <code>false</code>.
   *
   * @param itemName The name of the child item
   */
  public boolean checkRemoveItem(String itemName);


}