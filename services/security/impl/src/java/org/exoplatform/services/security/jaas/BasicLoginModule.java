/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.services.security.jaas;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.security.SecurityService;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class BasicLoginModule implements LoginModule {

  private static final String DEFAULT_DOMAIN = "portal";

  private SecurityService securityService_;
  private boolean success_;
  private String username_;
  private Subject subject_;
  private String portalDomain_ ;
  private CallbackHandler callbackHandler_;
  private Map sharedState_;
  private Log log_ ;

  public BasicLoginModule() {
    this.success_ = false;
    this.username_ = null;
  }

  public void initialize(Subject subject, CallbackHandler callbackHandler,
                         Map sharedState, Map options) {
    this.subject_ = subject;
    this.callbackHandler_ = callbackHandler;
    this.sharedState_ = sharedState;
  }

  public boolean login() throws LoginException {
    if (callbackHandler_ == null) {
      throw new LoginException("CallbackHandler null");
    }
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Username: ");
    callbacks[1] = new PasswordCallback("Password: ", false);
    try {
      // prompt for username and password
      callbackHandler_.handle(callbacks);
      username_ = ((NameCallback) callbacks[0]).getName();
      String identifier = new String(((PasswordCallback) callbacks[1]).getPassword());

      int split = identifier.lastIndexOf("@");
      String password;
      if(split == 0) {          // no password in password callback - login will be unsuccessful
        password = null;
        portalDomain_ = identifier.substring(split +1);
      } else if(split == -1) { // no Domain in password callback - default domain
        password = identifier;
        portalDomain_ = DEFAULT_DOMAIN;
      } else {
        password = identifier.substring(0, split);
        portalDomain_ = identifier.substring(split +1);
      }
      PortalContainer pcontainer = RootContainer.getInstance().getPortalContainer(portalDomain_);
      PortalContainer.setInstance(pcontainer) ;
      securityService_ = 
        (SecurityService) pcontainer.getComponentInstanceOfType(SecurityService.class);
      log_ = securityService_.getLog() ;
      
      if (username_ == null) {
        log_.debug("No user name entered");
        success_ = false;
        return false;
      }
      if (password == null) {
        log_.debug("No password entered");
        success_ = false;
        return false;
      }
      // share username and password with other LoginModules
      sharedState_.put("javax.security.auth.login.name", username_);
      sharedState_.put("javax.security.auth.login.password", password);
      
      ((PasswordCallback) callbacks[1]).clearPassword();
      success_ = securityService_.authenticate(this.username_, password);
      if (!success_) {
        log_.debug("Authentication failed");
        throw new LoginException("Authentication failed");
      }
      subject_.getPrivateCredentials().add(password);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      log_.error("error while trying to login", e);
      throw new LoginException("Authentication failed");
    } finally {
      HibernateService hservice = 
        (HibernateService) PortalContainer.getComponent(HibernateService.class) ;
      hservice.closeSession() ;
      PortalContainer.setInstance(null) ;
    }
  }

  public boolean commit() throws LoginException {
    if (success_) {
      try {        
        PortalContainer pcontainer = 
          RootContainer.getInstance().getPortalContainer(portalDomain_);
        PortalContainer.setInstance(pcontainer) ;
        securityService_.setUpAndCacheSubject(username_, subject_);
      } catch (ExoServiceException e) {
        throw new LoginException("error while filling subject with Principal in commit() " +
                                 "of BasicLoginModule");
      } finally {
        HibernateService hservice = 
          (HibernateService) PortalContainer.getComponent(HibernateService.class) ;
        hservice.closeSession() ;
        PortalContainer.setInstance(null) ;
      }
    }
    return success_;
  }

  public boolean abort() throws LoginException {
    log_.debug("call abort()") ;
    clear();
    if(success_)
      return true;
    return false;
  }

  public boolean logout() throws LoginException {
    log_.debug("logout user: " + username_ ) ;
    securityService_.removeSubject(username_);
    clear();
    return true;
  }

  private void clear() {
    subject_.getPrincipals().clear();
    subject_.getPrivateCredentials().clear();
    subject_.getPublicCredentials().clear();
    username_ = null;
  }
}