/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.portal;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.portal.faces.component.UIPortal;
import org.exoplatform.portal.faces.renderer.BaseRenderer;
import org.exoplatform.portal.html.PortalProviderRenderer;
/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalRenderer.java,v 1.11 2004/10/08 18:03:13 benjmestrallet Exp $
 */
public class PortalRenderer extends BaseRenderer {
  private PortalProviderRenderer portalProvider_ ;
  
  
  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    UIPortal uiPortal = (UIPortal) uiComponent ;
    PortalProviderRenderer portalProvider = getProvider() ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    ResponseWriter w = context.getResponseWriter();
    w.write("\n<!DOCTYPE html\n"); 
    w.write("     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
    w.write("     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");     
    w.write("<html xmlns=\"http://www.w3.org/1999/xhtml\"> \n");
    w.  write("<head>\n");
    portalProvider.renderTitle(w, res) ;
    w.    write("<base href='"); w.write(request.getScheme()); w.write("://"); w.write(request.getServerName()); w.write(":"); 
          w.write(Integer.toString(request.getServerPort())); w.write("'/>") ;
    portalProvider.renderMeta(w, res) ;
    w.    write("<link rel='stylesheet' type='text/css' href='") ;
          w.write(request.getContextPath()) ;
          w.write("/default-skin.css' />\n"); 
    w.    write(uiPortal.getUserCss());
    portalProvider.renderLink(w, res) ;
    portalProvider.renderScript(w, res) ;
    w.  write("</head>\n");
    if(uiPortal.hasMessage()) {
      w.write("<body onload=\"javascript:alert('"); w.write(getMessage(uiPortal, res)); w.write("')\">\n") ;
    } else {
      w.write("<body>\n") ;
    }
    renderChildren(context, uiPortal) ; 
    w.  write("</body>\n") ;
    w.write("</html>\n") ;
    uiPortal.setQueueEvent(false) ;
  }
  
  private String getMessage(UIPortal uiPortal, ResourceBundle res) {
    StringBuffer b = new StringBuffer() ;
    java.util.List messages = uiPortal.getMessages() ;
    for(int i = 0; i < messages.size(); i++) {
      ExoFacesMessage  m = (ExoFacesMessage) messages.get(i) ;
      b.append(m.getSummary(res)).append("\\n") ;
    }
    uiPortal.clearMessages() ;
    return b.toString()  ;
  }
  
  private PortalProviderRenderer getProvider() {
    if(portalProvider_ == null) {
      portalProvider_ = 
        (PortalProviderRenderer)PortalContainer.getComponent(PortalProviderRenderer.class) ;
    }
    return portalProvider_ ;
  }
}
