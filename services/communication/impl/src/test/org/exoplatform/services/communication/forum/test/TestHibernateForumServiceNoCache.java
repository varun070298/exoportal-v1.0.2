/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.backup.ImportExportService;
import org.exoplatform.services.communication.forum.ForumServiceContainer;

/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestHibernateForumServiceNoCache.java,v 1.6 2004/07/29 14:09:47 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestHibernateForumServiceNoCache extends ForumServiceTestCase {
  private ImportExportService bservice_ ;
  
  public TestHibernateForumServiceNoCache(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer manager  = PortalContainer.getInstance();
      container_ = 
        (ForumServiceContainer) manager.getComponentInstanceOfType(ForumServiceContainer.class) ;
      service_ = container_.findForumService("exo") ;
      bservice_ = (ImportExportService) manager.getComponentInstanceOfType(ImportExportService.class) ;
    }
  }

  public void runExportData() throws Exception {
    bservice_.exportServiceData() ;
    System.err.println(bservice_.getExportLogger().getTextSummary()) ;
  }
  
  public void runImportData() throws Exception {
    bservice_.importServiceData() ;
    System.err.println(bservice_.getImportLogger().getTextSummary()) ;
  }
  
  protected String getDescription() {
    return "Test hibernate forum Service No cache" ;
  }
}
