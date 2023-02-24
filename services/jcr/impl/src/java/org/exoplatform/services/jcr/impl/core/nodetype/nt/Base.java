/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.PropertyDef;
import javax.jcr.PropertyType;


import javax.jcr.nodetype.NodeDef;
import javax.jcr.StringValue;
import javax.jcr.Value;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: Base.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class Base extends NodeTypeImpl {

  public Base() {
    this.name = "nt:base";
    this.mixin = false;
    this.declaredSupertypes = null; //new NodeType[0];
    this.declaredNodeDefs = null; //new NodeDef[0];
    this.declaredPropertyDefs = new PropertyDef[2];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:primaryType", this.name, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COMPUTE, true, false, false);

    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:mixinType", this.name, PropertyType.STRING,
        null, null, false, false,
        OnParentVersionAction.COMPUTE, true, false, true);

  }

}
