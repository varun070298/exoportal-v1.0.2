/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.context.ExternalContext;
import javax.servlet.http.*;
import org.exoplatform.commons.map.*;


public class MockExternalContext extends ExternalContext {
protected HttpServletRequest request_ ;
  protected HttpServletResponse response_ ;
  protected Map  requestMap_ ;
  protected Map  requestParameterMap_ ;
  
  public void init(HttpServletRequest request,  HttpServletResponse response) {
    request_ = request ;
    response_ = response ;
    requestMap_ = null ;
    requestParameterMap_ = null ;
  }

  public Object getSession( boolean create ) { return request_.getSession(create) ; }

  public Object getContext() { return null ; }

  public Object getRequest() { return  request_ ;  }

  public Object getResponse() { return response_  ; }
  
  public ResourceBundle getApplicationResourceBundle() { return null ; }

  public String getInitParameter( String s ) { return null ; }

  public Map getInitParameterMap() { return null ; }

  public Map getApplicationMap() { return null ; }

  public Map getSessionMap() { return null  ; }

  public Map getRequestMap() { 
  	if(requestMap_ == null ) requestMap_ = new RequestMap(request_) ; 
    return  requestMap_ ;
  }

  public Map getRequestParameterMap() { 
  	if(requestParameterMap_ == null ) requestParameterMap_ = new RequestParameterMap(request_) ; 
    return  requestParameterMap_ ;
  }

  public Map getRequestParameterValuesMap() { return null ; }

  public Iterator getRequestParameterNames() { return null ; }

  public Map getRequestHeaderMap() { return null ; }

  public Map getRequestHeaderValuesMap() { return null ; }

  public Map getRequestCookieMap() { return null ; }

  public Locale getRequestLocale() { return null ; }
  public Iterator getRequestLocales() { return null ; }

  public String getRequestContextPath() { return null ; }

  public String getRequestPathInfo() { return  request_.getRequestURI() ; }

  public Cookie[] getRequestCookies() { return null ; }

  public Set getResourcePaths( String s ) { return null ; }
  public InputStream getResourceAsStream( String s ) {  return null ;  }
  
  public URL getResource( String s ) throws MalformedURLException { 
    return null ; 
  }

  public String encodeActionURL( String s ) { return s ; }

  public String encodeResourceURL( String s ) { return s; }

  public String encodeURL( String s ) { return s ; }

  public String encodeNamespace( String s ) { return s; }

  public void dispatch( String requestURI ) throws IOException  {
  }

  public void redirect( String sI ) throws IOException {
  }
  
  public void log(String message) { 
    System.out.println(message);
  }
  
  public void log(String message, Throwable throwable) {
  	System.out.println(message);
  	throwable.printStackTrace() ;
  }
  
  public String getRequestServletPath() {
    throw new UnsupportedOperationException();
  }
  
  public java.lang.String getAuthType() { return null ; }
  
  public java.lang.String getRemoteUser() { return "anonymous" ; }
  
  final public java.security.Principal getUserPrincipal() { return null ; }

  final public boolean isUserInRole(java.lang.String role) { return false  ; }
  
  public void reset() {
  }
}
