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
public class PortalMonitorListenerStack extends ListenerStack {
  public PortalMonitorListenerStack() {
    super() ;
  }
  
  public PortalMonitorListenerStack(int size) {
    super(size) ;
  }
  
  public void onError(PortalMonitor monitor, String message, Throwable t) {
    for(int i = 0; i < size(); i++) {
      PortalMonitorListener listener = (PortalMonitorListener) get(i) ;
      listener.onError(monitor, message, t);
    }
  }
  
  public void onStart(PortalMonitor monitor) {
    for(int i = 0; i < size(); i++) {
      PortalMonitorListener listener = (PortalMonitorListener) get(i) ;
      listener.onStart(monitor) ;
    }
  }
  
  public void onStop(PortalMonitor monitor) {
    for(int i = 0; i < size(); i++) {
      PortalMonitorListener listener = (PortalMonitorListener) get(i) ;
      listener.onStop(monitor) ;
    }
  }
}
