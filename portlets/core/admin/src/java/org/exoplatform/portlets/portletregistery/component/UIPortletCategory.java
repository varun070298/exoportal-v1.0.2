/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
import java.util.*;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class UIPortletCategory 
	extends org.exoplatform.portal.faces.component.UIPortletCategory  {

  public static final String EDIT_CATEGORY = "editCategory";
  public static final String ADD_PORTLET = "addPortlet";
  public static final String DELETE_CATEGORY = "deleteCategory";
  
  private PortletRegisteryService service_;

  public UIPortletCategory(PortletRegisteryService portletRegisteryService) {
	  super() ;
    this.service_ = portletRegisteryService;
    addFacesListener(new DeleteActionListener().setActionToListen(DELETE_CATEGORY));
    addFacesListener(new EditActionListener().setActionToListen(EDIT_CATEGORY));
    addFacesListener(new AddPortletActionListener().setActionToListen(ADD_PORTLET));
	}

  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletCategory";
  }

  public class DeleteActionListener extends ExoActionListener {
    public DeleteActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      String categoryName = categoryData.getPortletCategoryName() ;
      service_.removePortletCategoryByName(categoryName);
      UIPortletCategories uiCategories = (UIPortletCategories) getParent();
      Map dataMap = uiCategories.getPortletCaterories() ;
      dataMap.remove(categoryName) ;
      Iterator iterator =  dataMap.values().iterator();
      if(iterator.hasNext())
      	setPortletCategoryData((PortletCategoryData) iterator.next());
    }
  }

  public class EditActionListener extends ExoActionListener {
    public EditActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategory uiCategory = (UIPortletCategory) event.getComponent() ;
      UIPortletRegistry uiPortlet = 
      	(UIPortletRegistry) uiCategory.getAncestorOfType(UIPortletRegistry.class);   
      UIPortletCategoryForm uiForm = 
      	(UIPortletCategoryForm) uiPortlet.getChildComponentOfType(UIPortletCategoryForm.class);
      uiForm.setPortletCategoryData(categoryData) ;
      uiPortlet.setRenderedComponent(UIPortletCategoryForm.class);
    }
  }
  
  private class AddPortletActionListener extends ExoActionListener {
    public AddPortletActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletCategory uiCategory = (UIPortletCategory) event.getComponent() ;
      UIPortletRegistry  uiPortlet = 
      	(UIPortletRegistry) uiCategory.getAncestorOfType(UIPortletRegistry.class);
      UIPortletList portletList = 
      	(UIPortletList) uiPortlet.getChildComponentOfType(UIPortletList.class);
      portletList.setPortletCategoryData(categoryData);
      uiPortlet.setRenderedComponent(UIPortletList.class);
    }
  }
}