/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.user.validator.ValidUserValidator;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portal.community.CommunityConfigService;
import org.exoplatform.services.portal.community.CommunityPortal;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIGroupForm.java,v 1.11 2004/10/21 15:25:17 tuan08 Exp $
 */
public class UICommunityPortalForm extends UISimpleForm {
  static public List PRIORITIES ;
  static {
    PRIORITIES = new ArrayList() ;
    for(int i = 1; i <= 10; i++) {
      String num = Integer.toString(i) ;
      PRIORITIES.add(new SelectItem(num, num)) ;
    }
  }
  
  private UISelectBox membershipInput_ ;
  private UIStringInput portalInput_ ;
  private UISelectBox priorityInput_ ;
  private UITextArea descriptionInput_ ;
  private CommunityConfigService service_ ;
  private CommunityPortal communityPortal_ ;
  
  public UICommunityPortalForm(CommunityConfigService service, 
                               OrganizationService orgService) throws Exception {
    super("communityPortalForm", "post", null) ;
    service_ = service ;
    Collection memberships = orgService.findMembershipTypes() ;
    Iterator i = memberships.iterator() ;
    List mtypes = new ArrayList(10) ;
    while(i.hasNext()) {
      MembershipType mt = (MembershipType) i.next();
      String name = mt.getName() ;
      mtypes.add(new SelectItem(name, name)) ;
    }
    membershipInput_ = new UISelectBox("membership", "", mtypes) ;    
    portalInput_ = new UIStringInput("portal", "").
                   addValidator(ValidUserValidator.class) ;
    priorityInput_ =  new UISelectBox("priority", "1", PRIORITIES) ;    
    descriptionInput_ = new UITextArea("description", "") ;
    add(new HeaderRow().
        add(new Cell("#{UICommunityPortalForm.header.community-portal}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UICommunityPortalForm.label.membership}")).
        add(new ComponentCell(this, membershipInput_)));
    add(new Row().
        add(new LabelCell("#{UICommunityPortalForm.label.portal}")).
        add(new ComponentCell(this, portalInput_)));
    add(new Row().
        add(new LabelCell("#{UICommunityPortalForm.label.priority}")).
        add(new ComponentCell(this, priorityInput_)));
    add(new Row().
        add(new LabelCell("#{UICommunityPortalForm.label.description}")).
        add(new ComponentCell(this, descriptionInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UICommunityPortalForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UICommunityPortalForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveUpdateListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
 
  public void setCommunityPortal(CommunityPortal cp) {
    communityPortal_ = cp;
    if(cp == null)  {
      membershipInput_.setValue("") ;
      portalInput_.setValue("") ;
      priorityInput_.setValue("5") ;
      descriptionInput_.setValue("") ;
    } else {
      membershipInput_.setValue(cp.getMembership()) ;
      portalInput_.setValue(cp.getPortal()) ;
      priorityInput_.setValue(Integer.toString(cp.getPriority())) ;
      descriptionInput_.setValue(cp.getDescription()) ;
    }
  }
  
  static public class SaveUpdateListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UICommunityPortalForm uiForm = (UICommunityPortalForm) event.getComponent() ;
      CommunityPortal cp = uiForm.communityPortal_ ;
      if(cp == null) {
        UIGroupExplorer uiExplorer = 
          (UIGroupExplorer) uiForm.getAncestorOfType(UIGroupExplorer.class) ;
        Group currentGroup = uiExplorer.getCurrentGroup() ;
        if(currentGroup == null) {
          InformationProvider iprovider = findInformationProvider(uiForm);
          iprovider.addMessage(new ExoFacesMessage("#{UICommunityPortalForm.msg.no-group}")) ;
          uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
          return ;
        } 
        cp = new CommunityPortal() ;
        cp.setGroupId(currentGroup.getId()) ;
      }
      cp.setMembership(uiForm.membershipInput_.getValue()) ;
      cp.setPortal(uiForm.portalInput_.getValue()) ;
      cp.setPriority(Integer.parseInt(uiForm.priorityInput_.getValue())) ;
      cp.setDescription(uiForm.descriptionInput_.getValue()) ;
      uiForm.service_.addCommunityPortal(cp) ;
      UIGroupCommunityInfo uiInfo = 
        (UIGroupCommunityInfo) uiForm.getSibling(UIGroupCommunityInfo.class);
      uiInfo.setCommunityPortal(cp) ;
      uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UICommunityPortalForm uiForm = (UICommunityPortalForm) event.getComponent() ;
      uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
    }
  }
}