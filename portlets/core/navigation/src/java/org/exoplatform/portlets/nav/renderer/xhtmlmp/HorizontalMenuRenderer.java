/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.nav.renderer.xhtmlmp;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portlets.nav.component.UINavigation;
import org.exoplatform.portlets.nav.renderer.html.ExoMenuRenderer;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.PageReference;
/**
 * Thu, May 5, 2004 @ 15:38
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: HorizontalMenuRenderer.java,v 1.9 2004/10/07 21:57:17 fichatel Exp $
 */
public class HorizontalMenuRenderer extends ExoMenuRenderer {
  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    UINavigation uiMenu = (UINavigation) uiComponent;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    String mimeType = uiMenu.getPreferedMimeType() ;
    ResponseWriter w = context.getResponseWriter() ;
    Node selectNode = uiMenu.getSelectedNode() ;
    w.write(res.getString("UIHorizontalMenu.icon.banner"));
    w.write("<br/>");
    w.  write("<a href='"); w.write(ownerURI); w.write("/home/sitemap"); w.write("'>") ;
    w.      write(res.getString("UIHorizontalMenu.icon.site-map")) ;
    w.   write("</a>") ;      
    for (int i = 0; i < selectNode.getChildrenSize(); i++) {
      Node node = selectNode.getChild(i);
      PageReference pref = node.getPageReference(mimeType) ;
      if(pref == null || pref.isVisible()) {
        String label = node.getIcon();
        if(label == null) label = node.getResolvedLabel() ;
        w.  write("<a href='"); w.write(ownerURI); w.write(node.getUri()); w.write("'>") ;
        w.    write(label) ;
        w.   write("</a>") ;    
        w.write(' ') ;
      }
    }
  } 
}