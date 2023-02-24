/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.renderer.html.portlet;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import org.exoplatform.commons.utils.Formater;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPortletCategory;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletregistery.PortletCategory;
import java.io.IOException;
import java.util.ResourceBundle;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class PortletCategoryRenderer  extends HtmlBasicRenderer{
  
  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIPortletCategory uiCategory  = (UIPortletCategory) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    PortletCategoryData portletCategoryData = uiCategory.getPortletCategoryData() ;
    PortletCategory category = portletCategoryData.getPortletCategory() ;
    w.write("<table class='UIPortletCategory'>") ;
    w.  write("<tr>") ;
    w.    write("<th colspan='2'>"); w.write(res.getString("UIPortletCategory.header.portlet-category-properties")) ; w.write("</th>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletCategory.label.portlet-category-name")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(category.getPortletCategoryName()) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletCategory.label.portlet-category-creation-date")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(category.getCreatedDate())) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletCategory.label.portlet-category-modification-date")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(category.getModifiedDate())) ; w.write("</td>") ;
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td class='label'>"); w.write(res.getString("UIPortletCategory.label.portlet-category-description")) ; w.write("</td>") ;
    w.    write("<td>"); w.write(ft_.format(category.getDescription(), "N/A")); w.write("</td>") ;
    w.  write("</tr>") ;
    w.write("</table>") ;
    renderFooter(uiCategory, w, res) ;
  }
  
  protected void renderFooter(UIPortletCategory uiCategory, 
                              ResponseWriter w, ResourceBundle res) throws IOException {
  }
}