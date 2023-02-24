/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing.rdb;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.writing.AddPropertyTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 30 juil. 2004
 */
public class TestAddProperty extends AddPropertyTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("db1");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }

}
