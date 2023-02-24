/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portlets.nav.component.UIMenu;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.PageReference;
/**
 * Thu, May 5, 2004 @ 15:38
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ExoMenuTreeRenderer.java,v 1.2 2004/09/23 12:44:45 tuan08 Exp $
 */
public class ExoMenuTreeRenderer extends ExoMenuRenderer {
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIMenu uiMenu = (UIMenu) component;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter();
    SessionContainer scontainer = SessionContainer.getInstance() ;
    ExoPortal portal = (ExoPortal)scontainer.getComponentInstanceOfType(ExoPortal.class) ;
    RequestInfo rinfo = (RequestInfo)scontainer.getComponentInstanceOfType(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    String mimeType = uiMenu.getPreferedMimeType() ;
    Node root = portal.getRootNode() ;
    w.write("<a href='"); w.write(ownerURI);  w.write("'>") ;
    w.  write("Home") ;
    w.write("</a>") ;
    w.write("<ul>");
    for(int i = 0; i < root.getChildrenSize(); i++){
      Node child = root.getChild(i);
      PageReference pref = child.getPageReference(mimeType) ;
      if(pref == null || pref.isVisible()) {   
        w.write("<li>");
        w. write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
        w.      write(child.getResolvedLabel()) ;
        w. write("</a>") ;
        encodeNode(child,mimeType, w , ownerURI);
        w.write("</li>");
      }
    }
    w.write("</ul>");
    renderAdmin(res, w, uiMenu);
  }

  public void encodeNode(Node node, String mimeType, ResponseWriter w, String ownerURI) throws IOException {
    w.write("<ul>");
    for (int n = 0; n < node.getChildrenSize(); n++) {
      Node child = node.getChild(n);
      PageReference pref = child.getPageReference(mimeType) ;
      if(pref == null || pref.isVisible()) {
        w.write("<li>");
        w. write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
        w.      write(child.getResolvedLabel()) ;
        w. write("</a>") ;
        encodeNode(child, mimeType, w, ownerURI);
        w.write("</li>");
      }
    }
    w.write("</ul>");
  }
}