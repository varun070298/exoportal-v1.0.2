/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class MembershipTypeHandler extends BaseHandler {
	private LDAPService ldapService;

	public MembershipTypeHandler(
		LDAPService ldapService) {
		this.ldapService = ldapService;
	}

	public MembershipType createMembershipType(MembershipType mt)
		throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String membershipTypeDN = (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute") + "=" + mt.getName() + "," + (String)OrganizationServiceImpl.properties.get("ldap.memberships.url");
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipTypeDN);
				Attributes attrs = ctx.getAttributes(dn);
			} catch (NameNotFoundException e){
				Date now = new Date();
				mt.setCreatedDate(now);
				mt.setModifiedDate(now);
				
				// Group does not exist.
				BasicAttributes attributes = new BasicAttributes();

				// create objectclass attributes
	            BasicAttribute ocs = new BasicAttribute("objectclass");
	            ocs.add("top");
	            ocs.add((String)OrganizationServiceImpl.properties.get("ldap.membershiptype.objectclass"));
	            attributes.put(ocs);

	            // create cn attribute
	            BasicAttribute cn = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute"), mt.getName());
	            attributes.put(cn);

	            // create description attribute
	            String desc = mt.getDescription();
	            if (desc != null && desc.length() > 0){
		            BasicAttribute description = new BasicAttribute("description", mt.getDescription());
		            attributes.put(description);
	            }

	            ctx.createSubcontext(membershipTypeDN, attributes);
			}
		} finally {
			ctx.close();
		}
		return mt;
	}
	
	protected void createMembershipTypeEntry(MembershipType mt)throws Exception {
		this.createMembershipType(mt);
	}

	public MembershipType saveMembershipType(MembershipType mt)
		throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String membershipTypeDN = (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute") + "=" + mt.getName() + "," + (String)OrganizationServiceImpl.properties.get("ldap.memberships.url");
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipTypeDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null){
					ModificationItem[] mods = new ModificationItem[1];
					String desc = mt.getDescription();
					if (desc != null && desc.length() > 0){
						mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("description" , mt.getDescription()));
					} else {
						mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("description" , mt.getDescription()));
					}
					ctx.modifyAttributes(membershipTypeDN, mods);	
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
		return mt;
	}

	public MembershipType findMembershipType(String name) throws Exception {
		LdapContext ctx = null;
		MembershipTypeImpl membershipTypeImpl = null;
		try {
			ctx = ldapService.getLdapContext();
			String membershipTypeDN = (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute") + "=" + name + "," + (String)OrganizationServiceImpl.properties.get("ldap.memberships.url");
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipTypeDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null){
					membershipTypeImpl = new MembershipTypeImpl();
					membershipTypeImpl.setName(name);
					membershipTypeImpl.setDescription(this.getAttribute(attrs, "description"));
					membershipTypeImpl.setCreatedDate(new Date());
					membershipTypeImpl.setModifiedDate(new Date());
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
		return membershipTypeImpl;
	}

	public MembershipType removeMembershipType(String name) throws Exception {
		LdapContext ctx = null;
		MembershipType m = null;
		try {
			ctx = ldapService.getLdapContext();
			String membershipTypeDN = (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute") + "=" + name + "," + (String)OrganizationServiceImpl.properties.get("ldap.memberships.url");
			try {
				NameParser parser = ctx.getNameParser("");
				Name dn = parser.parse(membershipTypeDN);
				Attributes attrs = ctx.getAttributes(dn);
				if (attrs != null){
					m = new MembershipTypeImpl();
					m.setName(name);
					m.setDescription(this.getAttribute(attrs, "description"));
					m.setCreatedDate(new Date());
					m.setModifiedDate(new Date());
					ctx.destroySubcontext(membershipTypeDN);
				}
			} catch (NameNotFoundException e){
				
			}
		} finally {
			ctx.close();
		}
		return m;
	}

	/* used  by the importer / exporter */
	protected void removeMembershipTypeEntry(String name) throws Exception {
		this.removeMembershipType(name);
	}

	public Collection findMembershipTypes() throws Exception {
		LdapContext ctx = null;
		Collection memberships = new ArrayList();
		try {
			ctx = ldapService.getLdapContext();
			String searchBase = (String)OrganizationServiceImpl.properties.get("ldap.memberships.url");
			String filter = (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute") + "=*";
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(searchBase, filter, constraints);
			while (results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				Attributes attrs = sr.getAttributes();
				NameParser parser = ctx.getNameParser("");
				Name entryName = parser.parse(new CompositeName(sr.getName()).get(0));
				String membershipDN = entryName + "," + searchBase;
				MembershipTypeImpl m = new MembershipTypeImpl();
				m.setName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.membershiptype.name.attribute")));
				m.setDescription(this.getAttribute(attrs, "description"));
				m.setCreatedDate(new Date());
				m.setModifiedDate(new Date());
				memberships.add(m);
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return memberships;
	}
}