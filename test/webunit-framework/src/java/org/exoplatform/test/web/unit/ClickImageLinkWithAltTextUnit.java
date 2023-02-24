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
 * @version: $Id: ClickImageLinkWithAltTextUnit.java,v 1.2 2004/10/21 15:26:23 tuan08 Exp $
 **/
public class ClickImageLinkWithAltTextUnit extends WebUnit {
  private String altText_ ;

  public ClickImageLinkWithAltTextUnit(String name, String description) {
    super(name, description) ;
  }
  
  public ClickImageLinkWithAltTextUnit setAltText(String text) {
    altText_ = text ;
    return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                       ExoWebClient client) throws Exception {
    WebLink link = previousResponse.getLinkWithImageText(altText_);
    return link.click() ;
  }
  
  public String getActionDescription() { 
    return "This web unit look for the image link with alt text '" + altText_ + "' and click on it"; 
  }
}