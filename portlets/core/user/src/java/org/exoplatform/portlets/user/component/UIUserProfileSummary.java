/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIUserProfile.java,v 1.14 2004/09/28 18:19:23 tuan08 Exp $
 */
public class UIUserProfileSummary extends UIExoCommand {
  final static public String[] PERSONAL_INFO_KEYS = UIUserProfile.PERSONAL_INFO_KEYS ;
  final static public String[] HOME_INFO_KEYS = UIUserProfile.HOME_INFO_KEYS ;
  final static public String[] BUSINESE_INFO_KEYS  = UIUserProfile.BUSINESE_INFO_KEYS ;
 
  private Map userInfoMap_ ;
  private boolean showBackButton_ ;
  private OrganizationService service_ ;
   
  public UIUserProfileSummary(OrganizationService service) throws Exception {
  	setId("UIUserProfile") ;
  	setRendererType("UserProfileRenderer") ;
    service_ = service ;
    ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext() ;
    Map requestMap = eContext.getRequestMap() ;
    userInfoMap_ = (Map) requestMap.get(PortletRequest.USER_INFO) ;
    showBackButton_ = false  ;
    addActionListener(BackActionListener.class , BACK_ACTION) ;
  }
  
  public boolean showBackButton() { return showBackButton_ ; }
  
  public Map getUserInfoMap() { return userInfoMap_ ; } 
  
  public void setUserProfile(String userName) throws Exception { 
    userInfoMap_ = service_.findUserProfileByName(userName).getUserInfoMap() ;
    showBackButton_ = true  ;
  }
  
  static public class BackActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIUserProfileSummary uiProfile = (UIUserProfileSummary) event.getComponent() ;
      uiProfile.setRenderedSibling(UIListUser.class) ;
    }
  }
}