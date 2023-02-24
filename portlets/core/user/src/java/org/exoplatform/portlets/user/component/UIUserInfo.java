/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.Collection;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIUserInfo.java,v 1.10 2004/07/02 20:48:33 tuan08 Exp $
 */
public class UIUserInfo extends UIExoCommand {
  
  private User user_ ;
  private Collection memberships_ ;
  private OrganizationService service_ ; 
  private boolean adminRole_ ;
  private UIAccountForm  uiAccountForm_ ;
  private UIMembershipForm uiMembershipForm_ ;
   
  public UIUserInfo(OrganizationService service) throws Exception {
    setId("UIUserInfo") ;
    setRendererType("UserInfoRenderer") ;
    service_ = service ;
    
    uiAccountForm_ = (UIAccountForm)addChild(UIAccountForm.class) ;
    uiAccountForm_.setClazz("UICompactForm") ;
    uiAccountForm_.customizeUpdateAccountForm() ;
    
    uiMembershipForm_ = (UIMembershipForm) addChild(UIMembershipForm.class) ;
    uiMembershipForm_.setClazz("UICompactForm") ;
    uiMembershipForm_.setId("UIMembershipFormForUser") ;
    
    addActionListener(BackActionListener.class, "back") ;
    addActionListener(AddMembershipActionListener.class, "addMembership");
    addActionListener(DeleteMembershipActionListener.class, "deleteMembership");
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
  }
  
  public User getUser() { return user_ ; } 
  public Collection getMemberships() { return memberships_ ; }
  public boolean hasAdminRole() { return adminRole_ ; }
  public UIAccountForm getUIAccountForm() { return uiAccountForm_ ; }
  public UIMembershipForm getUIMembershipForm() { return uiMembershipForm_ ; }
  
  public void changeUser(String userName) throws Exception {
    user_ = service_.findUserByName(userName) ;
    memberships_ = service_.findMembershipsByUser(userName) ;
    UIAccountForm ui = 
      (UIAccountForm) getChildComponentOfType(UIAccountForm.class);
    ui.setEditingUser(user_) ;
    uiMembershipForm_.populateFormByUser(userName) ;
  }

  public void update() throws Exception {
    if (user_ != null) {
      String userName = user_.getUserName() ;
      user_ = service_.findUserByName(userName) ;
      memberships_ = service_.findMembershipsByUser(userName) ;
    }
  }
  static public class AddMembershipActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIUserInfo uiInfo = (UIUserInfo) event.getComponent() ;
      UIMembershipForm uiForm = 
      	(UIMembershipForm)uiInfo.getSibling(UIMembershipForm.class) ;
      uiForm.populateFormByUser(uiInfo.user_.getUserName()) ;
      uiInfo.setRenderedSibling(UIMembershipForm.class) ;
    }
  }
  
  static public class DeleteMembershipActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIUserInfo uiInfo = (UIUserInfo) event.getComponent() ;
  	  String membershipId = event.getParameter("membershipId") ;
  	  uiInfo.service_.removeMembership(membershipId) ;
      uiInfo.update() ;
    }
  }
  
  static public class BackActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIUserInfo uiInfo = (UIUserInfo) event.getComponent() ;
      uiInfo.setRenderedSibling(UIListUser.class) ;
    }
  }
}