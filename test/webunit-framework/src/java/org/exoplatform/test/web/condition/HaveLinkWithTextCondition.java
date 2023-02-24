/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.condition;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;

/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: HaveLinkWithTextCondition.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
public class HaveLinkWithTextCondition implements Condition {
  private String text_ ;
  
  public HaveLinkWithTextCondition(String text) {
    text_ = text ;
  }
  
  public boolean checkCondition(WebResponse response, WebTable block, ExoWebClient client) throws Exception {
    WebLink link = response.getLinkWith(text_) ;
    return link != null ;
  }
  
  public String getName() { return "HaveLinkWithTextCondition" ; }
  public String getDescription() {
    return "This unit test should run only if the previous return response has the the text '" + text_ + "'" ;
  }
}