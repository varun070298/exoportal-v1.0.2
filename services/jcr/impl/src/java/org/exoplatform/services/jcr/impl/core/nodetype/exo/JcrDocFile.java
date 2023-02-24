/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype.exo;


import javax.jcr.nodetype.NodeType;
import org.exoplatform.services.jcr.impl.core.nodetype.NodeTypeImpl;
import org.exoplatform.services.jcr.impl.core.nodetype.nt.File;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: JcrDocFile.java,v 1.1 2004/07/29 17:37:49 geaz Exp $
 */

public class JcrDocFile extends NodeTypeImpl {

  public JcrDocFile() {
    this.name = "exo:jcrdocfile";
    this.declaredSupertypes = new NodeType[1];
    this.declaredSupertypes[0] = new File();
  }


}
