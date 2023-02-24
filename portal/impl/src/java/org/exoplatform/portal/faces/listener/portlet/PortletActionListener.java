/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.portlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIPage;
import org.exoplatform.portal.faces.component.UIPortal;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.log.LogUtil;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: PortletActionListener.java,v 1.1 2004/09/26 02:25:48 tuan08 Exp $
 */
public class PortletActionListener extends ExoActionListener  {
  private PortletContainerService portletContainer_ ;
  
  public void execute(ExoActionEvent event) throws Exception {
    UIPortlet uiPortlet = (UIPortlet) event.getSource() ;
    FacesContext context = FacesContext.getCurrentInstance() ;
    SessionContainer scontainer = SessionContainer.getInstance() ;
    UserProfile uprofile = 
      (UserProfile) scontainer.getComponentInstanceOfType(UserProfile.class) ;
    HttpServletRequest request = 
      (HttpServletRequest) context.getExternalContext().getRequest();
    RequestInfo rinfo = (RequestInfo) scontainer.getComponentInstanceOfType(RequestInfo.class);
    
    String type = rinfo.getPortletActionType() ;
    String portletMode = rinfo.getPortletMode() ;
    String windowState = rinfo.getPortletWindowState() ;
    if (portletMode != null) {
      uiPortlet.setPortletMode(portletMode) ;
      Map renderParams = copyRequestParameterMap(request.getParameterMap()) ;
      uiPortlet.setRenderParameters(renderParams);
      uiPortlet.setUpdateCache(true) ;
    }

    if (type != null ) {
      if (type.equals("action")) {
        HttpServletResponse response = 
          (HttpServletResponse) context.getExternalContext().getResponse();
    
        PortletContainerService portletContainer = getPortletContainerService() ;
        //String baseUrl = request.getRequestURL().toString() ;
        //create the mock portlet window context
        ActionInput actionInput = new ActionInput();
        //actionInput.setBaseURL(baseUrl);
        actionInput.setWindowID(uiPortlet.getWindowId());
        actionInput.setUserAttributes(uprofile.getUserInfoMap());
        actionInput.setPortletMode(uiPortlet.getPortletMode());
        actionInput.setWindowState(uiPortlet.getWindowState());
        actionInput.setMarkup("text/html");
        actionInput.setStateChangeAuthorized(true);
        ActionOutput output = null;
        try {
          output = portletContainer.processAction(request, response, actionInput);
          String location = (String) output.getProperties().get(Output.SEND_REDIRECT);
          if(location != null){
            response.sendRedirect(location);
            //uiPortlet.setRequestToRedirect(true);
            String login = (String) output.getProperties().get(Output.LOGIN);         
            if(login != null){
              String password = (String) output.getProperties().get(Output.PASSWORD);      
              request.getSession().setAttribute(Output.LOGIN, login);
              request.getSession().setAttribute(Output.PASSWORD, password);
            }
            String logout = (String) output.getProperties().get(Output.LOGOUT);
            if(logout != null){
              request.getSession().invalidate();
            }
            FacesContext.getCurrentInstance().responseComplete();
          }
          PortletMode mode = output.getNextMode() ;
          WindowState state = output.getNextState() ;
          if (mode != null ) {
            uiPortlet.setPortletMode(mode) ;
          }
          if (state != null)  {
            windowState = state.toString() ;
          }
          uiPortlet.setRenderParameters(output.getRenderParameters()) ;
        } catch (Exception ex) {
         LogUtil.getLog(getClass()).error("Handle portlet action error: ", ex) ;
        }
        uiPortlet.setUpdateCache(true) ;
      } else {
        Map renderParams = copyRequestParameterMap(request.getParameterMap()) ;
        uiPortlet.setRenderParameters(renderParams);
        uiPortlet.setUpdateCache(true) ;
      }
    }
    
    if (windowState != null ) {
      UIPage uiPage =  (UIPage)uiPortlet.getAncestorOfType(UIPage.class) ;
      if (windowState.equals(WindowState.MAXIMIZED.toString())) {
        uiPortlet.setWindowState(WindowState.MAXIMIZED) ;
        if (uiPage != null) uiPage.setMaximizedPortlet(uiPortlet) ;
      } else if (windowState.equals(WindowState.MINIMIZED.toString())) {
        uiPortlet.setWindowState(WindowState.MINIMIZED) ;
        if(uiPage != null) uiPage.setMaximizedPortlet(null) ;
      } else {
        uiPortlet.setWindowState(WindowState.NORMAL) ;
        if(uiPage != null) uiPage.setMaximizedPortlet(null) ;
      }
    }
  }
  
  private Map copyRequestParameterMap(Map map) {
    Map temp = new HashMap(10) ;
    Iterator keys = map.keySet().iterator() ;
    while (keys.hasNext()) {
      String key = (String) keys.next() ;
      temp.put(key, map.get(key)) ;
    }
    return temp ;
  }
  
  protected void postExecute(ExoActionEvent action) {
    UIPortal portal = (UIPortal)SessionContainer.getComponent(ExoPortal.class) ;
    if(!portal.hasQueuEvent()) {
      FacesContext.getCurrentInstance().renderResponse() ;
    }
  }
  
  private PortletContainerService getPortletContainerService() {
    if ( portletContainer_ == null) {
      portletContainer_ = 
        (PortletContainerService) PortalContainer.getInstance().
                                  getComponentInstanceOfType(PortletContainerService.class);
    } 
    return portletContainer_ ;
  }
}