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
 * @version: $Id: ClickURLLinkUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class ClickURLLinkUnit extends WebUnit {
  protected String url_ ;

  public ClickURLLinkUnit(String name, String description) {
    super(name, description) ;
  }
  
  public ClickURLLinkUnit setURL(String text) {
    url_ = text ;
    return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                       ExoWebClient client) throws Exception {
    String name = client.getName() ;
    String url = url_.replaceAll("#\\{client.name\\}", name) ;
    WebLink link = Util.findLinkWithURL(previousResponse, block, url);
    if(link != null)  return link.click() ;
    throw new Exception("Cannot find the link " + url_ + ", Block ID " + getBlockId()) ;
  }
  
  public String getActionDescription() { 
    return "This web unit look for the link with the partial url '..." + url_ + "...' and click on it"; 
  }
}