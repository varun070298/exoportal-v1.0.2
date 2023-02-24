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
public class SessionMonitorListener implements Startable{
  
  public SessionMonitorListener(SessionMonitorListenerStack stack) {
    stack.add(this) ;
  }
  
  public void onLog(SessionMonitor monitor, ActionData data) {
    
  }
  
  public void onError(SessionMonitor monitor, String message, Throwable t) {
    
  }
  
  public void onStart(SessionMonitor monitor) {
    
  }
  
  public void onStop(SessionMonitor monitor) {
    
  }
  
  public void start() {} 
  public void stop() {}
}
