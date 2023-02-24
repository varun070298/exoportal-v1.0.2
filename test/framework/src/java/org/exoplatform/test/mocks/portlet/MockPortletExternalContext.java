/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.portlet;

import java.util.ResourceBundle;

import javax.portlet.* ;
import javax.servlet.http.*;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.faces.context.*;
import org.exoplatform.test.mocks.jsf.*;

/**
 * Apr 25, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockPortletExternalContext.java,v 1.1 2004/10/11 23:27:26 tuan08 Exp $
 **/
public class MockPortletExternalContext 
  extends MockExternalContext  implements PortletExternalContext {
  private MockPortletConfig portletConfig_ ;
  private MockPortletContext portletContext_ ;
  private MockPortletRequest portletRequest_ ;
    
  public MockPortletExternalContext() {
    portletContext_ = new MockPortletContext() ;
    portletConfig_ = new MockPortletConfig(portletContext_) ;
    portletRequest_ = new MockPortletRequest() ;
  }
  
  public void init(HttpServletRequest request,  HttpServletResponse response) {
  	super.init(request, response) ;
  }
  
  public PortletPreferences getPortletPreferences() {
    return portletRequest_.getPreferences() ; 
  }
  
  public Object getRequest() { return  portletRequest_ ;  }
  
  public MockPortletConfig getMockPortletConfig() {
    return portletConfig_ ; 
  }
  
  public MockPortletContext getMockPortletContext() {
    return portletContext_ ; 
  }
  
  public String encodeActionURL( String s ) {
  	if (s == null || s.length() == 0) {
      s = request_.getRequestURI() ;
      if(s.indexOf("?") < 0) s += "?junk=junk" ;
    	return s ;
    }else if ("http://".startsWith(s)) {
      return s ;
    } else {
    	String baseURL = request_.getRequestURI() ;
      //s = StringUtils.replace(s, "&", "?" , 1) ;
    	return baseURL + "?viewId=" + s ; 
    }
  }
  
  public PortletConfig getConfig() { return portletConfig_ ; }
  public ResourceBundle getApplicationResourceBundle() { return null ; }
}