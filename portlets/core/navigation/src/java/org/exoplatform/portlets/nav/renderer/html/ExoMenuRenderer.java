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
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.filter.AdminRequestFilter;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portlets.nav.component.UIMenu;
import org.exoplatform.services.portal.model.Node;
/**
 * Thu, May 5, 2004 @ 15:38
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ExoMenuRenderer.java,v 1.14 2004/10/19 14:30:53 benjmestrallet Exp $
 */
public class ExoMenuRenderer extends HtmlBasicRenderer {
  
  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    UIMenu uiMenu = (UIMenu) uiComponent ;
    SessionContainer scontainer = SessionContainer.getInstance() ;
    RequestInfo rinfo = (RequestInfo)scontainer.getComponentInstanceOfType(RequestInfo.class);
    ExoPortal portal = (ExoPortal)scontainer.getComponentInstanceOfType(ExoPortal.class) ;
    String ownerURI = rinfo.getOwnerURI() ;
    ResponseWriter w = context.getResponseWriter() ;
    Node selectNode = portal.getRootNode();
    w.write("<div class='UIMenu'>");
    if(uiMenu.isShowAdminButton()) {
      renderAdmin(res, w, uiMenu);
    }
    w.  write("<div class='tree'>");
    w.    write("<div class='root'>");
    w.      write("<a href='"); w.write(ownerURI); w.write(selectNode.getUri()); w.write("'>") ;
    w.        write(selectNode.getResolvedLabel()) ;
    w.      write("</a>") ;    
    w.    write("</div>");     
    renderNode(w, uiMenu, res, selectNode, ownerURI);
    w.  write("</div>");
    w.write("</div>");
    /*
    if(uiMenu.getShowReturn()) {
      w.write("<div class='inspect'>");
      w.write("<a href='"); w.write(ownerURI); w.write('?'); 
      w.write(AdminRequestFilter.ACTION); w.write('=') ; w.write(AdminRequestFilter.RETURN) ;
      w.write("'>") ;
      w.   write(res.getString("UIMenu.button.back")) ;
      w.write("</a>") ;
      w.write("</div>");
    }
    */
  }

  protected void renderNode(ResponseWriter w, UIMenu uiMenu, ResourceBundle res,
                            Node node, String ownerURI) throws IOException {
    int childrenSize = node.getChildrenSize();
    if (childrenSize == 0) return;
    w.write("<ul>");
    for (int i = 0; i < childrenSize; i++) {
      Node child = node.getChild(i);
      if(!child.isVisible()) continue ;
      if (child.getChildrenSize() > 0) {
        if (child.isSelectedPath()) {
          w.write("<li class='expanded-child'>");
        } else {          
          w.write("<li class='collapsed-child'>");
        }
      } else {
        w.write("<li class='leaf'>");
      }
      w.  write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
      w.      write(child.getResolvedLabel()) ;
      w.   write("</a>") ;
      if (child.isSelectedPath()) {
        renderNode(w, uiMenu, res,  child, ownerURI);
      }
      w.write("</li>");
    }
    w.write("</ul>");
  }

  protected void renderAdmin(ResourceBundle res, ResponseWriter w, UIMenu uiMenu) throws IOException {
    UIComponent uiChild = uiMenu.getUIToolbarView();
    uiChild.encodeBegin(FacesContext.getCurrentInstance());
    uiChild.encodeChildren(FacesContext.getCurrentInstance());
    uiChild.encodeEnd(FacesContext.getCurrentInstance());
  }
}