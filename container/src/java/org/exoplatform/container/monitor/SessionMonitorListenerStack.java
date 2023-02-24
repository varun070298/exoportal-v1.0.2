/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import org.exoplatform.commons.utils.ListenerStack;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 1, 2004
 * @version $Id$
 */
public class SessionMonitorListenerStack extends ListenerStack {
  public SessionMonitorListenerStack() {
    super() ;
  }
  
  public SessionMonitorListenerStack(int size) {
    super(size) ;
  }
  
  public void onLog(SessionMonitor monitor, ActionData data) {
    for(int i = 0; i < size(); i++) {
      SessionMonitorListener listener = (SessionMonitorListener) get(i) ;
      listener.onLog(monitor, data) ;
    }
  }
  
  public void onError(SessionMonitor monitor,  String message, Throwable t) {
    for(int i = 0; i < size(); i++) {
      SessionMonitorListener listener = (SessionMonitorListener) get(i) ;
      listener.onError(monitor, message, t);
    }
  }
  
  public void onStart(SessionMonitor monitor) {
    for(int i = 0; i < size(); i++) {
      SessionMonitorListener listener = (SessionMonitorListener) get(i) ;
      listener.onStart(monitor) ;
    }
  }
  
  public void onStop(SessionMonitor monitor) {
    for(int i = 0; i < size(); i++) {
      SessionMonitorListener listener = (SessionMonitorListener) get(i) ;
      listener.onStop(monitor) ;
    }
  }
}
