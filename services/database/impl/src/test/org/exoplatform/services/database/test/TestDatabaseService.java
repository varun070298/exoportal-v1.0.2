/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.database.HibernateService ;
import org.exoplatform.test.BasicTestCase;
/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestDatabaseService.java,v 1.2 2004/09/14 22:17:46 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestDatabaseService extends BasicTestCase {
  DatabaseService service_ ;
  HibernateService hservice_ ;
  public TestDatabaseService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
		service_ = (DatabaseService) pcontainer.getComponentInstanceOfType(DatabaseService.class) ;
    hservice_ = (HibernateService)pcontainer.getComponentInstanceOfType(HibernateService.class) ;
  }

  public void testDabaseService() throws Exception {
    assertTrue("Expect database service instance" , service_ != null) ;
    assertTrue("Expect hibernate service instance" , hservice_ != null) ;
  }
  
  protected String getDescription() {
    return "Test Database Service" ;
  }
}
