/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.OrganizationService;
import org.picocontainer.Startable;

/**
 * Author : Tuan Nguyen
 *          tuan08@ms.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 */
public class PortalMembershipEventListener extends MembershipEventListener implements Startable {
  
  private ExoCache nodeNavigationCache_ ;

  public PortalMembershipEventListener(OrganizationService orgService,
                                       CacheService cacheService) throws Exception {
    nodeNavigationCache_ = cacheService.getCacheInstance(NodeImpl.class.getName()) ;
    orgService.addMembershipEventListener(this) ;
  }
  
  public void postDelete(Membership m, XResources xresources) throws Exception {
    //invalidate the cache of membership owner.
    nodeNavigationCache_.remove(m.getUserName()) ;
  }
  
  public void start() { }
  public void stop() {} 
}
