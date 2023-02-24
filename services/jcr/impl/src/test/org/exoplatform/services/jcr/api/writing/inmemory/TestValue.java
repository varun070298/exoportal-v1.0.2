/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing.inmemory;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.writing.ValueTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 aout 2004
 */
public class TestValue extends ValueTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("mock");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }

}
