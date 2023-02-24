/*
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

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Group;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/
public class ActiveDirectoryGroupHandler extends GroupHandler {

	/**
	 * @param ldapService
	 * @param hibernateService
	 */
	public ActiveDirectoryGroupHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		super(ldapService, hibernateService);
	}
	
	public Collection findGroupByMembership(String userName,
			String membershipType) throws Exception {
		Collection groups = new ArrayList();
		groups = this.findGroups(userName, membershipType);
		return groups;
	}

	public Collection findGroupsOfUser(String userName) throws Exception {
		LdapContext ctx = null;
		Collection groups = new ArrayList();
		groups = this.findGroups(userName, null);
		return groups;
	}
	
	private Collection findGroups(String userName, String type) throws Exception {
		LdapContext ctx = null;
		Collection groups = new ArrayList();
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
					String membershipDN = this.findMembershipDNBySID(SID, type);
					if (membershipDN != null){
						Group group = this.getGroupFromMembershipDN(membershipDN);
						if (group != null){
							groups.add(group);
						}
					}
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return groups;
	}
	
	private String findMembershipDNBySID(byte[] sid, String scopedRole) throws Exception {
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
			String baseDN = (String) OrganizationServiceImpl.properties.get("ldap.groups.url");
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
