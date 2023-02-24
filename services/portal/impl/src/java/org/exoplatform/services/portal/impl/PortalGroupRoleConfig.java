/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 27, 2004
 * @version $Id: PortalGroupRoleConfig.java,v 1.1 2004/10/28 15:39:41 tuan08 Exp $
 */
public class PortalGroupRoleConfig {
  private List portalRule ;
  private List navigationRule ;
  
  public List getNavigationRule() { return navigationRule; }
  public void setNavigationRule(List l) {  this.navigationRule = l; }
  
  public List getPortalRule() {  return portalRule; }
  public void setPortalRule(List l) {   this.portalRule = l; }
  
  
  static public class InterceptorRule {
    private String role ;
    private String configuration ;
    
    public String getConfiguration() {  return configuration;  }
    public void setConfiguration(String s) {   this.configuration = s;   }
    
    public String getRole() {  return role;   }
    public void setRole(String role) {   this.role = role;   }
  }
}
