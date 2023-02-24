/**
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.organization.ldap;

import java.util.ArrayList;

import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import net.sf.hibernate.Session;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.organization.User;

/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james.chamberlain@gmail.com
*/

public class ActiveDirectoryUserHandler extends UserHandler {

	//	some useful constants from lmaccess.h
	private int UF_ACCOUNTDISABLE = 0x0002;
	private int UF_PASSWD_NOTREQD = 0x0020;
	private int UF_PASSWD_CANT_CHANGE = 0x0040;
	private int UF_NORMAL_ACCOUNT = 0x0200;
	private int UF_DONT_EXPIRE_PASSWD = 0x10000;
	private int UF_PASSWORD_EXPIRED = 0x800000;
	
	public ActiveDirectoryUserHandler(LDAPService ldapService,
			HibernateService hibernateService) {
		super(ldapService, hibernateService);
	}

	protected void createUserEntry(User user) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = ldapService.getLdapContext();
			String userDN = "cn="+user.getUserName()+","+(String)OrganizationServiceImpl.properties.get("ldap.users.url");
			if (userDN != null){
				NameParser parser = ctx.getNameParser("");
				Name name = parser.parse(userDN);
				BasicAttributes attrs = new BasicAttributes();
				
				// create objectclasses
				BasicAttribute oc = new BasicAttribute("objectClass");
				oc.add("top");
				oc.add("person");
				oc.add("organizationalPerson");
				oc.add("user");
				attrs.put(oc);
				
				// create cn
				BasicAttribute cn = new BasicAttribute("cn", user.getUserName());
				attrs.put(cn);
				
				// create displayName
				BasicAttribute displayName = new BasicAttribute("displayName", user.getFullName());
				attrs.put(displayName);
				
				// create account
				BasicAttribute account = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute"), user.getUserName());
				attrs.put(account);
				
				// create lastname
				BasicAttribute lastName = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute"), user.getLastName());
				attrs.put(lastName);
				
				// create firstname
				BasicAttribute firstName = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute"), user.getFirstName());
				attrs.put(firstName);
				
				// create mail
				BasicAttribute mail = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute"), user.getEmail());
				attrs.put(mail);
				
				// create description
				BasicAttribute description  = new BasicAttribute("description", "Account for "+user.getFullName());
				attrs.put(description);
				
				// create password
				//BasicAttribute password = new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.password.attribute"), encryptPassword(user.getPassword()));
				//attrs.put(password);
				
				// create userAccountControl - create the account disabled so it doesn't need a password
				BasicAttribute userAccountControl = new BasicAttribute("userAccountControl",Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_NOTREQD + UF_PASSWORD_EXPIRED+ UF_ACCOUNTDISABLE));
				attrs.put(userAccountControl);
				
				// Create user in directory
				ctx.createSubcontext(name, attrs);
				
				ModificationItem[] mods = new ModificationItem[2];
	 
				mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.password.attribute"), encryptPassword(user.getPassword())));
				mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",Integer.toString(UF_NORMAL_ACCOUNT)));
			
				// Perform the update
				ctx.modifyAttributes(name, mods);
			}
		} finally {
			ctx.close();
		}
	}

	public void saveUser(User user) throws Exception {
		LdapContext ctx = null;
		Session session = null;
		try {
			ctx = ldapService.getLdapContext();
			session = hibernateService.openSession();
			String userDN = this.getDNFromUsername(user.getUserName());
			if (userDN != null){
				User existingUser = this.findUserByDN(userDN, ctx);
				
				NameParser parser = ctx.getNameParser("");
				Name name = parser.parse(userDN);

				ArrayList modifications = new ArrayList();
				
				// update displayName & description
				if (!user.getFullName().equals(existingUser.getFullName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("displayName", user.getFullName()));
					modifications.add(mod);
					mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("description", "Account for "+user.getFullName()));
					modifications.add(mod);
				}
				
				// update account name
				if (!user.getUserName().equals(existingUser.getUserName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.username.attribute"), user.getUserName()));
					modifications.add(mod);
				}
				
				// update last name
				if (!user.getLastName().equals(existingUser.getLastName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute"), user.getLastName()));
					modifications.add(mod);
				}
				
				// update first name
				if (!user.getFirstName().equals(existingUser.getFirstName())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute"), user.getFirstName()));
					modifications.add(mod);
				}
				
				// update email
				if (!user.getEmail().equals(existingUser.getEmail())){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.mail.attribute"), user.getEmail()));
					modifications.add(mod);
				}
				
				if (!user.getPassword().equals("PASSWORD")){
					ModificationItem mod = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute((String)OrganizationServiceImpl.properties.get("ldap.user.password.attribute"), encryptPassword(user.getPassword())));
					modifications.add(mod);
				}
				
				ModificationItem[] mods = new ModificationItem[modifications.size()];
				modifications.toArray(mods);
				preSave(user, false, session);
				ctx.modifyAttributes(name, mods);
				postSave(user, false, session);
			}
		} finally {
			session.flush();
			hibernateService.closeSession(session);
			ctx.close();
		}
	}

	private byte[] encryptPassword(String password) throws Exception {
		byte passwordDigest[] = null;
		String newQuotedPassword = "\"" + password + "\"";
		passwordDigest = newQuotedPassword.getBytes("UTF-16LE");
		return passwordDigest;
	}
}
