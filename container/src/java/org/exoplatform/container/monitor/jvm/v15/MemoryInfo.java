/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm.v15;


import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class MemoryInfo  {
  private MemoryMXBean  mxbean_ ;
  
  public MemoryInfo() {
    mxbean_ = ManagementFactory.getMemoryMXBean();
  }
  
  public MemoryUsage getHeapMemoryUsage() { 
    return mxbean_.getHeapMemoryUsage() ;  
  } 

  public MemoryUsage getNonHeapMemoryUsage() { 
    return mxbean_.getNonHeapMemoryUsage() ;
  } 
  
  public int getObjectPendingFinalizationCount() { 
    return mxbean_.getObjectPendingFinalizationCount() ; 
  }
  
  public boolean   isVerbose() { return mxbean_.isVerbose() ; } 
  
}