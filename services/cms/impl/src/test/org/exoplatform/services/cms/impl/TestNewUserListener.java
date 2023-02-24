 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.cms.impl;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.Ticket;

import junit.framework.TestCase;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class TestNewUserListener extends TestCase{
  
  private Repository jcrRepository_;
  private static final String WORKSPACE = "ws";
  private OrganizationService orgService;

  public void setUp() throws Exception {
    PortalContainer servicesManager = PortalContainer.getInstance();
    LogService service = (LogService) RootContainer.getInstance().
      getComponentInstanceOfType(LogService.class);    
    service.setLogLevel("org.exoplatform.services.jcr", LogService.DEBUG, true);    
    RepositoryService jcrService = (RepositoryService) servicesManager.
       getComponentInstanceOfType(RepositoryService.class);
    jcrRepository_ = jcrService.getRepository();
    orgService = (OrganizationService)servicesManager.
       getComponentInstanceOfType(OrganizationService.class);
  }
  
  public void testCreation() throws Exception{
    Ticket ticket = jcrRepository_.login(null, WORKSPACE);
    Node root = ticket.getRootNode();
    
    assertTrue(root.hasNode("/cms/home"));
    assertTrue(root.hasNode("/cms/home/admin"));
    assertTrue(root.hasNode("/cms/home/demo"));

    
    Node node = root.getNode("/cms/home/demo/home-intro");    
    assertTrue(node.hasNode("home-intro_en.html"));
    Property prop = node.getNode("home-intro_en.html").getNode("jcr:content").getProperty("exo:content");
    assertNotNull(prop.getString());
    
    orgService.removeUser("exo");
  }


}
