/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.session;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.exoplatform.Constants;
import org.exoplatform.faces.context.PortletFacesContext;
/**
 * Apr 12, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalRequestParameters.java,v 1.6 2004/09/27 03:01:18 tuan08 Exp $
 **/
public class RequestInfo {
  final static public String PORTAL_VIEW_ID = "/portal.jsp";
  final static public String PAGE_VIEW_ID = "/page.jsp";
  
  final static public String PRIVATE_ACCESS = "private";
  final static public String PUBLIC_ACCESS = "public";
  final static public String ADMIN_ACCESS = "admin";
  
  private String portalOwner_ ;
  private String pageName_ ;
  private String portalAction_ ;
  private String targetComponentId_ ;
  private String portletWindowState_ ;
  private String portletMode_ ;
  private String portletActionType_ ;
  private String lang_ ;
  private String accessibility_  ;
  private String contextPath_ ;
  private String ownerURI_ ;
  private String nodeURI_ ;
  private String viewId_ ;
  
  public RequestInfo() {
    
  }
  
  public RequestInfo(HttpServletRequest req, String accessibility) {
    init(req, accessibility) ;
  }
  
  public void init(HttpServletRequest req, String accessibility) {
    //parse uri ;
    String pathInfo = req.getPathInfo() ;
    int slashIndex =  pathInfo.indexOf('/', 1) ;
    if(slashIndex > 0) {
      portalOwner_ = pathInfo.substring(1, slashIndex) ;
      pageName_ =  pathInfo.substring(slashIndex , pathInfo.length()) ;
    } else {
      portalOwner_ = pathInfo.substring(1, pathInfo.length()) ;
      pageName_ = null ;
    }
    if("/page.jsp".equals(pathInfo))   viewId_ = PAGE_VIEW_ID ;
    else  viewId_ =  PORTAL_VIEW_ID ;
    portalAction_ = req.getParameter(Constants.PORTAL_ACTION) ;
    targetComponentId_ = req.getParameter(Constants.COMPONENT_PARAMETER) ;
    portletWindowState_ = req.getParameter(Constants.WINDOW_STATE_PARAMETER) ;
    portletMode_ = req.getParameter(Constants.PORTLET_MODE_PARAMETER) ;
    portletActionType_ = req.getParameter(Constants.TYPE_PARAMETER);
    lang_ = req.getParameter(Constants.LANGUAGE_PARAMETER) ;
    contextPath_ = req.getContextPath() ;
    accessibility_ = accessibility ;
    ownerURI_  = new StringBuffer().append(contextPath_).append("/faces/").
                 append(accessibility).append("/").append(portalOwner_).toString() ;
    nodeURI_ = null ;
  }
  
  public String getPortalOwner() { return portalOwner_ ; }
  public String getPageName() { return pageName_ ; }
  
  public String getPortalAction() { return portalAction_ ; }
  public String getTargetComponentId() { return targetComponentId_ ; }
  public String getPortletWindowState() { return portletWindowState_ ; }
  public String getPortletMode() { return portletMode_ ; }
  public String getPortletActionType() { return portletActionType_ ; }
  public String getLanguage() { return lang_ ; }
  
  public String getContextPath() { return contextPath_ ; }
  public String getAccessibility() { return accessibility_ ; }
  
  public String getViewId() { return viewId_ ; }
  
  public String getOwnerURI() { return  ownerURI_ ; }
  
  public String getPageURI() {
    if(nodeURI_ == null) {
      String pageName = pageName_ ;
      if(pageName == null) {
        pageName = getPortal().getSelectedNode().getUri() ;
      }
      nodeURI_ = ownerURI_ + pageName ;
    }
    return nodeURI_ ; 
  }
  
  public ExoPortal getPortal() {
    FacesContext portalContext = FacesContext.getCurrentInstance() ;
    if (portalContext instanceof PortletFacesContext) {
      portalContext = ((PortletFacesContext) portalContext).getPortalFacesContext() ;   
    }
    return (ExoPortal) portalContext.getViewRoot().getChildren().get(0) ;
  }
  
  static public String getPortalOwner(HttpServletRequest req)  {
    String pathInfo = req.getPathInfo() ;
    int slashIndex =  pathInfo.indexOf('/', 1) ;
    String owner = null ;
    if(slashIndex > 0) {
      owner = pathInfo.substring(1, slashIndex) ;
    } else {
      owner = pathInfo.substring(1, pathInfo.length()) ;
    }
    return owner ;
  }
}