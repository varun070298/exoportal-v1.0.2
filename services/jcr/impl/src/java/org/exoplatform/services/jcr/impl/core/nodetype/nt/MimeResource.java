/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeDef;


import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.mix.Referenceable;
import javax.jcr.nodetype.PropertyDef;
import org.exoplatform.services.jcr.impl.core.nodetype.PropertyDefImpl;
import javax.jcr.PropertyType;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: File.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class MimeResource extends NodeTypeImpl {
//(String name, String declaringNodeType, int requiredType,
//                         String valueConstraint, Value defaultValue, boolean autoCreate, boolean mandatory,
//                         int onVersion, boolean readOnly, boolean primaryItem, boolean multiple)

  public MimeResource() {

    this.name = "nt:mimeResource";
    this.mixin = false;
    this.declaredSupertypes = new NodeType[2];
    this.declaredSupertypes[0] = new Base();
    this.declaredSupertypes[1] = new Referenceable();
    this.declaredPropertyDefs = new PropertyDef[4];

    this.declaredPropertyDefs[0] = new PropertyDefImpl("jcr:encoding", null, PropertyType.STRING,
        null, null, false, false,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[1] = new PropertyDefImpl("jcr:mimetype", null, PropertyType.STRING,
        null, null, true, true,
        OnParentVersionAction.COPY, false, false, false);
    this.declaredPropertyDefs[2] = new PropertyDefImpl("jcr:data", null, PropertyType.BINARY,
        null, null, true, true,
        OnParentVersionAction.COPY, false, true, false);
    this.declaredPropertyDefs[3] = new PropertyDefImpl("jcr:lastModified", null, PropertyType.DATE,
        null, null, false, true,
        OnParentVersionAction.IGNORE, false, false, false);

  }

}
