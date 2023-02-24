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
 * @version: $Id: CurrentResponseUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class CurrentResponseUnit extends WebUnit {

  public CurrentResponseUnit(String name, String description) {
    super(name, description) ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                       ExoWebClient client) throws Exception {
    return previousResponse ;
  }
  
  public void log(long executionTime , int contentLength, boolean error, boolean malformed) {
  	monitor_.log(0, contentLength, error, malformed) ;
  }
  
  public String getActionDescription() { 
    return "This web unit do not thing, just return the previouse response"; 
  }
}