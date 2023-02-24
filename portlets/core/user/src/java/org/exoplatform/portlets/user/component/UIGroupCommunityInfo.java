/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.event.UIComponentObserver;
import org.exoplatform.portal.filter.AdminRequestFilter;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.portal.community.CommunityConfigService;
import org.exoplatform.services.portal.community.CommunityNavigation;
import org.exoplatform.services.portal.community.CommunityPortal;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIUserProfile.java,v 1.14 2004/09/28 18:19:23 tuan08 Exp $
 */
public class UIGroupCommunityInfo extends UIExoCommand {
  static public Parameter[] ADMIN_PORTAL = {new Parameter(ACTION , "adminPortal") } ;
  static public Parameter[] ADD_EDIT_PORTAL = {new Parameter(ACTION , "addEditPortal") } ;
  static public Parameter[] DELETE_PORTAL = {new Parameter(ACTION , "deletePortal") } ;
  
  static public Parameter[] ADMIN_NAV = {new Parameter(ACTION , "adminNav") } ;
  static public Parameter[] ADD_EDIT_NAV = {new Parameter(ACTION , "addEditNav") } ;
  static public Parameter[] DELETE_NAV = {new Parameter(ACTION , "deleteNav") } ;
  
  private CommunityConfigService service_ ;
  private CommunityPortal communityPortal_ ;
  private CommunityNavigation communityNav_ ;
   
  public UIGroupCommunityInfo(CommunityConfigService service) throws Exception {
  	setRendererType("GroupCommunityInfoRenderer") ;
    service_ = service ;
    addActionListener(AdminPortalActionListener.class, "adminPortal") ;
    addActionListener(AddEditPortalActionListener.class, "addEditPortal") ;
    addActionListener(DeletePortalActionListener.class, "deletePortal") ;
    
    addActionListener(AdminNavActionListener.class, "adminNav") ;
    addActionListener(AddEditNavActionListener.class, "addEditNav") ;
    addActionListener(DeleteNavActionListener.class, "deleteNav") ;
  }
  
  public void registerComponentObserver(UIExoComponent parent)  {
    UIGroupExplorer uiExplorer = 
      (UIGroupExplorer) parent.getAncestorOfType(UIGroupExplorer.class) ;
    uiExplorer.addObserver(new GroupChangeObserver());
  }
  
  public CommunityPortal getCommunityPortal() { return communityPortal_ ; }
  public void  setCommunityPortal(CommunityPortal cp) {
    communityPortal_ = cp ;
  }
  
  public CommunityNavigation getCommunityNavigation() { return communityNav_ ; }
  public void  setCommunityNavigation(CommunityNavigation cn) {
    communityNav_ = cn ;
  }
  
  class GroupChangeObserver extends UIComponentObserver {
    public void onChange(UIExoComponent target) throws Exception  {
      UIGroupExplorer uiExplorer = (UIGroupExplorer) target ;
      Group group = uiExplorer.getCurrentGroup();
      if(group != null) {
        communityPortal_ = service_.getCommunityPortal(group) ;
        communityNav_ = service_.getCommunityNavigation(group) ;
      }
    }
  }
  
  static public class AdminPortalActionListener extends ExoActionListener  {
    public AdminPortalActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
      String path = rinfo.getContextPath() + "/faces/admin/" + 
                    uiInfo.communityPortal_.getPortal() + 
                    "?" + AdminRequestFilter.ACTION + "=" + AdminRequestFilter.ADMIN ; 
      ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
      ActionResponse response = (ActionResponse) eContext.getResponse();
      response.sendRedirect(path);
    }
  }
  
  static public class AddEditPortalActionListener extends ExoActionListener  {
    public AddEditPortalActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      UICommunityPortalForm uiForm = 
        (UICommunityPortalForm) uiInfo.getSibling(UICommunityPortalForm.class) ;
      uiForm.setCommunityPortal(uiInfo.communityPortal_) ;
      uiInfo.setRenderedSibling(UICommunityPortalForm.class) ;
    }
  }
  
  static public class DeletePortalActionListener extends ExoActionListener  {
    public DeletePortalActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      if(uiInfo.communityPortal_ != null) {
        uiInfo.service_.removeCommunityPortal(uiInfo.communityPortal_);
        uiInfo.communityPortal_ = null ;
      }
    }
  }
  
  static public class AddEditNavActionListener extends ExoActionListener  {
    public AddEditNavActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      UICommunityNavForm uiForm = 
        (UICommunityNavForm) uiInfo.getSibling(UICommunityNavForm.class) ;
      uiForm.setCommunityNavigation(uiInfo.communityNav_) ;
      uiInfo.setRenderedSibling(UICommunityNavForm.class) ;
    }
  }
  
  static public class DeleteNavActionListener extends ExoActionListener  {
    public DeleteNavActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      if(uiInfo.communityNav_ != null) {
        uiInfo.service_.removeCommunityNavigation(uiInfo.communityNav_);
        uiInfo.communityNav_ = null ;
      }
    }
  }
  
  static public class AdminNavActionListener extends ExoActionListener  {
    public AdminNavActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) event.getComponent() ;
      RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
      String path = rinfo.getContextPath() + "/faces/admin/" + 
                    uiInfo.communityNav_.getNavigation() + 
                    "?" + AdminRequestFilter.ACTION + "=" + AdminRequestFilter.ADMIN ; 
      ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
      ActionResponse response = (ActionResponse) eContext.getResponse();
      response.sendRedirect(path);
    }
  }
  
  public String getFamily() { return getClass().getName() ; }
}