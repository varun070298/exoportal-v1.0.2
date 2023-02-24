/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log;

import java.util.HashMap;
import java.util.Date ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.monitor.SessionMonitor ;
import org.exoplatform.container.monitor.ActionData;
import org.exoplatform.services.portal.log.impl.PortalLogServiceImpl ;
import org.exoplatform.services.portal.log.impl.SessionLogDataImpl ;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestPortalMonitorService.java,v 1.5 2004/07/24 16:34:05 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestPortalLogService extends BasicTestCase {

  PortalContainer pcontainer_  ;
  SessionContainer scontainer_ ;
  PortalLogServiceImpl service_ ;

  public TestPortalLogService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    setTestNumber(1) ;
    pcontainer_ = PortalContainer.getInstance();
    scontainer_ = pcontainer_.createSessionContainer("session.container", "anon") ;
    service_ = 
      (PortalLogServiceImpl) pcontainer_.getComponentInstanceOfType(PortalLogServiceImpl.class);
  }

  public void tearDown() throws Exception {
  }
  
  public void testPortalMonitorService() throws Exception {
    SessionMonitor monitor = scontainer_.getMonitor() ;
    HashMap map = new HashMap() ;
    map.put("param1", "value 1") ;
    map.put("param2", "value 2") ;
    scontainer_.startActionLifcycle() ;
    for(int i =0 ; i < 10; i++ ) {
      scontainer_.startActionLifcycle() ;
      ActionData data = new ActionData("user", "/page/name", "post", 500, map) ;
      scontainer_.getMonitor().log(data) ;
      scontainer_.endActionLifcycle() ;
    }
    scontainer_.startActionLifcycle() ;
    ActionData data = new ActionData("user", "/page/name", "post", 500, map) ;
    scontainer_.getMonitor().error("a errror", new Exception());
    scontainer_.getMonitor().log(data) ;
    scontainer_.endActionLifcycle() ;
    
    SessionLogData logData = new SessionLogDataImpl() ;
    logData.setAccessTime(new Date(monitor.getAccessTime())) ;
    logData.setRemoteUser(monitor.getRemoteUser()) ;
    logData.setRemoteUser(monitor.getIPAddress()) ;
    logData.setActionHistory(monitor.getHistory()) ;
    service_.saveSessionLogData(logData) ;
    
    SessionLogDataImpl ld = (SessionLogDataImpl)service_.getSessionLogData(logData.getId()) ;
    System.out.println(ld.getData()) ; 
    Thread.sleep(2000) ;
  }
  
  protected String getDescription() {
    return "Test portal monitor service " ;
  }
}
