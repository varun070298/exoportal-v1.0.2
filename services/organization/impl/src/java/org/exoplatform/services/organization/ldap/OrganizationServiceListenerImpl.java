/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.Iterator;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.PartialResultException;
import javax.naming.ldap.LdapContext;

import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationServiceListener;
import org.exoplatform.services.organization.User;
import org.picocontainer.Startable;

/**
 * Jul 20, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: OrganizationServiceListenerImpl.java,v 1.5 2004/10/28 15:36:43
 *           tuan08 Exp $
 */
public class OrganizationServiceListenerImpl extends
		OrganizationServiceListener implements Startable {

	private OrganizationConfig config_;
	private LDAPService ldapService;
	
	public OrganizationServiceListenerImpl(OrganizationService orgService,
			ConfigurationManager cService,
			LDAPService ldapService) throws Exception {
		this.ldapService = ldapService;
		//PageList users = orgService.getUserPageList(10);
		// if(users.getAvailable() > 0) return ; //Not new database
		orgService.addListener(this);
		ServiceConfiguration sconf = cService
				.getServiceConfiguration(getClass());
		config_ = (OrganizationConfig) sconf.getObjectParam(
				"organization.configuration").getObject();
	}

	public void inititalize(OrganizationService service) {
		try {
			initializeDirectory();
			createGroups(service);
			createMembershipTypes(service);
			createUsers(service);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initializeDirectory() throws Exception {
		LdapContext ctx = null;
		Attributes attrs = null;
		BasicAttributes attributes;
		try {
			ctx = ldapService.getLdapContext();
			// Create user base exo created users
			String userBaseDN = (String) OrganizationServiceImpl.properties.get("ldap.users.url");
			try {
				attrs = ctx.getAttributes(userBaseDN);
			} catch (NameNotFoundException e) {
				// try to create user base ou
				attributes = new BasicAttributes();
				// create objectclass attributes
				BasicAttribute ocs = new BasicAttribute("objectclass");
				ocs.add("top");
				ocs.add("organizationalUnit");
				attributes.put(ocs);
				// create ou attribute
				BasicAttribute ou = new BasicAttribute("ou", "users");
				attributes.put(ou);
				// create description attribute
				BasicAttribute description = new BasicAttribute("description", "container for users created by eXo");
				attributes.put(description);

				ctx.createSubcontext(userBaseDN, attributes);
			} catch (PartialResultException e){
				
			}
			
			// Create group base for exo groups & memberships
			String groupBaseDN = (String) OrganizationServiceImpl.properties.get("ldap.groups.url");
			try {
				attrs = ctx.getAttributes(groupBaseDN);
			} catch (NameNotFoundException e) {
				// try to create user base ou
				attributes = new BasicAttributes();
				// create objectclass attributes
				BasicAttribute ocs = new BasicAttribute("objectclass");
				ocs.add("top");
				ocs.add("organizationalUnit");
				attributes.put(ocs);
				// create ou attribute
				BasicAttribute ou = new BasicAttribute("ou", "groups");
				attributes.put(ou);
				// create description attribute
				BasicAttribute description = new BasicAttribute("description", "container for groups created by eXo");
				attributes.put(description);

				ctx.createSubcontext(groupBaseDN, attributes);
			} catch (PartialResultException e){
				
			}
			
			// Create membership base for exo membership types
			String membershipBaseDN = (String) OrganizationServiceImpl.properties.get("ldap.memberships.url");
			try {
				attrs = ctx.getAttributes(membershipBaseDN);
			} catch (NameNotFoundException e) {
				// try to create user base ou
				attributes = new BasicAttributes();
				// create objectclass attributes
				BasicAttribute ocs = new BasicAttribute("objectclass");
				ocs.add("top");
				ocs.add("organizationalUnit");
				attributes.put(ocs);
				// create ou attribute
				BasicAttribute ou = new BasicAttribute("ou", "memberships");
				attributes.put(ou);
				// create description attribute
				BasicAttribute description = new BasicAttribute("description", "container for memberships created by eXo");
				attributes.put(description);

				ctx.createSubcontext(membershipBaseDN, attributes);
			} catch (PartialResultException e){
				
			}
		} finally {
			ctx.close();
		}
		
	}
	
	private void createGroups(OrganizationService orgService) throws Exception {
		List groups = config_.getGroup();
		for (int i = 0; i < groups.size(); i++) {
			OrganizationConfig.Group data = (OrganizationConfig.Group) groups
					.get(i);
			Group group = orgService.createGroupInstance();
			group.setGroupName(data.getName());
			group.setDescription(data.getDescription());
			String parentId = data.getParentId();
			if (parentId == null || parentId.length() == 0) {
				orgService.createGroup(group);
			} else {
				Group parentGroup = orgService.findGroupById(parentId);
				orgService.addChild(parentGroup, group);
			}
		}
	}

	private void createMembershipTypes(OrganizationService orgService)
			throws Exception {
		List types = config_.getMembershipType();
		for (int i = 0; i < types.size(); i++) {
			OrganizationConfig.MembershipType data = (OrganizationConfig.MembershipType) types
					.get(i);
			MembershipType type = orgService.createMembershipTypeInstance();
			type.setName(data.getType());
			type.setDescription(data.getDescription());
			orgService.createMembershipType(type);
		}
	}

	private void createUsers(OrganizationService service) throws Exception {
		List users = config_.getUser();
		for (int i = 0; i < users.size(); i++) {
			OrganizationConfig.User data = (OrganizationConfig.User) users
					.get(i);
			User user = service.createUserInstance();
			user.setUserName(data.getUserName());
			user.setPassword(data.getPassword());
			user.setFirstName(data.getFirstName());
			user.setLastName(data.getLastName());
			user.setEmail(data.getEmail());
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

	public void start() {
	}

	public void stop() {
	}
}