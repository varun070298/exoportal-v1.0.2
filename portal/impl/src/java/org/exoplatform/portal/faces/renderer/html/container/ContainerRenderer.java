/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.container;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIContainer;
import org.exoplatform.portal.faces.renderer.BaseRenderer;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.PortalResources;
/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContainerRenderer.java,v 1.7 2004/09/21 03:41:34 tuan08 Exp $
 */
abstract public class ContainerRenderer extends BaseRenderer {
  
  protected boolean inEditMode = false;	

  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    UIContainer uiContainer = (UIContainer) uiComponent ;
    if(uiContainer.getComponentMode() == UIBasicComponent.COMPONENT_EDIT_MODE) {
    	renderEditMode(context, uiContainer);
    } else {
    	renderViewMode(context, uiContainer);
    }
  }

  protected void renderEditMode(FacesContext context, UIContainer uiContainer) throws IOException {
  
	inEditMode = true;
	
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    ResourceBundle res = appres.getApplicationResource();
    ResponseWriter w = context.getResponseWriter();
    w.write("<table class='customizer'");
    w.write(" id='") ; w.write(uiContainer.getId());w.write("-customizer'>") ;
    w.  write("<tr>") ;
    w.    write("<th align='left'>");
    w.    	write("Container: " + uiContainer.getDisplayTitle());
    w.    write("</th>");
    w.    write("<th align='right'>");
  	linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.add-portlet"), PortalConstants.addPortletParams);
  	linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.add-container"), PortalConstants.addContainerParams);
  	linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.edit-container"),  PortalConstants.editParams);
  	linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.remove-container"),PortalConstants.deleteParams);
    if(uiContainer.getFloat() == UIBasicComponent.FLOAT_BOTTOM) {
      linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.move-up"), PortalConstants.moveUpParams);
      linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.move-down"), PortalConstants.moveDownParams);
    } else {
      linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.move-left"), PortalConstants.moveUpParams);
      linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.move-right"), PortalConstants.moveDownParams);
    }
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    if(portal.getPortalMode() != ExoPortal.PORTAL_EDIT_PAGE_MODE) {
      linkRenderer_.render(w, uiContainer, res.getString("UIContainer.button.place-body"), PortalConstants.placeBodyParams);
    }
    w.    write("</th>");
    w.  write("</tr>") ;
    w.  write("<tr>") ;
    w.    write("<td colspan='2' style='height: 100%'>");
    renderViewMode(context, uiContainer) ;
    w.    write("</td>");
    w.  write("</tr>") ;
    w.write("</table>") ;
    
	inEditMode = false;
	    
  }
  
  abstract protected void renderViewMode(FacesContext context, UIContainer uiContainer) throws IOException ;
}