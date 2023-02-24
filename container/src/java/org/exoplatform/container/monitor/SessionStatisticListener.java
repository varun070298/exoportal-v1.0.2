/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 1, 2004
 * @version $Id$
 */
public class SessionStatisticListener extends SessionMonitorListener{
  private PortalMonitor pmonitor_ ;
  
  public SessionStatisticListener(SessionMonitorListenerStack stack, PortalMonitor monitor) {
    super(stack) ;
    pmonitor_ = monitor ;
  }
  
  public void onLog(SessionMonitor monitor, ActionData data) {
    pmonitor_.log(data) ;
  }
}
