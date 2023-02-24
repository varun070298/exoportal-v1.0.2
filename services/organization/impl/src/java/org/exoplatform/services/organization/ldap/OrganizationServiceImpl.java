/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.PropertiesParam;
import org.exoplatform.container.configuration.ServiceConfiguration;
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
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class OrganizationServiceImpl implements OrganizationService, Startable {

	static private String[] MAPPING =
	   {
	     "org/exoplatform/services/organization/ldap/UserProfileData.hbm.xml"
	   } ;
	
	protected static Map properties;
	
	GroupHandler groupHandler;

	private List listeners_;

	MembershipHandler membershipHandler;

	MembershipTypeHandler membershipTypeHandler;

	UserProfileHandler userProfileHandler;

	UserHandler userHandler;

	public OrganizationServiceImpl(HibernateService hibernateService,
			UserHandler userHandler,
			GroupHandler groupHandler,
			MembershipHandler membershipHandler,
			MembershipTypeHandler membershipTypeHandler,
			UserProfileHandler userProfileHandler,
			ConfigurationManager confService) {
		hibernateService.addMappingFiles(MAPPING);

		this.userHandler = userHandler;
		this.userProfileHandler = userProfileHandler;
		this.groupHandler = groupHandler;
		this.membershipHandler = membershipHandler;
		this.membershipTypeHandler = membershipTypeHandler;
		listeners_ = new ListenerStack(5);
		
		try {
			ServiceConfiguration sconf = confService.getServiceConfiguration(OrganizationService.class) ;
			PropertiesParam param = sconf.getPropertiesParam("exo.organization.service") ;
			properties = param.getProperties() ;
		} catch (Exception e) {
			
		}
	}

	final public void addChild(Group parent, Group child) throws Exception {
		groupHandler.addChild(parent, child);
	}

	public void addGroupEventListener(GroupEventListener listener) {
		groupHandler.addGroupEventListener(listener);
	}

	synchronized public void addListener(OrganizationServiceListener listener) {
		listeners_.add(listener);
	}

	public void addMembershipEventListener(MembershipEventListener listener) {
		membershipHandler.addMembershipEventListener(listener);
	}

	public void addUserEventListener(UserEventListener listener) {
		userHandler.addUserEventListener(listener);
	}

	public void addUserProfileEventListener(UserProfileEventListener listener) {
		userProfileHandler.addUserProfileEventListener(listener);
	}

	public boolean authenticate(String userName, String password)
			throws Exception {
		return userHandler.authenticate(userName, password);
	}

	final public void createGroup(Group group) throws Exception {
		groupHandler.createGroup(group);
	}

	final public Group createGroupInstance() {
		return new GroupImpl();
	}

	final public void createMembership(Membership m) throws Exception {
		membershipHandler.createMembership(m);
	}

	final public Membership createMembershipInstance() {
		return new MembershipImpl();
	}

	final public MembershipType createMembershipType(MembershipType mt)
			throws Exception {
		return membershipTypeHandler.createMembershipType(mt);
	}

	final public MembershipType createMembershipTypeInstance() {
		return new MembershipTypeImpl();
	}

	final public void createUser(User user) throws Exception {
		userHandler.createUser(user);
	}

	public User createUserInstance() {
		return new UserImpl();
	}

	final public UserProfile createUserProfileInstance() {
		return new UserProfileImpl();
	}

	public Group findGroupById(String groupId) throws Exception {
		return groupHandler.findGroupById(groupId);
	}

	final public Collection findGroupByMembership(String userName,
			String membershipType) throws Exception {
		return groupHandler.findGroupByMembership(userName,
				membershipType);
	}

	final public Collection findGroups(Group parent) throws Exception {
		return groupHandler.findGroups(parent);
	}

	final public Collection findGroupsOfUser(String user) throws Exception {
		return groupHandler.findGroupsOfUser(user);
	}

	final public Membership findMembership(String id) throws Exception {
		return membershipHandler.findMembership(id);
	}

	public Membership findMembershipByUserGroupAndType(String userName,
			String groupId, String type) throws Exception {
		return membershipHandler.findMembershipByUserGroupAndType(
				userName, groupId, type);
	}

	final public Collection findMembershipsByGroup(Group group)
			throws Exception {
		return membershipHandler.findMembershipsByGroup(group);
	}

	final public Collection findMembershipsByUser(String userName)
			throws Exception {
		return membershipHandler.findMembershipsByUser(userName);
	}

	final public Collection findMembershipsByUserAndGroup(String userName,
			String groupId) throws Exception {
		return membershipHandler.findMembershipsByUserAndGroup(userName,
				groupId);
	}

	final public MembershipType findMembershipType(String name)
			throws Exception {
		return membershipTypeHandler.findMembershipType(name);
	}

	final public Collection findMembershipTypes() throws Exception {
		return membershipTypeHandler.findMembershipTypes();
	}

	final public User findUserByName(String userName) throws Exception {
		return userHandler.findUserByName(userName);
	}

	final public UserProfile findUserProfileByName(String userName)
			throws Exception {
		return userProfileHandler.findUserProfileByName(userName);
	}

	final public Collection findUserProfiles() throws Exception {
		return userProfileHandler.findUserProfiles();
	}

	final public PageList findUsers(Query query) throws Exception {
		return userHandler.findUsers(query);
	}

	final public PageList findUsersByGroup(String groupId) throws Exception {
		return userHandler.findUsersByGroup(groupId);
	}

	final public List getUserEventListeners() {
		return userHandler.getUserEventListeners();
	}

	public PageList getUserPageList(int pageSize) throws Exception {
		return userHandler.getUserPageList(pageSize);
	}

	final public void linkMembership(String userName, Group group, Membership m)
			throws Exception {
		membershipHandler.linkMembership(userName, group, m);
	}

	final public Group removeGroup(Group group) throws Exception {
		return groupHandler.removeGroup(group);
	}

	synchronized public void removeListener(OrganizationServiceListener listener) {
		listeners_.remove(listener);
	}

	final public Membership removeMembership(String id) throws Exception {
		return membershipHandler.removeMembership(id);
	}

	final public MembershipType removeMembershipType(String name)
			throws Exception {
		return membershipTypeHandler.removeMembershipType(name);
	}

	final public User removeUser(String user) throws Exception {
		return userHandler.removeUser(user);
	}

	final public UserProfile removeUserProfile(String user) throws Exception {
		return userProfileHandler.removeUserProfile(user);
	}

	final public void saveGroup(Group group) throws Exception {
		groupHandler.saveGroup(group);
	}

	final public void saveMembership(Membership m) throws Exception {
		membershipHandler.saveMembership(m);
	}

	final public MembershipType saveMembershipType(MembershipType mt)
			throws Exception {
		return membershipTypeHandler.saveMembershipType(mt);
	}

	final public void saveUser(User user) throws Exception {
		userHandler.saveUser(user);
	}

	final public void saveUserProfile(UserProfile profile) throws Exception {
		userProfileHandler.saveUserProfile(profile);
	}

	public void start() {
		for (int i = 0; i < listeners_.size(); i++) {
			OrganizationServiceListener listener = (OrganizationServiceListener) listeners_
					.get(i);
			listener.inititalize(this);
		}
	}

	public void stop() {
	}
}