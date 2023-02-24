/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

/**
 * May 24, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Tasks.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 **/
public class Tasks {
  int numberOfTask_ ;
  int counter_ ;
  private ExoWebClient webClient_ ;
  private String taskNamePrefix_ ;
  
  public Tasks(int numberOfTask, ExoWebClient webClient) {
    numberOfTask_ = numberOfTask ;
    webClient_ = webClient ;
    reset() ;
  }
  
  public void reset() {
    counter_ = 0 ;
    taskNamePrefix_ = "user-" + ((int) (Math.random() * 100000)) + "-";
  }
  
  synchronized public int getUnfinishedTaskCounter() {
    return numberOfTask_ - counter_ ;
  }
  
  synchronized public Runnable getNextTask() {
    if (counter_ < numberOfTask_) {
      counter_++ ;
      return webClient_.clone(taskNamePrefix_ + counter_ ) ;
    } 
    return null ;
  }
}