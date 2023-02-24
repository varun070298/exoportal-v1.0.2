/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.List;

import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationServiceListener;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileEventListener;
import org.exoplatform.services.organization.impl.GroupImpl;
import org.exoplatform.services.organization.impl.MembershipImpl;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;
import org.exoplatform.services.organization.impl.UserImpl;
import org.exoplatform.services.organization.impl.UserProfileImpl;
import org.picocontainer.Startable;


/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class OrganizationServiceImpl implements OrganizationService, Startable {
  
  static private String[] MAPPING =
   {
     "org/exoplatform/services/organization/impl/UserImpl.hbm.xml",
     "org/exoplatform/services/organization/impl/MembershipImpl.hbm.xml" ,
     "org/exoplatform/services/organization/impl/GroupImpl.hbm.xml" ,
     "org/exoplatform/services/organization/impl/MembershipTypeImpl.hbm.xml" ,
     "org/exoplatform/services/organization/hibernate/UserProfileData.hbm.xml"
   } ;
  
  UserQueryHandler userQueryHandler_ ;
  UserProfileQueryHandler userProfileQueryHandler_ ;
  GroupQueryHandler groupQueryHandler_ ;
  MembershipQueryHandler membershipQueryHandler_ ;
  MembershipTypeQueryHandler membershipTypeQueryHandler_ ;
  private List listeners_ ;

  public OrganizationServiceImpl(HibernateService hibernateService,
                                 UserQueryHandler userQueryHandler,
                                 GroupQueryHandler groupQueryHandler,
                                 MembershipQueryHandler membershipQueryHandler,
                                 MembershipTypeQueryHandler membershipTypeQueryHandler,
                                 UserProfileQueryHandler userProfileQueryHandler) {
    hibernateService.addMappingFiles(MAPPING) ;
    
    userQueryHandler_ = userQueryHandler ;
    userProfileQueryHandler_ =   userProfileQueryHandler ;
    groupQueryHandler_ =  groupQueryHandler ;
    membershipQueryHandler_ = membershipQueryHandler ;
    membershipTypeQueryHandler_ = membershipTypeQueryHandler ;
    listeners_ = new ListenerStack(5) ;
  }
  
  public void start() { 
  	for(int i = 0 ; i < listeners_.size(); i++) {
  		OrganizationServiceListener listener = (OrganizationServiceListener) listeners_.get(i) ;
      listener.inititalize(this) ;
    }
  } 
  
  public void stop() {} 
  
  synchronized public void addListener(OrganizationServiceListener listener) {
    listeners_.add(listener) ;
  }
  
  
  synchronized public void removeListener(OrganizationServiceListener listener) {
     listeners_.remove(listener) ;
  }
  
  public User createUserInstance() {
    return new UserImpl() ; 
  }

  final public List getUserEventListeners() {
    return userQueryHandler_.getUserEventListeners() ;
  }
  
  public void addUserEventListener(UserEventListener listener) {
    userQueryHandler_.addUserEventListener(listener) ;
  }

  final public void createUser(User user) throws Exception {
    userQueryHandler_.createUser(user) ;
  }

  final public void saveUser(User user) throws Exception {
    userQueryHandler_.saveUser(user) ;
  }
  

  final public User removeUser(String user) throws Exception {
    return userQueryHandler_.removeUser(user) ;
  }


  final public User findUserByName(String userName) throws Exception {
    return userQueryHandler_.findUserByName(userName) ;
  }

  public PageList getUserPageList(int pageSize)  throws Exception {
    return userQueryHandler_.getUserPageList(pageSize) ;
  }
  
  final public PageList findUsers(Query query) throws Exception {
    return userQueryHandler_.findUsers(query) ;
  }

  final public PageList findUsersByGroup(String groupId) throws Exception {
    return userQueryHandler_.findUsersByGroup(groupId) ;
  }

  public void addUserProfileEventListener(UserProfileEventListener listener) {
    userProfileQueryHandler_.addUserProfileEventListener(listener) ;
  }


  final public UserProfile createUserProfileInstance() {
    return new UserProfileImpl() ; 
  }

  final public void saveUserProfile(UserProfile profile) throws Exception {
    userProfileQueryHandler_.saveUserProfile(profile) ;
  }

  final public UserProfile removeUserProfile(String user) throws Exception {
    return userProfileQueryHandler_.removeUserProfile(user) ;
  }

  final public UserProfile findUserProfileByName(String userName) throws Exception {
    return userProfileQueryHandler_.findUserProfileByName(userName) ;
  }

  final public Collection findUserProfiles() throws Exception {
    return userProfileQueryHandler_.findUserProfiles() ;
  }

  public void addGroupEventListener(GroupEventListener listener) {
    groupQueryHandler_.addGroupEventListener(listener) ;
  }

  final public Group createGroupInstance() {
    return new GroupImpl();
  }

  final public void createGroup(Group group) throws Exception {
    groupQueryHandler_.createGroup(group) ;
  }

  final public void addChild(Group parent, Group child) throws Exception {
    groupQueryHandler_.addChild(parent, child) ;
  }

  final public void saveGroup(Group group) throws Exception {
    groupQueryHandler_.saveGroup(group) ;
  }

  final public Group removeGroup(Group group) throws Exception {
    return groupQueryHandler_.removeGroup(group) ;
  }

  final public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    return groupQueryHandler_.findGroupByMembership(userName, membershipType) ;
  }
  
  public Group findGroupById(String groupId) throws Exception {
    return groupQueryHandler_.findGroupById(groupId) ;
  }
  
  final public Collection findGroups(Group parent) throws Exception {
    return groupQueryHandler_.findGroups(parent) ;
  }
  
  final public Collection findGroupsOfUser(String user) throws Exception {
    return groupQueryHandler_.findGroupsOfUser(user) ;
  }

  public void addMembershipEventListener(MembershipEventListener listener) {
    membershipQueryHandler_.addMembershipEventListener(listener) ;
  }

  final public Membership createMembershipInstance() {
    return new MembershipImpl();
  }

  final public void createMembership(Membership m) throws Exception {
    membershipQueryHandler_.createMembership(m) ;
  }

  final public void linkMembership(String userName, Group group, Membership m) throws Exception {
    membershipQueryHandler_.linkMembership(userName, group, m) ;
  }

  final public void saveMembership(Membership m) throws Exception {
    membershipQueryHandler_.saveMembership( m) ;
  }

  final public Membership removeMembership(String id) throws Exception {
    return membershipQueryHandler_.removeMembership(id) ;
  }

  public Membership findMembershipByUserGroupAndType(String userName, 
                                                     String groupId, 
                                                     String type) throws Exception {
    return membershipQueryHandler_.findMembershipByUserGroupAndType(userName, groupId, type) ;
  }
  
  final public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    return membershipQueryHandler_.findMembershipsByUserAndGroup(userName, groupId) ;
  }

  final public Collection findMembershipsByUser(String userName) throws Exception {
    return membershipQueryHandler_.findMembershipsByUser(userName) ;
  }

  final public Collection findMembershipsByGroup(Group group) throws Exception {
    return membershipQueryHandler_.findMembershipsByGroup(group) ;
  }
  
  final public Membership findMembership(String id) throws Exception {
    return membershipQueryHandler_.findMembership(id) ;
  }

  final public MembershipType createMembershipTypeInstance() {
    return new MembershipTypeImpl() ;
  }
  
  final public MembershipType createMembershipType(MembershipType mt) throws Exception {
    return membershipTypeQueryHandler_.createMembershipType(mt) ;
  }
  
  final public MembershipType saveMembershipType(MembershipType mt) throws Exception {
    return membershipTypeQueryHandler_.saveMembershipType(mt) ;
  }
  
  final public MembershipType findMembershipType(String name) throws Exception {
    return membershipTypeQueryHandler_.findMembershipType(name) ;
  }

  final public MembershipType removeMembershipType(String name) throws Exception {
    return membershipTypeQueryHandler_.removeMembershipType(name) ;
  }
  
  final public Collection findMembershipTypes() throws Exception {
    return membershipTypeQueryHandler_.findMembershipTypes() ;
  }
  
  public boolean authenticate(String username, String password) throws Exception  {
    User user = findUserByName(username);
    if(user == null) return false ;
    return user.getPassword().equals(password) ;
  }
}