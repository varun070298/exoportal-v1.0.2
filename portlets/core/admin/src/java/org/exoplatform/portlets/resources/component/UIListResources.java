/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.resources.component;

import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.resources.Query;
import org.exoplatform.services.resources.ResourceBundleData;
import org.exoplatform.services.resources.ResourceBundleDescription;
import org.exoplatform.services.resources.ResourceBundleService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen 
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListResources.java,v 1.6 2004/10/16 21:31:13 tuan08 Exp $
 */
public class UIListResources extends UIGrid  {
   private static Parameter[] VIEW_RESOURCE =  { new Parameter(ACTION , VIEW_ACTION) };
   private static Parameter[] DELETE_RESOURCE = { new Parameter(ACTION , DELETE_ACTION)} ;
	
  private UIPageListIterator uiPageIterator_ ;
  private ResourceBundleService service_ ;
  private boolean adminRole_ ;
  private Query query_ ;

  public UIListResources(ResourceBundleService service) throws Exception {
    setId("UIListResources");
    service_ = service ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    uiPageIterator_ = new UIPageListIterator(new ResourceDescriptionDataHandler()) ;
    add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
        add(new Column("#{UIListResources.header.name}", "name")).
        add(new Column("#{UIListResources.header.language}", "language")).
        add(new ActionColumn("#{UIListResources.header.action}", OBJECTID).
            add(new Button("#{UIListResources.button.view}", VIEW_RESOURCE)).
            add(adminRole_, new Button("#{UIListResources.button.delete}", DELETE_RESOURCE)).
             setCellClass("action-column"))) ;
    add(new Row().
        add(new ComponentCell(this, uiPageIterator_).
            addColspan("5").addStyle("text-align: right")));
    add(new Row().
        add(new ComponentCell(this, new UISearchForm(adminRole_)).
            addColspan("5")));
    addActionListener(ViewActionListener.class, VIEW_ACTION) ;
    addActionListener(DeleteActionListener.class, DELETE_ACTION) ;
    query_ = new Query(null, null) ;
    update() ;
  }
 
  public void update(String name , String lang) throws Exception {
    query_.setName(name) ;
    query_.setLanguage(lang) ;
    uiPageIterator_.setPageList(service_.findResourceDescriptions(query_)) ;
  }
  
  public void update() throws Exception {
    uiPageIterator_.setPageList(service_.findResourceDescriptions(query_)) ;
  }
 
  static public class ResourceDescriptionDataHandler extends PageListDataHandler  {
    private ResourceBundleDescription desc_ ;
    
    public String  getData(String fieldName)   {
      if(OBJECTID.equals(fieldName)) return desc_.getId() ;
      if("name".equals(fieldName)) return desc_.getName() ;
      if("language".equals(fieldName)) return desc_.getLanguage() ;
      return "" ;
    }
    
    public void setCurrentObject(Object o) { desc_ = (ResourceBundleDescription) o; }
  }
 
  static public class ViewActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UIListResources uiListResources = (UIListResources) event.getComponent() ;
  		UIResourcesPortlet uiPortlet = (UIResourcesPortlet)uiListResources.getParent() ;
      UIResource uiResource = 
      	(UIResource)uiPortlet.getChildComponentOfType(UIResource.class);
      String resourceId = event.getParameter(OBJECTID) ;
      ResourceBundleData data = uiListResources.service_.getResourceBundleData(resourceId) ; 
      uiResource.setResourceBundleData(data) ;
      uiResource.setMode(UIResource.VIEW_MODE) ;
      uiPortlet.setRenderedComponent(uiResource.getId()) ;
    }
  }
  
  static public class DeleteActionListener extends ExoActionListener  {
    public DeleteActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
  		UIListResources uicomp = (UIListResources) event.getComponent() ;
  		String resourceId = event.getParameter(OBJECTID) ;  
    	uicomp.service_.removeResourceBundleData(resourceId) ;
      uicomp.update();
    }
  }
}