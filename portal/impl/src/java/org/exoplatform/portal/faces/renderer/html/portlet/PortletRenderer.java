/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.portlet;

import java.io.IOException;
import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.ExceptionUtil ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.portal.faces.renderer.BaseRenderer;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.portal.model.Portlet;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletRenderer.java,v 1.19 2004/11/01 21:06:50 tuan08 Exp $
 */
public class PortletRenderer extends BaseRenderer {
  private PortletContainerService portletContainer_ ;
  
  public PortletRenderer() {
  }

  protected void decodeComponentAction(FacesContext context, UIComponent uiComponent) {
    UIPortlet basicComponent = (UIPortlet) uiComponent ;
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    basicComponent.broadcast(new ExoActionEvent(uiComponent, "portletAction", paramMap)) ;
  }
  
  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    UIPortlet uiPortlet = (UIPortlet) uiComponent ;
    ResponseWriter w = context.getResponseWriter();
    if(uiPortlet.hasError()) {
      w.write(uiPortlet.getErrorMessage()) ;
      return ;
    }
    PortletContainerService portletContainer = getPortletContainerService() ;
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest() ;
    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
    SessionContainer scontainer = SessionContainer.getInstance() ;
    RequestInfo rinfo = (RequestInfo) scontainer.getComponentInstanceOfType(RequestInfo.class);
    UserProfile uprofile = 
      (UserProfile) scontainer.getComponentInstanceOfType(UserProfile.class) ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    
    Map renderParams = uiPortlet.getRenderParameters() ;
    if (renderParams == null) {
      renderParams = copyRequestParameterMap(request.getParameterMap()) ;
      uiPortlet.setRenderParameters(renderParams);
    }
    RenderInput input = new RenderInput();
    String baseUrl = null ;
    String configurationSource = uiPortlet.getWindowId().getConfigurationSource() ;
    if(configurationSource == ExoWindowID.DEFAULT_PORTAL_CONFIG ||
       configurationSource == ExoWindowID.MOBILE_PORTAL_CONFIG ) {
      StringBuffer sB = new StringBuffer(rinfo.getOwnerURI()).
      append("?" + Constants.COMPONENT_PARAMETER).append("=").
      append(uiPortlet.getWindowId().getUniqueID());
      baseUrl = sB.toString() ;
    } else {
      StringBuffer sB = new StringBuffer(rinfo.getPageURI()).
      append("?" + Constants.COMPONENT_PARAMETER).append("=").
      append(uiPortlet.getWindowId().getUniqueID());
      baseUrl = sB.toString() ;
    }
    input.setBaseURL(baseUrl);
    input.setWindowID(uiPortlet.getWindowId());
    input.setUserAttributes(uprofile.getUserInfoMap());
    input.setPortletMode(uiPortlet.getPortletMode());
    input.setWindowState(uiPortlet.getWindowState());
    input.setMarkup("text/html");
    input.setRenderParameters(renderParams);
    input.setTitle(uiPortlet.getDisplayTitle());
    input.setUpdateCache(uiPortlet.getUpdateCache());
    RenderOutput output = null;
    String portletContent = "EXO-ERROR: Portlet container throw an exception\n" ;
    try {
      output = portletContainer.render(request, response, input);
      uiPortlet.setUpdateCache(false) ;
      if(output.getContent() == null) {
        //System.out.println("portlet has error " + uiPortlet.getId()) ;
        portletContent = uiPortlet.getId() + " has error";
      } else {
        portletContent = new String(output.getContent()) ;
      }
    } catch (Throwable ex) {
      ex = ExceptionUtil.getRootCause(ex) ;
      portletContent +=  ExceptionUtil.getStackTrace(ex, 100) ;
      LogUtil.getLog(getClass()).error("Render Portlet Error: ", ex) ;
    }
    if (portletContent == null) portletContent = "" ;   
    String  portletTitle = null ;
    if(output != null) portletTitle = output.getTitle() ;
    if(portletTitle == null ) portletTitle = "Portlet" ;
    
