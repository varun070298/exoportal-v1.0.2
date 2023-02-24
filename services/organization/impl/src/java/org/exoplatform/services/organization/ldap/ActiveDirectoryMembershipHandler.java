/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

//import netscape.ldap.LDAPDN;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.impl.MembershipImpl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class ActiveDirectoryMembershipHandler extends MembershipHandler {

	/**
	 * @param ldapService
	 * @param hibernateService
	 */
	public ActiveDirectoryMembershipHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		super(ldapService, hibernateService);
	}

	public Membership findMembershipByUserGroupAndType(String userName,
			String groupId, String type) throws Exception {
		MembershipImpl membership = null;
		String groupDN = this.getGroupDNFromGroupId(groupId);
		ArrayList memberships = (ArrayList)this.findMemberships(userName, groupDN, type);
		if (memberships.size() > 0){
			membership = (MembershipImpl)memberships.get(0);
		}
		return membership;
	}
	
	public Collection findMembershipsByUser(String userName) throws Exception {
		Collection memberships = new ArrayList();
		memberships = (ArrayList)this.findMemberships(userName, null, null);
		return memberships;
	}
	
	public Collection findMembershipsByUserAndGroup(String userName,
			String groupId) throws Exception {
		ArrayList memberships = new ArrayList();
		String groupDN = this.getGroupDNFromGroupId(groupId);
		memberships = (ArrayList)this.findMemberships(userName, groupDN, null);
		return memberships;
	}
	
	private Collection findMemberships(String userName, String groupId, String type) throws Exception {
		LdapContext ctx = null;
		Collection memberships = new ArrayList();
		try {
			ctx = ldapService.getLdapContext();
			String userDN = this.getDNFromUsername(userName);

			NameParser parser = ctx.getNameParser("");
			Name searchBase = parser.parse(userDN);
			
			String filter = (String) OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
			String retAttrs[] = {"tokenGroups"};
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.OBJECT_SCOPE);
			constraints.setReturningAttributes(retAttrs);

			NamingEnumeration results = ctx.search(searchBase, filter, constraints);
			while (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				Attributes attrs = sr.getAttributes();
				Attribute attr = attrs.get("tokenGroups");
				for (int x = 0; x < attr.size(); x++){
					byte[] SID = (byte[])attr.get(x);
					String membershipDN = this.findMembershipDNBySID(SID, groupId, type);
					if (membershipDN != null){
						Group group = this.getGroupFromMembershipDN(membershipDN);
						if (type == null){
							type = this.explodeDN(parser.parse(membershipDN), true)[0];
						}
						MembershipImpl membership = new MembershipImpl();
						membership.setId(userName + "," + type + "," + group.getId());
						membership.setUserName(userName);
						membership.setMembershipType(type);
						membership.setGroupId(group.getId());
						memberships.add(membership);
					}
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return memberships;
	}
	
	private String findMembershipDNBySID(byte[] sid, String baseDN, String scopedRole) throws Exception {
		LdapContext ctx = null;
		String dn = null;
		try {
			ctx = ldapService.getLdapContext();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String attrList[] = { "" };
			constraints.setReturningAttributes(attrList);
			constraints.setDerefLinkFlag(true);
			String filter = null;
			if (baseDN == null){
				baseDN = (String) OrganizationServiceImpl.properties.get("ldap.groups.url");
			}
			NamingEnumeration answer;
			if (scopedRole == null){
				answer = ctx.search(baseDN, "objectSid={0}", new Object[] {sid} , constraints);
			} else {
				answer = ctx.search(baseDN, "(& (objectSid={0}) ("+ (String) OrganizationServiceImpl.properties.get("ldap.role.name.attribute") + "={1}))", new Object[] {sid, scopedRole} , constraints);
			}
			while (answer.hasMore()) {
				SearchResult sr = (SearchResult) answer.next();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName()).get(0));
				dn = entryName + "," + baseDN;
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return dn;
	}
}
