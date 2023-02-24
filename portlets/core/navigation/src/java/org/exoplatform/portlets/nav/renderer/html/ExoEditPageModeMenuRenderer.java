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
import org.exoplatform.portlets.nav.component.UIMenu;
import org.exoplatform.portlets.nav.component.UINavigation;
import org.exoplatform.services.portal.model.Node;
/**
 * Thu, May 5, 2004 @ 15:38
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ExoEditPageModeMenuRenderer.java,v 1.9 2004/10/19 13:20:03 benjmestrallet Exp $
 */
public class ExoEditPageModeMenuRenderer extends ExoMenuRenderer {
  
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
        renderNode(w, uiMenu, res, child, ownerURI);
      }
      w.write("</li>");
    }
    w.write("</ul>");
  }

  protected void renderAdmin(ResourceBundle res, ResponseWriter w, UIMenu uiMenu) throws IOException {
    UIComponent uiChild = uiMenu.getUIToolbarPage();
    uiChild.encodeBegin(FacesContext.getCurrentInstance());
    uiChild.encodeChildren(FacesContext.getCurrentInstance());
    uiChild.encodeEnd(FacesContext.getCurrentInstance());    
  }
}