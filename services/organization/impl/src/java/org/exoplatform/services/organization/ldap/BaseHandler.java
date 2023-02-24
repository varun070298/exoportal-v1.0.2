/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.impl.GroupImpl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public abstract class BaseHandler {

	protected LDAPService ldapService;

	protected String getAttribute(Attributes attributes, String attribute) {
		String attrString = "";
		try {
			if (attributes != null) {
				Attribute attr = attributes.get(attribute);
				if (attr != null) {
					attrString = (String) attr.get();
				}
			}
		} catch (Exception e) {

		}
		return attrString;
	}

	protected List getAttributes(Attributes attributes, String attribute) {
		List results = new ArrayList();
		try {
			if (attributes != null) {
				Attribute attr = attributes.get(attribute);
				for (int x = 0; x < attr.size(); x++) {
					results.add(attr.get(x));
				}
			}
		} catch (Exception e) {

		}
		return results;
	}

	protected String getDNFromUsername(String username) throws Exception {
		String dn = null;
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String baseDN = (String) OrganizationServiceImpl.properties
					.get("ldap.base.url");
			String userIdAttribute = (String) OrganizationServiceImpl.properties
					.get("ldap.user.username.attribute");

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String attrList[] = { "" };
			constraints.setReturningAttributes(attrList);
			constraints.setDerefLinkFlag(true);
			String filter = null;
			filter = "(" + userIdAttribute + "=" + username + ")";
			NamingEnumeration answer = ctx.search(baseDN, filter, constraints);
			while (answer.hasMore()) {
				SearchResult sr = (SearchResult) answer.next();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName())
						.get(0));
				dn = entryName + "," + baseDN;
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return dn;
	}

	protected GroupImpl getGroupByDN(String groupDN) throws Exception {
		LdapContext ctx = null;
		GroupImpl group = null;
		try {
			ctx = ldapService.getLdapContext();
			NameParser parser = ctx.getNameParser("");
			String groupBaseDN = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			StringBuffer idBuffer = new StringBuffer();
			String parentId = null;
			String baseParts[] = this.explodeDN(parser.parse(groupBaseDN), true);
			String membershipParts[] = this.explodeDN(parser.parse(groupDN), true);
			for (int x = (membershipParts.length - baseParts.length - 1); x > -1; x--) {
				idBuffer.append("/" + membershipParts[x]);
				if (x == 1) {
					parentId = idBuffer.toString();
				}
			}
			if (idBuffer != null) {
				Attributes attrs = ctx.getAttributes(groupDN);
				group = new GroupImpl();
				group.setGroupName(membershipParts[0]);
				group.setId(idBuffer.toString());
				group.setDescription(this.getAttribute(attrs, "description"));
				group.setParentId(parentId);
			}
		} finally {
			ctx.close();
		}
		return group;
	}

	protected GroupImpl getGroupFromMembershipDN(String membershipDN)
			throws Exception {
		GroupImpl group = null;
		LdapContext ctx = null;

		try {
			ctx = ldapService.getLdapContext();
			NameParser parser = ctx.getNameParser("");
			String membershipParts[] = this.explodeDN(parser.parse(membershipDN), false);

			StringBuffer buffer = new StringBuffer();
			for (int x = 1; x < membershipParts.length; x++) {
				if (x == membershipParts.length - 1) {
					buffer.append(membershipParts[x]);
				} else {
					buffer.append(membershipParts[x] + ",");
				}
			}
			group = this.getGroupByDN(buffer.toString());
		} finally {
			ctx.close();
		}
		return group;
	}

	protected GroupImpl getGroupFromGroupDN(String groupDN) throws Exception {
		GroupImpl group = null;
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			NameParser parser = ctx.getNameParser("");
			String SEARCHBASE_DN = (String) OrganizationServiceImpl.properties
					.get("ldap.groups.url");
			StringBuffer idBuffer = new StringBuffer();
			String parentId = null;
			String baseParts[] = this.explodeDN(parser.parse(SEARCHBASE_DN), true);
			String membershipParts[] = this.explodeDN(parser.parse(groupDN), true);
	
			for (int x = (membershipParts.length - baseParts.length - 1); x > -1; x--) {
				idBuffer.append("/" + membershipParts[x]);
				if (x == 1) {
					parentId = idBuffer.toString();
				}
			}
			if (idBuffer != null) {
				group = new GroupImpl();
				group.setGroupName(membershipParts[0]);
				group.setId(idBuffer.toString());
				group.setParentId(parentId);
			}
		} finally {
			ctx.close();
		}
		return group;
	}

	protected String getGroupDNFromGroupId(String groupId) {
		String groupBase = (String) OrganizationServiceImpl.properties
				.get("ldap.groups.url");
		StringBuffer buffer = new StringBuffer();
		String groupParts[] = groupId.split("/");

		for (int x = (groupParts.length - 1); x > 0; x--) {
			buffer.append("ou=" + groupParts[x] + ", ");
		}
		buffer.append(groupBase);

		return buffer.toString();
	}

	protected String escapeDN(String dn) {
		StringBuffer buf = new StringBuffer(dn.length());
		for (int i = 0; i < dn.length(); i++) {
			char c = dn.charAt(i);
			switch (c) {
			case '\\':
				buf.append("\\5c");
				break;
			case '*':
				buf.append("\\2a");
				break;
			case '(':
				buf.append("\\28");
				break;
			case ')':
				buf.append("\\29");
				break;
			case '\0':
				buf.append("\\00");
				break;
			default:
				buf.append(c);
				break;
			}
		}
		return buf.toString();
	}

	protected String[] explodeDN(Name dn, boolean removeTypes){
		String explodedDN[] = null;
		
		Enumeration enumeration = dn.getAll();
		List list = new ArrayList();
		while(enumeration.hasMoreElements()){
			String ldap = (String)enumeration.nextElement();
			if (removeTypes){
				int position = ldap.indexOf("=");
				String value = ldap.substring(position + 1);
				list.add(0, value);
			} else {
				list.add(0, ldap);
			}
		}
		explodedDN = new String[list.size()];
		list.toArray(explodedDN);
		return explodedDN;
	}
	
}
