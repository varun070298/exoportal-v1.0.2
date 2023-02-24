/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import javax.faces.context.* ;
import org.exoplatform.Constants;
import org.exoplatform.commons.exception.ExoPermissionException;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class CheckRoleInterceptor extends ActionInterceptor {
  final static public String ADMIN = Constants.ADMIN_ROLE ;
  final static public String USER = Constants.USER_ROLE  ;
  
	private String role_ ;
	
	public CheckRoleInterceptor(String role) {
		role_ = role ;
	}
	
	public void preExecute(ExoActionEvent event) throws Exception {
    ExternalContext econtext = 
      FacesContext.getCurrentInstance().getExternalContext() ;
    if(!econtext.isUserInRole(role_))  {
      throw new ExoPermissionException(event.getAction() , role_) ;
    }
	}
	
	final public void postExecute(ExoActionEvent event) throws Exception {
		
	} 
}