/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.test.web.unit.ClickLinkWithText;
import org.exoplatform.test.web.unit.WebUnit;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: WebUnitSuite.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
abstract public class WebUnitSuite {
  final static private int NAME_COLUMN = 30 ;
  final static private int COUNTER_COLUMN = 8 ;
  final static private int ERROR_COLUMN = 8 ;
  final static private int MALFORMED_COLUMN = 10 ;
  final static private int AVG_TIME_COLUMN = 10 ;
  final static private int CONTENT_LENGTH_COLUMN = 10 ;
  final static private int SUM_CONTENT_LENGTH_COLUMN = 10 ;
  
  final static public int NOT_RUN_STATUS = 0 ;
  final static public int OK_STATUS = 1 ;
  final static public int ERROR_STATUS = 2 ;
  final static public int IGNORE_STATUS = 3 ;
  
  private List units_ ;
  private String name_ ;
  private String description_ ;
  private int status_ = NOT_RUN_STATUS ;
  
  public WebUnitSuite() {
  }
  
  public WebUnitSuite(String name, String description) {
    name_ = name ;
    description_ = description ;
    units_ = new ArrayList() ;
  }
  
  public int  getStatus() { return status_ ; }
  public void setStatus(int status) { status_ = status ; }
  
  public List getRequiredSuites() { return null ; }
  
  public void goToPage(String path) {
  	String[] links = StringUtils.split(path, "/") ;
  	for(int i = 0; i < links.length; i++) {
  		addWebUnit(new ClickLinkWithText("GoTo:" + links[i], "Go to " + links[i] + "page").
  				       setTextLink(links[i])) ;
  	}
  }
  
  public String getName() { return name_ ; }
  public String getDescription() { return description_ ; }
  
  public List getWebUnits() { return units_ ; }

  protected void addWebUnit(WebUnit unit)  {
    units_.add(unit) ;
  }
  
  protected void add(WebUnit unit)  {
    units_.add(unit) ;
  }
  
  public void reset() {
    if(getStatus() == WebUnitSuite.IGNORE_STATUS) return ;
    status_ = NOT_RUN_STATUS ;
    for(int i = 0; i < units_.size() ; i++) {
      WebUnit unit = (WebUnit) units_.get(i) ;
      unit.reset() ;
    }
  }
  
  private void appendColumn(StringBuffer b, String value, int width) {
    b.append(value) ;
    for(int i = 0; i < width- value.length(); i++) {
      b.append(" ") ;
    }
  }
  
  public String getTextSummary() {
    StringBuffer b = new StringBuffer(5000) ;
    b.append("Suite Name: ").append(name_).append("\n");
    b.append("Description: ").append(description_).append("\n");
    appendColumn(b, "Unit Name", NAME_COLUMN) ;
    appendColumn(b, "Counter", COUNTER_COLUMN) ;
    appendColumn(b, "Error", ERROR_COLUMN) ;
    appendColumn(b, "Malformed", MALFORMED_COLUMN) ;
    appendColumn(b, "Avg(ms)", AVG_TIME_COLUMN) ;
    appendColumn(b, "Avg(kb)", CONTENT_LENGTH_COLUMN) ;
    appendColumn(b, "Sum(kb)", SUM_CONTENT_LENGTH_COLUMN) ;
    b.append("\n") ;
    for(int i = 0 ; i < units_.size(); i++) {
      WebUnit unit = (WebUnit) units_.get(i) ; 
      WebUnitMonitor monitor = unit.getMonitor() ;
      appendColumn(b, unit.getName(), NAME_COLUMN) ; 
      appendColumn(b, Integer.toString(monitor.getCounter()), COUNTER_COLUMN) ; 
      appendColumn(b, Integer.toString(monitor.getErrorCounter()), ERROR_COLUMN) ; 
      appendColumn(b, Integer.toString(monitor.getXhtmlMalformedCounter()), MALFORMED_COLUMN) ; 
      appendColumn(b, Long.toString(monitor.getAvgExecutionTime()), AVG_TIME_COLUMN) ; 
      appendColumn(b, Float.toString((float)monitor.getAvgContentLength()/1000), CONTENT_LENGTH_COLUMN) ; 
      appendColumn(b, Float.toString((float)monitor.getSumContentLength()/1000), SUM_CONTENT_LENGTH_COLUMN) ; 
      b.append("\n") ;
    }
    return b.toString() ;
  }
  
  public void appendHtmlTextSummary(StringBuffer b) {
    b.append("Suite Name: ").append(name_).append("<br/>");
    b.append("Description: ").append(description_).append("<br/>");
    b.append("<table class='WebUnitSuite' width='100%'>").
				append("<tr>").
					append("<th>").append("Unit Name").append("</th>").
					append("<th>").append("req").append("</th>").
					append("<th>").append("err").append("</th>").
					append("<th>").append("Malformed").append("</th>").
					append("<th>").append("Avg(ms)").append("</th>").
					append("<th>").append("Avg(kb)").append("</th>").
					append("<th>").append("Sum(kb)").append("</th>").
					append("<th>").append("Desc").append("</th>").
			  append("</tr>");
    for(int i = 0 ; i < units_.size(); i++) {
      WebUnit unit = (WebUnit) units_.get(i) ; 
      WebUnitMonitor monitor = unit.getMonitor() ;
      b.append("<tr>").
      	  append("<td>").append(unit.getName()).append("</td>").
      	  append("<td align='center'>").append(monitor.getCounter()).append("</td>").
      	  append("<td align='center'>").append(monitor.getErrorCounter()).append("</td>").
      	  append("<td align='center'>").append(monitor.getXhtmlMalformedCounter()).append("</td>").
      	  append("<td align='center'>").append(monitor.getAvgExecutionTime()).append("</td>").
      	  append("<td align='center'>").append((float)monitor.getAvgContentLength()/1000).append("</td>").
      	  append("<td align='center'>").append((float)monitor.getSumContentLength()/1000).append("</td>").
      	  append("<td>").append(unit.getDescription()).append("</td>").
				append("</tr>") ;
    }
    b.append("</table>") ;
  }
  
  static public String STYLES = 
    "<style type='text/css'> \n" +
    "  .WebUnitSuite { \n" + 
    "  } \n" +
    "  .WebUnitSuite th { \n" + 
    "    border: 1px solid #CCCCCC ; \n" +
    "    padding: 2px ; \n" +
    "    background: #bbb ; \n" +
    "    color: #074B88; \n" +
    "  } \n" +
    "  .WebUnitSuite  td { \n" + 
    "    border: 1px solid #CCCCCC ; \n" +
    "    padding: 2px ; \n" +
    "  } \n" +
    "</style>\n" ;
  
  public String getHtmlTextSummary() {
    StringBuffer b = new StringBuffer() ;
    b.append("<html>\n") ;
    b.  append("<head>\n") ;
    b.append(STYLES) ;
    b.  append("</head>\n") ;
    b.  append("<body>\n") ;
    appendHtmlTextSummary(b)  ;
    b.  append("</body>\n") ;
    b.append("</html>\n") ;
    return b.toString() ;
  }
}
