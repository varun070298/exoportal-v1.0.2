/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.accessing.rdb;


import javax.jcr.Repository;

import org.exoplatform.services.jcr.api.accessing.AccessRepositoryTest;

/**
 * Created by The eXo Platform SARL
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: TestRDBAccessRepository.java,v 1.1 2004/11/02 18:57:02 geaz Exp $
 */

public class TestRDBAccessRepository extends AccessRepositoryTest{

  public void setUp() throws Exception {
    super.setUp();    
    repository = (Repository) repositoryService.getRepository("db1");
  }

}
