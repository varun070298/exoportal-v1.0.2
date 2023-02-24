/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import org.exoplatform.commons.exception.ExoPermissionException;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portal.PageDescription;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.Query;
import org.exoplatform.services.portal.model.Page;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPageList.java,v 1.13 2004/10/19 13:20:02 benjmestrallet Exp $
 */
public class UIPageList extends UIGrid  {
  public static Parameter[] PREVIEW_PAGE = { new Parameter(ACTION , "preview") } ;
  public static Parameter[] SELECT_PAGE = { new Parameter(ACTION , "select") } ;
  public static Parameter[] DELETE_PAGE = { new Parameter(ACTION , DELETE_ACTION) } ;
  public static Parameter NEW_PAGE = new Parameter(ACTION , "newPage") ;
  public static Parameter RETURN_PAGE = new Parameter(ACTION , "return") ;
  
  public static Parameter[] newParams = { NEW_PAGE} ;
  public static Parameter[] returnParams = { RETURN_PAGE } ;  
  
  private UIPageListIterator uiPageIterator_ ;
  private UISearchForm uiSearchForm_ ;
  private Query query_ ;
  
  public UIPageList() throws Exception {
    setId("UIPageList") ;   
    UIToolbar uiToolbar = new UIToolbar();
    uiToolbar.addLeftButton(new Button("#{UIPageList.button.new-page}", newParams));
    uiToolbar.addRightButton(new Button("#{UIPageList.button.cancel}", returnParams));
    uiPageIterator_ = new UIPageListIterator(new PageDescriptionHandler()) ;
    uiSearchForm_ = new UISearchForm() ;
    add(new Row().
        add(new ComponentCell(this, uiToolbar).
            addColspan("5")));
    add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
        add(new Column("#{UIPageList.header.owner}", "owner")).
        add(new Column("#{UIPageList.header.name}", "name")).
        add(new Column("#{UIPageList.header.view-permission}", "viewPermission")).
        add(new Column("#{UIPageList.header.edit-permission}", "editPermission")).
        add(new ActionColumn("#{UIPageList.header.action}", OBJECTID).
            add(true, new Button("#{UIPageList.button.preview}", PREVIEW_PAGE )).
            add(true, new Button("#{UIPageList.button.select}", SELECT_PAGE )).
            add(true,new Button("#{UIPageList.button.delete}",DELETE_PAGE)).
            setCellClass("action-column"))) ;
    add(new Row().
        add(new ComponentCell(this, uiPageIterator_).
            addColspan("5").addStyle("text-align: right")));
    add(new Row().
        add(new ComponentCell(this, uiSearchForm_).
            addColspan("5")));


    addActionListener(DeleteActionListener.class, DELETE_ACTION) ;
    addActionListener(SelectActionListener.class, "select") ;
    addActionListener(PreviewActionListener.class, "preview") ;
    addActionListener(NewPageActionListener.class, "newPage") ;
    addActionListener(ReturnActionListener.class, "return") ;   
  }
  
  
  public void queryDescriptions(String owner, String viewPerm, String editPerm) throws Exception {
    uiSearchForm_.setOwner(owner) ;
    uiSearchForm_.setViewPermission(viewPerm) ;
    uiSearchForm_.setEditPermission(editPerm) ;
    PortalConfigService service = ((UIPageBrowser) getParent()).getPortalConfigService() ;
    query_=  new Query(owner, viewPerm, editPerm) ;
    uiPageIterator_.setPageList(service.findAllPageDescriptions(query_)) ;
  }
  
  public void update() throws Exception {
    PortalConfigService service = ((UIPageBrowser) getParent()).getPortalConfigService() ;
    uiPageIterator_.setPageList(service.findAllPageDescriptions(query_)) ;
  }
  
  static public class PageDescriptionHandler extends PageListDataHandler {
    private PageDescription desc_  ;
    
    public String  getData(String fieldName)   {
      if(OBJECTID.equals(fieldName)) return desc_.getId() ;
      if("owner".equals(fieldName)) return desc_.getOwner() ;
      if("name".equals(fieldName)) return desc_.getName() ;
      if("viewPermission".equals(fieldName)) return desc_.getViewPermission() ;
      if("editPermission".equals(fieldName)) return desc_.getEditPermission() ;
      return null ;
    }
    
    public void setCurrentObject(Object o) { desc_ = (PageDescription) o; }
  }
  
  static public class PreviewActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPageList uiList = (UIPageList) event.getComponent() ;
      UIPortal uiPortal = (UIPortal) uiList.getAncestorOfType(UIPortal.class) ;
      UIPagePreview uiPagePreview = 
        (UIPagePreview)uiPortal.getPortalComponent(UIPagePreview.class) ;
      String pageId = event.getParameter(OBJECTID) ;
      if(uiPagePreview.setPage(uiPortal, pageId)) {
        uiPortal.setBodyComponent(uiPagePreview) ;
      }
    }
  }
  
  static public class SelectActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
      UIPageList uiList = (UIPageList) event.getComponent() ;
			String pageId = event.getParameter(OBJECTID);
			UIPageBrowser uiPageBrowser = (UIPageBrowser) uiList.getParent() ;
			uiPageBrowser.setSelectNodeReferencePage(pageId);
		}
	}
  
  static public class DeleteActionListener extends ExoActionListener {
    public DeleteActionListener() {
      
    }
    
		public void execute(ExoActionEvent event) throws Exception {
      UIPageList uiList = (UIPageList) event.getComponent() ;
			UIPageBrowser uiPageBrowser = (UIPageBrowser) uiList.getParent();
      UIPortal uiPortal = (UIPortal) uiPageBrowser.getAncestorOfType(UIPortal.class) ;
			PortalConfigService service = uiPageBrowser.getPortalConfigService();
			String pageId = event.getParameter(OBJECTID);
      Page page = service.getPage(pageId) ;
      PortalACL acl = service.getPortalACL() ;
      if(!acl.hasEditPagePermission(page, uiPortal.getOwner())) {
        throw new ExoPermissionException(getActionName(), "onwer") ;
      }
			service.removePage(pageId);
       PortalComponentCache  cache = 
          (PortalComponentCache) SessionContainer.getComponent(PortalComponentCache.class) ; 
			cache.removeUIPageFromCache(pageId) ;
			uiList.queryDescriptions(uiList.uiSearchForm_.getOwner(), 
                               uiList.uiSearchForm_.getViewPermission(), 
					                     uiList.uiSearchForm_.getEditPermission());
			InformationProvider ip = findInformationProvider(event.getComponent());
			ip.addMessage(new ExoFacesMessage("#{UIPageList.msg.delete-page-success}"));
		}
	}
 
  static public class ReturnActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception { 
      UIPageList uiList = (UIPageList) event.getSource() ;
      UIPageBrowser uiPageBrowser = (UIPageBrowser)uiList.getParent() ;
      uiPageBrowser.goBack() ;
    }
  } 

  static public class NewPageActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception { 
      UIPageList uiList = (UIPageList) event.getSource() ;
      UIPageBrowser uiPageBrowser = (UIPageBrowser)uiList.getParent() ;
      uiPageBrowser.setRenderedComponent(UIPageModelForm.class) ;
    }
  }  
  
}