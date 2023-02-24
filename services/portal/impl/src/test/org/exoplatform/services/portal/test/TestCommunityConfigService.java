/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.test;

import java.util.List ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portal.community.CommunityConfigService ;
import org.exoplatform.services.portal.community.CommunityPortal;
import org.exoplatform.services.portal.community.CommunityNavigation ;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestConverter.java,v 1.6 2004/07/20 12:41:09 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestCommunityConfigService  extends BasicTestCase {
  CommunityConfigService  service_ ;
  
  public TestCommunityConfigService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    service_ = (CommunityConfigService) manager.getComponentInstanceOfType(CommunityConfigService.class) ;
  }
  
  public void testCommunityService() throws Exception {
    CommunityPortal cp = service_.findCommunityPortal("demo") ;
    assertTrue("expect community portal for /user", cp != null) ;
    
    List list = service_.findCommunityNavigation("demo") ;
    assertTrue("expect one shared navigations", list.size() == 1) ;
  }
  
  protected String getDescription() {
    return "Test Converter" ;
  }
}