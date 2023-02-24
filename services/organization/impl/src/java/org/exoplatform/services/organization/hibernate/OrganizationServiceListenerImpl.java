/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.hibernate;

import java.util.Iterator;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationServiceListener;
import org.exoplatform.services.organization.User;
import org.picocontainer.Startable;
/**
 * Jul 20, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: OrganizationServiceListenerImpl.java,v 1.5 2004/10/28 15:36:43 tuan08 Exp $
 */
public class OrganizationServiceListenerImpl 
  extends OrganizationServiceListener implements Startable {
  
  private OrganizationConfig config_ ;
  
  public OrganizationServiceListenerImpl(OrganizationService orgService,
                                         ConfigurationManager cService) throws Exception { 
    PageList users = orgService.getUserPageList(10);
    if(users.getAvailable() > 0) return ; //Not new database 
    orgService.addListener(this);
    ServiceConfiguration sconf = cService.getServiceConfiguration(getClass()) ;
    config_ = (OrganizationConfig)sconf.getObjectParam("organization.configuration").getObject() ;
  }
  
  public void inititalize(OrganizationService service) { 
  	try {
  		createGroups(service) ;      
      createMembershipTypes(service) ;
      createUsers(service) ;
  	} catch (Exception ex) {
  		ex.printStackTrace() ; 
  	}
  }
  
  private void createGroups(OrganizationService orgService) throws Exception {
    List groups = config_.getGroup() ;
    for(int i = 0 ; i < groups.size() ; i++) {
      OrganizationConfig.Group data = (OrganizationConfig.Group) groups.get(i);
      Group group = orgService.createGroupInstance();
      group.setGroupName(data.getName());
      group.setDescription(data.getDescription()) ;
      String parentId = data.getParentId() ;
      if(parentId == null || parentId.length() == 0) {
        orgService.createGroup(group);
      } else {
        Group parentGroup =  orgService.findGroupById(parentId) ;
        orgService.addChild(parentGroup, group) ;
      }
    }
  }
    
  private void createMembershipTypes(OrganizationService orgService) throws Exception {
    List types = config_.getMembershipType() ;
    for(int i = 0 ; i < types.size() ; i++) {
      OrganizationConfig.MembershipType data = (OrganizationConfig.MembershipType) types.get(i);
      MembershipType type = orgService.createMembershipTypeInstance();
      type.setName(data.getType()) ; 
      type.setDescription(data.getDescription()) ;
      orgService.createMembershipType(type) ;
    }
  }
  
  private void createUsers(OrganizationService service) throws Exception {
    List users = config_.getUser() ;
  	for(int i = 0 ; i < users.size() ; i++) {
      OrganizationConfig.User data = (OrganizationConfig.User) users.get(i);
  		User user = service.createUserInstance() ;
  		user.setUserName(data.getUserName()) ;
  		user.setPassword(data.getPassword()) ;
  		user.setFirstName(data.getFirstName()) ;
  		user.setLastName(data.getLastName()) ;
  		user.setEmail(data.getEmail()) ;
  		service.createUser(user);
      List groups = data.getGroups();
      for (Iterator iter = groups.iterator(); iter.hasNext();) {
        String groupId = (String) iter.next();
        Group group = service.findGroupById(groupId);     
        Membership m = service.createMembershipInstance();
        m.setMembershipType("member");              
        service.linkMembership(data.getUserName(), group, m);
      }            
  	}
  }
  
  public void start() {} 
  public void stop() {} 
}