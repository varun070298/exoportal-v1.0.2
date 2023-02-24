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
 * @version $Id: Content.java,v 1.2 2004/07/08 23:36:48 benjmestrallet Exp $
 */

public class Content extends NodeTypeImpl {

  public Content() {
    this.name = "nt:content";
    this.declaredSupertypes = new NodeType[2]; //3
    this.declaredSupertypes[0] = new Default();
    this.declaredSupertypes[1] = new org.exoplatform.services.jcr.impl.core.nodetype.mix.Referenceable();
//        this.declaredSupertypes[2] = new Versionable();

  }

}
