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
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portlets.nav.component.UIMenu;
import org.exoplatform.portlets.nav.component.UINavigation;
import org.exoplatform.services.portal.model.Node;
/**
 * Thu, May 5, 2004 @ 15:38
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ExoEditNavigationModeMenuRenderer.java,v 1.9 2004/10/19 13:20:03 benjmestrallet Exp $
 */
public class ExoEditNavigationModeMenuRenderer extends ExoMenuRenderer {
  
  protected void renderNode(ResponseWriter w, UIMenu uiMenu, ResourceBundle res, 
                                  Node node, String ownerURI) throws IOException {
    Parameter nodeURIParam = new Parameter(PortalConstants.NODE_URI, "") ;
    Parameter actionParam = new Parameter(ACTION, PortalConstants.EDIT_NODE_ACTION) ;
    Parameter[] params =   {actionParam, nodeURIParam };
    if(node.getParent() == null){
      w.write("<span class='edit-actions'>") ;
      nodeURIParam.setValue(node.getUri()) ;
      actionParam.setValue(PortalConstants.EDIT_NODE_ACTION) ;
      linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.edit"), params);
      actionParam.setValue(PortalConstants.ADD_NODE_ACTION) ;
      linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.add-node"), params);        
      w.write("</span>") ;            
    }        
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
        w.   write("<span class='edit-actions'>") ;
        nodeURIParam.setValue(child.getUri()) ;
        actionParam.setValue(PortalConstants.EDIT_NODE_ACTION) ;
        linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.edit"), params);
        actionParam.setValue(PortalConstants.ADD_NODE_ACTION) ;
        linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.add-node"), params);
        actionParam.setValue(PortalConstants.DELETE_NODE_ACTION) ;
        linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.delete"), params);        
        actionParam.setValue(PortalConstants.MOVE_DOWN_NODE_ACTION);
        linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.down"), params);
        actionParam.setValue(PortalConstants.MOVE_UP_NODE_ACTION) ;
        linkRenderer_.render(w, uiMenu, res.getString("UINavigationEdit.button.up"), params);
        w.   write("</span>") ;      
        renderNode(w, uiMenu, res, child, ownerURI);
      }      
      w.write("</li>");
    }
    w.write("</ul>");
  }

  protected void renderAdmin(ResourceBundle res, ResponseWriter w, UIMenu uiMenu) throws IOException{     
    UIComponent uiChild = uiMenu.getUIToolbarNav();
    uiChild.encodeBegin(FacesContext.getCurrentInstance());
    uiChild.encodeChildren(FacesContext.getCurrentInstance());
    uiChild.encodeEnd(FacesContext.getCurrentInstance());        
  }
}