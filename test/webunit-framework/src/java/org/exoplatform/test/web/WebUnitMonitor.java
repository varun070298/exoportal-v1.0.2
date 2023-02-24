/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;
/**
 * May 22, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WebUnitMonitor.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 **/
public class WebUnitMonitor {
  private int counter_ ;
  private int errorCounter_ ;
  private int xhtmlMalformedCounter_ ;
  private long sumExecutionTime_ ;
  private long sumContentLength_ ;
  
  public WebUnitMonitor() {
    reset() ;
  }
  
  public void reset() {
    counter_ = 0;
    sumExecutionTime_ = 0 ;
    sumContentLength_ = 0 ;
    xhtmlMalformedCounter_ = 0;
    errorCounter_ = 0 ;
  }
  
  public int getCounter() { return counter_ ; }
  public int getErrorCounter() { return errorCounter_ ; }
  public int getXhtmlMalformedCounter() { return xhtmlMalformedCounter_ ; }
  
  public long getAvgExecutionTime() {
    if(counter_ == 0) return 0 ;
    return  sumExecutionTime_/counter_ ;
  }
  
  public long getAvgContentLength() {
    if(counter_ == 0) return 0 ;
    return  sumContentLength_/counter_ ;
  }

  public long getSumContentLength() {
    return  sumContentLength_ ;
  }
  
  synchronized public void log(long executionTime , int contentLength, boolean error, boolean malformed) {
    sumExecutionTime_ += executionTime ;
    sumContentLength_ += contentLength ;
    counter_++ ;
    if(error) errorCounter_++ ;
    if(malformed) xhtmlMalformedCounter_++ ;
  }
}
