/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.UIComponentObserver;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.User;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIViewUserInGroup.java,v 1.1 2004/10/22 14:23:30 tuan08 Exp $
 */
public class UIViewUserInGroup extends UIGrid {  
  
	private UIPageListIterator uiPageIterator_ ;
  private UIMembershipForm uiMembershipForm_ ;
	
	public UIViewUserInGroup() throws Exception {
    uiMembershipForm_ = new UIMembershipForm() ;
    uiMembershipForm_.setRendered(false);
    uiMembershipForm_.setId("UIMembershipFormForGroup") ;
		uiPageIterator_ = new UIPageListIterator(new UserDataHandler()) ;
		add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
				add(new Column("#{UIViewUserInGroup.header.user-name}", "userName")).
				add(new Column("#{UIViewUserInGroup.header.first-name}", "firstName")).
				add(new Column("#{UIViewUserInGroup.header.last-name}", "lastName")).
				add(new Column("#{UIViewUserInGroup.header.email}", "email")));
		add(new Row().
				add(new ComponentCell(this, uiPageIterator_).
						addColspan("4").addStyle("text-align: right")));
    add(new Row().
        add(new ComponentCell(this, uiMembershipForm_).
            addColspan("4")));
    uiPageIterator_.setPageList(PageList.EMPTY_LIST) ;
	}
  
	public void registerComponentObserver(UIExoComponent parent)  {
	  UIGroupExplorer uiExplorer = 
      (UIGroupExplorer) parent.getAncestorOfType(UIGroupExplorer.class) ;
    uiExplorer.addObserver(new GroupChangeObserver());
	}
	
	static public class UserDataHandler extends PageListDataHandler {
		private User user_  ;
		
		public String  getData(String fieldName)   {
			if("userName".equals(fieldName)) return user_.getUserName() ;
			if("lastName".equals(fieldName)) return user_.getLastName() ;
			if("firstName".equals(fieldName)) return user_.getFirstName()  ;
			if("email".equals(fieldName)) return user_.getEmail() ;
			return null ;
		}
    
		public void setCurrentObject(Object o) { user_ = (User) o; }
	}
  
  public void update() throws Exception { 
    UIGroupExplorer uiExplorer = 
      (UIGroupExplorer) getAncestorOfType(UIGroupExplorer.class) ;
    uiPageIterator_.setPageList(uiExplorer.getMemberOfTheCurrentGroup()) ;
  }
  
	public class GroupChangeObserver extends UIComponentObserver {
	  public void onChange(UIExoComponent target) throws Exception  {
	    UIGroupExplorer uiExplorer = (UIGroupExplorer) target ;
      uiPageIterator_.setPageList(uiExplorer.getMemberOfTheCurrentGroup()) ;
      Group group  = uiExplorer.getCurrentGroup();
      if(group == null) {
        uiMembershipForm_.setRendered(false);
      } else {
        uiMembershipForm_.setRendered(true);
        uiMembershipForm_.populateFormByGroup(group.getId());
      }
	  }
	}
}