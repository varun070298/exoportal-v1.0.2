/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.nodetypes.rdb;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.nodetypes.AssigningNodeTypeTest;

public class TestAssigningNodeType extends AssigningNodeTypeTest {
  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("db1");    
    ticket = repository.login(credentials, "ws");
    root = ticket.getRootNode();
  }
}
