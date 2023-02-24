/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.Query;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class VLVUserHandler extends UserHandler {

	public VLVUserHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		super(ldapService, hibernateService);
	}

	public PageList findUsers(Query q) throws Exception {
		int pageSize = 20;
		String filter = null;
		ArrayList list = new ArrayList();
		if (q.getUserName() != null && q.getUserName().length() > 0) {
			list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute") + "=" + q.getUserName() + ")");
		}
		if (q.getFirstName() != null && q.getFirstName().length() > 0) {
			list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute") + "=" + q.getFirstName() + ")");
		}
		if (q.getLastName() != null && q.getLastName().length() > 0) {
			list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute") + "=" + q.getLastName() + ")");
		}
		if (q.getEmail() != null && q.getEmail().length() > 0) {
			list.add("(" + (String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute") + "=" + q.getEmail() + ")");
		}
		
		if (list.size() > 0){
			StringBuffer buffer = new StringBuffer();
			buffer.append("(&");
			if (list.size() > 1){
				for (int x = 0; x < list.size(); x++){
					if (x == (list.size() - 1)){
						buffer.append(list.get(x));
					} else {
						buffer.append(list.get(x)+" || ");
					}
				}
			} else {
				buffer.append(list.get(0));
			}
			buffer.append(" (" +(String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter") + ") )");
			filter = buffer.toString();
		} else {
			filter = (String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
		}
		String searchBase = (String)OrganizationServiceImpl.properties.get("ldap.base.url");
			
		return new VLVUserPageList(ldapService, pageSize, searchBase, filter);
	}
	
	public PageList getUserPageList(int pageSize) throws Exception {
		String filter = (String)OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
		String searchBase = (String)OrganizationServiceImpl.properties.get("ldap.base.url");
		return new VLVUserPageList(ldapService, pageSize, searchBase, filter);
	}
}
