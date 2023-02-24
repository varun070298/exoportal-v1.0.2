/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm.v15;

import java.lang.management.ManagementFactory;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.RootContainerInitializer;
import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 11, 2005
 * @version $Id$
 */
public class JVM15RootContainerInitializer implements RootContainerInitializer {
  
  public void initialize(RootContainer container) {
    container.registerComponentInstance(ManagementFactory.getOperatingSystemMXBean()) ;
    container.registerComponentInstance(ManagementFactory.getRuntimeMXBean()) ;
    container.registerComponentInstance(ManagementFactory.getThreadMXBean()) ;
    container.registerComponentInstance(ManagementFactory.getClassLoadingMXBean()) ;
    container.registerComponentInstance(ManagementFactory.getCompilationMXBean()) ;
    
    container.registerComponentInstance(new MemoryInfo()) ;
    container.registerComponentInstance(JVMRuntimeInfo.MEMORY_MANAGER_MXBEANS, ManagementFactory.getMemoryManagerMXBeans()) ;
    container.registerComponentInstance(JVMRuntimeInfo.MEMORY_POOL_MXBEANS, ManagementFactory.getMemoryPoolMXBeans()) ;
    container.registerComponentInstance(JVMRuntimeInfo.GARBAGE_COLLECTOR_MXBEANS, ManagementFactory.getGarbageCollectorMXBeans()) ;
  }
}