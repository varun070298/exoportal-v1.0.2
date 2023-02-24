/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.PropertyType;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 juil. 2004
 */
public class Version extends NodeTypeImpl {
  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public Version() {
    this.name = "nt:version";
    this.mixin = false;
    this.declaredSupertypes = new javax.jcr.nodetype.NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredPropertyDefs = new javax.jcr.nodetype.PropertyDef[7];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:versionLabels", null, PropertyType.STRING,
        null, null, true, false,
        OnParentVersionAction.ABORT, true, false, true);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:versionDate", null, PropertyType.DATE,
        null, null, true, true,
        OnParentVersionAction.ABORT, true, false, false);
    this.declaredPropertyDefs[2] = new PropertyDefImpl("jcr:successors", null, PropertyType.REFERENCE,
        "nt:version", null, true, false,
        OnParentVersionAction.ABORT, true, false, true);
    this.declaredPropertyDefs[3] = new PropertyDefImpl("jcr:forzenPrimaryType", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.ABORT, true, false, false);
    this.declaredPropertyDefs[4] = new PropertyDefImpl("jcr:forzenMixinTypes", null, PropertyType.STRING,
        null, null, false, false,
        OnParentVersionAction.ABORT, true, false, true);
    this.declaredPropertyDefs[5] = new PropertyDefImpl("jcr:frozenUUID", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.ABORT, true, false, false);
    this.declaredPropertyDefs[6] = new PropertyDefImpl(null, null, PropertyType.UNDEFINED,
        null, null, false, false,
        OnParentVersionAction.ABORT, true, false, true);
    this.declaredNodeDefs = new NodeDef[1];
    this.declaredNodeDefs[0] = new NodeDefImpl(null, null, null,
        null, null,
        false, false, OnParentVersionAction.ABORT, false, false,
        true);    
  }
}
