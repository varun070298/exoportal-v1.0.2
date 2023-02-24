/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.community.impl;

import java.util.*;
import org.exoplatform.services.config.ConfigurationService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portal.community.CommunityConfig;
import org.exoplatform.services.portal.community.CommunityConfigService;
import org.exoplatform.services.portal.community.CommunityNavigation;
import org.exoplatform.services.portal.community.CommunityPortal;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 4, 2004
 * @version $Id$
 */
public class CommunityConfigServiceImpl implements CommunityConfigService {
  private Map communityPortals_ ;
  private Map communityNavigations_ ;
  private OrganizationService orgService_ ;
  private ConfigurationService confService_ ;
  
  public CommunityConfigServiceImpl(ConfigurationService confService,
                                    OrganizationService  orgService) throws Exception {
    orgService_ = orgService ;
    confService_ = confService ;
    communityPortals_ = new HashMap() ;
    communityNavigations_ = new HashMap() ;
    CommunityConfig communityConfig = 
      (CommunityConfig)confService.getServiceConfiguration(CommunityConfigService.class);
    populateConfiguration(communityConfig) ;
  }
  
  private void populateConfiguration(CommunityConfig config) {
    List list = config.getCommunityPortals() ;
    for(int i = 0; i < list.size(); i++) {
      CommunityPortal cp = (CommunityPortal) list.get(i) ;
      communityPortals_.put(cp.getGroupId(), cp) ;
    }
    list = config.getCommunityNavigations() ;
    for(int i = 0; i < list.size(); i++) {
      CommunityNavigation cn = (CommunityNavigation) list.get(i) ;
      communityNavigations_.put(cn.getGroupId(), cn) ;
    }
  }
  
  public CommunityPortal findCommunityPortal(String user) throws Exception {
    Collection memberships = orgService_.findMembershipsByUser(user) ;
    CommunityPortal found = null ;
    Iterator mitr = memberships.iterator() ;
    while(mitr.hasNext()) {
      Membership m = (Membership) mitr.next() ;
      Iterator i = communityPortals_.values().iterator() ;
      while(i.hasNext()) {
        CommunityPortal cp = (CommunityPortal) i.next();
        if(m.getMembershipType().equals(cp.getMembership()) &&
           m.getGroupId().equals(cp.getGroupId())) {
          if(found == null) {
            found = cp ;
          } else {
            if(cp.getPriority() > found.getPriority()) {
              found = cp ;
            }
          }
        }
      }
    }
    return found;
  }
  
  public CommunityPortal getCommunityPortal(Group group) throws Exception {
    Iterator i = communityPortals_.values().iterator() ;
    while(i.hasNext()) {
      CommunityPortal cp = (CommunityPortal)i.next();
      if(cp.getGroupId().equals(group.getId())) return cp ;
    }
    return null;
  }

  synchronized public void addCommunityPortal(CommunityPortal cp) throws Exception {
    communityPortals_.put(cp.getGroupId(), cp) ;
    confService_.saveServiceConfiguration(CommunityConfigService.class, createCommunityConfig());
  }

  synchronized public void removeCommunityPortal(CommunityPortal cp) throws Exception {
    communityPortals_.remove(cp.getGroupId()) ;
    confService_.saveServiceConfiguration(getClass(), createCommunityConfig());
  }

  public List findCommunityNavigation(String user) throws Exception {
    List list = new ArrayList() ;
    Collection memberships = orgService_.findMembershipsByUser(user) ;
    Iterator mitr = memberships.iterator() ;
    while(mitr.hasNext()) {
      Membership m = (Membership) mitr.next() ;
      Iterator i = communityNavigations_.values().iterator() ;
      while(i.hasNext()) {
        CommunityNavigation cn = (CommunityNavigation) i.next();
        if(m.getMembershipType().equals(cn.getMembership()) &&
           m.getGroupId().equals(cn.getGroupId())) {
          list.add(cn) ;
        }
      }
    }
    return list;
  }

  public CommunityNavigation getCommunityNavigation(Group group) throws Exception {
    Iterator i = communityNavigations_.values().iterator() ;
    while(i.hasNext()) {
      CommunityNavigation cn = (CommunityNavigation)i.next();
      if(cn.getGroupId().equals(group.getId())) return cn ;
    }
    return null;
  }

  synchronized public void addCommunityNavigation(CommunityNavigation cn) throws Exception {
    communityNavigations_.put(cn.getGroupId(), cn) ;
    confService_.saveServiceConfiguration(getClass(), createCommunityConfig());
  }

  synchronized public void removeCommunityNavigation(CommunityNavigation cn) throws Exception {
    communityNavigations_.remove(cn.getGroupId()) ;
    confService_.saveServiceConfiguration(CommunityConfigService.class, createCommunityConfig());
  }
  
  private CommunityConfig createCommunityConfig() {
    CommunityConfig config = new  CommunityConfig() ;
    List portals = config.getCommunityPortals() ;
    Iterator i = communityPortals_.values().iterator() ;
    while(i.hasNext()) {
      CommunityPortal cp = (CommunityPortal)i.next();
      portals.add(cp) ;
    }
    
    List navigations = config.getCommunityNavigations() ;
    i = communityNavigations_.values().iterator() ;
    while(i.hasNext()) {
      CommunityNavigation cn = (CommunityNavigation)i.next();
      navigations.add(cn) ;
    }
    return config ;
  }
}
