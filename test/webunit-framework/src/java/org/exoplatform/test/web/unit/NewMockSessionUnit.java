/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.unit;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NewMockSessionUnit.java,v 1.1 2004/10/11 23:36:06 tuan08 Exp $
 **/
public class NewMockSessionUnit extends WebUnit {
  private static String SERVLET_NAME = "portal";
	private String servletName_ ;
  private String uicomponent_ ;
  private ServletRunner srunner_ ;
  
  public NewMockSessionUnit(String name, String description) {
    super(name, description) ;
    srunner_ =  new ServletRunner();
  }
  
  public NewMockSessionUnit setServletName(String name) {
    servletName_  = name ;
    srunner_.registerServlet(SERVLET_NAME, servletName_);
    return this ;
  }
  
  public NewMockSessionUnit setUIComponent(String component) {
    uicomponent_  = component ;
    return this ;
  }
  
  public WebResponse execute(WebResponse previousResponse, WebTable block, 
														 ExoWebClient client) throws Exception {
  	ServletUnitClient webClient = srunner_.newClient();
  	WebRequest request   = new GetMethodWebRequest("http://localhost/" + SERVLET_NAME);
  	request.setParameter("component", uicomponent_) ;
    client.setWebClient(webClient) ;
    client.setHomePageURL("http://localhost/" + SERVLET_NAME) ;
  	WebResponse response = webClient.getResponse( request );
  	return response ;
  }
  
  public String getActionDescription() { 
    return "This web unit create a new mock session environment for the servlet" + servletName_ ; 
  }
}