/***************************************************************************
 * Copyright 2001-2004 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
/**
 * Created by The eXo Platform SARL        .
 * Author : James Chamberlain
 *          james@echamberlains.com
 * Date: 11/2/2005
 * 
 */

package org.exoplatform.services.ldap.impl;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.PropertiesParam;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.ldap.LDAPService;
import org.exoplatform.services.log.LogService;

public class LDAPServiceImpl implements LDAPService {
	private LogService logService_;
	private Map env = null;
	private String userIdAttribute = null;
	
	public LDAPServiceImpl(LogService lservice,
			ConfigurationManager confService) throws Exception {
		try {
			ServiceConfiguration sconf = confService
					.getServiceConfiguration(LDAPService.class);
			PropertiesParam param = sconf
					.getPropertiesParam("exo.ldap.service");
			ExoProperties properties = param.getProperties();
		
			env = new HashMap();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, properties.getProperty("ldap.user.dn").trim());
			env.put(Context.SECURITY_CREDENTIALS, properties.getProperty("ldap.user.password").trim());
			env.put("com.sun.jndi.ldap.connect.timeout", properties.getProperty("ldap.timeout").trim());
			env.put("com.sun.jndi.ldap.connect.pool", "true");
			env.put("java.naming.ldap.version", properties.get("ldap.version"));
			// this is support Active Directory nested groups...
			env.put("java.naming.ldap.attributes.binary","tokenGroups");
			env.put(Context.REFERRAL, properties.getProperty("ldap.referral.mode").trim());
			
			boolean ssl = Boolean.valueOf(properties.getProperty("ldap.ssl.enabled").trim()).booleanValue();
			String connPrefix;
			if (ssl){
				 env.put(Context.SECURITY_PROTOCOL, "ssl");
				 connPrefix = "ldaps";
			} else {
				connPrefix = "ldap";
			}
			
			String serversString = properties.getProperty("ldap.host").trim();
			if (serversString != null && serversString.length() > 0){
				String servers[] = serversString.split(",");
				StringBuffer buffer = new StringBuffer();
				for (int x = 0; x < servers.length; x++){
					if (x == 0){
						buffer.append(connPrefix+"://"+servers[x].trim()+"/");
					} else {
						buffer.append(" "+connPrefix+"://"+servers[x].trim()+"/");
					}
				}
				env.put(Context.PROVIDER_URL, buffer.toString());
			}
			
			userIdAttribute = properties.getProperty("ldap.user.username.attribute");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public LdapContext getLdapContext() throws ExoServiceException {
		LdapContext ctx;
		try {
			ctx = new InitialLdapContext(new Hashtable(env), null);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ExoServiceException(e);
		}
		return ctx;
	}


	public InitialContext getInitialContext() throws ExoServiceException {
		InitialContext ctx;
		try {
			Hashtable props = new Hashtable(env);
			props.put(Context.OBJECT_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
			props.put(Context.STATE_FACTORIES, "com.sun.jndi.ldap.obj.LdapGroupFactory");
			ctx = new InitialLdapContext(props, null);
		} catch (NamingException e) {
			throw new ExoServiceException(e);
		}
		return ctx;
	}


	public boolean authenticate(String userDN, String password) throws ExoServiceException {
		boolean authenticated = false;
		try {
			Hashtable props = new Hashtable(env);
			props.remove(Context.SECURITY_PRINCIPAL);
			props.put(Context.SECURITY_PRINCIPAL, userDN);
			props.remove(Context.SECURITY_CREDENTIALS);
			props.put(Context.SECURITY_CREDENTIALS, password);
			props.remove("com.sun.jndi.ldap.connect.pool");
			props.put("com.sun.jndi.ldap.connect.pool", "false");
			LdapContext ctx = new InitialLdapContext(props, null);
			authenticated = true;
		} catch (NamingException e) {
			throw new ExoServiceException(e);
		}
		return authenticated;
	}

	

}
