/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core.nodetype.exo;


import javax.jcr.nodetype.PropertyDef;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.PropertyType;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.nt.Base;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 5 ao�t 2004
 */
public class MockNodeType extends NodeTypeImpl {
  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public MockNodeType() {
    this.name = "exo:mockNodeType";
    this.mixin = false;
    this.declaredSupertypes = new javax.jcr.nodetype.NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredPropertyDefs = new PropertyDef[2];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:nodeTypeName", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:supertypes", null, PropertyType.STRING,
        null, null, true, false,
        OnParentVersionAction.COPY, false, false, true);
    this.declaredNodeDefs = new NodeDef[2];
    String[] types = {"nt:propertyDef"};
    this.declaredNodeDefs[0] = new NodeDefImpl("jcr:propertyDef", types, "nt:propertyDef",
        null, null,
        false, true, OnParentVersionAction.VERSION, false, false,
        true);
    types[0] = "nt:childNodeDef";
    this.declaredNodeDefs[1] = new NodeDefImpl("jcr:childNodeDef", types, "nt:childNodeDef",
        null, null,
        false, true, OnParentVersionAction.VERSION, false, false,
        true);
  }
}
