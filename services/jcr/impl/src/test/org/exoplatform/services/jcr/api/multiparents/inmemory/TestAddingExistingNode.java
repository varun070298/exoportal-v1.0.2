/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.multiparents.inmemory;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.multiparents.AddingExistingNodeTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 25 juil. 2004
 */
public class TestAddingExistingNode extends AddingExistingNodeTest {

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("mock");    
    ticket = repository.login(credentials, "ws");
  }

}
