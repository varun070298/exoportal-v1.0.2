/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import java.util.*;
import java.io.IOException;
import java.io.Writer   ;
import javax.portlet.* ;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.exoplatform.services.wsrp.consumer.*;
/*
 * @author  Tuan Nguyne
 *          tuan08@users.sourceforge.net
 * Tue, Feb 24, 2004 @ 14:35
 */
public class WSRPConfigModeHandler {
  public static final String UI_WSRP_CONFIG = "ui-wsrp-config" ;
  
  private Log log_ ;
  private ConsumerEnvironment consumerEnvironment_ ;
  private PortletConfig portletConfig_ ;
  
  public WSRPConfigModeHandler(PortletConfig config, ConsumerEnvironment env, Log log) {
    log_ = log ;
    consumerEnvironment_ = env ;
    portletConfig_ = config ;
  }
  
  public void render(RenderRequest request, RenderResponse response) {
    response.setContentType("text/html") ;
    try {
      ResourceBundle res = portletConfig_.getResourceBundle(response.getLocale()) ;
      UIWSRPConfig config  = getUIWSRPConfig(request) ;
      Writer w = response.getWriter() ;
      config.render(request, response, res) ;
      String baseURL = response.createActionURL().toString() ;
      w.write("<div align='center' height='35'>") ;
      writeRefreshLink(w,  baseURL, res) ;
      w.write("</div>") ;
    } catch (Exception ex) {
      log_.error("Error: ", ex) ;
    }
  }

  public void processAction(ActionRequest request, ActionResponse response) throws IOException {
    try {
      String action = request.getParameter("action") ;
      if ("refresh".equals(action)) {
        request.getPortletSession().removeAttribute(UI_WSRP_CONFIG) ;
      }
      UIWSRPConfig config  = getUIWSRPConfig(request) ;
      config.processAction(request, response) ;
    } catch (Exception ex) {
      log_.error("Error: ", ex) ;
    }
  }

  private UIWSRPConfig getUIWSRPConfig(PortletRequest request) throws Exception {
    PortletSession session = request.getPortletSession() ;
    UIWSRPConfig config = (UIWSRPConfig) session.getAttribute(UI_WSRP_CONFIG) ;
    if (config == null) {
      config = new UIWSRPConfig(consumerEnvironment_ , log_) ;
      session.setAttribute(UI_WSRP_CONFIG, config) ;
    }
    
    return config ;
  }

  private void writeRefreshLink(Writer w,  String baseURL, ResourceBundle res) throws IOException {
    w.write("<a href='" + baseURL + "&action=refresh'>") ;
    w.  write(res.getString("WSRPConfigModeHandler.button.refresh")) ;
    w.write("</a>") ;
  }
}
