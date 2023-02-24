/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

/**
 * May 22, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WorkerThread.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
public class WorkerThread extends Thread {
  private Tasks tasks_ ;
  
  public WorkerThread(ThreadGroup group, String name, Tasks tasks) {
    super (group, name) ;
    tasks_ = tasks ;
  }
  
  public WorkerThread() {
    super() ; 
  }
  
  public void  setTasks(Tasks tasks) { tasks_ = tasks ; }
  
  public void run() {
    boolean availableTask = true ;
    while(availableTask) {
      if(isInterrupted()) return ;
      Runnable task = tasks_.getNextTask() ;
      if (task != null) {
        //System.out.println("run task" )  ;
        task.run() ;
      } else {
        availableTask = false ;  
      }
    }
  }
}
