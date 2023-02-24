/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.jcr.impl;


import java.io.ByteArrayInputStream;

import javax.jcr.Repository;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.ContainerEntry;
import org.exoplatform.services.jcr.config.RepositoryServiceConfig;
import org.exoplatform.services.jcr.impl.config.DummyConfig;
import org.exoplatform.services.jcr.impl.config.XMLConfig;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.impl.core.TicketImpl;
import org.exoplatform.services.xml.querying.XMLQueryingService;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: TestService.java,v 1.2 2004/09/22 13:54:55 geaz Exp $
 */

public class TestService extends BaseTest {

  private XMLQueryingService xmlQueryingService;
  private RepositoryService repositoryService;

  public void setUp() throws Exception {
    super.setUp();
    try {
      xmlQueryingService = (XMLQueryingService) PortalContainer.getInstance().
          getComponentInstanceOfType(XMLQueryingService.class);
      repositoryService = (RepositoryService) PortalContainer.getInstance().
          getComponentInstanceOfType(RepositoryService.class);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void testManager() throws Exception {

    RepositoryServiceConfig config = new DummyConfig();

    PortalContainer manager = PortalContainer.getInstance();
    assertEquals(PortalContainer.getInstance(), manager.getParent());

    RepositoryImpl rep = (RepositoryImpl) manager.getComponentInstance("mock");
    assertNotNull(rep);
    assertNotNull(rep.getRepositoryManager());
    assertNotNull(rep.getContainer("ws"));
    assertEquals("mock",rep.getName());

    rep = (RepositoryImpl) manager.getComponentInstance("fs1");
    assertNotNull(rep);
    assertNotNull(rep.getRepositoryManager());
    assertNotNull(rep.getContainer("ws"));
    assertEquals("fs1",rep.getName());
  }

  public void testXMLConfig() throws Exception {
    String str = "<repositories-config><default-repository name=\"mock\"/><repositories><repository name=\"mock\" manager=\"inmemory\"><workspaces><workspace name=\"ws\" default=\"true\"/></workspaces></repository></repositories><containers><container name=\"c.mock\" class=\"org.exoplatform.services.jcr.impl.storage.inmemory.ContainerImpl\"><properties><property name=\"name\" value=\"value\"/></properties></container></containers><managers><manager name=\"inmemory\" class=\"org.exoplatform.services.jcr.impl.storage.inmemory.RepositoryManagerImpl\"/></managers></repositories-config>";
    ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
    RepositoryServiceConfig config = new XMLConfig(xmlQueryingService, bis);
    assertNotNull(config.getRepositoryEntry("mock"));
    assertTrue("config.getRepositoryEntries().length!=1", config.getRepositoryEntries().length == 1);
    assertNotNull("c.mock is null", config.getContainerEntry("c.mock"));
    assertEquals("org.exoplatform.services.jcr.impl.storage.inmemory.ContainerImpl", 
                  config.getContainerEntry("c.mock").getType().getName());
    assertEquals("value", 
                 config.getContainerEntry("c.mock").getParameters().getProperty("name"));
    assertTrue("config.getSupportedContainerEntries().length!=1", config.getSupportedContainerEntries().length == 1);
    assertNotNull(config.getRepositoryManagerEntry("inmemory"));
    assertTrue(config.getSupportedRepositoryManagerEntries().length == 1);
    assertNotNull(config.getWorkspaceEntry("mock","ws"));
    assertTrue(config.getWorkspaceEntries().length == 1);
  }

  public void testReadDefaultXMLConfig() throws Exception {
    RepositoryServiceConfig config = new XMLConfig(xmlQueryingService);
    assertNotNull(config);
    assertTrue(config.getRepositoryEntries().length>0);
    assertNotNull(config.getDefaultRepositoryName());
    log.debug("DEF NAME "+config.getDefaultRepositoryName());


  }

  public void testDefaultRepository() throws Exception {
    TicketImpl ticket = (TicketImpl)repositoryService.getRepository().login(null, "ws");
    log.debug("CONTAINER "+ticket.getContainer().getName());
  }

  public void testPortalContainerRegistration() throws Exception {
    PortalContainer manager = PortalContainer.getInstance();

    RepositoryService service = (RepositoryService) manager.getComponentInstanceOfType(RepositoryService.class);
    assertNotNull(service);
    Repository rep = service.getRepository("mock");
    assertNotNull("Repository(1) is Null", rep);

    RepositoryServiceConfig config = (RepositoryServiceConfig) manager.getComponentInstanceOfType(RepositoryServiceConfig.class);
    assertNotNull("Config is null", config);
    ContainerEntry entry = config.getContainerEntry("c.inmemory");
    assertNotNull("'c.inmemory' entry not found!", entry);

    assertEquals(config, service.getConfig());
    String containerName = entry.getType().getName();
    assertEquals("org.exoplatform.services.jcr.impl.storage.inmemory.ContainerImpl", containerName);
    assertNotNull(Class.forName(containerName));

    rep = service.getRepository("mock");
    assertNotNull("Repository(2) is Null", rep);

  }

}
