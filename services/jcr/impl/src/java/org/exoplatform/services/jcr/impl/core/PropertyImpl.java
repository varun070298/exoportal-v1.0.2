/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.BinaryValue;
import javax.jcr.BooleanValue;
import javax.jcr.DateValue;
import javax.jcr.DoubleValue;
import javax.jcr.Item;
import javax.jcr.ItemVisitor;
import javax.jcr.LongValue;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.StringIterator;
import javax.jcr.StringValue;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDef;
import javax.jcr.util.ISO8601;

import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;
import org.exoplatform.services.jcr.impl.util.EntityCollection;
import org.exoplatform.services.jcr.impl.util.PropertyConvertor;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PropertyImpl.java,v 1.17 2004/11/02 18:36:33 geaz Exp $
 */

public class PropertyImpl extends ItemImpl implements Property {


//  protected int type;
  protected Value[] values = new Value[1];

  public PropertyImpl(String path, Value value, int type)
      throws PathNotFoundException, RepositoryException {
    super(path);

    this.values[0] = value;
    this.values = PropertyConvertor.convert(values, type);

//    this.type = type;

  }

  public PropertyImpl(String path, Value[] values, int type)
      throws PathNotFoundException, RepositoryException {

    super(path);
    log.debug("PropertyImpl()-- "+path+" type = "+type);
    this.values = PropertyConvertor.convert(values, type);

//    this.values = values;
//    this.type = type;

  }

  // Deep copy
  public PropertyImpl(PropertyImpl prop) throws PathNotFoundException, RepositoryException {

    super(prop.getPath());
//    System.out.println(">>>>>>>>>>  "+prop);

    this.values = PropertyConvertor.convert(prop.getValues(), prop.getType());
//    this.type = prop.type;
  }

  /**
   * 6.2.3
   * Returns the value of the Property as a generic Value
   * object or null if the Property has no value.
   */
  public Value getValue() {
    if (values.length == 0)
      return null;
    return this.values[0];
  }

  /**
   * Returns an array of all the values of this property. Used to access
   * multi-value properties.
   *
   * @return a <code>Value</code> array
   * @throws RepositoryException if an error occurs.
   */
  public Value[] getValues() throws RepositoryException {
    return this.values;
  }

