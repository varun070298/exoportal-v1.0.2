/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.page;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.UIPage;
import org.exoplatform.portal.faces.renderer.BaseRenderer;
/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PageRenderer.java,v 1.6 2004/08/28 18:59:18 tuan08 Exp $
 */
abstract public class PageRenderer extends BaseRenderer {

  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {	
    UIPage uiPage = (UIPage) uiComponent ;   
    if(uiPage.getComponentMode() == UIPage.COMPONENT_VIEW_MODE) {
    	renderViewMode(context, uiPage);
    } else {
    	renderEditMode(context, uiPage);
    }
  }

  private void renderEditMode(FacesContext context, UIPage uiPage) throws IOException {
  	ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter();
    w.write("<table class='customizer'");
    w.write(" id='") ; w.write(uiPage.getId());w.write("-customizer'>") ;
    w.  write("<tr>") ;
    w.    write("<th align='left'>");
    w.    	write("Page: " + uiPage.getDisplayTitle());
    w.    write("</th>");
    w.    write("<th align='right'>");
   	linkRenderer_.render(w, uiPage, res.getString("UIPage.button.add-portlet"), PortalConstants.addPortletParams);
   	linkRenderer_.render(w, uiPage, res.getString("UIPage.button.add-container"), PortalConstants.addContainerParams);
   	linkRenderer_.render(w, uiPage, res.getString("UIPage.button.edit-page"), PortalConstants.editParams);
    w.    write("</th>");
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td colspan='2' style='height: 100%'>");
    renderViewMode(context, uiPage) ;
    w.    write("</td>");
    w.  write("</tr>") ;
    w.write("</table>") ;
  }
  
  abstract protected void renderViewMode(FacesContext context, UIPage uiPage) throws IOException ;
}