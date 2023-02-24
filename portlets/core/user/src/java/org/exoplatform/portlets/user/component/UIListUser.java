/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.text.SimpleDateFormat;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListUser.java,v 1.18 2004/10/22 14:23:30 tuan08 Exp $
 */
public class UIListUser extends UIGrid {  
  private static SimpleDateFormat ft_ = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;
  
	static Parameter[] VIEW_PROFILE = { new Parameter(ACTION , "viewProfile") } ;
	static Parameter[] VIEW_INFO =   { new Parameter(ACTION , "viewUserInfo")  } ;
	static Parameter[] DELETE =  { new Parameter(ACTION , DELETE_ACTION) } ;
	
	private UIPageListIterator uiPageIterator_ ;
	private boolean adminRole_ ;
	private OrganizationService service_ ;
  private Query lastQuery_ ;
	
	public UIListUser(OrganizationService service) throws Exception {
		setId("UIListUser") ;
		service_ = service  ;
		uiPageIterator_ = new UIPageListIterator(new UserDataHandler()) ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
		add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
				add(new Column("#{UIListUser.header.user-name}", "userName")).
				add(new Column("#{UIListUser.header.first-name}", "firstName")).
				add(new Column("#{UIListUser.header.last-name}", "lastName")).
				add(new Column("#{UIListUser.header.email}", "email")).
        add(new Column("#{UIListUser.header.last-login}", "lastLogin")).
				add(new ActionColumn("#{UIListUser.header.action}", "userName").
						add(new Button("#{UIListUser.button.view-profile}", VIEW_PROFILE)).
						add(adminRole_, new Button("#{UIListUser.button.view}", VIEW_INFO)).
						add(adminRole_, new Button("#{UIListUser.button.delete}", DELETE)).
             setCellClass("action-column"))) ;
		add(new Row().
				add(new ComponentCell(this, uiPageIterator_).
						addColspan("6").addStyle("text-align: right")));
		
		addActionListener(DeleteUserActionListener.class, DELETE_ACTION) ;
		addActionListener(ViewProfileActionListener.class, "viewProfile") ;
		addActionListener(ViewUserInfoActionListener.class, "viewUserInfo") ;
		search(new Query()) ;
	}

	
	public void search(Query query) throws Exception {
    lastQuery_ = query ;
		uiPageIterator_.setPageList(service_.findUsers(query)) ;
	}
  
  public void refresh() throws Exception {
    uiPageIterator_.setPageList(service_.findUsers(lastQuery_)) ;
  }
	
	public boolean hasAdminRole() { return adminRole_ ; }
	
	static public class UserDataHandler extends PageListDataHandler {
		private User user_  ;
		
		public String  getData(String fieldName)   {
			if("userName".equals(fieldName)) return user_.getUserName() ;
			if("lastName".equals(fieldName)) return user_.getLastName() ;
			if("firstName".equals(fieldName)) return user_.getFirstName()  ;
			if("email".equals(fieldName)) return user_.getEmail() ;
      if("lastLogin".equals(fieldName)) return ft_.format(user_.getLastLoginTime());
			return null ;
		}
		
		public void setCurrentObject(Object o) { user_ = (User) o; }
	}
	
	static public class ViewUserInfoActionListener extends ExoActionListener  {
    public ViewUserInfoActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
		public void execute(ExoActionEvent event) throws Exception {
      UIListUser uiListUser = (UIListUser) event.getComponent() ;
			String userName = event.getParameter(OBJECTID) ;
			UIUserInfo uiUserInfo = 
				(UIUserInfo)uiListUser.getSibling(UIUserInfo.class) ;
			uiUserInfo.changeUser(userName) ;
			uiListUser.setRenderedSibling(UIUserInfo.class) ;
		}
	}
	
	static public class ViewProfileActionListener extends ExoActionListener  {
		public void execute(ExoActionEvent event) throws Exception {
      UIListUser uiListUser = (UIListUser) event.getComponent() ;
			String userName = event.getParameter(OBJECTID);
			UIUserProfileSummary uiUserProfile = 
				(UIUserProfileSummary)uiListUser.getSibling(UIUserProfileSummary.class) ;
			uiUserProfile.setUserProfile(userName) ;
			uiListUser.setRenderedSibling(UIUserProfileSummary.class) ;
		}
	}
	
	static public class DeleteUserActionListener extends ExoActionListener  {
    public DeleteUserActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
		public void execute(ExoActionEvent event) throws Exception {
      UIListUser uiListUser = (UIListUser) event.getComponent() ;
			String userName = event.getParameter(OBJECTID) ;
			uiListUser.service_.removeUser(userName) ;
			uiListUser.refresh() ;
		}
	}
}