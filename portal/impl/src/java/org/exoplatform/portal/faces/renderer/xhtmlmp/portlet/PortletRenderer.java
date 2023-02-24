/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.xhtmlmp.portlet;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.context.ResponseWriter;
import javax.portlet.WindowState;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.services.portal.model.Portlet;

/**
 * Fri, Aug 7th, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletRenderer.java,v 1.1 2004/08/08 19:25:39 tuan08 Exp $
 */
public class PortletRenderer 
	extends org.exoplatform.portal.faces.renderer.html.portlet.PortletRenderer {
 
  protected void renderViewMode(ResponseWriter w, UIPortlet uiPortlet, ResourceBundle res, 
                                String title, String content, String baseUrl) throws IOException {
  	 Portlet model = uiPortlet.getPortletModel() ;
     w.write("<table class='default-decorator'") ;
     w.write(" id='") ; w.write(uiPortlet.getId());w.write("'>") ; 
     renderPortletHeaderBar(w, uiPortlet, res, title, baseUrl) ;
     if (uiPortlet.getWindowState() != WindowState.MINIMIZED) {
       renderPortletBody(w, uiPortlet, content) ;
     }
     renderPortletFooterBar(w, uiPortlet, title, baseUrl) ;
     w.write("</table>\n");
  }
  
  protected void renderPortletBody(ResponseWriter w, UIPortlet uiPortlet, String content) throws IOException {
    w.write("<tr>\n<td colspan='2' class='default-portlet'>");
    w.write(content) ;
    w.write("\n</td>\n</tr>\n");
  }
}