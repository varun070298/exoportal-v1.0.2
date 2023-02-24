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
 * @version: $Id: AddRoleUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class AddRoleUnit extends WebUnit {
	
	private String roleToAdd_ ;
	
  public AddRoleUnit(String name, String description) {
    super(name, description) ;
  }
  
  public WebUnit setRoleToAdd(String role) { 
  	roleToAdd_ = role ;
  	return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
  		                       ExoWebClient client) throws Exception {
  	client.getRoles().put(roleToAdd_, roleToAdd_) ;
    return previousResponse ;
  }
  
  public void log(long executionTime , int contentLength, boolean error, boolean malformed) {
  	monitor_.log(0, contentLength, error, malformed) ;
  }
  
  public String getActionDescription() { 
    return "This unit do not submit any request to the server. It just add the role '" + roleToAdd_  +"' "  +
           "to web client so later the web client can decide to run/ignore certain web units"; 
  }
}