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
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.user.validator.ValidUserValidator;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portal.community.CommunityConfigService;
import org.exoplatform.services.portal.community.CommunityNavigation;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIGroupForm.java,v 1.11 2004/10/21 15:25:17 tuan08 Exp $
 */
public class UICommunityNavForm extends UISimpleForm {
  private UISelectBox membershipInput_ ;
  private UIStringInput navigationInput_ ;
  private UITextArea descriptionInput_ ;
  private CommunityConfigService service_ ;
  private CommunityNavigation communityNav_ ;
  
  public UICommunityNavForm(CommunityConfigService service,
                            OrganizationService orgService) throws Exception {
    super("communityNavForm", "post", null) ;
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
    navigationInput_ = new UIStringInput("navigation", "").
                       addValidator(ValidUserValidator.class) ;
    descriptionInput_ = new UITextArea("description", "") ;
    add(new HeaderRow().
        add(new Cell("#{UICommunityNavForm.header.community-navigation}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UICommunityNavForm.label.membership}")).
        add(new ComponentCell(this, membershipInput_)));
    add(new Row().
        add(new LabelCell("#{UICommunityNavForm.label.navigation}")).
        add(new ComponentCell(this, navigationInput_)));
    add(new Row().
        add(new LabelCell("#{UICommunityNavForm.label.description}")).
        add(new ComponentCell(this, descriptionInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UICommunityNavForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UICommunityNavForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveUpdateListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
 
  public void setCommunityNavigation(CommunityNavigation cn) {
    communityNav_ = cn;
    if(cn == null)  {
      membershipInput_.setValue("") ;
      navigationInput_.setValue("") ;
      descriptionInput_.setValue("") ;
    } else {
      membershipInput_.setValue(cn.getMembership()) ;
      navigationInput_.setValue(cn.getNavigation()) ;
      descriptionInput_.setValue(cn.getDescription()) ;
    }
  }
  
  static public class SaveUpdateListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UICommunityNavForm uiForm = (UICommunityNavForm) event.getComponent() ;
      CommunityNavigation cn = uiForm.communityNav_ ;
      if(cn == null) {
        UIGroupExplorer uiExplorer = 
          (UIGroupExplorer) uiForm.getAncestorOfType(UIGroupExplorer.class) ;
        Group currentGroup = uiExplorer.getCurrentGroup() ;
        if(currentGroup == null) {
          InformationProvider iprovider = findInformationProvider(uiForm);
          iprovider.addMessage(new ExoFacesMessage("#{UICommunityNavForm.msg.no-group}")) ;
          uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
          return ;
        }
        cn = new CommunityNavigation() ;
        cn.setGroupId(currentGroup.getId()) ;
      }
      cn.setMembership(uiForm.membershipInput_.getValue()) ;
      cn.setNavigation(uiForm.navigationInput_.getValue()) ;
      cn.setDescription(uiForm.descriptionInput_.getValue()) ;
      uiForm.service_.addCommunityNavigation(cn) ;
      UIGroupCommunityInfo uiInfo = 
        (UIGroupCommunityInfo) uiForm.getSibling(UIGroupCommunityInfo.class);
      uiInfo.setCommunityNavigation(cn) ;
      uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
      UICommunityNavForm uiForm = (UICommunityNavForm) event.getComponent() ;
      uiForm.setRenderedSibling(UIGroupCommunityInfo.class) ;
    }
  }
}