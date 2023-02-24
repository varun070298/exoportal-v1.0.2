/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.renderer.html.portlet;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPortletInfo;
import org.exoplatform.services.portletregistery.Portlet;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juin 2004
 */
public class PortletInfoRenderer extends HtmlBasicRenderer{
  private static Parameter[] selectPortletParams = { new Parameter(ACTION , UIPortletInfo.SELECT_PORTLET) }; 

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIPortletInfo uiPortletInfo  = (UIPortletInfo) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    Portlet portlet = uiPortletInfo.getPortletData() ;
    w.write("<table class='UIPortletInfo'>") ;
    w.  write("<tr>") ;
    w.    write("<th colspan='2'>"); w.write(res.getString("UIPortletInfo.header.portlet-properties")) ; w.write("</th>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-display-name")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(portlet.getDisplayName()) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-creation-date")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(portlet.getCreatedDate())) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-modification-date")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(portlet.getModifiedDate())) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-application-name")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(portlet.getPortletApplicationName()) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-name")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(portlet.getPortletName()) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletInfo.label.portlet-description")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(portlet.getDescription())); w.write("</td>") ;
    w.  write("</tr>") ;
    w.write("</table>") ;
    renderFooter(uiPortletInfo, w , res) ;
  }
  
  protected void renderFooter(UIPortletInfo uiPortletInfo, 
                              ResponseWriter w, ResourceBundle res) throws IOException {
  	w.write("<div align='center'>") ;
  	linkRenderer_.render(w, uiPortletInfo, res.getString("UIPortletInfo.button.select"), selectPortletParams) ;
    w.write("</div>") ;
  }
}