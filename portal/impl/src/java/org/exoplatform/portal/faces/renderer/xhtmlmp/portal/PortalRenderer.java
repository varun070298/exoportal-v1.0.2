/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.xhtmlmp.portal;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIPortal;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portal.session.RequestInfo;
/**
 * Date: Jul 02, 2004
 * @author : Moron Fran�ois
 * @email:   francois.moron@rd.francetelecom.com
 * 
 */
public class PortalRenderer 
	extends org.exoplatform.portal.faces.renderer.html.portal.PortalRenderer {

  public PortalRenderer() {
  }
  
  protected void decodePortalAction(String portalAction, FacesContext context,
  		                              UIBasicComponent component) {
  }
  
  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    SessionContainer scontainer = SessionContainer.getInstance() ;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class) ;
    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
    UIPortal uiPortal = (UIPortal) uiComponent ;
    PortalResources appres = 
      (PortalResources)scontainer.getComponentInstanceOfType(PortalResources.class);
    appres.getLocaleConfig().setOutput(response) ;
    
    ResponseWriter w = context.getResponseWriter();
    w.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
    w.write("<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\"\n");
    w.write("\"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\" >\n\n");
    w.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n") ;
    w.write("<head>\n");
    w.	write("<title>"); w.write(rinfo.getPortalOwner()); w.write("'s Page</title>");
    w.	write(uiPortal.getUserCss());
    w.  write("<link rel='stylesheet' type='text/css' href='") ;
    	w.write(rinfo.getContextPath()) ;
      w.write("/xhtmlmp-default-skin.css' />\n"); 
    w.write("</head>");
    w.	write("<body>") ;
    renderChildren(context, uiPortal) ;
    w.  write("</body>") ;
    w.write("</html>") ;
    uiPortal.setQueueEvent(false) ;
  }
}