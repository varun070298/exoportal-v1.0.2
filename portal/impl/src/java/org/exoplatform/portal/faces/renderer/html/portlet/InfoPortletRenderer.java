/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.portlet;

import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import javax.faces.context.ResponseWriter;
import javax.portlet.WindowState ;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.services.portal.model.Portlet;

/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: InfoPortletRenderer.java,v 1.3 2004/09/30 01:00:05 tuan08 Exp $
 */
public class InfoPortletRenderer extends PortletRenderer {
    
  protected void renderPortletHeaderBar(ResponseWriter w, UIPortlet uiPortlet,
                                        String portletTitle, String baseUrl) throws IOException {
    if (uiPortlet.getPortletModel().getShowInfoBar()) {
      w.write("<tr>");
      w.  write("<td valign='bottom' width='*' class='portlet-info-bar'>");
      w.    write(portletTitle);
      w.  write("</td>");
      w.write("</tr>");
    }
  }

  protected void renderPortletBody(ResponseWriter w, UIPortlet uiPortlet, String content) throws IOException {
    w.write("<tr>");
    w.  write("<td height='100%' class='>");
    w.  write(uiPortlet.getDecorator()); w.write("-portlet>");
    w.    write(content) ;
    w.  write("</td>");
    w.write("</tr>");
  }
  
  protected void renderPortletFooterBar(ResponseWriter w, UIPortlet uiPortlet, ResourceBundle res, 
                                        String portletTitle, String baseUrl) throws IOException {
  	Portlet model = uiPortlet.getPortletModel() ;
    w.write("<tr>");
    w.  write("<td class='portlet-footer-bar'>");
    if (model.getShowInfoBar()) {
      //icon actions
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
      //w.write(generateWindowStateLink(uiPortlet, WindowState.MINIMIZED, baseUrl)) ;
      if (model.getShowWindowState()) {
      	renderWindowStateLink(w, uiPortlet, res, WindowState.MAXIMIZED, baseUrl) ;
      }
    }
    w.  write("</td>");
    w.write("</tr>");
  }
}