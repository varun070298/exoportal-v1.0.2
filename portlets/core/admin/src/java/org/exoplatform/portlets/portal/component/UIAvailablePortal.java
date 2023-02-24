/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.portal.component;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.container.SessionContainer ;
import org.exoplatform.portal.filter.AdminRequestFilter;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.PortalConfigDescription;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.Query;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListUser.java,v 1.18 2004/10/22 14:23:30 tuan08 Exp $
 */
public class UIAvailablePortal extends UIGrid {  
	static Parameter[] VIEW = { new Parameter(ACTION , "view") } ;
	static Parameter[] EDIT =   { new Parameter(ACTION , "edit")  } ;
  static Parameter[] ADMIN =   { new Parameter(ACTION , "admin")  } ;
	
	private UIPageListIterator uiPageIterator_ ;
	private boolean adminRole_ ;
	private PortalConfigService service_ ;
	public UIAvailablePortal(UISearchForm uiSearchForm,
                           PortalConfigService service) throws Exception {
		setId("UIAvailablePortal") ;		
    service_ = service ;
		uiPageIterator_ = new UIPageListIterator(new PortalDataHandler()) ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    adminRole_ = true ;
		add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
				add(new Column("#{UIAvailablePortal.header.owner}", "owner")).
        add(new Column("#{UIAvailablePortal.header.edit-permission}", "editPermission")).
        add(new Column("#{UIAvailablePortal.header.view-permission}", "viewPermission")).
				add(new ActionColumn("#{UIAvailablePortal.header.action}", "owner").
						//add(adminRole_,new Button("#{UIAvailablePortal.button.view}", VIEW)).
            //add(adminRole_,new Button("#{UIAvailablePortal.button.edit}", EDIT)).
						add(adminRole_,new Button("#{UIAvailablePortal.button.admin}", ADMIN)))) ;
		add(new Row().
				add(new ComponentCell(this, uiPageIterator_).
						addColspan("4").addClazz("footer")));
    add(new Row().
        add(new ComponentCell(this, uiSearchForm).
            addColspan("4")));

		addActionListener(ViewActionListener.class, "view") ;
    addActionListener(AdminActionListener.class, "admin") ;
    refresh(new Query(null, null, null)) ;
	}

  public void refresh(Query q) throws Exception {
    uiPageIterator_.setPageList(service_.findAllPortalConfigDescriptions(q)) ;
  }
	
	static public class PortalDataHandler extends PageListDataHandler {
		private PortalConfigDescription desc_  ;
		
		public String  getData(String fieldName)   {
			if("owner".equals(fieldName)) return desc_.getOwner() ;
      if("viewPermission".equals(fieldName)) return desc_.getViewPermission() ;
      if("editPermission".equals(fieldName)) return desc_.getEditPermission() ;
			return null ;
		}
		
		public void setCurrentObject(Object o) { desc_ = (PortalConfigDescription) o; }
	}
	
  static public class AdminActionListener extends ExoActionListener  {
    public AdminActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      String owner = event.getParameter(OBJECTID) ;
      RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
      String path = rinfo.getContextPath() + "/faces/admin/" + owner + 
                    "?" + AdminRequestFilter.ACTION + "=" + AdminRequestFilter.ADMIN ; 
      ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
      ActionResponse response = (ActionResponse) eContext.getResponse();
      response.sendRedirect(path);
    }
  }
  
	static public class ViewActionListener extends ExoActionListener  {
    public ViewActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
		public void execute(ExoActionEvent event) throws Exception {
		}
	}
}