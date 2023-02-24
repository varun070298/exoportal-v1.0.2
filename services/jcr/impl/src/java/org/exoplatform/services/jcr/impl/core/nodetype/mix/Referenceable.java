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
 * @version $Id: Referenceable.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class Referenceable extends NodeTypeImpl {

  public Referenceable() {
    this.name = "mix:referenceable";
    this.mixin = true;
    this.declaredPropertyDefs = new PropertyDef[1];
    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:uuid", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.INITIALIZE, true, false, false);

  }

}
