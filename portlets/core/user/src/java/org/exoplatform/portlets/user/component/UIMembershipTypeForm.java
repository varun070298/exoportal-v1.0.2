/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.user.component.UIOrganizationPortlet.UIMembershipNode;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIMembershipTypeForm.java,v 1.10 2004/09/21 00:16:12 tuan08 Exp $
 */
public class UIMembershipTypeForm extends UISimpleForm {
  private MembershipType membershipType_ ;
  private UIStringInput nameInput_ ;
  private UITextArea    descriptionInput_ ;
  private OrganizationService service_ ;

  public UIMembershipTypeForm(OrganizationService service) throws Exception {
    super("membershipTypeForm", "post", null) ;
    service_ = service ;
    nameInput_ = new UIStringInput("name", "") ;
    descriptionInput_ = new UITextArea("description", "") ;    
    add(new HeaderRow().
        add(new Cell("#{UIMembershipTypeForm.header}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIMembershipTypeForm.label.name}")).
        add(new ComponentCell(this, nameInput_)));
    add(new Row().
        add(new LabelCell("#{UIMembershipTypeForm.label.description}")).
        add(new ComponentCell(this, descriptionInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIMembershipTypeForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIMembershipTypeForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public void setMembershipType(String name) throws Exception {
    membershipType_ = service_.findMembershipType(name) ;
    nameInput_.setValue(name) ;
    nameInput_.setEditable(false) ;
    descriptionInput_.setValue(membershipType_.getDescription()) ;
  }

  public void addMembershipType()  {
    membershipType_ = null ;
    nameInput_.setValue("") ;
    nameInput_.setEditable(true) ;
    descriptionInput_.setValue("") ;
  }
  
  static public class SaveActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIMembershipTypeForm uiForm = (UIMembershipTypeForm) event.getSource() ;
  		String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
      if(user == null ) user = "" ;
      if (uiForm.membershipType_ == null) {
         uiForm.membershipType_ = uiForm.service_.createMembershipTypeInstance() ;
         uiForm.membershipType_.setName(uiForm.nameInput_.getValue()) ;
         uiForm.membershipType_.setDescription(uiForm.descriptionInput_.getValue()) ;
         uiForm.membershipType_.setOwner(user) ;
         uiForm.service_.createMembershipType(uiForm.membershipType_) ;
       } else {
         uiForm.membershipType_.setDescription(uiForm.descriptionInput_.getValue()) ;
         uiForm.service_.saveMembershipType(uiForm.membershipType_) ;
       }
       UIMembershipNode uiController = (UIMembershipNode) uiForm.getParent() ;
       UIListMembershipType uiList = 
       	(UIListMembershipType)uiController.getChildComponentOfType(UIListMembershipType.class) ;
       uiList.update() ;
       uiController.setRenderedComponent(uiList.getId()) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIMembershipTypeForm uiForm = (UIMembershipTypeForm) event.getSource() ;
  		UIMembershipNode uiController = (UIMembershipNode) uiForm.getParent() ;
      uiController.setRenderedComponent(UIListMembershipType.class) ;
    }
  }
}