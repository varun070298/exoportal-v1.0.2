/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.*;
import javax.jcr.PropertyType;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 juil. 2004
 */
public class PropertyDefNT extends NodeTypeImpl {
  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public PropertyDefNT() {
    this.name = "nt:propertyDef";
    this.mixin = false;
    this.declaredSupertypes = new javax.jcr.nodetype.NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredPropertyDefs = new javax.jcr.nodetype.PropertyDef[10];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:name", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:type", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[2] = new PropertyDefImpl("jcr:valueConstraint", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[3] = new PropertyDefImpl("jcr:defaultValue", null, PropertyType.UNDEFINED,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[4] = new PropertyDefImpl("jcr:autoCreate", null, PropertyType.BOOLEAN,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[5] = new PropertyDefImpl("jcr:mandatory", null, PropertyType.BOOLEAN,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[6] = new PropertyDefImpl("jcr:onParentVersion", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[7] = new PropertyDefImpl("jcr:readOnly", null, PropertyType.BOOLEAN,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[8] = new PropertyDefImpl("jcr:primaryItem", null, PropertyType.BOOLEAN,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[9] = new PropertyDefImpl("jcr:multiple", null, PropertyType.BOOLEAN,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
  }
}
