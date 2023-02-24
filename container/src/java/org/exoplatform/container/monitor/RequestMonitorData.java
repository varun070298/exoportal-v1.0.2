/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;
/*
 * Tue, May 27, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: RequestMonitorData.java,v 1.3 2004/05/05 21:28:44 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class RequestMonitorData {
  private long minRange_ ;
  private long maxRange_ ;
  private int requestCounter_ ;
  private long minExecutionTime_ ;
  private long maxExecutionTime_ ;
  private long sumExecutionTime_ = 0 ;
  
  public RequestMonitorData(long minRange, long maxRange) {
    requestCounter_ = 0 ;
    minExecutionTime_  = 0 ;
    maxExecutionTime_ = 0 ;
    minRange_ = minRange ;
    maxRange_ = maxRange ;
  }

  public long minRange() { return minRange_ ; }
  public long maxRange() { return maxRange_ ; }

  public int getRequestCounter() { return requestCounter_  ; }

  public long minExecutionTime() { return minExecutionTime_ ; }

  public long maxExecutionTime() { return maxExecutionTime_ ; }

  public long averageExecutionTime() {
    if(requestCounter_ <= 0) return 0 ;
    return sumExecutionTime_/requestCounter_ ;
  }

  public long sumExecutionTime() { return sumExecutionTime_ ; }
  
  public void logRequest(long startTime, long endTime) {
    requestCounter_++ ;
    if(requestCounter_ > 0) {
      long executionTime = endTime - startTime ;
      sumExecutionTime_  += executionTime;
      if(executionTime < minExecutionTime_) minExecutionTime_ = executionTime ;
      if(executionTime > maxExecutionTime_) maxExecutionTime_ = executionTime ;
    }
  }

  public void logRequest(long executionTime) {
    requestCounter_++ ;
    if(requestCounter_ > 0) {
      sumExecutionTime_  += executionTime;
      if(executionTime < minExecutionTime_) minExecutionTime_ = executionTime ;
      if(executionTime > maxExecutionTime_) maxExecutionTime_ = executionTime ;
    }
  }
}
