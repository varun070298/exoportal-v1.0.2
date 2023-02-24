/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import org.picocontainer.Startable;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 1, 2004
 * @version $Id$
 */
public class PortalMonitorListener implements Startable {
  
  public PortalMonitorListener(PortalMonitor monitor) {
    monitor.addListener(this) ;
  }
  
  
  public void onError(PortalMonitor monitor, String message, Throwable t) {
    
  }
  
  public void onStart(PortalMonitor monitor) {
    
  }
  
  public void onStop(PortalMonitor monitor) {
    
  }
  
  public void start() {} 
  public void stop() {}
}
