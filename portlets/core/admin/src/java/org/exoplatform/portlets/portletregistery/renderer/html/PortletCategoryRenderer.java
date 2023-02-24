/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.renderer.html;


import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portlets.portletregistery.component.UIPortletCategory;
import java.io.IOException;
import java.util.ResourceBundle;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class PortletCategoryRenderer  
	extends org.exoplatform.portal.faces.renderer.html.portlet.PortletCategoryRenderer {
	
  private static Parameter[] editCategoryParams = 
  	{ new Parameter(FacesConstants.ACTION ,  UIPortletCategory.EDIT_CATEGORY) };
  private static Parameter[] addPortletParams = 
  	{ new Parameter(FacesConstants.ACTION , UIPortletCategory.ADD_PORTLET) } ;
  private static Parameter[] deleteCategoryParams = 
  	{ new Parameter(FacesConstants.ACTION , UIPortletCategory.DELETE_CATEGORY) } ;
  
  protected void renderFooter(org.exoplatform.portal.faces.component.UIPortletCategory uiCategory, 
                              ResponseWriter w, ResourceBundle res) throws IOException {
  	w.write("<div align='center'>") ;
    linkRenderer_.render(w, uiCategory, res.getString("UIPortletCategory.button.edit-category"), editCategoryParams) ;
    linkRenderer_.render(w, uiCategory, res.getString("UIPortletCategory.button.add-portlet"), addPortletParams) ;
    linkRenderer_.render(w,uiCategory, res.getString("UIPortletCategory.button.delete-category"), deleteCategoryParams) ;
    w.write("</div>") ;
  }
}