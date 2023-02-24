/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portlets.portletregistery.component.UIPortletInfo;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juin 2004
 */
public class PortletInfoRenderer 
	extends org.exoplatform.portal.faces.renderer.html.portlet.PortletInfoRenderer {
  
  private static Parameter[] editPortletParams = 
  	{ new Parameter(ACTION , UIPortletInfo.EDIT_PORTLET) }; 
  private static Parameter[] addRoleParams = 
  	{ new Parameter(ACTION , UIPortletInfo.ADD_ROLE) } ;
  private static Parameter[] deletePortletParams = 
  	{ new Parameter(ACTION , UIPortletInfo.DELETE_PORTLET) } ;
  
  protected void renderFooter(org.exoplatform.portal.faces.component.UIPortletInfo uiPortletInfo, 
    ResponseWriter w, ResourceBundle res) throws IOException {
  	w.write("<div align='center'>") ;
  	linkRenderer_.render(w, uiPortletInfo, res.getString("UIPortletInfo.button.edit-portlet"), editPortletParams) ;
  	linkRenderer_.render(w, uiPortletInfo,res.getString("UIPortletInfo.button.add-role"), addRoleParams) ;
  	linkRenderer_.render(w,uiPortletInfo,res.getString("UIPortletInfo.button.delete-portlet"), deletePortletParams) ;
  	w.write("</div>") ;
  }
}