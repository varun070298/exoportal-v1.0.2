/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.nodetypes.inmemory;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.nodetypes.PrimaryNodeTypesTest;

public class TestPrimaryNodeTypes extends PrimaryNodeTypesTest {
  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("mock");    
    ticket = repository.login(credentials, "ws");
    root = ticket.getRootNode();
  }
}
