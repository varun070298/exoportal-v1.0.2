/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.*;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.UIPortlet;

/**
 * Sat, Jan 03, 2004 @ 11:16
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIOrganizationPortlet.java,v 1.5 2004/08/26 04:18:03 tuan08
 *           Exp $
 */
public class UIOrganizationPortlet extends UIPortlet {

  public UIOrganizationPortlet(UIGroupNode uiGroupNode,
      UIMembershipNode uiMembershipNode) throws Exception {
    setId("organization-portlet");
    setRendererType("PyramidTabBarRenderer");
    setClazz("UIOrganizationPortlet");

    List children = getChildren();

    UINode uiUserNode = new UINode();
    uiUserNode.setRendererType("ChildrenRenderer");
    uiUserNode.setId("usernode");
    uiUserNode.setName("Users");
    uiUserNode.addChild(UIUserManager.class);
    children.add(uiUserNode);

    uiGroupNode.setRendered(false);
    children.add(uiGroupNode);
    uiMembershipNode.setRendered(false);
    children.add(uiMembershipNode);
  }

  static public class UIMembershipNode extends UINode {
    public UIMembershipNode(UIListMembershipType uiListMembershipType,
        UIMembershipTypeForm uiMembershipTypeForm) throws Exception {
      setName("Memberships");
      setId("UIMembershipNode");
      setRendererType("ChildrenRenderer");
      List children = getChildren();
      uiListMembershipType.setRendered(true);
      children.add(uiListMembershipType);
      uiMembershipTypeForm.setRendered(false);
      children.add(uiMembershipTypeForm);
    }
  }

  static public class UIGroupNode extends UINode {
    public UIGroupNode(UIGroupExplorer uiGroupExplorer, UIGroupForm uiGroupForm)
        throws Exception {
      setName("Groups");
      setId("UIGroupNode");
      setRendererType("ChildrenRenderer");
      List children = getChildren();
      uiGroupExplorer.setRendered(true);
      children.add(uiGroupExplorer);
      uiGroupForm.setRendered(false);
      children.add(uiGroupForm);
    }
  }
}