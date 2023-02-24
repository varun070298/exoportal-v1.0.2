/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer ;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.monitor.PortalMonitor;
import org.exoplatform.container.monitor.jvm.*;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestPortalMonitorService.java,v 1.5 2004/07/24 16:34:05 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestPortalMonitorService extends BasicTestCase {

  PortalContainer pcontainer_  ;
  SessionContainer scontainer_ ;

  public TestPortalMonitorService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    setTestNumber(1) ;
    pcontainer_ = PortalContainer.getInstance();
    scontainer_ = pcontainer_.createSessionContainer("session.container", "anon") ;
  }

  public void tearDown() throws Exception {
  }
  
  public void testPortalMonitorService() {
    scontainer_.startActionLifcycle() ;
    PortalMonitor pmonitor = 
      (PortalMonitor)pcontainer_.getComponentInstanceOfType(PortalMonitor.class) ;
    assertTrue("Found pmonitor", pmonitor != null) ;
    pmonitor.error("error", new Exception()) ;
    assertTrue("session has error", scontainer_.getMonitor().getErrorCount() == 1);
    scontainer_.endActionLifcycle() ;
    pmonitor.error("error", new Exception()) ;
  }
  
  public void testOSEnvironment() {
    System.out.println(RootContainer.getInstance().getOSEnvironment()) ;
  }

  public void testRuntimInfo() {
    JVMRuntimeInfo info = 
      (JVMRuntimeInfo)RootContainer.getInstance().getComponentInstanceOfType(JVMRuntimeInfo.class) ;
    System.out.println(info) ;
  }
  
  
  protected String getDescription() {
    return "Test portal monitor service " ;
  }
}
