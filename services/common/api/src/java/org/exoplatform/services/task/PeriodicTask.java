/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.task;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 30, 2004
 * @version $Id$
 */
abstract public class PeriodicTask extends BaseTask {
  private long period_ ;
  private long nextExecutionTime_ ;
  
  public PeriodicTask(long period) {
    period_ = period ;
    nextExecutionTime_ = System.currentTimeMillis() + period ;
  }
  
  final public void execute() throws Exception {
    long currentTime =  System.currentTimeMillis()  ;
    if(currentTime > nextExecutionTime_) {
      try {
        run() ;
      } finally {
        nextExecutionTime_ = System.currentTimeMillis() + period_ ;
      }
    }
  }
  
  abstract public void run() throws Exception ;
}
