/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing.fs;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.writing.AddNodeTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 juil. 2004
 */
public class TestAddNode extends AddNodeTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("fs1");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }

}
