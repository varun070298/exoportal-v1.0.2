/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

import java.util.*;
import org.exoplatform.test.web.unit.WebUnit;
import org.exoplatform.test.web.validator.WellFormedXhtmlValidator;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoWebClient.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 **/
public class ExoWebClient implements Runnable {
  private static WellFormedXhtmlValidator 
    wellFormedXhtmlValidator_ = new WellFormedXhtmlValidator() ;
  private String name_ ;
  private String url_ ;
  private TestSuites suites_ ;
  private long interval_  = 0 ;
  private Map attributes_;
  private Map roles_ ;
  
  private boolean validateWellFormedXhtml_ ;
  private boolean validateWebUnit_ ;
  private boolean printSummary_ ;
  private WebClient webClient_ ;
  private TimeListener timeListener_ ;
  private boolean error_ ;
  
  private WebResponse response_ ;
  
  public ExoWebClient(String name, String url) {
    name_ = name ;
    url_ = url ;
    validateWebUnit_ = true ; 
    validateWellFormedXhtml_ = false ;
    printSummary_ = true ;
    webClient_ = new WebConversation();
    timeListener_ = new TimeListener() ;
    webClient_.addClientListener(timeListener_) ;
    
    attributes_ = new HashMap() ;
    roles_ = new HashMap() ;
    roles_.put("guest", "guest") ;
    attributes_.put("client.name", name_ );
    reset() ;
  }
 
  public void reset() {
    response_ = null ;
    error_ = false ;
  }
  
  public String getName() { return name_ ; }
  public void   setName(String name) { name_ = name ; }
  
  public String getHomePageURL() { return url_ ; }
  public void   setHomePageURL(String s) { url_ = s ; }
 
  public long  getInterval() { return interval_ ; }
  public void  setInterval(long interval) { interval_ = interval ; }
  
  public Object getAttribute(String key) { return attributes_.get(key) ; }
  public void   setAttribute(String key , Object obj) { attributes_.put(key, obj) ;} 
  
  public Map getRoles() { return roles_ ; }

  public void setSuites(TestSuites  suites) { suites_ = suites ; }
  
  public WebClient getWebClient() { return webClient_ ; }
  public void setWebClient(WebClient wc) { 
  	webClient_ = wc ; 
  	webClient_.addClientListener(timeListener_) ;
  }

  public void setValidateWellFormedXhtml(boolean b) {  validateWellFormedXhtml_ = b ; }
  
  public void setValidateWebUnit(boolean b) {  validateWebUnit_ = b ; }
  
  public boolean getPrintSummary() { return printSummary_ ; }
  public void    setPrintSummary(boolean b) { printSummary_ = b ; }
  
  public boolean hasError() { return error_ ; }
  
  public WebResponse getResponse() { return response_ ; }
  
  public String getResponseText() throws Exception { return response_.getText() ; }
  
  public WebResponse beforeRunSuite(WebResponse previousResponse) throws Exception {
    return previousResponse ;
  }
  
  public WebResponse afterRunSuite(WebResponse previousResponse) throws Exception {
    return previousResponse ;
  }
  
  public void executeUnit(WebUnit webUnit) throws Exception {
    WebTable table  = null ;
    if(webUnit.getBlockId() != null) {
        table = response_.getFirstMatchingTable(WebTable.MATCH_ID, webUnit.getBlockId()) ;
    } 
    //check conidtions
    if(!webUnit.checkConditions(response_, table,  this)) return   ;
    response_ = webUnit.execute(response_, table,this) ;
    int contentLength = response_.getText().length()  ;
    boolean malformed = false ;
    if(validateWellFormedXhtml_) {
      malformed = !wellFormedXhtmlValidator_.validate(response_, this) ;
    }
    boolean hasError = !webUnit.validate(response_, this) ;
    webUnit.log(timeListener_.getExecutionTime(), contentLength, hasError, malformed) ;
    if(hasError) {
      error_ = true ;
      suites_.getCurrentWebUnitSuite().setStatus(WebUnitSuite.ERROR_STATUS) ;
    }
    return  ;
  }
  
  public void run()  { run(suites_) ; }
  
  public void run(TestSuites suites)  {
    WebUnit webUnit = null ;
    try {  
      suites_  = suites ;
      while(suites.nextUnit()) {
        webUnit = suites.getCurrentWebUnit() ;
        executeUnit(webUnit) ;
        if(interval_  > 0 ) Thread.sleep(interval_) ;
        if(Thread.interrupted()) return ;
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt() ;
    } catch (Exception ex) {
      if(webUnit != null) {
        System.err.println(webUnit.getUnitSummaryInXHTML()) ;
      }
      ex.printStackTrace() ;
    }
  }
  
  public String getTextSummary() {
    if(suites_ == null) { return "N/A" ; }
    return suites_.getTextSummary() ;
  }
  
  public String getHtmlSummary() throws Exception {
  	if(suites_ == null) { return "N/A" ; }
    return suites_.getHtmlSummary() ;
  }
  
  public ExoWebClient clone(String name) {
    ExoWebClient newClient = new ExoWebClient(name, url_) ;
    if(suites_ != null)  newClient.setSuites( suites_.softClone()) ;
    newClient.setInterval(interval_) ;
    newClient.setValidateWebUnit(validateWebUnit_) ;
    newClient.setValidateWellFormedXhtml(validateWellFormedXhtml_) ;
    return newClient ;
  }

  class TimeListener implements WebClientListener {
    private long endTime_ ;
    private long startTime_ ;
    
    public long getExecutionTime() { return endTime_ - startTime_ ;}
    
    public void requestSent(com.meterware.httpunit.WebClient src, WebRequest req) {
      startTime_ = System.currentTimeMillis() ;
    }

    public void responseReceived(com.meterware.httpunit.WebClient src, WebResponse resp) {
      endTime_ = System.currentTimeMillis() ;
    }
  }
}
