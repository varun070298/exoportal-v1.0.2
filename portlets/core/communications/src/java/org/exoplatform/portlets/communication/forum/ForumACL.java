/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum;

import javax.faces.context.FacesContext;
import org.exoplatform.services.communication.forum.*;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ForumACL.java,v 1.1.1.1 2004/03/02 18:58:51 benjmestrallet Exp $
 */
public class ForumACL {
  private String user_ ;
  
  public ForumACL(String user) {
    user_ = user ;
  }
  
  public String getRemoteUser() { return user_ ; } 
   
  public boolean hasViewForumRole(Forum forum) {
    String role = forum.getViewForumRole() ;
    if(role == null || role.length() == 0 || "any".equals(role) || "guest".equals(role)) {
      return true ;
    }
    return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role) ;
  }
  
  public boolean hasCreateTopicRole(Forum forum) {
    String role = forum.getCreateTopicRole() ;
    if(role == null || role.length() == 0 || "any".equals(role) || "guest".equals(role)) {
      return true ;
    }
    return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role) ;
  }

  public boolean hasReplyTopicRole(Forum forum) {
    String role = forum.getReplyTopicRole() ;
    if(role == null || role.length() == 0 || "any".equals(role) || "guest".equals(role)) {
      return true ;
    }
    return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role) ;
  }

  public boolean hasModeratorRole(Forum forum) {
    return forum.isModerator(user_) ;
  }
}
