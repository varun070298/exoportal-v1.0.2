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
 * @version $Id: Folder.java,v 1.3 2004/07/16 16:12:22 benjmestrallet Exp $
 */

public class Folder extends NodeTypeImpl {

  public Folder() {
    this.name = "nt:folder";
    this.mixin = false;
    this.declaredSupertypes = new NodeType[1];
    this.declaredSupertypes[0] = new HierarchyItem();
    this.declaredNodeDefs = new NodeDef[1];
    String[] types = {"nt:hierarchyItem"};
    this.declaredNodeDefs[0] = new NodeDefImpl(null, types, "nt:hierarchyItem",
        null, null,
        false, false, OnParentVersionAction.VERSION, false, false,
        false);
  }

}
