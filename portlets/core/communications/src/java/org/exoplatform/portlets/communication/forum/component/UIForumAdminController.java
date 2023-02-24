/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.* ;
import javax.portlet.PortletPreferences;
import org.exoplatform.Constants;
import org.exoplatform.faces.FacesUtil;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.services.communication.forum.*;

/**
 * Fri, Jan 16, 2004 @ 21:43 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIForumAdminController.java,v 1.5 2004/06/28 03:32:21 tuan08 Exp $
 */
public class UIForumAdminController extends UIPortlet {
  public UIForumAdminController(ForumServiceContainer container) throws Exception {
    setId("UIForumAdminPortlet");
    setRendererType("ChildrenRenderer") ;
    PortletPreferences prefs = FacesUtil.getPortletPreferences() ;
    String owner =  prefs.getValue("forum-owner", Constants.DEFAUL_PORTAL_OWNER) ;
    ForumService service = container.findForumService(owner) ;

    List children = getChildren() ;
    UIAdminViewCategories uiCategories = new UIAdminViewCategories(service) ;
    uiCategories.setRendered(true) ;
    children.add(uiCategories) ;

    UICategoryForm uiCategoryForm = new UICategoryForm(service) ;
    uiCategoryForm.setRendered(false) ;
    children.add(uiCategoryForm) ;

    UIForumForm uiForumForm = new UIForumForm(service) ;
    uiForumForm.setRendered(false) ;
    children.add(uiForumForm) ;
  }
}