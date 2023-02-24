/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.task.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator ;
import org.exoplatform.services.task.Task;
import org.exoplatform.services.task.TaskService;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogService;
import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer ;
import org.picocontainer.Startable;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 30, 2004
 * @version $Id$
 */
public class TaskServiceImpl implements TaskService , Startable {
  static private Collection EMPTY = new LinkedList() ;
  
  private Collection repeatTasks_ ;
  private Collection tasks_ ;
  private Log log_; 
  private TaskThread thread_ ;
  
  public TaskServiceImpl(LogService lservice) {
    log_ = lservice.getLog(getClass()) ;
    repeatTasks_ = new LinkedList() ;
    tasks_ = new LinkedList() ;
  }
  
  synchronized public void queueTask(Task task) {
    tasks_.add(task) ;
  }

  synchronized public  Collection getTasks() {
    if(tasks_.size() == 0)  return EMPTY ;
    return new LinkedList(tasks_) ;
  }
  
  synchronized Collection dequeueTasks() {
    if(tasks_.size() == 0)  return EMPTY ;
    Collection temp = tasks_ ;
    tasks_ = new LinkedList() ;
    return temp ;
  }
  
  synchronized public void queueRepeatTask(Task task) {
    repeatTasks_.add(task) ;
  }
 
  synchronized public Collection getRepeatTasks() { 
    if(repeatTasks_.size() == 0)  return EMPTY ;
    return new LinkedList(repeatTasks_) ; 
  }
  
  synchronized Collection dequeueRepeatTasks() {
    if(repeatTasks_.size() == 0)  return EMPTY ;
    return new LinkedList(repeatTasks_) ;
  }
  
  synchronized public void removeRepeatTask(Task task) {
    repeatTasks_.remove(task) ;
  }
  
  public void start() {
    thread_ = new TaskThread(1000) ;
    thread_.start() ;
  }
  
  public void stop() {
    if(thread_ != null) thread_.interrupt() ;
  }
  
  void runTasks(Collection tasks) {
    Iterator i = tasks.iterator() ;
    while(i.hasNext()) {
      Task task = (Task) i.next() ;
      try {
        PortalContainer pcontainer = task.getPortalContainer() ;
        if(pcontainer != null) {
          PortalContainer.setInstance(pcontainer) ;
        }
        task.execute() ;
        if(pcontainer != null) {
          HibernateService hservice = 
            (HibernateService) pcontainer.getComponentInstanceOfType(HibernateService.class) ;
          if(hservice != null) hservice.closeSession() ;
          PortalContainer.setInstance(null) ;
        }
      } catch (Throwable t) {
        String error = "task: " + task.getName() +
                        "\n task description: " + task.getDescription();
        log_.error(error, t) ;
      }
    }
  }
  
  public class TaskThread extends Thread {
    private long period_ ;
    
    public TaskThread(long period) {
      setPriority(NORM_PRIORITY) ;
      period_ = period ;
    }
    
    public void run() {
      while (true) {
        try {
          sleep(period_) ;
          runTasks(dequeueTasks()) ;
          runTasks(dequeueRepeatTasks()) ;
        } catch (InterruptedException ex) {
          return ;
        } catch (Throwable ex) {
          ex.printStackTrace() ;
        }
      }
    }
  }
}
