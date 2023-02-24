/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import net.sf.hibernate.Session;
//import netscape.ldap.LDAPDN;

import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.impl.MembershipImpl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class MembershipHandler extends BaseHandler {

	protected HibernateService hibernateService;

	protected LDAPService ldapService;

	protected List listeners_;

	public MembershipHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		this.ldapService = ldapService;
		super.ldapService = ldapService;
		this.hibernateService = hibernateService;
		listeners_ = new ListenerStack(5);
	}

	public void addMembershipEventListener(MembershipEventListener listener) {
		listeners_.add(listener);
	}

	public void createMembership(Membership m) throws Exception {
		this.createMembership(m, true);
	}

	private void createMembership(Membership m, boolean fireEvents)
			throws Exception {
		LdapContext ctx = null;
		Session session = null;
		boolean groupExists = false;
		boolean membershipExists = false;
		
		String groupId = m.getGroupId();
		String membershipType = m.getMembershipType();
		String username = m.getUserName();
		if (groupId != null && groupId.length() > 0 &&
				membershipType != null && membershipType.length() > 0 &&
				username != null && username.length() > 0) {
			
			try {
				ctx = ldapService.getLdapContext();
				session = hibernateService.openSession();
	
				String userDN = this.getDNFromUsername(m.getUserName());
				String groupDN = this.getGroupDNFromGroupId(m.getGroupId());
	
				String membershipDN = (String) OrganizationServiceImpl.properties
						.get("ldap.membershiptype.name.attribute")
						+ "=" + m.getMembershipType() + "," + groupDN;
					
				try {
					NameParser parser = ctx.getNameParser("");
					Name dn = parser.parse(membershipDN);
					Attributes attrs = ctx.getAttributes(dn);
					if (attrs != null) {
						// Group does exist, is userDN in it?
						List members = this.getAttributes(attrs,
								(String) OrganizationServiceImpl.properties
										.get("ldap.group.member.attribute"));
						if (!members.contains(userDN)) {
							ModificationItem[] mods = new ModificationItem[1];
							mods[0] = new ModificationItem(
									DirContext.ADD_ATTRIBUTE,
									new BasicAttribute(
											(String) OrganizationServiceImpl.properties
													.get("ldap.group.member.attribute"),
											userDN));
							if (fireEvents){
								preSave(m, true, session);
								ctx.modifyAttributes(membershipDN, mods);
								postSave(m, true, session);
								session.flush();
							} else {
								ctx.modifyAttributes(membershipDN, mods);
							}
						}
					}
				} catch (NameNotFoundException e) {
					// Group does not exist.
					BasicAttributes attributes = new BasicAttributes();
	
					// create objectclass attributes
					BasicAttribute ocs = new BasicAttribute("objectclass");
					ocs.add("top");
					ocs.add((String) OrganizationServiceImpl.properties
							.get("ldap.role.objectclass"));
					attributes.put(ocs);
	
					// create cn attribute
					BasicAttribute cn = new BasicAttribute(
							(String) OrganizationServiceImpl.properties
									.get("ldap.role.name.attribute"), m
									.getMembershipType());
					attributes.put(cn);
	
					// create description attribute
					BasicAttribute member = new BasicAttribute(
							(String) OrganizationServiceImpl.properties
									.get("ldap.group.member.attribute"), userDN);
					attributes.put(member);
	
					if (fireEvents){
						preSave(m, true, session);
						ctx.createSubcontext(membershipDN, attributes);
						postSave(m, true, session);
						session.flush();
					} else {
						ctx.createSubcontext(membershipDN, attributes);
					}
				}
			} finally {
				if (fireEvents){
					hibernateService.closeSession(session);
				}
				ctx.close();
			}
		}
	}

	public void createMembershipEntries(Collection c)
			throws Exception {
		Iterator i = c.iterator();
		while (i.hasNext()) {
			Membership impl = (Membership) i.next();
			this.createMembership(impl, false);
		}
	}

	public Membership findMembership(String id) throws Exception {
		String membershipParts[] = id.split(",");
		Membership membership = this.findMembershipByUserGroupAndType(
				membershipParts[0], membershipParts[2], membershipParts[1]);
		return membership;
	}

	public Membership findMembershipByUserGroupAndType(String userName,
			String groupId, String type) throws Exception {
		LdapContext ctx = null;
		boolean membershipFound = false;

		MembershipImpl membership = new MembershipImpl();
		membership.setGroupId(groupId);
		membership.setUserName(userName);
		membership.setMembershipType(type);
		membership.setId(userName + "," + type + "," + groupId);

		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);
			String groupDN = this.getGroupDNFromGroupId(groupId);
			String membershipDN = (String) OrganizationServiceImpl.properties
					.get("ldap.role.name.attribute")
					+ "=" + type + "," + groupDN;

			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null) {
					List members = this.getAttributes(attrs,
							(String) OrganizationServiceImpl.properties
									.get("ldap.group.member.attribute"));
					if (members.contains(userDN)) {
						membershipFound = true;
					}
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
		if (!membershipFound) {
			return null;
		} else {
			return membership;
		}
	}

	public Collection findMembershipsByGroup(Group group) throws Exception {
		Collection memberships = this.findMembershipsByUserAndGroup(null, group
				.getId());
		return memberships;
	}

	public Collection findMembershipsByUser(String userName) throws Exception {
		LdapContext ctx = null;
		Collection memberships = new ArrayList();
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
					String membershipDN = entryName + "," + searchBase;
					Group group = this.getGroupFromMembershipDN(membershipDN);
					String type = this.explodeDN(parser.parse(membershipDN), true)[0];
					MembershipImpl membership = new MembershipImpl();
					membership.setId(userName + "," + type + "," + group.getId());
					membership.setUserName(userName);
					membership.setMembershipType(type);
					membership.setGroupId(group.getId());
					memberships.add(membership);
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return memberships;
	}

	public Collection findMembershipsByUserAndGroup(String userName,
			String groupId) throws Exception {
		LdapContext ctx = null;
		ArrayList memberships = new ArrayList();

		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);
			String groupDN = this.getGroupDNFromGroupId(groupId);

			String searchBase = groupDN;
			String filter = (String) OrganizationServiceImpl.properties
					.get("ldap.group.member.attribute")
					+ "=" + this.escapeDN(userDN);

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter,
					constraints);
			if (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName())
						.get(0));
				String membershipDN = entryName + "," + searchBase;
				String type = this.explodeDN(parser.parse(membershipDN), true)[0];
				MembershipImpl membership = new MembershipImpl();
				membership.setId(userName + "," + type + "," + groupId);
				membership.setUserName(userName);
				membership.setMembershipType(type);
				membership.setGroupId(groupId);
				memberships.add(membership);
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return memberships;
	}

	public void linkMembership(String userName, Group g, Membership m)
			throws Exception {
		MembershipImpl membership = (MembershipImpl) m;
		membership.setUserName(userName);
		membership.setGroupId(g.getId());
		this.createMembership(membership);
	}

	private void postDelete(Membership membership, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			MembershipEventListener listener = (MembershipEventListener) listeners_
					.get(i);
			listener.postDelete(membership, xresources);
		}
	}

	private void postSave(Membership membership, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			MembershipEventListener listener = (MembershipEventListener) listeners_
					.get(i);
			listener.postSave(membership, isNew, xresources);
		}
	}

	private void preDelete(Membership membership, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			MembershipEventListener listener = (MembershipEventListener) listeners_
					.get(i);
			listener.preDelete(membership, xresources);
		}
	}

	private void preSave(Membership membership, boolean isNew, Session session)
			throws Exception {
		XResources xresources = new XResources();
		xresources.addResource(Session.class, session);
		for (int i = 0; i < listeners_.size(); i++) {
			MembershipEventListener listener = (MembershipEventListener) listeners_
					.get(i);
			listener.preSave(membership, isNew, xresources);
		}
	}

	public Membership removeMembership(String id) throws Exception {
		return this.removeMembership(id, true);
	}

	private Membership removeMembership(String id, boolean fireEvents) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		MembershipImpl m = new MembershipImpl();

		String membershipParts[] = id.split(",");
		String username = membershipParts[0];
		String membershipType = membershipParts[1];
		String groupId = membershipParts[2];

		m.setGroupId(groupId);
		m.setId(id);
		m.setMembershipType(membershipType);
		m.setUserName(username);

		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();

			String userDN = this.getDNFromUsername(username);
			String groupDN = this.getGroupDNFromGroupId(groupId);
			String membershipDN = (String) OrganizationServiceImpl.properties
					.get("ldap.role.name.attribute")
					+ "=" + membershipType + ", " + groupDN;

			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null) {
					// Group does exist, is userDN in it?
					List members = this.getAttributes(attrs,
							(String) OrganizationServiceImpl.properties
									.get("ldap.group.member.attribute"));
					if (members.contains(userDN)) {
						if (members.size() > 1) {
							ModificationItem[] mods = new ModificationItem[1];
							mods[0] = new ModificationItem(
									DirContext.REMOVE_ATTRIBUTE,
									new BasicAttribute(
											(String) OrganizationServiceImpl.properties
													.get("ldap.group.member.attribute"),
											userDN));
							if (fireEvents){
								preSave(m, true, session);
								ctx.modifyAttributes(membershipDN, mods);
								postSave(m, true, session);
								session.flush();
							} else {
								ctx.modifyAttributes(membershipDN, mods);
							}
						} else {
							if (fireEvents){
								preSave(m, true, session);
								ctx.destroySubcontext(membershipDN);
								postSave(m, true, session);
								session.flush();
							} else {
								ctx.destroySubcontext(membershipDN);
							}
						}
					}
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			if (fireEvents){
				hibernateService.closeSession(session);
			}
			ctx.close();
		}
		return m;
	}
	
	protected void removeMembershipEntriesOfGroup(Group group)
			throws Exception {
		ArrayList memberships = (ArrayList)this.findMembershipsByGroup(group);
		for (int x = 0; x < memberships.size(); x++){
			Membership membership = (Membership)memberships.get(x);
			this.removeMembership(membership.getId(), false);
		}
	}

	protected void removeMembershipEntriesOfUser(String userName)
			throws Exception {
		ArrayList memberships = (ArrayList)this.findMembershipsByUser(userName);
		for (int x = 0; x < memberships.size(); x++){
			Membership membership = (Membership)memberships.get(x);
			this.removeMembership(membership.getId(), false);
		}
	}

	public void saveMembership(Membership m) throws Exception {
		/*
		 * Session session = service_.openSession(); preSave(m, false, session) ;
		 * session.update(m); postSave(m, false,session) ; session.flush();
		 */
	}
}