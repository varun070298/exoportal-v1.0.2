/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

import groovy.lang.GroovyClassLoader;
import java.net.URL;

/**
 * May 22, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WebLoadRunner.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 **/
public class WebLoadRunner  {
  private int numberOfWorker_ = 1 ;
  private int numberOfTask_ =  1 ;
  private boolean validateWellFormedXhtml_  = true ;
  private boolean validateWebUnit_ = true ; 
  private String defaultURL_ ;
  
  private WorkerThread[] workers_ ;
  private Tasks tasks_ ;
  
  public WebLoadRunner() {
  }
  
  public WebLoadRunner(int numberOfUser, int numberOfTask) {
    numberOfWorker_ = numberOfUser ;
    numberOfTask_ = numberOfTask ;
  }
  
  public void setValidateWellFormedXhtml(boolean b) {  validateWellFormedXhtml_ = b ; }
  public void setValidateWebUnit(boolean b) {  validateWebUnit_ = b ; }
  public void setDefaultURL(String s) { defaultURL_ = s ; }
  public void setNumberOfWorker(int number) { numberOfWorker_ = number ; }
  public void setNumberOfTask(int number) { numberOfTask_ = number ; }
  
  public void run(ExoWebClient webClient) {
    if(isRunning()) return ;
    tasks_ = new Tasks(numberOfTask_ , webClient);
    ThreadGroup threadGroup_ = new ThreadGroup("Group Of Worker") ;
    int limit = numberOfWorker_ ;
    int unfinishedTaskCounter = tasks_.getUnfinishedTaskCounter() ;
    if (limit > unfinishedTaskCounter) limit = unfinishedTaskCounter ;
    workers_ = new WorkerThread[limit] ;
    for (int i = 0; i < limit ;  i++) {
      workers_[i] =  new WorkerThread(threadGroup_, "worker-" + i, tasks_) ; 
      workers_[i].setPriority(Thread.MIN_PRIORITY) ; 
      workers_[i].start() ;     
    }
  }
  
  public boolean isRunning() {
    if(workers_ == null) return false ;
    for(int i = 0 ; i < workers_.length; i++) {
      if(workers_[i].isAlive()) return true ;
    }
    return false ;
  }
  
  public void stop() {
    if(workers_ == null) return ;
    for(int i = 0 ; i < workers_.length; i++) {
      if(workers_[i].isAlive()) {
        workers_[i].interrupt();
      }
    }
  }
  
  public int getUnfinishedTaskCounter() {
    if(tasks_ == null) return 0 ;
    return tasks_.getUnfinishedTaskCounter() ;
  }
  
  public void run(TestSuites suites) throws Exception {
    ExoWebClient webClient = new ExoWebClient("exo", defaultURL_);
    webClient.setValidateWebUnit(validateWebUnit_) ;
    webClient.setValidateWellFormedXhtml(validateWellFormedXhtml_) ;
    webClient.setSuites(suites);
    run(webClient) ;
    while(isRunning()) Thread.sleep(1000) ;
  }
  
  static private Object createGroovyObject(String groovyscript) throws Exception { 
    System.out.println("groovy src : " + groovyscript) ;
    ClassLoader parentLoader =  WebLoadRunner.class.getClassLoader() ;
    GroovyClassLoader classloader =  new GroovyClassLoader(parentLoader) ;
    URL  classURL = classloader.getResource(groovyscript) ;
    Class clazz = classloader.parseClass(classURL.openStream()) ;
    return clazz.newInstance() ;
  }
  
  static public void main(String[] args) throws Exception {
    String runnableScript = args[0] ;
    Runnable runnable = (Runnable)createGroovyObject(runnableScript) ;
    runnable.run() ;
  }
  
  /*
  static public void main(String[] args) throws Exception {
    WebLoadRunner runner = new WebLoadRunner() ;
    runner.setDefaultURL(args[0]) ;
    runner.setNumberOfWorker(Integer.parseInt(args[1])) ;
    runner.setNumberOfTask(Integer.parseInt(args[2])) ;
    runner.setValidateWebUnit("true".equals(args[3])) ;
    runner.setValidateWellFormedXhtml("true".equals(args[4])) ;
    String className = args[5] ;
    TestSuites suites = null ;
    if(className.endsWith(".groovy")) {
      suites =  (TestSuites)createGroovyObject(className) ;
    } else {
      Class clazz = Class.forName(className) ;
      suites = (TestSuites)clazz.newInstance() ;
    }
    runner.run(suites) ;
    System.out.print(suites.getTextSummary()) ;
  }
  */
}
