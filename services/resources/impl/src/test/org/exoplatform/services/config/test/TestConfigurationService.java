/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.config.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.config.ConfigurationService;
import org.exoplatform.services.config.impl.*;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2004 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestResourceBundleService.java,v 1.2 2004/10/27 03:04:43 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestConfigurationService extends BasicTestCase {
  
  private ConfigurationService service_ ;
  
  public TestConfigurationService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    service_ = (ConfigurationService) manager.getComponentInstanceOfType(ConfigurationService.class) ;
  }
  
  public void tearDown() throws Exception {
  }
  
  public void testResourceBundleService() throws Exception {
    ConfigurationDataImpl impl = new ConfigurationDataImpl() ;
    impl.setData("this is a test") ;
    service_.saveServiceConfiguration(ConfigurationDataImpl.class, impl) ;
    
    impl = (ConfigurationDataImpl) service_.getServiceConfiguration(ConfigurationDataImpl.class) ;
    assertTrue("expect the configuration" , impl != null) ;
  }
}