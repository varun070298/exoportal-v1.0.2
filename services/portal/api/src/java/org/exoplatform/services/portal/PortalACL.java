/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal;

import org.exoplatform.services.portal.model.*;
/**
 * Apr 20, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalACL.java,v 1.3 2004/09/28 15:13:51 tuan08 Exp $
 **/
public interface PortalACL {
  final static public String ANY_PERMISSION = "any" ;
  final static public String OWNER_PERMISSION = "owner" ;
  final static public String NOONE_PERMISSION = "noone" ;
  final static public String ADMIN_PERMISSION = "admin" ;
   
	public boolean hasViewPagePermission(Page page, String user) ;	
  public boolean hasEditPagePermission(Page page, String user) ;
  
	public boolean hasViewPortalPermission(PortalConfig portal, String user) 	;
  public boolean hasEditPortalPermission(PortalConfig portal, String user) ;
  
  public boolean hasViewNodePermission(Node node, String portalOwner, String user) ;
  public boolean hasEditNodePermission(Node node, String portalOwner, String user) ;
  
  public String[] getPermissionList() ;
	
}