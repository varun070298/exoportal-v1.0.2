/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.UserImpl;

import com.sun.jndi.ldap.ctl.SortControl;
import com.sun.jndi.ldap.ctl.VirtualListViewControl;
import com.sun.jndi.ldap.ctl.VirtualListViewResponseControl;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class VLVUserPageList extends PageList {
	private String searchBase;
	private String filter;
	private LDAPService ldapService;

	public VLVUserPageList(LDAPService ldapService, int pageSize) throws Exception {
		super(pageSize);
		this.ldapService = ldapService;
		this.setAvailablePage(this.getResultSize());
	}

	public VLVUserPageList(LDAPService ldapService, int pageSize, String searchBase, String filter) throws Exception {
		super(pageSize);
		this.ldapService = ldapService;
		this.searchBase = searchBase;
		this.filter = filter;
		this.setAvailablePage(this.getResultSize());
	}
	
	protected void populateCurrentPage(int page) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			int pageSize = this.getPageSize();
			int offSet = 1;
			if (page > 1){
				offSet = ((page * pageSize) - pageSize + 2);
			}
			List users = new ArrayList();
			
			VirtualListViewControl vctl = new VirtualListViewControl(
					offSet, 0, 0, pageSize, Control.CRITICAL);
			
			String keys[] = { (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") };
			SortControl sctl = new SortControl(keys, Control.CRITICAL);

			// Set context's request controls
			ctx.setRequestControls(new Control[] { sctl, vctl });
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration results = ctx.search(this.searchBase, filter, constraints);
			if (results != null) {
				while (results.hasMoreElements()) {
					SearchResult result = (SearchResult) results.next();
					Attributes attrs = result.getAttributes();
					// Create user object and add to arraylist
					User user = new UserImpl();
					user.setUserName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
					user.setFirstName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
					user.setLastName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute")));
					user.setEmail(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
					user.setCreatedDate(new Date());
					user.setLastLoginTime(new Date());
					user.setPassword("PASSWORD");
					users.add(user);
				}
				Control[] responseControls = ctx.getResponseControls();
				for (int z = 0; z < responseControls.length; z++) {
					if (responseControls[z] instanceof VirtualListViewResponseControl) {
						VirtualListViewResponseControl vlvResponse = (VirtualListViewResponseControl)responseControls[z];
						//this.setAvailablePage(vlvResponse.getListSize());
					}
				}
			}
			this.currentListPage_ = users;
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
	}

	public List getAll() throws Exception {
		System.out.println("test");
		return null;
	}

	private int getResultSize() throws Exception {
		LdapContext ctx = null;
		int resultSize = 0;
		List users = new ArrayList();
		try {
			ctx = ldapService.getLdapContext();
			int pageSize = this.getPageSize();
			
			VirtualListViewControl vctl = new VirtualListViewControl(
					1, 0, 0, pageSize, Control.CRITICAL);
			
			String keys[] = { (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") };
			SortControl sctl = new SortControl(keys, Control.CRITICAL);

			// Set context's request controls
			ctx.setRequestControls(new Control[] { sctl, vctl });
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration results = ctx.search(this.searchBase, this.filter, constraints);
			if (results != null) {
				while (results.hasMoreElements()) {
					SearchResult result = (SearchResult) results.next();
					Attributes attrs = result.getAttributes();
					// Create user object and add to arraylist
					User user = new UserImpl();
					user.setUserName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
					user.setFirstName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
					user.setLastName(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.lasttname.attribute")));
					user.setEmail(this.getAttribute(attrs, (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
					user.setCreatedDate(new Date());
					user.setLastLoginTime(new Date());
					user.setPassword("PASSWORD");
					users.add(user);
				}
				Control[] responseControls = ctx.getResponseControls();
				if (responseControls != null){
					for (int z = 0; z < responseControls.length; z++) {
						if (responseControls[z] instanceof VirtualListViewResponseControl) {
							VirtualListViewResponseControl vlvResponse = (VirtualListViewResponseControl)responseControls[z];
							resultSize = vlvResponse.getListSize();
						}
					}
				}
			}
		} catch (PartialResultException e){
			
		} finally {
			ctx.close();
		}
		return resultSize;
	}
	
	private String getAttribute(Attributes attributes, String attribute) {
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
}
