/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeDef;


import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeDefImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: File.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class File extends NodeTypeImpl {

  public File() {
    this.name = "nt:file";
    this.mixin = false;
    this.declaredSupertypes = new NodeType[1];
    this.declaredSupertypes[0] = new HierarchyItem();
    this.declaredNodeDefs = new NodeDef[1];
    String[] types = {"nt:content"};
    String[] defMixinTypes = {"mix:referenceable"};
    String[] reqMixinTypes = {"mix:referenceable"};
    this.declaredNodeDefs[0] = new NodeDefImpl("jcr:content", types, "nt:content",
        reqMixinTypes, defMixinTypes,
        true, false, OnParentVersionAction.VERSION, false, true,
        false);

  }

}
