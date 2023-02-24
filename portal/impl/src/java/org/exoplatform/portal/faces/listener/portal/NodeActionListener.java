/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.portal;

import java.util.List;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.*;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Node;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: NodeActionListener.java,v 1.1 2004/09/26 02:25:47 tuan08 Exp $
 */
public class NodeActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIPortal uiPortal = (UIPortal) event.getSource() ;
    String portalAction = event.getAction() ;
    if (PortalConstants.EDIT_NODE_ACTION.equals(portalAction)) {
      UIPageNodeForm uiPageNodeForm = 
        (UIPageNodeForm)uiPortal.getPortalComponent(UIPageNodeForm.class) ;
      uiPageNodeForm.setEditingNavigationNode(uiPortal.getSelectedNode());
      uiPortal.setBodyComponent(uiPageNodeForm);
    } else if (PortalConstants.ADD_NODE_ACTION.equals(portalAction)) {
      UIPageNodeForm uiPageNodeForm = 
        (UIPageNodeForm)uiPortal.getPortalComponent(UIPageNodeForm.class) ;
      uiPageNodeForm.setEditingNavigationNode(null);
      uiPortal.setBodyComponent(uiPageNodeForm);          
    } else if (PortalConstants.DELETE_NODE_ACTION.equals(portalAction)) {
      uiPortal.removeSelectedNode() ;
    } else if (PortalConstants.SAVE_NODE_ACTION.equals(portalAction)) {
      PortalConfigService service = 
        (PortalConfigService)PortalContainer.getInstance().
                             getComponentInstanceOfType(PortalConfigService.class) ;
      service.saveNodeNavigation(uiPortal.getOwner(), uiPortal.getRootNode()) ;
    } else if (PortalConstants.MOVE_UP_NODE_ACTION.equals(portalAction)) {
      Node root = uiPortal.getRootNode() ;
      Node node = root.findNode(event.getParameter(PortalConstants.NODE_URI)) ;
      if(node != null) moveUp(node) ;
    } else if (PortalConstants.MOVE_DOWN_NODE_ACTION.equals(portalAction)) {
      Node root = uiPortal.getRootNode() ;
      Node node = root.findNode(event.getParameter(PortalConstants.NODE_URI)) ;
      if(node != null) moveDown(node) ;
    } else if (PortalConstants.BROWSE_PAGE_ACTION.equals(portalAction)) {
      UIPageBrowser uiPageBrowser = 
        (UIPageBrowser)uiPortal.getPortalComponent(UIPageBrowser.class);
      uiPageBrowser.setReturnModule(UIPageBrowser.UI_PAGE) ;
      if (uiPageBrowser != null) {
        uiPortal.setBodyComponent(uiPageBrowser);
      }
    } 
  }
  
  public void moveUp(Node node) {
    Node parent = node.getParent();
    if(parent == null ) return ;
    List children = parent.getChildren() ;
    for (int i = 0; i < children.size(); i++) {
      Node child =  (Node)children.get(i);
      if (child == node) {
        if(i != 0) {
          Object tmp = children.remove(i);
          children.add(i - 1, tmp);
        }
        return;
      }
    }
  }

  public void moveDown(Node node) {
    Node parent = node.getParent();
    if(parent == null ) return ;
    List children = parent.getChildren() ;
    for (int i = 0; i < children.size(); i++) {
      Node child = (Node) children.get(i);
      if (child == node) {
        if(i != children.size() - 1) {
          Object tmp = children.remove(i);
          children.add(i + 1, tmp);
        }
        return;
      }
    }
  }
}