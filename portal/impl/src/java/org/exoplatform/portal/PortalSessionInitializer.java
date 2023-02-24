/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal;

import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.container.SessionContainerInitializer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.portal.session.ExoPortal ;
import org.exoplatform.portal.faces.component.UIPortal ;
import org.exoplatform.portal.faces.component.PortalComponentCache ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 11, 2005
 * @version $Id$
 */
public class PortalSessionInitializer  implements SessionContainerInitializer {
  
  public void initialize(SessionContainer container)  {
    try {
      // register user profile to the session container
      OrganizationService orgService = 
        (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class) ;
      UserProfile userProfile = orgService.findUserProfileByName(container.getOwner());
      if(userProfile == null) userProfile = orgService.createUserProfileInstance() ;
      container.registerComponentInstance(userProfile.getClass(), userProfile) ;
      
      container.registerComponentInstance(new RequestInfo()) ;
      container.registerComponentImplementation(PortalComponentCache.class) ;
      container.registerComponentImplementation(ExoPortal.class, UIPortal.class) ;
    } catch (Exception ex) {
      LogUtil.getLog(getClass()).error("Error in PortalSessionInitializer", ex) ;
    }
  }
}
