/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portletcontainer.monitor.PortletContainerMonitor;
import org.exoplatform.services.portletregistery.PortletRegisteryService;

import java.util.* ;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class UIPortletCategories extends org.exoplatform.portal.faces.component.UIPortletCategories {
  public static final String CREATE_CATEGORY = "createCategory";
  public static final String IMPORT_PORTLETS = "importPortets";
  public static final String CLEAN_ALL = "cleanAll";

  public static final String PORTLET_CATEGORY_NAME_PARAM = "portletCategoryName";
  public static final String PORTLET_NAME_PARAM = "portletName";
  
  private PortletContainerMonitor portletContainerMonitor_ ;
  
  public UIPortletCategories(PortletRegisteryService portletRegisteryService,
                             PortletContainerMonitor portletContainerMonitor,
                             UIPortletCategory uiPortletCategory,
														 UIPortletInfo uiPortletInfo) throws Exception {
    super(portletRegisteryService,  uiPortletCategory, uiPortletInfo) ;
    portletContainerMonitor_ = portletContainerMonitor ;
    addActionListener(CreateActionListener.class, CREATE_CATEGORY);
    addActionListener(ImportActionListener.class, IMPORT_PORTLETS);
    addActionListener(CleanAllActionListener.class, CLEAN_ALL);
  }

  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletCategories" ;
  }

  public static class CreateActionListener extends ExoActionListener {
    public CreateActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletRegistry uiPortlet = 
      	(UIPortletRegistry) event.getComponent().getParent();
      UIPortletCategoryForm uiForm = 
      	(UIPortletCategoryForm)uiPortlet.getChildComponentOfType(UIPortletCategoryForm.class) ;
      uiForm.setPortletCategoryData(null) ;
      uiPortlet.setRenderedComponent(UIPortletCategoryForm.class);
    }
  }

  static public class ImportActionListener extends ExoActionListener {
    public ImportActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletCategories uiCategories =	(UIPortletCategories) event.getComponent();
      Collection portletDatas = 
      	uiCategories.portletContainerMonitor_.getPortletRuntimeDataMap().values();
      uiCategories.portletRegisteryService.importPortlets(portletDatas);
      uiCategories.reset();
    }
  }
  
  static public class CleanAllActionListener extends ExoActionListener {
    public CleanAllActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletCategories uiCategories =	(UIPortletCategories) event.getComponent();
      uiCategories.portletRegisteryService.clearRepository();
      uiCategories.reset();
    }
  }
}