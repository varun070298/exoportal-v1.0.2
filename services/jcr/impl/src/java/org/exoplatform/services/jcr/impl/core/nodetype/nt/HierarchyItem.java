/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.PropertyDef;


import javax.jcr.PropertyType;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: HierarchyItem.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class HierarchyItem extends NodeTypeImpl {
  //name, declaringNodeType, requiredType,
  // vlueConstraint, defaultValue, autoCreate, mandatory,
  // onVersion, readOnly, primaryItem, multiple)

  public HierarchyItem() {
    this.name = "nt:hierarchyItem";
    this.mixin = false;
    this.declaredSupertypes = new NodeType[1];
    this.declaredSupertypes[0] = new Base();
    this.declaredPropertyDefs = new PropertyDef[2];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:created", null, PropertyType.DATE,
        null, null, true, true,
        OnParentVersionAction.COPY, true, false, false);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:lastModified", null, PropertyType.DATE,
        null, null, true, false,
        OnParentVersionAction.COPY, true, false, false);

  }

}
