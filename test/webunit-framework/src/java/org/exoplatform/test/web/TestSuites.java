/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

import java.util.* ;
import org.exoplatform.test.web.unit.WebUnit;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 2, 2004
 * @version $Id: TestSuites.java,v 1.1 2004/10/11 23:36:03 tuan08 Exp $
 */
public class TestSuites {
  private List suites_ ;
  private int suiteIdx_ ;
  private int unitIdx_ ;
  private WebUnit currentUnit_ ;
  private WebUnitSuite currentSuite_ ;
  
  public TestSuites() {
    suites_ = new ArrayList() ;
  }
  
  public void addSuite(WebUnitSuite suite) { suites_.add(suite) ; }
  public List getSuites() { return suites_ ; }
  public void setSuites(List  suites) { suites_ = suites ; }

  public void reset() {
    suiteIdx_  = 0 ;
    unitIdx_ = 0 ;
    for(int i = 0; i < suites_.size(); i++) {
      WebUnitSuite suite = (WebUnitSuite)suites_.get(i) ;
      if(suite.getStatus() != WebUnitSuite.IGNORE_STATUS) {
        suite.setStatus(WebUnitSuite.NOT_RUN_STATUS) ;
      }
      List units  = suite.getWebUnits() ;
      for(int j = 0; j  < units.size(); j++) {
        WebUnit unit = (WebUnit) units.get(j) ;
        unit.getMonitor().reset() ;
      }
    }
  }
  
  public WebUnit getCurrentWebUnit() { return currentUnit_ ;  }
  public WebUnitSuite getCurrentWebUnitSuite() { return currentSuite_ ; }
  
  public boolean nextUnit() {
    currentUnit_ = null ;
    if(suiteIdx_ == suites_.size()) {
      currentSuite_ = null ;
      return false  ;
    }
    WebUnitSuite currentSuite = (WebUnitSuite)suites_.get(suiteIdx_) ;
    if(currentSuite.getStatus() == WebUnitSuite.IGNORE_STATUS) {
      suiteIdx_++ ;
      unitIdx_ = 0 ;
      return nextUnit() ;
    }
    if(unitIdx_ == currentSuite.getWebUnits().size()) {
      suiteIdx_++ ;
      unitIdx_ = 0 ;
      return nextUnit() ;
    }
    currentUnit_ =  (WebUnit) currentSuite.getWebUnits().get(unitIdx_) ;
    currentSuite_ = currentSuite ;
    if(currentSuite_.getStatus() == WebUnitSuite.NOT_RUN_STATUS) {
      currentSuite_.setStatus(WebUnitSuite.OK_STATUS) ;
    }
    unitIdx_++ ; 
    return true ;
  }
  
  public TestSuites softClone()  {
    TestSuites ts = new TestSuites() ;
    ts.setSuites(suites_) ;
    return ts ;
  }
  
  public String getHtmlSummary() throws Exception {
    StringBuffer b = new StringBuffer(10000) ;
    b.append("<html>").
        append("<body>") ;
    for(int i = 0 ; i < suites_.size(); i++) {
      WebUnitSuite suite = (WebUnitSuite) suites_.get(i) ;
      suite.appendHtmlTextSummary(b) ;
    }
    b.  append("</body>").
      append("</html>") ;
    return b.toString() ;
  }
  
  public String getTextSummary() {
    StringBuffer b = new StringBuffer(10000) ;
    for(int i = 0 ; i < suites_.size(); i++) {
      WebUnitSuite suite = (WebUnitSuite) suites_.get(i) ;
      b.append(suite.getTextSummary()).append("\n") ;
    }
    return b.toString() ;
  }
}