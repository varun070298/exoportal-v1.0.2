/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;
/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalConfig.java,v 1.7 2004/08/06 03:02:29 tuan08 Exp $
 **/
public class PortalConfig extends Component {
	private String owner ;
  private String locale ;
  private String viewPermission ;
  private String editPermission ;
  
  private Container portalLayout ;
  private Container mobilePortalLayout ;
  transient private boolean sharedLayout = false ;
  
  public String getOwner() { return owner ; }
  public void   setOwner(String s) { owner = s  ; } 
 
  public String getLocale() { return locale ; }
  public void   setLocale(String s) { locale = s ; }
  
  public String getViewPermission() { return viewPermission ; }
  public void   setViewPermission(String s) { viewPermission = s ; }
  
  public String getEditPermission() { return editPermission ; }
  public void   setEditPermission(String s) { editPermission = s ; }
  
  public Container getLayout() { return portalLayout ; }
  public void setLayout(Container layout) { portalLayout = layout; }
  
  public Container getMobilePortalLayout() { return mobilePortalLayout ; }
  public void setMobilePortalLayout(Container layout) { mobilePortalLayout = layout; }
  
  public boolean isSharedLayout() { return sharedLayout ; }
  public void    setSharedLayout(boolean b) { sharedLayout = b ;}
  
  public Component softCloneObject() { 
  	PortalConfig config = new PortalConfig() ;
  	config.copyBasicProperties(this) ;
  	config.setOwner(owner) ;
  	config.setLocale(locale) ;
  	config.setViewPermission(viewPermission) ;
  	config.setLayout(portalLayout) ;
  	return config ;
  }
}