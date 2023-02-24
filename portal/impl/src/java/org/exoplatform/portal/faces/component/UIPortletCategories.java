/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.component;


import java.util.* ;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.portal.faces.listener.share.ShowLastBodyComponentActionListener;
import org.exoplatform.services.portletregistery.*;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class UIPortletCategories extends UIExoCommand {
  
  public static final String SELECT_PORTLET_CATEGORY = "selectPortletCategory";
  public static final String SHOW_PORTLET_CATEGORY_MONITOR = "showPortletCategory";
  public static final String SHOW_PORTLET_MONITOR = "showPortlet";

  public static final String PORTLET_CATEGORY_NAME_PARAM = "portletCategoryName";
  public static final String PORTLET_NAME_PARAM = "portletName";
  
  protected UIPortletCategory uiPortletCategory_ ;
  protected UIPortletInfo uiPortletInfo_ ;
  protected PortletRegisteryService portletRegisteryService;
  protected Map portletCaterories = new HashMap() ;

  public UIPortletCategories(PortletRegisteryService portletRegisteryService,
                             UIPortletCategory uiPortletCategory,
														 UIPortletInfo uiPortletInfo) throws Exception {
    setRendererType("PortletCategoriesRenderer");
    setId("UIPortletCategories");
    this.portletRegisteryService = portletRegisteryService;
    uiPortletCategory_ =  uiPortletCategory ;
    List children = getChildren();
    uiPortletCategory.setRendered(true);
    children.add(uiPortletCategory);
    uiPortletInfo_ = uiPortletInfo ;
    uiPortletInfo_.setRendered(false) ;
    children.add(uiPortletInfo) ;
    addActionListener(ShowLastBodyComponentActionListener.class, CANCEL_ACTION);
    addActionListener(SelectCategoryActionListener.class, SELECT_PORTLET_CATEGORY);
    addActionListener(ShowCategoryActionListener.class, SHOW_PORTLET_CATEGORY_MONITOR);
    addActionListener(ShowPortletActionListener.class, SHOW_PORTLET_MONITOR);
    reset() ;
  }

  public String getFamily() {
    return "org.exoplatform.portal.faces.component.UIPortletCategories" ;
  }
  
  
  public void reset() throws Exception {
  	portletCaterories.clear() ;
    Iterator i =  portletRegisteryService.getPortletCategories().iterator();
    while(i.hasNext()){
    	PortletCategory portletCategory = (PortletCategory) i.next();
    	Collection portlets = portletRegisteryService.getPortlets(portletCategory.getId()) ;
    	PortletCategoryData data = new PortletCategoryData(portletCategory, portlets) ;
      portletCaterories.put(portletCategory.getPortletCategoryName(), data) ;
    }
    if(portletCaterories.size() > 0) {
    	PortletCategoryData select = 
    	 	(PortletCategoryData)portletCaterories.values().iterator().next() ;
    	select.setSelect(true) ;
    	uiPortletCategory_.setPortletCategoryData(select) ;
    }
  }
  
  public Map getPortletCaterories() { return portletCaterories; }
  
  public boolean hasCategory() {
    if(portletCaterories.isEmpty())  return false;
    return true;
  }
  
  static public class SelectCategoryActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategories uiCategories = (UIPortletCategories) event.getComponent() ;
    	String categoryName = event.getParameter(PORTLET_CATEGORY_NAME_PARAM) ;
    	PortletCategoryData portletCategoryData = 
    		(PortletCategoryData) uiCategories.portletCaterories.get(categoryName) ;
    	portletCategoryData.setSelect(!portletCategoryData.isSelect()) ;
    }
  }

  static public class ShowCategoryActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategories uiCategories = (UIPortletCategories) event.getComponent() ;
    	String categoryName = event.getParameter(PORTLET_CATEGORY_NAME_PARAM) ;
    	PortletCategoryData portletCategoryData = 
    		(PortletCategoryData) uiCategories.portletCaterories.get(categoryName) ;
    	portletCategoryData.setSelect(true) ;
    	uiCategories.uiPortletCategory_.setPortletCategoryData(portletCategoryData) ;
    	uiCategories.setRenderedComponent(UIPortletCategory.class);
    }
  }

  static public class ShowPortletActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategories uiCategories = (UIPortletCategories) event.getComponent() ;
    	String categoryName =  event.getParameter(PORTLET_CATEGORY_NAME_PARAM) ;
      String portletName =  event.getParameter(PORTLET_NAME_PARAM) ;
      PortletCategoryData categoryData = 
      	(PortletCategoryData)uiCategories.portletCaterories.get(categoryName) ;
      Portlet portlet = categoryData.findPortlet(portletName) ;
      uiCategories.uiPortletInfo_.setPortletData(portlet);
      uiCategories.setRenderedComponent(UIPortletInfo.class);
    }
  }
}