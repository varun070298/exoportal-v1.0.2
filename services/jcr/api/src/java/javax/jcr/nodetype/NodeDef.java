/*
 * $Id: NodeDef.java,v 1.3 2004/07/31 11:49:25 benjmestrallet Exp $
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

import javax.jcr.version.OnParentVersionAction;

/**
 * A node definition. Used in node typed definition
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 * @see NodeType#getChildNodeDefs
 * @see javax.jcr.Node#getDefinition
 */
public interface NodeDef {

  /**
   * Gets the node type that contains the declaration of <i>this</i>
   * <code>NodeDef</code>.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType getDeclaringNodeType();

  /**
   * Gets the name of the child node. If <code>null</code>, this
   * <code>NodeDef</code> defines a residual set of child nodes. That is,
   * it defines the characteristics of all those child nodes with names apart
   * from the names explicitly used in other property or child node
   * definitions.
   *
   * @return a <code>String</code> or <code>null</code>.
   */
  public String getName();

  /**
   * Gets the minimum set of primary node types that the child node must have.
   * Returns an array to support those implementations with multiple
   * inheritance. The simplest case would be to return <code>nt:base</code>,
   * which is the base of all primary node types and therefore, in this
   * context, represents the least restrictive requirement.
   * <p/>
   * A node must still have only one assigned primary node type, though
   * this attribute can restrict that node type by taking advantage of any
   * inheritance hierarchy that the implementation may support.
   *
   * @return an array of <code>NodeType</code> objects.
   */
  public NodeType[] getRequiredPrimaryTypes();

  /**
   * Gets the default primary node type that will be assigned to the child
   * node if it is created without an explicitly specified primary node type.
   * This node type must be a subtype of (or the same type as) the node types
   * returned by <code>getRequiredPrimaryTypes</code>.
   *
   * @return a <code>NodeType</code>.
   */
  public NodeType getDefaultPrimaryType();

  /**
   * Gets the minimum set of mixin node types that the child node must have.
   *
   * @return an array of <code>NodeType</code> objects.
   */
  public NodeType[] getRequiredMixinTypes();

  /**
   * Gets the default set of mixin node types that will be assigned to the child
   * node upon creation. This set of mixin node types must include <i>at least
   * those</i>, or subtypes of those, returned by
   * <code>getRequiredMixinTypes</code>.
   *
   * @return an array of <code>NodeType</code> objects.
   */
  public NodeType[] getDefaultMixinTypes();

  /**
   * Reports whether the child node is to be automatically created when its
   * parent node is created. If set to <code>true</code> then this
   * <code>NodeDef</code> must not be a residual set definition
   * but must specify an actual child node name. This child node
   * may be of a node type that specifies further auto-created child nodes.
   * In such a case a chain of nodes will be created.
   *
   * @return a <code>boolean</code>.
   */
  public boolean isAutoCreate();

  /**
   * Reports whether the child node is mandatory. A mandatory property is one
   * that, if its parent node exists, must also exist. It cannot be removed
   * through this API except by removing its parent. An attempt to save a
   * node that has a mandatory child node without first creating that child node,
   * will throw a <code>ConstraintViolationException</code> on <code>save</code>.
   * <p/>
   * If a child node is mandatory then the following restrictions must be
   * enforced:
   * <ul>
   * <li>
   * If <code>autoCreate</code> is <code>false</code> then the client must
   * ensure that the child node is created  before the parent node is saved, otherwise
   * a <code>ConstraintViolationException</code> will be thrown on <code>save</code>.
   * </li>
   * <li>
   * Once created, the child node cannot be removed without removing its parent node,
   * otherwise a <code>ConstraintViolationException</code> is thrown on <code>save</code>.
   * </li>
   * </ul>
   *
   * @return a <code>boolean</code>
   */
  public boolean isMandatory();

  /**
   * Gets the on-parent-version status of the child node. This governs what to do if
   * the parent node of this child node is versioned; an
   * {@link OnParentVersionAction}.
   *
   * @return an <code>int</code>.
   */
  public int getOnParentVersion();

  /**
   * Reports whether the child node is read-only.A read-only node is one that
   * cannot be changed (i.e., have child nodes or properties added or removed)
   * through this API. However, it may be altered by the implementation itself
   * through some mechanism not defined by this specification.
   *
   * @return a <code>boolean</code>.
   */
  public boolean isReadOnly();

  /**
   * Reports whether this node is the primary child item of its parent
   * node. Since any given node may have either zero or one primary child
   * items, this means that a maximum of one <code>PropertyDefNT</code> or
   * <code>NodeDef</code> within a particular <code>NodeType</code> may
   * return <code>true</code> on this call. The primary child item flag
   * is used by the method <code>{@link javax.jcr.Node#getPrimaryItem()
   * Node.getPrimaryItem()}</code>.
   *
   * @return a <code>boolean</code>.
   */
  public boolean isPrimaryItem();

  /**
   * Reports whether this child node can have same-name siblings. In other
   * words, whether the parent node can have more than one child node of this
   * name.
   *
   * @return a boolean.
   */
  public boolean allowSameNameSibs();
}