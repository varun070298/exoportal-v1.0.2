/*
 * $Id: PropertyDef.java,v 1.3 2004/07/31 11:49:25 benjmestrallet Exp $
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
import javax.jcr.version.OnParentVersionAction;

/**
 * A property definition. Used in node type definitions.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface PropertyDef {

  /**
   * Gets the node type that contains the declaration of <i>this</i>
   * <code>PropertyDefNT</code>.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType getDeclaringNodeType();

  /**
   * Gets the name of the property that this definition applies to. If
   * <code>null</code>, then this <code>PropertyDefNT</code> defines a residual
   * set of properties. That is, it defines the characteristics of all those
   * properties with names apart from the names explicitly used in other
   * property or child node definitions.
   *
   * @return A <code>String</code>, or <code>null</code>.
   */
  public String getName();

  /**
   * Gets the required type of the property. One of:
   * <ul>
   * <li><code>PropertyType.STRING</code></li>
   * <li><code>PropertyType.DATE</code></li>
   * <li><code>PropertyType.BINARY</code></li>
   * <li><code>PropertyType.DOUBLE</code></li>
   * <li><code>PropertyType.LONG</code></li>
   * <li><code>PropertyType.BOOLEAN</code></li>
   * <li><code>PropertyType.SOFTLINK</code></li>
   * <li><code>PropertyType.REFERENCE</code></li>
   * <li><code>PropertyType.UNDEFINED</code></li>
   * </ul>
   * <code>PropertyType.UNDEFINED</code> is returned if this property may be
   * of any type.
   *
   * @return an int
   */
  public int getRequiredType();

  /**
   * Gets the constraint string. This string describes the constraints
   * that exist on future values of the property.
   * <p/>
   * Reporting of value constraints is <i>optional</i>. An implementation
   * is free to return <code>null</code> to this call, indicating that value
   * constraint information is unavailable (though a constraint may still
   * exist).
   * <p/>
   * If a string is returned then it is interpreted in different ways
   * depending on the type specified for this property. The following
   * describes the value constraint syntax for each property type:
   * <p/>
   * <ul>
   * <p/>
   * <li><code>STRING</code>: The constraint string is a regular expression
   * pattern. For example the regular expression <b><code>".*"</code></b> means
   * "any string"</li>
   * <p/>
   * <li><code>REFERENCE</code>: The constraint string is a comma separated list
   * of node type names. For example <code>"nt:authored, mynt:newsArticle"</code>.
   * </li>
   * <p/>
   * <li><code>BOOLEAN</code>: Either <code>"true"</code> or
   * <code>"false"</code>.</li>
   * <p/>
   * </ul>
   * <p/>
   * The remaining types all have value constraints in the form of inclusive
   * or exclusive ranges: i.e., <code>"[<i>min</i>, <i>max</i>]"</code>,
   * <code>"(<i>min</i>, <i>max</i>)"</code>, <code>"(<i>min</i>, <i>max</i>]"</code> or
   * <code>"[<i>min</i>, <i>max</i>)"</code>. Where <code>"["</code> and <code>"]"</code>
   * indicate "inclusive", while <code>"("</code> and <code>")"</code>
   * indicate "exclusive". A missing <code><i>min</i></code> or <code><i>max</i></code> value
   * indicates no bound in that direction. For example <code>[,5]</code>
   * means no minimum but a maximum of 5 (inclusive).
   * The syntax and meaning of the <code><i>min</i></code> and <code><i>max</i></code> values
   * themselves differs between types as follows:
   * <p/>
   * <ul>
   * <p/>
   * <li><code>BINARY</code>: <code><i>min</i></code> and <code><i>max</i></code>
   * specify the allowed size range of the binary value in bytes.</li>
   * <p/>
   * <li><code>DATE</code>: <code><i>min</i></code> and <code><i>max</i></code>
   * are dates specifiying the allowed date range. The date strings must be
   * in the ISO8601-compliant format:
   * <code><i>YYYY-MM-DD</i>T<i>hh:mm:ss.sssTZD</i></code>.</li>
   * <p/>
   * <li><code>LONG</code>, <code>DOUBLE</code>: <code><i>min</i></code> and
   * <code><i>max</i></code> are numbers.</li>
   * <p/>
   * </ul>
   *
   * @return a <code>String</code> or <code>null</code>
   */
  public String getValueConstraint();

  /**
   * Gets the default value of the property. This is the value that the
   * property will be assigned if it is either automatically created, or
   * created without a specified initial value. If <code>null</code>,
   * then the property has no fixed default value. Note that this does not
   * exclude the possibility that the property still assumes some value
   * automatically, but that value may be variable (for example, "the current
   * date") and hence cannot be expressed as a single fixed value.
   *
   * @return a <code>Value</code> or <code>null</code>
   */
  public Value getDefaultValue();

  /**
   * Reports whether the property is to be automatically created when its
   * parent node is created. If <code>true</code> then this
   * <code>PropertyDefNT</code> will necessarily  not be a residual set
   * definition but will specify an actual property name (in other words
   * <code>getName()</code> will return a non-null value).
   *
   * @return a <code>boolean</code>
   */
  public boolean isAutoCreate();

  /**
   * Reports whether the property is mandatory. A mandatory property is one
   * that, if its parent node exists, must also exist. It cannot be removed
   * through this API except by removing its parent. An attempt to save a
   * node that has a mandatory property without first creating that property,
   * will throw a <code>ConstraintViolationException</code> on <code>save</code>.
   * <p/>
   * If a property is mandatory then the following restrictions must be
   * enforced:
   * <ul>
   * <li>
   * If <code>autoCreate</code> is <code>false</code> then the client must
   * ensure that the property is created (and if no default value is
   * indicated, given a value) before the parent node is saved, otherwise
   * a <code>ConstraintViolationException</code> will be thrown on <code>save</code>.
   * </li>
   * <li>
   * Once created, the property cannot be removed without removing its parent node,
   * otherwise a <code>ConstraintViolationException</code> is thrown on <code>save</code>.
   * </li>
   * </ul>
   *
   * @return a <code>boolean</code>
   */
  public boolean isMandatory();

  /**
   * Gets the on-parent-version status of the property. This governs what to do if
   * the parent node of this property is versioned; an
   * {@link OnParentVersionAction}.
   *
   * @return an <code>int</code>
   */
  public int getOnParentVersion();

  /**
   * Reports whether the property is read-only. A read-only
   * property cannot be written-to via this API. It may be written to,
   * however, by the implementation itself via some mechanism not
   * specified by this specification.
   *
   * @return a <code>boolean</code>
   */
  public boolean isReadOnly();

  /**
   * Reports whether this property is the primary child item of its parent
   * node. Since any given node may have either zero or one primary child
   * items, this means that a maximum of one <code>PropertyDefNT</code> or
   * <code>NodeDef</code> within a particular <code>NodeType</code> may
   * return <code>true</code> on this call. The primary child item flag
   * is used by the method <code>{@link javax.jcr.Node#getPrimaryItem()}</code>.
   *
   * @return a <code>boolean</code>
   */
  public boolean isPrimaryItem();

  /**
   * Reports whether this property can have multiple values.
   *
   * @return a <code>boolean</code>
   */
  public boolean isMultiple();
}
