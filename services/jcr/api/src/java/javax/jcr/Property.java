/*
 * $Id: Property.java,v 1.3 2004/07/31 11:49:25 benjmestrallet Exp $
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

import javax.jcr.nodetype.PropertyDef;
import java.io.InputStream;
import java.util.Calendar;

/**
 * A <code>Property</code> object represents the smallest granularity of content
 * storage.
 * <p/>
 * <b>Level 1 and 2</b>
 * <p/>
 * A property must have one and only one parent node. A property does
 * not have children. When we say that node A "has" property B it means that B
 * is a child of A.
 * <p/>
 * A property consists of a name and a value. See <code>{@link Value}</code>.
 *
 * @author Peeter Piegaze
 * @author Stefan Guggisberg
 */
public interface Property extends Item {

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * If this property's property type is not constrained by the node type of
   * its parent node, then the property type is changed to that of the supplied
   * <code>value</code>. If the property type is constrained, then a
   * best-effort conversion is attempted. If conversion fails, a
   * <code>ValueFormatException</code> is thrown immediately (not on <code>save</code>).
   * The change will be persisted (if valid) on <code>save</code>
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(Value value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to the array <code>values</code>.
   * If this property's property type is not constrained by the node type of
   * its parent node, then the property type is changed to that of the supplied
   * <code>value</code>. If the property type is constrained, then a
   * best-effort conversion is attempted. If conversion fails, a
   * <code>ValueFormatException</code> is thrown immediately (not on <code>save</code>).
   * If this property is not a multi-valued then a ValueFormatException is
   * thrown immediately. The change will be persisted (if valid) on <code>save</code>
   *
   * @param values The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(Value[] values) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>String</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(String value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>boolean</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(boolean value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>long</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(long value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>double</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(double value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>InputStream</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(InputStream value) throws ValueFormatException, RepositoryException;

  /**
   * Sets the value of this <code>Property</code> to <code>value</code>.
   * Same as <code>{@link #setValue(Value value)}</code> except that the
   * value is specified as a <code>Calendar</code>.
   *
   * @param value The new value to set the property to.
   * @throws ValueFormatException if the type or format of the specified value
   *                              is incompatible with the <code>type</code> of this property.
   * @throws RepositoryException  if another error occurs.
   */
  public void setValue(Calendar value) throws ValueFormatException, RepositoryException;

  /**
   * Returns the value of this property.
   * Returns the value of the <code>Property</code> as a generic
   * <code>Value</code> object or <code>null</code> if the
   * <code>Property</code> has no value.
   *
   * @return the value
   * @throws RepositoryException if an error occurs.
   */
  public Value getValue() throws RepositoryException;

  /**
   * Returns an array of all the values of this property. Used to access
   * multi-value properties.
   *
   * @return a <code>Value</code> array
   * @throws RepositoryException if an error occurs.
   */
  public Value[] getValues() throws RepositoryException;

  /**
   * Returns a <code>String</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getString()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A string representation of the value of this <code>Property</code>.
   */
  public String getString();

  /**
   * Returns a <code>double</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getDouble()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A string representation of the value of this <code>Property</code>.
   */
  public double getDouble();

  /**
   * Returns a <code>InputStream</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getStream()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A string representation of the value of this <code>Property</code>.
   */
  public InputStream getStream();

  /**
   * Returns a <code>Calendar</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getDate()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A string representation of the value of this <code>Property</code>.
   */
  public Calendar getDate();

  /**
   * Returns a <code>boolean</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getBoolean()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A string representation of the value of this <code>Property</code>.
   */
  public boolean getBoolean();

  /**
   * Returns a <code>Slong</code> representation of the value of this
   * <code>Property</code>. A shortcut for
   * <code>Property.getValue().getLong()</code>. See {@link Value}.
   * If this Property is multi-valued, this method returns the first value.
   *
   * @return A long representation of the value of this <code>Property</code>.
   */
  public long getLong();

  /**
   * Indicates whether this property has a value.
   * <p/>
   * <b>Level 1 and 2:</b>
   * <p/>
   * Returns true if this property has a value. A property with no value
   * is the same as a property with value set to <code>null</code>.
   *
   * @return <code>true</code> if the property has content;
   *         <code>false</code> otherwise.
   */
  public boolean hasValue();

  /**
   * Returns the length of the value of this property.
   * <p/>
   * <b>Level 1 and 2:</b>
   * <p/>
   * Returns the length of the value of this property in bytes if the value
   * is a <code>PropertyType.BINARY</code>, otherwise it returns the number
   * of characters needed to display the value (for strings this is the string
   * length, for numeric types it is the number of characters needed to
   * display the number). Returns –1 if the implementation cannot determine
   * the length. Returns 0 if the property has no value.
   *
   * @return an integer
   */
  public long getLength();

  /**
   * Returns the definition of <i>this</i> <code>Property</code>. This method
   * is actually a shortcut to searching through this property's parent's
   * node type (and its supertypes) for the property definition applicable
   * to this property.
   *
   * @return a <code>PropertyDefNT</code> object.
   * @see javax.jcr.nodetype.NodeType#getPropertyDefs
   */
  public PropertyDef getDefinition();
}
