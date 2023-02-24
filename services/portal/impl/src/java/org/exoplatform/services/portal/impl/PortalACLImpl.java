/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.model.*;
/**
 * Apr 20, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalACLImpl.java,v 1.4 2004/09/28 15:13:53 tuan08 Exp $
 **/
public class PortalACLImpl implements PortalACL {
	
  public PortalACLImpl() {
    
  }
  
  public boolean hasViewPagePermission(Page page, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(page.getViewPermission())) return page.getOwner().equals(user) ;
    return ANY_PERMISSION.equals(page.getViewPermission());
    
  }
  
  public boolean hasEditPagePermission(Page page, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(page.getEditPermission())) return page.getOwner().equals(user) ;
    return ANY_PERMISSION.equals(page.getOwner())  ;
  }
  
  public boolean hasViewPortalPermission(PortalConfig config, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(config.getViewPermission())) return config.getOwner().equals(user) ;
    return ANY_PERMISSION.equals(config.getViewPermission()) ;
  }
  
  public boolean hasEditPortalPermission(PortalConfig config, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(config.getEditPermission())) return config.getOwner().equals(user) ;
    return ANY_PERMISSION.equals(config.getEditPermission()) ;
  }
  
  public boolean hasViewNodePermission(Node node, String portalOwner, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(node.getViewPermission())) return portalOwner.equals(user) ;
    return ANY_PERMISSION.equals(node.getViewPermission()) ;
  }
  
  public boolean hasEditNodePermission(Node node, String portalOwner, String user) {
    if(ADMIN_PERMISSION.equals(user)) return true  ;
    if(OWNER_PERMISSION.equals(node.getEditPermission())) return portalOwner.equals(user) ;
    return ANY_PERMISSION.equals(node.getEditPermission()) ;
  }
  
  public String[] getPermissionList()  {
  	return new String[] {ANY_PERMISSION, OWNER_PERMISSION, ADMIN_PERMISSION } ;
  }
}