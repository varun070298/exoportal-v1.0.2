/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.skin.model;

import java.util.* ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 19, 2004
 */
public class SkinConfig {
  private List portalDecorators ;
  private List pageDecorators ;
  private List containerDecorators ;
  private List portletDecorators ;
  private List portletStyleConfig ;
  
  public SkinConfig() {
    portalDecorators = new ArrayList(3) ;
    pageDecorators = new ArrayList(3) ;
    containerDecorators = new ArrayList(3) ;
    portletDecorators = new ArrayList(3) ;
    portletStyleConfig = new ArrayList(3) ;
  }
  
  
  public List getContainerDecorators() { return containerDecorators ; }
  public void setContainerDecorators(List list) { this.containerDecorators = list ; }
  
  public List getPageDecorators() {  return pageDecorators ; }
  public void setPageDecorators(List list) {pageDecorators = list ;}
  
  public List getPortalDecorators() {  return portalDecorators ; }
  public void setPortalDecorators(List list) {portalDecorators = list ;  }
  
  public List getPortletDecorators() {  return portletDecorators ; }
  public void setPortletDecorators(List list) {portletDecorators = list; }
  
  public List getPortletStyleConfig() {  return portletStyleConfig ; }
  public void setPortletStyleConfig(List list) {portletStyleConfig = list ;}
}