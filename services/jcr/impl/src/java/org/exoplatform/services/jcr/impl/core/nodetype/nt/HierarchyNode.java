/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.nt;


import javax.jcr.nodetype.NodeType;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: HierarchyNode.java,v 1.3 2004/07/31 11:49:26 benjmestrallet Exp $
 */

public class HierarchyNode extends NodeTypeImpl {

  public HierarchyNode() {
    this.name = "nt:hierarchyNode";
    this.declaredSupertypes = new NodeType[2];
    this.declaredSupertypes[0] = new File();
    this.declaredSupertypes[1] = new Folder();
  }

}
