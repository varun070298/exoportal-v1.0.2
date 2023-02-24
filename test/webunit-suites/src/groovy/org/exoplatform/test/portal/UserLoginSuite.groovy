package org.exoplatform.test.portal ;

import org.exoplatform.test.web.*;
import org.exoplatform.test.web.unit.*;
import org.exoplatform.test.web.unit.NewSessionUnit;
import org.exoplatform.test.web.validator.ExpectLinkWithTextValidator;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserLoginSuite.java,v 1.1 2004/10/11 23:27:29 tuan08 Exp $
 **/
public class UserLoginSuite extends WebUnitSuite {
  
  public UserLoginSuite(String userName, String password, String[] roles) {
 	super("UserLoginSuite",  "Go to the home page and login, using web client name for user name and password") ;
    addWebUnit(
      new NewSessionUnit("NewSession", "Create new session and go to the home page for the first time")
    );
    
    addWebUnit(
      new SubmitFormUnit("Login", "Login with user name ${userName} and password ${password}").
      setFormName("loginForm").
      setField(userName:userName, password:password, op:'login').
      addValidator(new ExpectLinkWithTextValidator("Logout")) 
    );
        
    for(role in roles) {
      addWebUnit(
        new AddRoleUnit("AddRole: ${role}", "Add user role to the web client").
        setRoleToAdd(role)
      );
    }
  }
}