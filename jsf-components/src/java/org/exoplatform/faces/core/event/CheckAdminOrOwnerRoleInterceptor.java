/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.RequestInfo;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class CheckAdminOrOwnerRoleInterceptor extends ActionInterceptor {
	
	public void preExecute(ExoActionEvent event) throws Exception {
    ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext() ;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class) ;
    if(rinfo.getPortalOwner().equals(econtext.getRemoteUser())) {
      return ;
    }
    if (econtext.isUserInRole("admin") ) {
      return ;
    }
    Object[] args = {event.getAction()} ;
    throw new ExoMessageException("CheckAdminOrOwnerRoleInterceptor.msg.owner-or-admin-require", args) ;
	}
	
	final public void postExecute(ExoActionEvent event) throws Exception {
		
	}
}