    if(uiPortlet.getComponentMode() == UIBasicComponent.COMPONENT_EDIT_MODE) {
    	renderEditMode(w , uiPortlet, res, portletTitle, portletContent, baseUrl) ; 
    } else {
    	renderViewMode(w, uiPortlet, res, portletTitle, portletContent, baseUrl) ;
    }
  }

  protected void renderViewMode(ResponseWriter w, UIPortlet uiPortlet, ResourceBundle res, 
                                String title, String content, String baseUrl) throws IOException {
  	 Portlet model = uiPortlet.getPortletModel() ;
     w.write("<table class='"); w.write(model.getDecorator()) ; w.write("-decorator'") ;
     w.write(" id='") ; w.write(uiPortlet.getId());w.write("'>") ; 
     renderPortletHeaderBar(w, uiPortlet, res, title, baseUrl) ;
     if (uiPortlet.getWindowState() != WindowState.MINIMIZED) {
       renderPortletBody(w, uiPortlet, content) ;
     }
     renderPortletFooterBar(w, uiPortlet, title, baseUrl) ;
     w.write("</table>\n");
  }
  
  protected void renderEditMode(ResponseWriter w , UIPortlet uiPortlet, ResourceBundle res, 
                                String title, String content, String baseURL) throws IOException {
    w.write("<table class='customizer'") ;
    w.write(" id='") ; w.write(uiPortlet.getId());w.write("-customizer'>") ;
    w.  write("<tr>") ;
    w.    write("<th align='left'>") ;
    w.      write("Portlet");
    w.    write("</th>") ;
    w.    write("<th align='right'>") ;
    linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.edit-portlet"), PortalConstants.editParams);
    linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.remove-portlet"), baseURL, PortalConstants.deleteParams, null);
    if(uiPortlet.getFloat() == UIBasicComponent.FLOAT_BOTTOM) {
      linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.move-up"),  PortalConstants.moveUpParams);
      linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.move-down"), PortalConstants.moveDownParams);
    } else {
      linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.move-left"), PortalConstants.moveUpParams);
      linkRenderer_.render(w, uiPortlet, res.getString("UIPortlet.button.move-right"), PortalConstants.moveDownParams);
    }
    w.    write("</th>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td colspan='2' style='height: 100%'>") ;
    renderViewMode(w, uiPortlet, res, title, content, baseURL) ;
    w.    write("</td>") ;
    w.  write("</tr>") ;
    w.write("</table>") ;
  }
  
  protected void renderModeLink(ResponseWriter w , UIPortlet uiPortlet, ResourceBundle res, 
                                String mode, String baseUrl) throws IOException {
  	if(uiPortlet.isNew()) return ;
    String currentMode = uiPortlet.getPortletMode().toString() ;
    if(currentMode.equals(mode)) mode = UIPortlet.PORTLET_VIEW_MODE ;
    w.write("<a href='");  w.write(baseUrl) ;
    w.write(Constants.AMPERSAND + Constants.PORTLET_MODE_PARAMETER + "="); w.write(mode);
    w.write("'>") ;
    w.write(res.getString(mode)) ;
    w.write("</a>");
  }

  protected void renderWindowStateLink(ResponseWriter w, UIPortlet portlet, ResourceBundle res, 
                                       WindowState state, String baseUrl) throws IOException {
    WindowState currentState = portlet.getWindowState()  ;
    if(currentState == state) state = WindowState.NORMAL  ;
    w.write("<a  href='") ;
    w.write(baseUrl) ;
    w.write(Constants.AMPERSAND + Constants.WINDOW_STATE_PARAMETER + "=");
    w.write(state.toString());
    w.write("'>") ;
    w.write(res.getString(state.toString())) ;
    w.write("</a>");
  }

  protected void renderPortletBody(ResponseWriter w, UIPortlet uiPortlet, String content) throws IOException {
    w.write("<tr>\n<td colspan='2' class='"); 
    w.write(uiPortlet.getPortletModel().getPortletStyle()); w.write("-portlet'>");
    w.write(content) ;
    w.write("\n</td>\n</tr>\n");
  }

  protected void renderPortletHeaderBar(ResponseWriter w, UIPortlet uiPortlet, ResourceBundle res, 
                                        String portletTitle, String baseUrl) throws IOException {
    Portlet model = uiPortlet.getPortletModel() ;
    if (!model.getShowInfoBar())  return ;
    w.write("<tr>\n");
    w.  write("<td class='portlet-info-bar'>");
    w.    write( portletTitle);
    w.  write("</td>\n");
    //icon actions
    w.  write("<td class='portlet-info-bar' align='right'>\n");
    if(model.getShowPortletMode()) {
      List modes = uiPortlet.getHtmlSupportModes() ;
      if (uiPortlet.hasComponentAdminRole()) {
        for (int i =0 ; i < modes.size() ; i++) {
          String mode = (String) modes.get(i) ;
          renderModeLink(w, uiPortlet,res,  mode, baseUrl) ;
        }
      } else {
        for (int i =0 ; i < modes.size() ; i++) {
          String mode = (String) modes.get(i) ;
          if("help".equals(mode)) {
            renderModeLink(w, uiPortlet, res,  mode, baseUrl) ;
          }
        }
      }
    }
    if(model.getShowWindowState()) {
      renderWindowStateLink(w, uiPortlet, res, WindowState.MINIMIZED, baseUrl) ;
      renderWindowStateLink(w, uiPortlet, res, WindowState.MAXIMIZED, baseUrl) ;
    }
    w.  write("</td>\n");
    w.write("</tr>\n");
  }

  protected void renderPortletFooterBar(ResponseWriter w, UIPortlet uiPortlet,
                                        String portletTitle, String baseUrl) throws IOException {
  }

  private PortletContainerService getPortletContainerService() {
    if ( portletContainer_ == null) {
      portletContainer_ = 
        (PortletContainerService) PortalContainer.getInstance().
                                  getComponentInstanceOfType(PortletContainerService.class);
    } 
    return portletContainer_ ;
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
}
