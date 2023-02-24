/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer ;
import org.exoplatform.test.BasicTestCase;
import org.exoplatform.container.configuration.*;
import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestConfigurationService.java,v 1.5 2004/10/29 01:55:23 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestConfigurationService extends BasicTestCase {
	private ConfigurationManager service_ ;
  
  public TestConfigurationService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
  	PortalContainer manager  = PortalContainer.getInstance();
    service_ = (ConfigurationManager) manager.getComponentInstanceOfType(ConfigurationManager.class) ;
  }

  public void testConfigurationService() throws Exception {
  	ValuesParam param = service_.getGlobalInitParam("smtp.mail.server") ;
    assertEquals("mail server", "localhost", param.getValue());
    ServiceConfiguration sconf = 
      service_.getServiceConfiguration("org.exoplatform.container.mocks.MockService") ;
    ObjectParam objParam = sconf.getObjectParam("new.user.configuration") ;
    objParam.getObject() ;
  }
  
  public void testJVMEnvironment() throws Exception {
    JVMRuntimeInfo jvm = 
      (JVMRuntimeInfo)RootContainer.getInstance().getComponentInstanceOfType(JVMRuntimeInfo.class) ;
    System.out.println(jvm.getSystemPropertiesAsText()) ;
  }
  
  protected String getDescription() {
    return "Test Configuration Service" ;
  }
}
