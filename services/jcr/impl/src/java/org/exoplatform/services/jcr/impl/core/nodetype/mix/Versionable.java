/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.mix;


import javax.jcr.nodetype.PropertyDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeDef;


import javax.jcr.PropertyType;
import javax.jcr.Value;
import javax.jcr.StringValue;


import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.nt.Base;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: Versionable.java,v 1.1 2004/11/02 18:36:34 geaz Exp $
 */

public class Versionable extends NodeTypeImpl {

  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public Versionable() {
    this.name = "mix:versionable";
    this.mixin = true;
    this.declaredSupertypes = new NodeType[1];
    this.declaredSupertypes[0] = new Referenceable();
    this.declaredPropertyDefs = new PropertyDef[4];

    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:versionHistory", null, PropertyType.REFERENCE,
        "\"nt:versionHistory\"", null, true, true,
        OnParentVersionAction.COPY, true, false, false);

    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:baseVersion", null, PropertyType.REFERENCE,
        "\"nt:version\"", null, true, true,
        OnParentVersionAction.IGNORE, true, false, false);

    this.declaredPropertyDefs[2] = new PropertyDefImpl("jcr:isCheckedOut", null, PropertyType.BOOLEAN,
        null, new StringValue("[\"true\"]"), true, true,
        OnParentVersionAction.IGNORE, true, false, false);

    this.declaredPropertyDefs[3] = new PropertyDefImpl("jcr:predecessors", null, PropertyType.REFERENCE,
        "\"nt:version\"", null, true, true,
        OnParentVersionAction.COPY, false, false, true);

  }

}
