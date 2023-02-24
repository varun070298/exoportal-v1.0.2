/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.unit;

import java.util.* ;
import org.exoplatform.test.web.*;
import org.exoplatform.test.web.condition.Condition;
import org.exoplatform.test.web.validator.Validator;
import com.meterware.httpunit.*;

/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WebUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
abstract public class WebUnit {
	final static public String ACTION = "op" ;

  private String name_ ;
  private String description_ ;
  protected List validators_ ;
  protected List conditions_ ;
  protected WebUnitMonitor monitor_ ;
  protected String blockId_;

  public WebUnit() {   
    name_ = getClass().getName() ;
    description_ = name_ ;
    monitor_ = new WebUnitMonitor() ;
  }

  

  public WebUnit(String name , String desc) {
    name_ = name ;
    description_ = desc ;
    monitor_ = new WebUnitMonitor() ;
  }
 
  public void reset() { monitor_.reset() ; }
  
  public String getBlockId() { return blockId_ ; }

  public WebUnit setBlockId(String id) { 
  	blockId_ = id ;
  	return this ;
  }

  public String getName() { return name_ ; }

  public String getActionDescription() { return "No Description" ; }
  
  public String getDescription() { 
    if(description_ == null) return "No Description" ;
    return description_ ; 
  }
  
  public String getExtraInfo() { return "N/A" ; }
  
  abstract public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                                ExoWebClient client) throws Exception ;

  public List getValidators() { return validators_ ; }

  public WebUnit addValidator(Validator validator) {
    if(validators_ == null) validators_ = new ArrayList() ;
    validators_.add(validator) ;
    return this ;
  }


  public List getConditions() { return conditions_ ; }

  public WebUnit addCondition(Condition condition) {
    if(conditions_ == null) conditions_ = new ArrayList() ;
    conditions_.add(condition) ;
    return this ;
  }

  public WebUnitMonitor getMonitor() { return monitor_ ; }

  

  static public String getRealValue(ExoWebClient client, String value)  {
    if(value.length() < 3 )  return value ;
    String oldValue = value ;
    if(value.charAt(0) == '#' && value.charAt(1) == '{' &&
       value.charAt(value.length() - 1) == '}') {
      value = value.substring(2, value.length() - 1) ;
      value = (String) client.getAttribute(value) ;
      if(value == null) return oldValue ;
    }
    return value ;
  }

  public boolean checkConditions(WebResponse response ,WebTable block, ExoWebClient client)  throws Exception {
    if(conditions_ != null) {
     	for(int k = 0 ; k < conditions_.size(); k++) {
     		Condition condition = (Condition) conditions_.get(k) ;
     		if (!condition.checkCondition(response, block, client)) {
     			return false ;
     		}
     	}
    }
    return true ;
  }

  public boolean validate(WebResponse response , ExoWebClient client)  throws Exception {
  	if(validators_ != null) {
      for(int k = 0 ; k < validators_.size(); k++) {
        Validator validator = (Validator) validators_.get(k) ;
        if (!validator.validate(response, client)) {  
          return false  ;
        }
      }
    }
  	return true ;
  }

  public void log(long executionTime , int contentLength, boolean error, boolean malformed) {
  	monitor_.log(executionTime, contentLength, error, malformed) ;
  }
  
  static public String STYLES = 
    "<style type='text/css'> \n" +
    "  .WebUnit { \n" + 
    "  } \n" +
    "  .WebUnit th { \n" + 
    "    border: 1px solid #CCCCCC ; \n" +
    "    padding: 2px ; \n" +
    "    background: #bbb ; \n" +
    "    color: #074B88; \n" +
    "  } \n" +
    "  .WebUnit  td { \n" + 
    "    border: 1px solid #CCCCCC ; \n" +
    "    padding: 2px ; \n" +
    "  } \n" +
    "</style>\n" ;
  
  public String getUnitSummaryInXHTML() {
    StringBuffer b = new StringBuffer() ;
    b.append("<html>\n") ;
    b.  append("<head>\n") ;
    b.append(STYLES) ;
    b.  append("</head>\n") ;
    b.  append("<body>\n") ;
    b.    append("<table class='WebUnit'>\n") ;
    b.      append("<tr>\n") ;
    b.        append("<td>Web Unit Name</td>\n") ;
    b.        append("<td>").append(name_).append("</td>\n") ;
    b.      append("</tr>\n") ;
    
    b.      append("<tr>\n") ;
    b.        append("<td>Description</td>\n") ;
    b.        append("<td>").append(getDescription()).append("</td>\n") ;
    b.      append("</tr>\n") ;
    
    b.      append("<tr>\n") ;
    b.        append("<td>Action Description</td>\n") ;
    b.        append("<td>").append(getActionDescription()).append("</td>\n") ;
    b.      append("</tr>\n") ;
    
    if(conditions_ != null) {
      b.    append("<tr>\n") ;
      b.      append("<td>Conditions</td>\n") ;
      b.      append("<td>");
      for(int i = 0 ; i < conditions_.size(); i++) {
        Condition condition = (Condition) conditions_.get(i) ;
        b.append(condition.getName()).append(": ").
          append(condition.getDescription()).append("<br/>") ;
      }
      b.      append("</td>\n") ;
      b.    append("</tr>\n") ;
    }
    
    if(validators_ != null) {
      b.    append("<tr>\n") ;
      b.      append("<td>Validators</td>\n") ;
      b.      append("<td>");
      for(int i = 0 ; i < validators_.size(); i++) {
        Validator validator = (Validator) validators_.get(i) ;
        b.append(validator.getName()).append(": ").
          append(validator.getDescription()).append("<br/>") ;
      }
      b.      append("</td>\n") ;
      b.    append("</tr>\n") ;
    }
    
    b.      append("<tr>\n") ;
    b.        append("<td>Extra Information</td>\n") ;
    b.        append("<td>").append(getExtraInfo()).append("</td>\n") ;
    b.      append("</tr>\n") ;
    
    b.    append("<table>\n") ;
    b.  append("</body>\n") ;
    b.append("</html>\n") ;
    return b.toString() ;
  }
}
