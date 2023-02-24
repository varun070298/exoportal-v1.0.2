/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.unit;

import org.exoplatform.test.web.*;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ClickLink.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ClickLink extends WebUnit {
  private String textLink_ ;

  public ClickLink(String name, String description) {
    super(name, description) ;
  }
  
  public ClickLink setTextLink(String text) {
    textLink_ = text ;
    return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse,WebTable block, 
  		                       ExoWebClient client) throws Exception {
  	WebLink link = Util.findLink(previousResponse, block, textLink_) ;
  	if(link != null)  return link.click() ;
  	throw new Exception("Cannot find the link: " + textLink_ + ", Block ID " + getBlockId()) ;
  }
  
  public String getActionDescription() { 
    return "This web unit look for the link with the exact matched text '" + textLink_ + "' and click on it"; 
  }
}