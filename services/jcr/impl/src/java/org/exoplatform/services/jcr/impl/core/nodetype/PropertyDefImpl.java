/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype;

import javax.jcr.nodetype.PropertyDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.Value;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.PropertyType;
import javax.jcr.version.OnParentVersionAction;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PropertyDefImpl.java,v 1.3 2004/07/31 11:49:26 benjmestrallet Exp $
 */

public class PropertyDefImpl implements PropertyDef {

  private String name;
  private String declaringNodeType;
  private int requiredType;
  private String valueConstraint;
  private Value defaultValue;
  private boolean autoCreate;
  private boolean mandatory;
  private int onVersion;
  private boolean readOnly;
  private boolean primaryItem;
  private boolean multiple;

  public PropertyDefImpl(String name, String declaringNodeType, int requiredType,
                         String valueConstraint, Value defaultValue, boolean autoCreate, boolean mandatory,
                         int onVersion, boolean readOnly, boolean primaryItem, boolean multiple) {

    this.name = name;
    this.declaringNodeType = declaringNodeType;
    this.requiredType = requiredType;
    this.valueConstraint = valueConstraint;
    this.defaultValue = defaultValue;
    this.autoCreate = autoCreate;
    this.mandatory = mandatory;
    this.onVersion = onVersion;
    this.readOnly = readOnly;
    this.primaryItem = primaryItem;
    this.multiple = multiple;
  }

  // Residual definition
  public PropertyDefImpl(String declaringNodeType) {

    this.name = null;
    this.declaringNodeType = declaringNodeType;
    this.requiredType = PropertyType.UNDEFINED;
    this.valueConstraint = null;
    this.defaultValue = null;
    this.autoCreate = false;
    this.mandatory = false;
    this.onVersion = OnParentVersionAction.IGNORE;
    this.readOnly = false;
    this.primaryItem = false;
    this.multiple = true;
  }

  /**
   * 6.10.6
   * Gets the name of the property to which this definition
   * applies. If null, then this PropertyDefNT defines a residual
   * set of properties. That is, it defines the characteristics of
   * all those properties with names apart from the names
   * explicitly used in other property or child node definitions.
   */
  public String getName() {
    return name;
  }

  /**
   * 6.10.6
   * Gets the node type that contains the declaration of this PropertyDefNT.
   */
  public NodeType getDeclaringNodeType() /*throws NoSuchNodeTypeException*/ {
    try {
      return NodeTypeManagerImpl.getInstance().getNodeType(declaringNodeType);
    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }


  /**
   * 6.10.6
   * Gets the required type of the property. One of STRING,
   * BINARY, DATE, LONG, DOUBLE, SOFTLINK or BOOLEAN. See
   * Property Types, above. If null, then this property may
   * be of any type.
   */
  public int getRequiredType() {
    return requiredType;
  }

  /**
   * 6.10.6
   * Gets the constraint string. This string describes the
   * constraints that exist on future values of the property.
   * Reporting of value constraints is optional. An
   * implementation is free to return null to this call,
   * indicating that value constraint information is unavailable
   * (though a constraint may still exist). If a string is
   * returned then it is interpreted in different ways depending
   * on the type specified for this property.
   */
  public String getValueConstraint() {
    return valueConstraint;
  }

  /**
   * 6.10.6
   */
  public Value getDefaultValue() {
    return defaultValue;
  }

  /**
   * 6.10.6
   */
  public boolean isAutoCreate() {
    return autoCreate;
  }

  /**
   * 6.10.6
   */
  public boolean isMandatory() {
    return mandatory;
  }

  /**
   * 6.10.6
   */
  public int getOnParentVersion() {
    return onVersion;
  }

  /**
   * 6.10.6
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * 6.10.6
   */
  public boolean isPrimaryItem() {
    return primaryItem;
  }

  /**
   * 6.10.6
   */
  public boolean isMultiple() {
    return multiple;
  }

  public String toString() {
    return "PropertyDefImpl: " + name;
  }

  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof PropertyDefImpl))
      return false;
    if (this.getName() == null)
      return ((PropertyDefImpl) obj).getName() == null;
    return this.getName().equals(((PropertyDefImpl) obj).getName());
  }

}
