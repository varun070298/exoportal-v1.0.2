/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.renderer.html;


import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portlets.portletregistery.component.UIPortletCategories;
import java.io.IOException;
import java.util.ResourceBundle;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class PortletCategoriesRenderer 
	extends org.exoplatform.portal.faces.renderer.html.portlet.PortletCategoriesRenderer {
  private static Parameter[] createCategoryParams =	{new Parameter(ACTION , UIPortletCategories.CREATE_CATEGORY) } ;
  private static Parameter[] importPortletParams = 	{new Parameter(ACTION , UIPortletCategories.IMPORT_PORTLETS) } ;
  private static Parameter[] cleanAllParams = 	{new Parameter(ACTION , UIPortletCategories.CLEAN_ALL) } ;
  
  protected void renderFooter(org.exoplatform.portal.faces.component.UIPortletCategories uiCategories, 
                              ResponseWriter w, ResourceBundle res) throws IOException {
  	w.write("<div align='center'>") ;
    linkRenderer_.render(w, uiCategories,res.getString("UIPortletCategories.button.create-category"), createCategoryParams) ;
    linkRenderer_.render(w, uiCategories,res.getString("UIPortletCategories.button.import-portlets"), importPortletParams) ;
    linkRenderer_.render(w, uiCategories,res.getString("UIPortletCategories.button.clean-all"), cleanAllParams) ;
    w.write("</div>") ;
  }
}