/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import java.util.* ;
import org.exoplatform.commons.utils.PageList;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:46:04 PM
 */
public interface OrganizationService  {
	public void addListener(OrganizationServiceListener listener) ;
  public void removeListener(OrganizationServiceListener listener) ;
  
  public User createUserInstance() ;
  public void createUser(User user) throws Exception ;
  public void saveUser(User user) throws Exception ;
  public User removeUser(String userName) throws Exception ;
  public User findUserByName(String userName) throws Exception ;
  public PageList findUsersByGroup(String groupId) throws Exception ; 
  public PageList getUserPageList(int pageSize) throws Exception ;
  public PageList findUsers(Query query) throws Exception ;

  public UserProfile createUserProfileInstance() ;
  public void saveUserProfile(UserProfile profile) throws Exception ;
  public UserProfile removeUserProfile(String userName) throws Exception ;
  public UserProfile findUserProfileByName(String userName) throws Exception ;
  public Collection findUserProfiles() throws Exception ;

  public Group createGroupInstance();
  public void createGroup(Group group) throws Exception ;
  public void addChild(Group parent, Group child) throws Exception;
  public void saveGroup(Group group) throws Exception ;
  public Group removeGroup(Group group) throws Exception ;
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception ;
  public Group findGroupById(String groupId) throws Exception ;
  public Collection findGroups(Group parent) throws Exception ;
  public Collection findGroupsOfUser(String user) throws Exception ;

  public Membership createMembershipInstance();
  public void createMembership(Membership m) throws Exception;
  public void linkMembership(String userName, Group group, Membership m) throws Exception ;
  public void saveMembership(Membership m) throws Exception ;
  public Membership removeMembership(String id) throws Exception ;
  public Membership findMembership(String id) throws Exception ;
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception ;
  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception ;
  public Collection findMembershipsByUser(String userName) throws Exception ;
  public Collection findMembershipsByGroup(Group group) throws Exception ;

  public MembershipType createMembershipTypeInstance();
  public MembershipType createMembershipType(MembershipType mt) throws Exception ;
  public MembershipType saveMembershipType(MembershipType mt) throws Exception  ;
  public MembershipType removeMembershipType(String name) throws Exception ;
  public MembershipType findMembershipType(String name) throws Exception ;
  public Collection     findMembershipTypes() throws Exception ;

  public void addUserEventListener(UserEventListener listener) ;
  public void addUserProfileEventListener(UserProfileEventListener listener) ;
  public void addGroupEventListener(GroupEventListener listener) ;
  public void addMembershipEventListener(MembershipEventListener listener) ;
  
  public boolean authenticate(String username, String password) throws Exception ;
}