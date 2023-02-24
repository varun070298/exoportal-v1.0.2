/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.Collection;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.UICommandNode;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIGroupExplorer.java,v 1.15 2004/10/22 14:23:30 tuan08 Exp $
 */
public class UIGroupExplorer extends UICommandNode {
  final static public String GROUP_ID = "groupId" ;
  
  private OrganizationService service_ ; 
  private Group currentGroup_ ;
  private Group parentGroup_ ;
  private Collection childrenGroup_ ;
  private boolean adminRole_ ;
   
  public UIGroupExplorer(OrganizationService service) throws Exception {
  	setId("UIGroupExplorer") ;
    setRendererType("GroupExplorerRenderer") ;
    service_ = service ;
    customizeGroupDetail() ;
    childrenGroup_ = service_.findGroups(null) ;
    addActionListener(ChangeGroupActionListener.class, "changeGroup") ;
    addActionListener(AddGroupActionListener.class, "addGroup") ;
    addActionListener(DeleteGroupActionListener.class, "deleteGroup") ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
  }
  
  protected void customizeGroupDetail() throws Exception {
    UINode uiGroupDetail = (UINode)addChild(UINode.class) ;
    uiGroupDetail.setId("UIGroupDetail") ;
    uiGroupDetail.setRendererType("SimpleTabRenderer") ;
    uiGroupDetail.addChild(UIViewUserInGroup.class) ;
    
    UINode uiCommunityNode = (UINode)uiGroupDetail.addChild(UINode.class) ;
    uiCommunityNode.setId("UICommunityNode") ;
    uiCommunityNode.setRendererType("ChildrenRenderer") ;
    uiCommunityNode.setRendered(false);
    uiCommunityNode.addChild(UIGroupCommunityInfo.class) ;
    uiCommunityNode.addChild(UICommunityPortalForm.class).setRendered(false) ;
    uiCommunityNode.addChild(UICommunityNavForm.class).setRendered(false) ;
  }
  
  public boolean hasAdminRole() { return adminRole_ ; } 
  public Group getCurrentGroup() { return currentGroup_ ; }
  public Group getParentGroup() { return parentGroup_ ; }
  public Collection getChildrenGroup() { return childrenGroup_ ; }
  
  public PageList getMemberOfTheCurrentGroup() throws Exception {
    if(currentGroup_ == null) return PageList.EMPTY_LIST ; 
    return service_.findUsersByGroup(currentGroup_.getId());
  }
  
  public void update() throws Exception {
    if(currentGroup_ != null ) {
      currentGroup_  = service_.findGroupById(currentGroup_.getId()) ;
      parentGroup_ = service_.findGroupById(currentGroup_.getParentId()) ;
      childrenGroup_ = service_.findGroups(parentGroup_) ;
    } else {
      currentGroup_  = null ;
      childrenGroup_ = service_.findGroups(null) ;
      parentGroup_ = null  ;
    }
  }
  
  private void changeGroup(String groupId) throws Exception {
  	if(groupId != null && groupId.length() > 0) {
  		currentGroup_  = service_.findGroupById(groupId) ;
      if(currentGroup_.getParentId() != null) {
      	parentGroup_ = service_.findGroupById(currentGroup_.getParentId()) ;
      } else {
        parentGroup_ = null ; 
      }
  		childrenGroup_ = service_.findGroups(currentGroup_) ;
  	} else {
  		currentGroup_  = null ;
  		childrenGroup_ = service_.findGroups(null) ;
  		parentGroup_ = null  ;
  	}
    broadcastOnChange();
  }
  
  static public class ChangeGroupActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIGroupExplorer uiExplorer = (UIGroupExplorer) event.getComponent() ;
  		String groupName = event.getParameter(GROUP_ID) ;
  		uiExplorer.changeGroup(groupName) ;
  	}
  }
  
  static public class AddGroupActionListener extends ExoActionListener  {
    public AddGroupActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIGroupExplorer uiExplorer = (UIGroupExplorer) event.getComponent() ;
  		String parentGroupId = event.getParameter(GROUP_ID) ;
  		UIGroupForm uiForm = 
  			(UIGroupForm)uiExplorer.getSibling(UIGroupForm.class) ;
  		uiForm.setParentGroup(parentGroupId) ;
  		uiExplorer.setRenderedSibling(UIGroupForm.class);
  	}
  }
  
  static public class DeleteGroupActionListener extends ExoActionListener  {
    public DeleteGroupActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIGroupExplorer uiExplorer = (UIGroupExplorer) event.getComponent() ;
  		if(uiExplorer.currentGroup_ != null) {
  		  uiExplorer.service_.removeGroup(uiExplorer.currentGroup_) ;
  		  uiExplorer.currentGroup_ = uiExplorer.parentGroup_ ;
  		  uiExplorer.update() ;
  		}
  	}
  }
}