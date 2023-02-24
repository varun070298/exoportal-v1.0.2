/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.container.SessionContainer;
/**
 * Tue, May 27, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: PortalMonitorServiceImpl.java,v 1.2 2004/04/29 15:07:27 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class PortalMonitor {

  static int  NUMBER_OF_REQUEST_MONITOR = 10 ;
  static long TIME_RANGE = 200 ; // 200ms

  private List  requestMonitors_  ;
  private PortalMonitorListenerStack listeners_ ;
  
  public PortalMonitor() {
    requestMonitors_ = new ArrayList() ;
    long min = 0 ;
    long max =  TIME_RANGE  - 1; 
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR ; i++) {
      requestMonitors_.add(new RequestMonitorData(min, max)) ;
      min += TIME_RANGE ;
      max += TIME_RANGE ;
    }
    listeners_ = new PortalMonitorListenerStack(3) ;
  }
  
  public void addListener(PortalMonitorListener listener) {
    listeners_.add(listener) ;
  }
  
  final public void log(ActionData data) {
    long executionTime = data.getHandleTime();
    int index = (int) (executionTime / TIME_RANGE) ;
    if (index >= NUMBER_OF_REQUEST_MONITOR) index = NUMBER_OF_REQUEST_MONITOR - 1 ;
    RequestMonitorData monitor = (RequestMonitorData) requestMonitors_.get(index) ;
    monitor.logRequest(executionTime) ;
  }

  public void error(String errorMessage, Throwable t) {
    listeners_.onError(this, errorMessage, t) ;
    SessionContainer scontainer = SessionContainer.getInstance() ;
    if(scontainer != null) {
      scontainer.getMonitor().error(errorMessage, t) ;
    }
  }

  final public List getRequestMonitorData() { return requestMonitors_ ; }
  
  final public int getRequestCounter() {
    int counter = 0 ;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR ; i++) {
      RequestMonitorData monitor = (RequestMonitorData) requestMonitors_.get(i) ;
      counter += monitor.getRequestCounter() ;
    }
    return counter ;
  }

  final public long minExecutionTime() {
    long minExecutionTime = 0 ;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR ; i++) {
      RequestMonitorData monitor = (RequestMonitorData) requestMonitors_.get(i) ;
      if (i == 0) {
        minExecutionTime = monitor.minExecutionTime() ;
      } else {
        if (monitor.minExecutionTime() < minExecutionTime) 
          minExecutionTime =  monitor.minExecutionTime() ;
      }
    }
    return minExecutionTime ;
  }

  final public long maxExecutionTime() {
    long maxExecutionTime = 0 ;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR ; i++) {
      RequestMonitorData monitor = (RequestMonitorData) requestMonitors_.get(i) ;
      if (i == 0) {
        maxExecutionTime = monitor.maxExecutionTime() ;
      } else {
        if (monitor.maxExecutionTime() > maxExecutionTime) 
          maxExecutionTime =  monitor.maxExecutionTime() ;
      }
    }
    return maxExecutionTime ;
  }

  final public long averageExecutionTime() {
    long sumExecutionTime = 0 ;
    int  requestCounter = 0 ;
    for (int i = 0; i < NUMBER_OF_REQUEST_MONITOR ; i++) {
      RequestMonitorData monitor = (RequestMonitorData) requestMonitors_.get(i) ;
      sumExecutionTime += monitor.sumExecutionTime() ;
      requestCounter += monitor.getRequestCounter() ;
    }
    if (requestCounter == 0) return  0 ;
    return sumExecutionTime/requestCounter ;
  }
}