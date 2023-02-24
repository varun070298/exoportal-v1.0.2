/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.*;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIMembershipForm.java,v 1.11 2004/10/22 14:23:30 tuan08 Exp $
 */
public class UIMembershipForm extends UIForm {
  private static String USERNAME = "username" , MEMBERSHIP_NAME = "mname" ,
                        GROUP_ID = "groupId" ;

  public UIMembershipForm() throws Exception {
    super("membership") ;
    setId("UIMembershipForm");
    setClazz("UICompactForm") ;
    addContainer("#{UIMembershipForm.header.membership}").
      add(new StringField(USERNAME, "#{UIMembershipForm.label.user-name}", "")).
      add(new SelectBoxField(MEMBERSHIP_NAME , "#{UIMembershipForm.label.membership-name}", "", Constants.EMPTY_LIST)).
      add(new StringField(GROUP_ID, "#{UIMembershipForm.label.group-id}", ""));
    addButton(new Button("#{UIMembershipForm.button.save}", SAVE_ACTION)) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
  }
 
  public void populateFormByGroup(String groupId) throws Exception {
    populateCommon() ;
    StringField groupIdField = getStringField(GROUP_ID) ;
    groupIdField.setValue(groupId) ;
    groupIdField.setEditable(false) ; 
    setStringFieldValue(USERNAME, "") ;
  }
  
  public void populateFormByUser(String userName) throws Exception {
    populateCommon() ;
    StringField usernameField = getStringField(USERNAME) ;
    usernameField.setValue(userName) ;
    usernameField.setEditable(false) ; 
    setStringFieldValue(GROUP_ID, "") ;
  }
  
  private void populateCommon() throws Exception {
    OrganizationService service =
      (OrganizationService) PortalContainer.getComponent(OrganizationService.class);
    Collection memberships = service.findMembershipTypes() ;
    Iterator i = memberships.iterator() ;
    List roleOptions = new ArrayList(10) ;
    while(i.hasNext()) {
      MembershipType mt = (MembershipType) i.next();
      String name = mt.getName() ;
      roleOptions.add(new SelectItem(name, name)) ;
    }
    SelectBoxField sfield = (SelectBoxField)getField(MEMBERSHIP_NAME) ;
    sfield.setValue("") ;
    sfield.setOptions(roleOptions) ; 
  }
  
  static public class SaveActionListener extends ExoActionListener  {
    public SaveActionListener( ) {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
      UIMembershipForm uiForm = (UIMembershipForm) event.getSource() ;
      OrganizationService service =
        (OrganizationService) PortalContainer.getComponent(OrganizationService.class);
  		String userName = uiForm.getStringFieldValue(USERNAME) ;
      User user = service.findUserByName(userName) ;
      if(user == null) {
        Object[] args = { userName } ;
        throw new ExoMessageException("UIMembershipForm.user-not-found", args) ;
      }
      String groupId = uiForm.getStringFieldValue(GROUP_ID);
      Group group = service.findGroupById(groupId) ;
      if(group == null) {
        Object[] args = { groupId } ;
        throw new ExoMessageException("UIMembershipForm.group-not-found", args) ;
      }
  		Membership membership = service.createMembershipInstance();
      membership.setMembershipType(uiForm.getStringFieldValue(MEMBERSHIP_NAME));
      service.linkMembership(userName, group, membership);
      Object parent = uiForm.getParent() ;
      if(parent instanceof UIUserInfo) {
        UIUserInfo uiParent = (UIUserInfo) parent ;
        uiParent.update();
      } else if(parent instanceof UIViewUserInGroup) {
        UIViewUserInGroup uiParent = (UIViewUserInGroup) parent ;
        uiParent.update() ;
      }
    }
  }
}