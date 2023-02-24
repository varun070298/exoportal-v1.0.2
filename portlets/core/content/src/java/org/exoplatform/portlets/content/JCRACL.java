/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content;

import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: JCRACL.java,v 1.3 2004/07/16 09:51:34 oranheim Exp $
 */
public class JCRACL implements ACL {
  
  public JCRACL() {
  }
    
  public boolean hasReadRole(NodeDescriptor node)  {
    return true ;
  }
  
  public boolean hasWriteRole(NodeDescriptor node) {
    return false ;
  }
}
