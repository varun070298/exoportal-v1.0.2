/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal;

import java.util.*  ;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.commons.utils.PageList ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface PortalConfigService {
  final public static String PAGE_NOT_FOUND_ERROR = "admin:/error/page/page-not-found" ;
  final public static String USER_PAGE_VIEW_PERMISSION_ERROR = "admin:/error/page/user-page-view-permission" ;
  final public static String USER_PAGE_EDIT_PERMISSION_ERROR = "admin:/error/page/user-page-edit-permission" ;
  final public static String USER_PORTAL_VIEW_PERMISSION_ERROR = "admin:/error/portal/user-portal-view-permission" ;
  final public static String USER_PORTAL_EDIT_PERMISSION_ERROR = "admin:/error/portal/user-portal-edit-permission" ;
  
  public void savePortalConfig(String owner, String xml) throws Exception  ;
  public void savePortalConfig(PortalConfig config) throws Exception  ;
  public PortalConfig getPortalConfig(String owner) throws Exception ; 
  public String       getPortalConfigAsXmlString(String owner) throws Exception ; 
  public void removePortalConfig(String owner) throws Exception ; 
  public PageList  findAllPortalConfigDescriptions(Query q) throws Exception ;

  public void  savePage(Page page) throws Exception  ;
  public void  savePage(String xml) throws Exception  ;
  public Page  getPage(String refId) throws Exception ; 
  public String  getPageAsXmlString(String refId) throws Exception ; 
  public void  removePage(String refId) throws Exception ;
  public void  removePageOfOwner(String owner) throws Exception ;
  public PageList  findAllPageDescriptions(Query q) throws Exception ;
  
  public void saveNodeNavigation(String owner , Node node) throws Exception ;
  public void removeNodeNavigation(String owner) throws Exception ;
  public Node getNodeNavigation(String owner) throws Exception ;
  public Node createNodeInstance() ;
  
  public List getPredefinedTemplates(String owner) ;
  public Page getPredefinedTemplate(String owner, String name) throws Exception ;
  
  public PortalACL getPortalACL() ;
}