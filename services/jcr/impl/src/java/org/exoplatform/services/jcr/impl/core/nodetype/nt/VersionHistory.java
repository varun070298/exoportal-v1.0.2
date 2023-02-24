/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.*;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 juil. 2004
 */
public class VersionHistory extends NodeTypeImpl {

  public VersionHistory() {
    this.name = "nt:versionHistory";
    this.mixin = false;
    this.declaredSupertypes = new javax.jcr.nodetype.NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredNodeDefs = new NodeDef[2];
    String[] types = {"nt:version"};
    String[] mixinTypes = {"mix:referenceable"};
    this.declaredNodeDefs[0] = new NodeDefImpl("jcr:rootVersion", types, "nt:verson",
        mixinTypes, mixinTypes,
        true, true, OnParentVersionAction.ABORT, true, false,
        false);
    this.declaredNodeDefs[1] = new NodeDefImpl(null, types, "nt:version",
        mixinTypes, mixinTypes,
        false, false, OnParentVersionAction.ABORT, true, false,
        false);
  }
}
