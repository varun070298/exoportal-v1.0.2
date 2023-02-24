package org.exoplatform.services.ldap;

import javax.naming.InitialContext;
import javax.naming.ldap.LdapContext;

import org.exoplatform.services.exception.ExoServiceException;

/**
 * Created by the eXo platform team User: Daniel Summer Date: 25/5/2004
 * 
 * interface abstracted from JSDK
 */
public interface LDAPService {

	//	 Normal context for all directories
	public LdapContext getLdapContext() throws ExoServiceException;
	
	// LDAP booster pack context for v3 directories (except Active Directory)
	public InitialContext getInitialContext() throws ExoServiceException;
	
	// LDAP bind authentication
	public boolean authenticate(String userDN, String password) throws ExoServiceException;

}
