/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm.v15;

import java.lang.management.* ;
import org.exoplatform.container.RootContainer;
import org.exoplatform.test.BasicTestCase;
/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestPortalMonitorService.java,v 1.5 2004/07/24 16:34:05 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestJVMManagement extends BasicTestCase {

  public TestJVMManagement(String name) {
    super(name);
  }
  
  public void testThreadManagement() {
    ThreadMXBean threadBean = 
      (ThreadMXBean)RootContainer.getComponent(ThreadMXBean.class) ;
    if(threadBean == null) return ;
    ThreadInfo[] infos = threadBean.getThreadInfo(threadBean.getAllThreadIds());
    for(int i = 0; i < infos.length; i++) {
      System.out.println("Thread Name" + infos[i].getThreadName()) ;
    }
  }

}
