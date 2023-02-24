/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.unit;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NewSessionUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class NewSessionUnit extends WebUnit {
  private String website_ ;
  
  public NewSessionUnit(String name, String description) {
    super(name, description) ;
  }
  
  public NewSessionUnit setWebsite(String url) {
    website_ = url ;
    return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                       ExoWebClient client) throws Exception {
    WebConversation wc = new WebConversation() ;
    client.setWebClient(wc) ;
    String url = website_ ;
    if(url == null) url = client.getHomePageURL() ;
    WebRequest     req = new GetMethodWebRequest(url);
    WebResponse   response = wc.getResponse( req );
    return response ;
  }
  
  public String getActionDescription() { 
    return "This web unit create a new session for the site '" + website_ + "' " +
           "if there is no defined website, the web unit will use the default website of the web client at the runtime"; 
  }
}