  /**
   * 6.2.3
   * Returns a String representation of the value of this
   * Property. If conversion is not possible or the
   * property has no value, an empty string is returned.
   * This behavior is in contrast to Value.getString(),
   * which throws an exception in these circumstances.
   */
  public String getString() {
    try {
      return values[0].getString();
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * 6.3.2
   * Returns a double representation of the value of this
   * Property. If conversion is not possible or the
   * property has no value, 0.0 is returned. This behavior
   * is in contrast to Value.getDouble(), which throws an
   * exception in these circumstances.
   */
  public double getDouble() {
    try {
      return values[0].getDouble();
    } catch (Exception e) {
      return 0.0;
    }
  }

  /**
   * 6.3.2
   * Returns a long representation of the value of this
   * Property. If conversion is not possible or the
   * property has no value, 0 is returned. This behavior is
   * in contrast to Value.getLong(), which throws an
   * exception in these circumstances.
   */
  public long getLong() {
    try {
      return values[0].getLong();
    } catch (Exception e) {
      return 0l;
    }

  }

  /**
   * 6.3.2
   * Returns an InputStream representation of the value
   * of this Property. If conversion is not possible or the
   * property has no value, an empty stream is returned.
   */
  public InputStream getStream() {
    try {
      return values[0].getStream();
    } catch (Exception e) {
      return new ByteArrayInputStream(new byte[0]);
    }

  }

  /**
   * 6.3.2
   * Returns a Calendar representation of the value of this
   * Property. If conversion is not possible or the
   * property has no value, a Calendar value of 1970-01-01T00:00:00Z is returned.
   */
  public Calendar getDate() {
    try {
      return values[0].getDate();
    } catch (Exception e) {
      return ISO8601.parse("1970-01-01T00:00:00.00Z");
    }

  }

  /**
   * 6.3.2
   * Returns a boolean representation of the value of this
   * Property. If conversion is not possible or the
   * property has no value, a value of false is returned.
   */
  public boolean getBoolean() {
    try {
      return values[0].getBoolean();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 6.3.2
   * Returns true if this property has a value. Returns false otherwise.
   */
  public boolean hasValue() {
    if (values.length == 0 || values[0] == null || getLength() == 0)
      return false;
    return true;
  }

  /**
   * 6.3.2
   * Returns the length of the value of this property in
   * bytes if the value is a PropertyType.BINARY,
   * otherwise it returns the number of characters needed
   * to display the value (for strings this is the string
   * length, for numeric types it is the number of
   * characters needed to display the number). Returns �1
   * if the implementation cannot determine the length.
   * Returns 0 if the property has no value.
   */
  public long getLength() {
    try {
      return values[0].getString().length();
    } catch (IllegalStateException e) {
      try {
        return getStream().available();
      } catch (IOException ioe) {
        return -1;
      }
    } catch (Exception e) {
      return -1;
    }
  }

  /**
   * 6.3.2
   * Sets the value of this Property to value. If this property's
   * property type is not constrained by the node type of its
   * parent node, then the property type is changed to that of the
   * supplied value. If the property type is constrained, then a
   * best-effort conversion is attempted. If conversion fails, a
   * ConstraintViolationException is thrown on the next save.
   */
  public void setValue(Value value) throws ValueFormatException, RepositoryException {
    log.debug("setValue for " + getPath()+" "+value);
    Value[] newValues = new Value[1];
    newValues[0] = value;
    setValue(newValues);
  }

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
  public void setValue(Value[] values) throws ValueFormatException, RepositoryException {
    log.debug("setValues for " + getPath()+" length="+values.length+" propType="+getType()+" valueType="+values[0].getType());

    NodeImpl parent = (NodeImpl) getParent();
    if (parent == null)
      throw new RepositoryException("Parent for <" + getPath() + "> is null!");

    if(values.length > 1)
      if (!((NodeTypeImpl) parent.getPrimaryNodeType()).getPropertyDef(getName()).isMultiple())
        throw new ValueFormatException("Can not add multiple value to this property");

    if ((getType() != PropertyType.UNDEFINED) && (getType() != values[0].getType()))
      this.values = PropertyConvertor.convert(values, getType());
    else
      this.values = values;

    parent.updateProperty(this.getName(), getType(), values);

  }

  /**
   * 6.3.2
   * Same as setValue(Value value) except that the value is specified as a String.
   */
  public void setValue(String value) throws ValueFormatException, RepositoryException {
    setValue(new StringValue(value));
  }

  /**
   * 6.3.2
   * Same as setValue(Value value) except that the value is specified as a InputStream.
   */
  public void setValue(InputStream stream) throws ValueFormatException, RepositoryException {
    setValue(new BinaryValue(stream));
  }

  /**
   * 6.3.2
   */
  public void setValue(double number) throws ValueFormatException, RepositoryException {
    setValue(new DoubleValue(number));
  }

  /**
   * 6.3.2
   */
  public void setValue(long number) throws ValueFormatException, RepositoryException {
    setValue(new LongValue(number));
  }

  /**
   * 6.3.2
   */
  public void setValue(Calendar date) throws ValueFormatException, RepositoryException {
    setValue(new DateValue(date));
  }

  /**
   * 6.3.2
   */
  public void setValue(boolean b) throws ValueFormatException, RepositoryException {
    setValue(new BooleanValue(b));
  }

  public PropertyDef getDefinition() /*throws  ItemNotFoundException , AccessDeniedException*/ {

    NodeType parentType;
    try {
      parentType = getParent().getPrimaryNodeType();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    PropertyDef[] propDefs = parentType.getPropertyDefs();
    for (int i = 0; i < propDefs.length; i++)
      if (propDefs[i].getName() == null || propDefs[i].getName().equals(getName()))
        return propDefs[i];

    // Residual def
    return new PropertyDefImpl(parentType.getName());
  }


 //////////////////// Item implementation ////////////////////
  public void accept(ItemVisitor visitor) throws RepositoryException {
     visitor.visit(this);
  }

  public boolean isNode() {
      return false;
  }

  protected boolean isItemIdentical(Item otherItem) {

    Property otherProperty = (Property) otherItem;
    if (this.getName().equals(otherProperty.getName()))
      return toString().equals(otherProperty.toString());
    else
      return false;
  }

  public StringIterator getPaths() {

    try {

      NodeImpl parent = (NodeImpl) this.getParent();
      StringIterator parentPaths = parent.getPaths();
      EntityCollection paths = new EntityCollection();
      while(parentPaths.hasNext())
         paths.add(new ItemLocation(parentPaths.nextString()+"/"+getName()).getPath());

      return paths;

    } catch (Exception e) {
        log.error("Property.getPaths() Error while obtaining Parent for "+this+" Reason:"+e);
        e.printStackTrace();
        throw new RuntimeException("Property.getPaths() Error while obtaining Parent for "+this+" Reason:"+e);
//        return new EntityCollection();
    }

  }


  //////////////////////////////////

  public int getType() {
//    return type;
    if(values[0] == null)
        return PropertyType.UNDEFINED;
    else
        return values[0].getType();
  }


  public String toString() {
    String strValues = "";
    try {
       Value[] newValues = PropertyConvertor.convert(values, PropertyType.STRING);
       for(int i=0; i<values.length; i++) {
          strValues+=newValues[i].getString()+" ";
       }
    } catch (Exception e) { strValues = e.getMessage(); }

    String strType;
    try {
       strType = PropertyType.nameFromValue(getType());
    } catch (Exception e) {
       strType = "Undefined";
    }

    return getName()+"("+strType+")=["+strValues+"]";
  }

}
