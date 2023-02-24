/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.test;


import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.UserPrincipal;
import org.exoplatform.test.BasicTestCase;
import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import org.exoplatform.services.security.impl.mock.MockCallbackHandler;
import java.io.File;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class TestLoginModule extends BasicTestCase {
  protected static SecurityService service_;
  protected static OrganizationService organizationService;

  public TestLoginModule(String name) {
    super(name);
  }

  protected String getDescription() {
    return "test JAAS Login module";
  }

  protected void setUp() throws Exception {
    if (service_ == null) {
      System.setProperty("java.security.auth.login.config", "src/resource/login.conf" );
      RootContainer rootContainer = RootContainer.getInstance();
      PortalContainer manager = rootContainer.getPortalContainer("default");
      PortalContainer.setInstance(manager) ;
      service_ = (SecurityService) manager.getComponentInstanceOfType(SecurityService.class);
      organizationService = 
        (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
      User user = organizationService.createUserInstance();
      user.setUserName("login");
      user.setUserName("login");
      user.setPassword("password");
      organizationService.createUser(user);
      ((LogService) manager.getComponentInstanceOfType(LogService.class)).
          setLogLevel("org.exoplatform.services.security", LogService.DEBUG, false);
    }
  }

  public void testLogin() throws Exception {
    MockCallbackHandler handler = new MockCallbackHandler("login", "password@default");
    LoginContext loginContext = new LoginContext("eXo", handler);
    loginContext.login();
    assertNotNull(loginContext.getSubject());
    Subject subject = service_.getSubject("login");
    assertNotNull(loginContext.getSubject());
    assertSame(loginContext.getSubject(), subject);
    Set principals = subject.getPrincipals(UserPrincipal.class);
    UserPrincipal userPrincipal = ((UserPrincipal) principals.iterator().next());
    assertEquals("login", userPrincipal.getName());
    Iterator p = subject.getPrincipals(Group.class).iterator();
    if (p.hasNext()) {
      Group roles = (Group) p.next();
      Enumeration roleEnum = roles.members();
      Principal princ = (Principal) roleEnum.nextElement();
//      assertEquals("user", princ.getName());
      assertTrue(roles.isMember(princ));
    }
    loginContext.logout();
    assertNull(service_.getSubject("login"));
  }
}
