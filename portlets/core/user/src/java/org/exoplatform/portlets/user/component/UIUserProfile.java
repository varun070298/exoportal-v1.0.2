/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.Iterator;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.UIForm.Container;
import org.exoplatform.faces.core.component.UIForm.StringField;
import org.exoplatform.faces.core.component.UIForm.TextAreaField;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIConfig.java,v 1.7 2004/06/30 19:54:39 tuan08 Exp $
 */
public class UIUserProfile extends UINode {
  final static public String[] PERSONAL_INFO_KEYS =
  {
    "user.name.given", "user.name.family", "user.name.nickName", "user.bdate" , 
    "user.gender" , "user.employer" , "user.department",  "user.jobtitle", 
  } ;

 final static public String[] HOME_INFO_KEYS =
  {
    "user.home-info.postal.name", "user.home-info.postal.street", 
    "user.home-info.postal.city", "user.home-info.postal.stateprov" ,
    "user.home-info.postal.postalcode", "user.home-info.postal.country",
    "user.home-info.telecom.mobile.number", "user.home-info.telecom.telephone.number", 
    "user.home-info.online.email", "user.home-info.online.uri" 
  } ;

 final static public String[] BUSINESE_INFO_KEYS = {
    "user.business-info.postal.name", "user.business-info.postal.city", 
    "user.business-info.postal.stateprov", "user.business-info.postal.postalcode",
    "user.business-info.postal.country", "user.business-info.telecom.mobile.number",
    "user.business-info.telecom.telephone.number", "user.business-info.online.email",
    "user.business-info.online.uri"
  } ;
 
  final static public String[] OTHER_KEYS = {
    "user.other-info.avatar.url", "user.other-info.signature", 
  } ;
  
  public UIUserProfile() throws Exception {
    setRendererType("PyramidTabBarRenderer") ;
    addChild(UIUserProfileSummary.class) ;
    String userName = SessionContainer.getInstance().getOwner() ;
    ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext() ;
    //cannot edit profile if remote user is owner
    if(!userName.equals(eContext.getRemoteUser())) return ;
    
    Map requestMap = eContext.getRequestMap() ;
    Map userInfoMap = (Map) requestMap.get(PortletRequest.USER_INFO) ;
    
    UINode uiAdmin = (UINode)  addChild(UINode.class) ;
    uiAdmin.setRendered(false) ;
    uiAdmin.setRendererType("SimpleTabRenderer") ;
    uiAdmin.setId("ProfileAdmin") ;
    
    UIForm uiForm = new UIForm("personal") ;
    uiForm.setId("PersonalInfo") ;
    Container container = uiForm.addContainer("#{UIUserProfile.header.personal-info}") ;
    for (int i =0 ; i < PERSONAL_INFO_KEYS.length; i++) {
      String key = PERSONAL_INFO_KEYS[i] ;
      container.add(new StringField(key,"#{" + key+ "}", (String)userInfoMap.get(key))) ;
    }

    container = uiForm.addContainer("#{UIUserProfile.header.user-home-info}") ;
    for (int i =0 ; i < HOME_INFO_KEYS.length; i++) {
      String key = HOME_INFO_KEYS[i] ;
      container.add(new StringField(key,"#{" + key+ "}", (String)userInfoMap.get(key))) ;
    }
    
    uiForm.addButton(new UIForm.Button("#{UIUserProfile.button.save}", SAVE_ACTION)) ;
    uiForm.addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    uiAdmin.getChildren().add(uiForm) ;
    
    uiForm = new UIForm("busineseInfo") ;
    uiForm.setId("BusineseInfo") ;
    uiForm.setRendered(false) ;
    container = uiForm.addContainer("#{UIUserProfile.header.user-businese-info}") ;
    for (int i =0 ; i < BUSINESE_INFO_KEYS.length; i++) {
      String key = BUSINESE_INFO_KEYS[i] ;
      container.add(new StringField(key,"#{" + key+ "}", (String)userInfoMap.get(key))) ;
    }
    uiForm.addButton(new UIForm.Button("#{UIUserProfile.button.save}", SAVE_ACTION)) ;
    uiForm.addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    uiAdmin.getChildren().add(uiForm) ;
    
    uiForm = new UIForm("otherInfo") ;
    uiForm.setId("OtherInfo") ;
    uiForm.setRendered(false) ;
    container = uiForm.addContainer("#{UIUserProfile.header.user-other-info}") ;
    container.add(new StringField("user.other-info.avatar.url","#{user.other-info.avatar.url}", 
                                  (String)userInfoMap.get("user.other-info.avatar.url"))) ;
    container.add(new TextAreaField("user.other-info.signature","#{user.other-info.signature}", 
                                  (String)userInfoMap.get("user.other-info.signature"))) ;
    uiForm.addButton(new UIForm.Button("#{UIUserProfile.button.save}", SAVE_ACTION)) ;
    uiForm.addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    uiAdmin.getChildren().add(uiForm) ;
    UIAccountForm ui = new UIAccountForm() ;
    ui.customizeUpdateAccountForm() ;
    ui.setEditingUser(userName) ;
    ui.setRendered(false) ;
    uiAdmin.getChildren().add(ui) ;
  }
  
  
  static public class SaveActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIForm uiForm = (UIForm) event.getSource() ;
      ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext() ;
      Map requestMap = eContext.getRequestMap() ;
      Map userInfoMap = (Map) requestMap.get(PortletRequest.USER_INFO) ;
      Iterator i = uiForm.getMapFields().values().iterator() ;
      while (i.hasNext())  {
        UIForm.StringField field = (UIForm.StringField) i.next() ;
        String value = field.getValue() ;
        if(value == null || value.length() == 0) {
          userInfoMap.remove(field.getName()) ;
        } else {
          userInfoMap.put(field.getName(), value) ;
        }
      }
      String userName = SessionContainer.getInstance().getOwner() ;
      OrganizationService service = 
        (OrganizationService)PortalContainer.getComponent(OrganizationService.class) ;
      UserProfile profile = service.findUserProfileByName(userName) ;
      profile.setUserInfoMap(userInfoMap) ;
      service.saveUserProfile(profile) ;
    }
  }
}