/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.exporter.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.backup.ImportExportService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.test.BasicTestCase;


/**
 * Thu, May 15, 2004    
 * @author: Tuan Nguyen
 * @version: $Id: TestExportService.java,v 1.7 2004/07/24 16:38:59 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestExportService extends BasicTestCase {
	private static ImportExportService  service_ ;
	private static  OrganizationService orgService_ ;
	
  public TestExportService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer manager  = PortalContainer.getInstance();
      manager.getComponentInstanceOfType(PortalConfigService.class) ;
      service_ = (ImportExportService ) manager.getComponentInstanceOfType(ImportExportService.class) ;
      orgService_ = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    }
  }

  public void tearDown() throws Exception {
  	PortalContainer manager  = PortalContainer.getInstance();
  	HibernateService hservice = 
  		(HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
  	hservice.closeSession();
  }
  
  public void testExportUserData() throws Exception {
  	service_.exportUserData() ;
  	System.err.println(service_.getExportLogger().getTextSummary()) ;
  }

  public void testImportUserData() throws Exception {
  	orgService_.removeUser("exo") ;
  	service_.importUserData("exo") ;
  	User user = orgService_.findUserByName("exo") ;
  	assertTrue("user is not null" , user != null) ;
  	System.err.println(service_.getImportLogger().getTextSummary()) ;
  	
  	service_.importUserData("exo") ;
  	user = orgService_.findUserByName("exo") ;
  	assertTrue("user is not null" , user != null) ;
  	System.err.println(service_.getImportLogger().getTextSummary()) ;
  }
  
  public void testExportServiceData() throws Exception {
  	service_.exportServiceData() ;
  	System.err.println(service_.getExportLogger().getTextSummary()) ;
  }
  
  public void testImportServiceData() throws Exception {
  	service_.importServiceData() ;
  	System.err.println(service_.getImportLogger().getTextSummary()) ;
  }
  
  protected String getDescription() {
    return "Test Export Service" ;
  }
}
