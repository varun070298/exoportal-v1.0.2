/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import net.sf.hibernate.Session;

import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class GroupHandler extends BaseHandler {
	protected HibernateService hibernateService;

	protected LDAPService ldapService;

	protected List listeners_;

	public GroupHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		this.ldapService = ldapService;
		super.ldapService = ldapService;
		this.hibernateService = hibernateService;
		listeners_ = new ListenerStack(5);
	}

	// Add a child to an existing group
	public void addChild(Group parent, Group child) throws Exception {
		this.addChild(parent, child, true);
	}

	private void addChild(Group parent, Group child, boolean fireEvents) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		try {
			ctx = ldapService.getLdapContext();
			if (fireEvents){
				session = hibernateService.openSession();
			}
			String baseDN = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			StringBuffer buffer = new StringBuffer();

			if (parent != null) {
				String dnParts[] = parent.getId().split("/");
				for (int x = (dnParts.length - 1); x > 0; x--) {
					buffer.append("ou=" + dnParts[x] + ", ");
				}
			}
			buffer.append(baseDN);
			String groupDN = "ou=" + child.getGroupName() + ", "
					+ buffer.toString();
			String searchBase = buffer.toString();
			String filter = "ou=" + child.getGroupName();

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter,
					constraints);

			// check to see if the group already exists
			if (!results.hasMore()) {
				BasicAttributes attrs = new BasicAttributes();

				// create objectclass attributes
				BasicAttribute ocs = new BasicAttribute("objectclass");
				ocs.add("top");
				ocs.add("organizationalUnit");
				attrs.put(ocs);

				// create ou attribute
				BasicAttribute ou = new BasicAttribute("ou", child
						.getGroupName());
				attrs.put(ou);

				// create description attribute
				String desc = child.getDescription();
	            if (desc != null && desc.length() > 0){
		            BasicAttribute description = new BasicAttribute("description", child.getDescription());
		            attrs.put(description);
	            }

				GroupImpl group = (GroupImpl) child;
				if (parent != null) {
					group.setId(parent.getId() + "/" + child.getGroupName());
					group.setParentId(parent.getId());
				}

				if (fireEvents){
					preSave(group, true, session);
					ctx.createSubcontext(groupDN, attrs);
					postSave(group, true, session);
					session.flush();
				} else {
					ctx.createSubcontext(groupDN, attrs);
				}

			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
			if (fireEvents){
				hibernateService.closeSession(session);
			}
		}
	}
	
	public void addGroupEventListener(GroupEventListener listener) {
		listeners_.add(listener);
	}

	// Add a new top-level group
	public void createGroup(Group group) throws Exception {
		addChild(null, group);
	}

	protected void createGroupEntry(Group group) throws Exception {
		this.addChild(null, group, false);
	}
	
	public Group findGroupById(String groupId) throws Exception {
		GroupImpl groupImpl = null;
		String parentId = null;
		String name = null;
		LdapContext ctx = null;

		if (groupId != null) {
			try {
				ctx = ldapService.getLdapContext();
				StringBuffer buffer = new StringBuffer();
				String groupIdParts[] = groupId.split("/");
				for (int x = 1; x < groupIdParts.length; x++) {
					buffer.append("/" + groupIdParts[x]);
					if (x == (groupIdParts.length - 2)) {
						parentId = buffer.toString();
					}
				}
				name = groupIdParts[groupIdParts.length - 1];

				String groupDN = this.getGroupDNFromGroupId(groupId);
				try {
					Attributes attrs = ctx.getAttributes(groupDN);
					String description = this.getAttribute(attrs, "description");
	
					groupImpl = new GroupImpl();
					groupImpl.setGroupName(name);
					groupImpl.setId(groupId);
					groupImpl.setDescription(description);
					groupImpl.setParentId(parentId);
				} catch (NameNotFoundException e){
					
				}
			} finally {
				ctx.close();
			}
		}
		return groupImpl;
	}

	public Collection findGroupByMembership(String userName,
			String membershipType) throws Exception {
		LdapContext ctx = null;
		List groups = new ArrayList();
		List groupsImpl = new ArrayList();
		Session session = null;
		String userDN = null;
		try {
			ctx = ldapService.getLdapContext();

			userDN = this.getDNFromUsername(userName);
			String searchBase = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			String filter = "(&("
					+ (String) OrganizationServiceImpl.properties
							.get("ldap.group.member.attribute")
					+ "="
					+ this.escapeDN(userDN)
					+ ")("
					+ (String) OrganizationServiceImpl.properties
							.get("ldap.role.member.attribute") + "="
					+ membershipType + "))";

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter,
					constraints);
			if (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName())
						.get(0));
				String groupDN = entryName + "," + searchBase;
				Group group = this.getGroupByDN(groupDN);
				if (group != null) {
					groups.add(group);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return groups;
	}

	public Collection findGroups(Group parent) throws Exception {
		LdapContext ctx = null;
		List groups = new ArrayList();
		Session session = null;
		String groupsBaseDN = (String) OrganizationServiceImpl.properties
				.get("ldap.groups.url");
		StringBuffer buffer = new StringBuffer();

		if (parent != null) {
			String dnParts[] = parent.getId().split("/");
			for (int x = (dnParts.length - 1); x > 0; x--) {
				buffer.append("ou=" + dnParts[x] + ", ");
			}
		}
		buffer.append(groupsBaseDN);

		try {
			ctx = ldapService.getLdapContext();
			String searchBase = buffer.toString();
			String filter = (String) OrganizationServiceImpl.properties.get("ldap.group.objectclass.filter");
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter, constraints);
			while (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				NameParser parser = ctx.getNameParser("");
				CompositeName name = new CompositeName(sr.getName());
				if (name.size() > 0){
					Name entryName = parser.parse(name.get(0));
					String groupDN = entryName + "," + searchBase;
					Group group = this.getGroupByDN(groupDN);
					if (group != null)
						groups.add(group);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return groups;
	}

	public Collection findGroupsOfUser(String userName) throws Exception {
		LdapContext ctx = null;
		List groups = new ArrayList();
		Session session = null;

		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);

			String searchBase = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			String filter = (String) OrganizationServiceImpl.properties
					.get("ldap.group.member.attribute")
					+ "=" + this.escapeDN(userDN);
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter, constraints);
			while (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				NameParser parser = ctx.getNameParser("");
				CompositeName name = new CompositeName(sr.getName());
				if (name.size() > 0){
					Name entryName = parser.parse(name.get(0));
					String groupDN = entryName + "," + searchBase;
					Group group = this.getGroupFromMembershipDN(groupDN);
					if (group != null) {
						groups.add(group);
					}
				}
			}
		} catch (PartialResultException e){
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			ctx.close();
		}
		return groups;
	}

	private void postDelete(Group group, Session session) throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			GroupEventListener listener = (GroupEventListener) listeners_
					.get(i);
			listener.postDelete(group, xresources);
		}
	}

	private void postSave(Group group, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			GroupEventListener listener = (GroupEventListener) listeners_
					.get(i);
			listener.postSave(group, isNew, xresources);
		}
	}

	private void preDelete(Group group, Session session) throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			GroupEventListener listener = (GroupEventListener) listeners_
					.get(i);
			listener.preDelete(group, xresources);
		}
	}

	private void preSave(Group group, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			GroupEventListener listener = (GroupEventListener) listeners_
					.get(i);
			listener.preSave(group, isNew, xresources);
		}
	}

	public Group removeGroup(Group group) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();
			String groupBaseDN = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			String filter = "ou=" + group.getGroupName();
			StringBuffer buffer = new StringBuffer();

			if (group.getParentId() != null) {
				String dnParts[] = group.getParentId().split("/");
				for (int x = (dnParts.length - 1); x > 0; x--) {
					buffer.append("ou=" + dnParts[x] + ", ");
				}
			}
			buffer.append(groupBaseDN);
			String searchBase = buffer.toString();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter,
					constraints);
			if (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName())
						.get(0));
				String groupDN = entryName + "," + searchBase;
				group = this.getGroupByDN(groupDN);
				if (group != null) {
					preDelete(group, session);
					ctx.destroySubcontext(groupDN);
					postDelete(group, session);
					session.flush();
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			hibernateService.closeSession(session);
			ctx.close();
		}
		return group;
	}
	
	/* used by Importer / Exporter */
	protected void removeGroupEntry(String groupId)
			throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String groupDN = this.getGroupDNFromGroupId(groupId);
			try {
				Attributes attrs = ctx.getAttributes(groupDN);
				if (attrs != null){
					ctx.destroySubcontext(groupDN);
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
	}

	public void saveGroup(Group group) throws Exception {
		/*
		 * LDAPService service = null; Session session = null; GroupImpl
		 * groupImpl = (GroupImpl) group; try { service =
		 * serviceContainer_.createLDAPService(); session =
		 * service_.openSession(); String olddn = groupImpl.getId();
		 * LDAPModificationSet mods = new LDAPModificationSet(); mods.add(
		 * LDAPModification.REPLACE, new LDAPAttribute("groupname",
		 * groupImpl.getGroupName()));
		 * 
		 * preSave(group, false, session); service.modify(olddn, mods);
		 * service.rename(olddn, "uid=" + groupImpl.getGroupName(), true);
		 * postSave(group, false, session); session.flush(); } finally {
		 * service_.closeSession(session);
		 * serviceContainer_.closeLDAPService(service); }
		 */
	}
}