/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading.fs;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.reading.PropertyTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 24 juil. 2004
 */
public class TestProperty extends PropertyTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("fs1");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }


}
