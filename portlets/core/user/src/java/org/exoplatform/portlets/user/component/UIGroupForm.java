/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea ;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIGroupForm.java,v 1.11 2004/10/21 15:25:17 tuan08 Exp $
 */
public class UIGroupForm extends UISimpleForm {
  private UIStringInput groupNameInput_ ;
  private UIStringInput labelInput_ ;
  private UITextArea descInput_ ;
  private OrganizationService service_ ;
  private Group parentGroup_ ;
  
  public UIGroupForm(OrganizationService service) throws Exception {
    super("groupForm", "post", null) ;    
    setId("UIGroupForm");
    service_ = service ;
    groupNameInput_ = new UIStringInput("groupName", "") ;
    labelInput_ = new UIStringInput("label", "") ;
    descInput_ = new UITextArea("description", "") ;
    add(new HeaderRow().
        add(new Cell("#{UIGroupForm.header.add-group}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIGroupForm.label.group-name}")).
        add(new ComponentCell(this, groupNameInput_)));
    add(new Row().
        add(new LabelCell("#{UIGroupForm.label.label}")).
        add(new ComponentCell(this, labelInput_)));
    add(new Row().
        add(new LabelCell("#{UIGroupForm.label.description}")).
        add(new ComponentCell(this, descInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIGroupForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIGroupForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveUpdateListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public void setParentGroup(String parentGroupId) throws Exception {
    if(parentGroupId !=null) {
      parentGroup_  = service_.findGroupById(parentGroupId) ;
    } else {
      parentGroup_ = null ;
    }
    groupNameInput_.setValue("") ;
    descInput_.setValue("") ;
  }
 
  static public class SaveUpdateListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIGroupForm uiForm = (UIGroupForm) event.getComponent() ;
      Group group = uiForm.service_.createGroupInstance() ;
      group.setGroupName(uiForm.groupNameInput_.getValue()) ;
      group.setLabel(uiForm.labelInput_.getValue()) ;
      group.setDescription(uiForm.descInput_.getValue()) ;
      if (uiForm.parentGroup_ == null) {
        uiForm.service_.createGroup(group) ;
      } else {
        uiForm.service_.addChild(uiForm.parentGroup_, group) ;
      }
      UIGroupExplorer uiGroupExplorer = (UIGroupExplorer)uiForm.getSibling(UIGroupExplorer.class) ;
      uiGroupExplorer.update() ;
      uiForm.setRenderedSibling(UIGroupExplorer.class);
    }
  }
  
  static public class CancelActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UIGroupForm uiForm = (UIGroupForm) event.getComponent() ;
  		uiForm.setRenderedSibling(UIGroupExplorer.class);
    }
  }
}