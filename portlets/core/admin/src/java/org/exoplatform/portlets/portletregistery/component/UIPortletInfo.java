/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import java.util.Collection;


import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletregistery.PortletCategory;
import org.exoplatform.services.portletregistery.PortletRegisteryService;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 juin 2004
 */
public class UIPortletInfo 
	extends org.exoplatform.portal.faces.component.UIPortletInfo {
	
  public static final String EDIT_PORTLET = "editPortlet";
  public static final String ADD_ROLE = "addRole";
  public static final String DELETE_PORTLET = "deletePortlet";

  private PortletRegisteryService portletRegisteryService;

  public UIPortletInfo(PortletRegisteryService portletRegisteryService) {
    super() ;
    this.portletRegisteryService = portletRegisteryService;
    addActionListener(new EditActionListener().setActionToListen(EDIT_PORTLET));
    addActionListener(new AddRoleActionListener().setActionToListen(ADD_ROLE));
    addActionListener(new DeleteActionListener().setActionToListen(DELETE_PORTLET));
	}

  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletInfo";
  }

  public  class EditActionListener extends ExoActionListener {
    public EditActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletRegistry uiPortlet = 
    		(UIPortletRegistry) getAncestorOfType(UIPortletRegistry.class) ;
      UIPortletForm uiForm = 
      	(UIPortletForm)uiPortlet.getChildComponentOfType(UIPortletForm.class);
      uiForm.setPortletData(portlet_) ;
      uiPortlet.setRenderedComponent(UIPortletForm.class) ;
    }
  }

  private class AddRoleActionListener extends ExoActionListener {
    public AddRoleActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletRegistry uiPortlet = 
    		(UIPortletRegistry) getAncestorOfType(UIPortletRegistry.class) ;
    	UIPortletRole uiForm = (UIPortletRole) uiPortlet.getChildComponentOfType(UIPortletRole.class) ;
    	uiForm.setCurrentPortlet(portlet_) ;
    	uiPortlet.setRenderedComponent(uiForm.getId()) ;
    }
  }

  private class DeleteActionListener extends ExoActionListener {
    public DeleteActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      portletRegisteryService.removePortlet(portlet_.getId());
      UIPortletCategory uiCategory = (UIPortletCategory)getSibling(UIPortletCategory.class) ;
      PortletCategoryData categoryData = uiCategory.getPortletCategoryData() ;
      PortletCategory portletCategory = categoryData.getPortletCategory() ;
      Collection portlets = portletRegisteryService.getPortlets(portletCategory.getId()) ;
      categoryData.reset(portletCategory, portlets) ;
      setRenderedSibling(UIPortletCategory.class) ;
    }
  }
}