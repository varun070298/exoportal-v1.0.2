/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.PropertyType;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 juil. 2004
 */
public class Query extends NodeTypeImpl {
  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public Query() {
    this.name = "nt:query";
    this.mixin = false;
    this.declaredSupertypes = new javax.jcr.nodetype.NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredPropertyDefs = new javax.jcr.nodetype.PropertyDef[2];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:statement", null, PropertyType.STRING,
        null, null, false, false,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:language", null, PropertyType.STRING,
        null, null, false, false,
        OnParentVersionAction.COPY, false, false, false);
  }
}
