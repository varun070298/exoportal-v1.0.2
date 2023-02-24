/*
 * Created on Feb 3, 2005
 */
package org.exoplatform.services.jcr.impl.mock;

import java.util.Collection;

import org.exoplatform.commons.utils.PageList;
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

/**
 * @author benjaminmestrallet
 */
public class DummyOrganizationService implements OrganizationService{


  public void addListener(OrganizationServiceListener listener) {}


  public void removeListener(OrganizationServiceListener listener) {  
  }

  public User createUserInstance() {
    return null;
  }

  public void createUser(User user) throws Exception {
  }

  public void saveUser(User user) throws Exception {
  }

  public User removeUser(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findUserByName(java.lang.String)
   */
  public User findUserByName(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findUsersByGroup(java.lang.String)
   */
  public PageList findUsersByGroup(String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#getUserPageList(int)
   */
  public PageList getUserPageList(int pageSize) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findUsers(org.exoplatform.services.organization.Query)
   */
  public PageList findUsers(Query query) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createUserProfileInstance()
   */
  public UserProfile createUserProfileInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#saveUserProfile(org.exoplatform.services.organization.UserProfile)
   */
  public void saveUserProfile(UserProfile profile) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#removeUserProfile(java.lang.String)
   */
  public UserProfile removeUserProfile(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findUserProfileByName(java.lang.String)
   */
  public UserProfile findUserProfileByName(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findUserProfiles()
   */
  public Collection findUserProfiles() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createGroupInstance()
   */
  public Group createGroupInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createGroup(org.exoplatform.services.organization.Group)
   */
  public void createGroup(Group group) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#addChild(org.exoplatform.services.organization.Group, org.exoplatform.services.organization.Group)
   */
  public void addChild(Group parent, Group child) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#saveGroup(org.exoplatform.services.organization.Group)
   */
  public void saveGroup(Group group) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#removeGroup(org.exoplatform.services.organization.Group)
   */
  public Group removeGroup(Group group) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findGroupByMembership(java.lang.String, java.lang.String)
   */
  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findGroupById(java.lang.String)
   */
  public Group findGroupById(String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findGroups(org.exoplatform.services.organization.Group)
   */
  public Collection findGroups(Group parent) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findGroupsOfUser(java.lang.String)
   */
  public Collection findGroupsOfUser(String user) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createMembershipInstance()
   */
  public Membership createMembershipInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createMembership(org.exoplatform.services.organization.Membership)
   */
  public void createMembership(Membership m) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#linkMembership(java.lang.String, org.exoplatform.services.organization.Group, org.exoplatform.services.organization.Membership)
   */
  public void linkMembership(String userName, Group group, Membership m) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#saveMembership(org.exoplatform.services.organization.Membership)
   */
  public void saveMembership(Membership m) throws Exception {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#removeMembership(java.lang.String)
   */
  public Membership removeMembership(String id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembership(java.lang.String)
   */
  public Membership findMembership(String id) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipByUserGroupAndType(java.lang.String, java.lang.String, java.lang.String)
   */
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipsByUserAndGroup(java.lang.String, java.lang.String)
   */
  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipsByUser(java.lang.String)
   */
  public Collection findMembershipsByUser(String userName) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipsByGroup(org.exoplatform.services.organization.Group)
   */
  public Collection findMembershipsByGroup(Group group) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createMembershipTypeInstance()
   */
  public MembershipType createMembershipTypeInstance() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#createMembershipType(org.exoplatform.services.organization.MembershipType)
   */
  public MembershipType createMembershipType(MembershipType mt) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#saveMembershipType(org.exoplatform.services.organization.MembershipType)
   */
  public MembershipType saveMembershipType(MembershipType mt) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#removeMembershipType(java.lang.String)
   */
  public MembershipType removeMembershipType(String name) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipType(java.lang.String)
   */
  public MembershipType findMembershipType(String name) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#findMembershipTypes()
   */
  public Collection findMembershipTypes() throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#addUserEventListener(org.exoplatform.services.organization.UserEventListener)
   */
  public void addUserEventListener(UserEventListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#addUserProfileEventListener(org.exoplatform.services.organization.UserProfileEventListener)
   */
  public void addUserProfileEventListener(UserProfileEventListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#addGroupEventListener(org.exoplatform.services.organization.GroupEventListener)
   */
  public void addGroupEventListener(GroupEventListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#addMembershipEventListener(org.exoplatform.services.organization.MembershipEventListener)
   */
  public void addMembershipEventListener(MembershipEventListener listener) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.organization.OrganizationService#authenticate(java.lang.String, java.lang.String)
   */
  public boolean authenticate(String username, String password) throws Exception {
    if("exo".equals(username) && "exo".equals(password))
      return true;
    return false;
  }

}
