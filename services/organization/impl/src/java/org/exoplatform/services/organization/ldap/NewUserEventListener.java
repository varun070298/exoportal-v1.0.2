/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ObjectParam;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.impl.MembershipImpl;
import org.picocontainer.Startable;
/**
 * Jul 20, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NewUserEventListener.java,v 1.7 2004/10/28 15:36:43 tuan08 Exp $
 */
public class NewUserEventListener extends UserEventListener implements Startable {
  private MembershipHandler membershipHandler_ ;
  private NewUserConfig config_ ;
  
	public NewUserEventListener(ConfigurationManager cService, 
                              OrganizationService orgService,
                              MembershipHandler membershipHandler) throws Exception {
    membershipHandler_ = membershipHandler ;
    orgService.addUserEventListener(this) ;

    ServiceConfiguration sconf  = cService.getServiceConfiguration(getClass()) ;
    if (sconf == null) return  ;
    ObjectParam param = sconf.getObjectParam("new.user.configuration") ;
    config_ = (NewUserConfig) param.getObject() ;
  }
  
  public void start() { }
  
  public void stop() { }
  
	public void postSave(User user, boolean isNew, XResources xresources) throws Exception {
    if(config_ == null) return ;
    if(isNew && !config_.isIgnoreUser(user.getUserName())) {
      Session session = (Session) xresources.getResource(Session.class) ;    	
      createDefaultUserMemberships(user , session) ;
    }
	}
	  
  private void createDefaultUserMemberships(User user, Session session) throws Exception {
    List groups = config_.getGroup() ;
    if(groups.size() == 0)  return ;
    ArrayList memberships = new ArrayList() ;
    for(int i = 0; i <  groups.size(); i++) {
      NewUserConfig.JoinGroup group = (NewUserConfig.JoinGroup) groups.get(i) ;
      MembershipImpl m = new MembershipImpl() ;
      m.setGroupId(group.getGroupId()) ;
      m.setMembershipType(group.getMembership()) ;
      m.setUserName(user.getUserName()) ;
      m.setId(IdentifierUtil.generateUUID(m)) ;
      memberships.add(m) ;
    }
    membershipHandler_.createMembershipEntries(memberships) ;
  }
}