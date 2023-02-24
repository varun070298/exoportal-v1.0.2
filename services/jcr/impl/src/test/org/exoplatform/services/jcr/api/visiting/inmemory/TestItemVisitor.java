/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.visiting.inmemory;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.visiting.ItemVisitorTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 14 aout 2004
 */
public class TestItemVisitor extends ItemVisitorTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("mock");    
    ticket = repository.login(credentials, "ws");   
  }

}
