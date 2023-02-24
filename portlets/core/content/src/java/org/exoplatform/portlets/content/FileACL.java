/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content;

import javax.faces.context.FacesContext;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: FileACL.java,v 1.6 2004/08/26 22:12:38 benjmestrallet Exp $
 */
public class FileACL implements ACL {
  private String user_ ;
  private String readRole_ ;
  private String writeRole_ ;
  
  public FileACL(String user, String readRole, String writeRole) {
    user_ = user ;
    readRole_ = readRole ;
    writeRole_ = writeRole ;
  }
    
  public boolean hasReadRole(NodeDescriptor node)  {
    if(readRole_ == null  || readRole_.length() == 0 || "any".equals(readRole_)) {
      return true ;
    }
    FacesContext context = FacesContext.getCurrentInstance() ;
    return context.getExternalContext().isUserInRole(readRole_) ;
  }
  
  public boolean hasWriteRole(NodeDescriptor node) {
    if(writeRole_ == null  || writeRole_.length() == 0 || "any".equals(writeRole_)) {
      return true ;
    }
    FacesContext context = FacesContext.getCurrentInstance() ;
    return context.getExternalContext().isUserInRole(writeRole_) ;
  }
}