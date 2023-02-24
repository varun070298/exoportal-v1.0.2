/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces.context;

import org.apache.commons.logging.Log;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.map.*;
import org.exoplatform.commons.utils.EnumIterator;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.*;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.portlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
/**
 * The JSR-127 specification chapter 6.1.1 outlines the requirements of the ExternalContext that Java Server Faces
 * depends on. The ExternalContext implements a thin layer between two external resources and the portlet facility,
 * so they can share resource data - namely a bridge.
 *
 * The FacesPortletContextFactoryImpl is the callee of this object.
 *
 * @author Ove Ranheim (oranheim@users.sourceforge.net)
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 6, 2003 4:18:34 PM
 *
 */
public class ExternalContextImpl extends ExternalContext implements PortletExternalContext {
  private PortletConfig portletConfig_ ;
  private PortletContext portletContext_;
  private ServletContext servletContext_;
  private PortletRequestImp portletRequest_;
  private PortletResponseImp portletResponse_;
  private ResourceBundle applicationResource_ ;
  private String requestPathInfo_ ;
  private boolean renderPhase_ ;

  private Map m_applicationMap;
  private Map m_requestParameterMap;
  private Map m_requestParameterValuesMap;
  private Map m_requestHeaderMap;
  private Map m_requestHeaderValuesMap;
  private Map m_requestCookieMap;
  private Map m_initParameterMap;


  public ExternalContextImpl(Object config, Object request, Object response, boolean renderPhase) {
    portletConfig_ = (PortletConfig) config ;
    HttpServletResponse httpResponse = (HttpServletResponse) response ;
    applicationResource_ = portletConfig_.getResourceBundle(httpResponse.getLocale()) ;
    portletContext_ = portletConfig_.getPortletContext() ;
    servletContext_ = ((PortletContextImpl)portletContext_).getWrappedServletContext() ;
    portletRequest_ = (PortletRequestImp) request;
    portletResponse_ = (PortletResponseImp) response;
    renderPhase_ = renderPhase ;
  }

  protected void finalize() throws Throwable {
    m_applicationMap = null;
    m_requestParameterMap = null;
    m_requestParameterValuesMap = null;
    m_requestHeaderMap = null;
    m_requestHeaderValuesMap = null;
    m_requestCookieMap = null;
    m_initParameterMap = null;
    portletConfig_ = null;
    portletContext_ = null;
    portletRequest_ = null;
    portletResponse_ = null;
    requestPathInfo_ = null ;
    super.finalize();
  }

  public Object getSession( boolean create ) {
    return portletRequest_.getPortletSession( create );
  }

  public Object getContext() { return servletContext_; }

  public Object getRequest() { return portletRequest_; }

  public Object getResponse() { return portletResponse_; }
    
  public PortletConfig getConfig() { return portletConfig_  ; }

  public ResourceBundle getApplicationResourceBundle() { return applicationResource_  ; }

  public String getInitParameter( String s ) { return portletContext_.getInitParameter( s ); }

  public Map getInitParameterMap() {
    if (m_initParameterMap == null) {
      m_initParameterMap = new InitParameterMap( portletContext_ );
    }
    return m_initParameterMap;
  }


  public Map getApplicationMap() {
    if (m_applicationMap == null) {
      m_applicationMap = new ApplicationMap( portletContext_ );
    }
    return m_applicationMap;
  }

  public Map getSessionMap() {
    return (PortletSessionImp) portletRequest_.getPortletSession();
  }

  public Map getRequestMap() { return portletRequest_ ; }

  public Map getRequestParameterMap() {
    if (m_requestParameterMap == null) {
      m_requestParameterMap = new RequestParameterMap( portletRequest_ );
    }
    return m_requestParameterMap;
  }

  public Map getRequestParameterValuesMap() {
    if (m_requestParameterValuesMap == null) {
      m_requestParameterValuesMap = new RequestParameterValuesMap( portletRequest_ );
    }
    return m_requestParameterValuesMap;
  }

  public Iterator getRequestParameterNames() {
    return new EnumIterator( portletRequest_.getParameterNames() );
  }

  public Map getRequestHeaderMap() { return null ; }

  public Map getRequestHeaderValuesMap() { return null ; }

  public Map getRequestCookieMap() { return null ; }

  public Locale getRequestLocale() { return portletRequest_.getLocale(); }
  public Iterator getRequestLocales() { return null ; }

  public String getRequestContextPath() {
    String path = portletRequest_.getContextPath();
    return path;
  }

  public String getRequestPathInfo() {
    if (requestPathInfo_ == null) { 
      requestPathInfo_ = 
        (String) portletRequest_.getAttribute("javax.servlet.include.path_info") ;
    }
    return  requestPathInfo_  ;
  }


  public Cookie[] getRequestCookies() { return null ; }

  public Set getResourcePaths( String s ) { return portletContext_.getResourcePaths( s ); }
  public InputStream getResourceAsStream( String s ) { 
    return portletContext_.getResourceAsStream( s ); 
  }
  
  public URL getResource( String s ) throws MalformedURLException { 
    return portletContext_.getResource( s ); 
  }

  public String encodeActionURL( String s ) {
    if(!renderPhase_) 
      throw new IllegalStateException("You cannot use this in processAction phase") ;
    RenderResponse renderResponse = (RenderResponse) portletResponse_ ;
    if(s == null || s.length() == 0) {
      return renderResponse.createActionURL().toString()  ;
    } else if ("http://".startsWith(s)) {
      return s ;
    } else {
      int question = s.indexOf('?') ;
      if (question > 0) {
        s = StringUtils.replace(s, "?", "&" , 1) ;
      }
      s = "&.view-id=" + s ;
      return renderResponse.createActionURL().toString() + s ;
    }
  }

  public String encodeResourceURL( String s ) { return s; }

  public String encodeURL( String s ) { return s ; }

  public String encodeNamespace( String s ) { return s; }

  public void dispatch( String requestURI ) throws IOException  {
    if (!(portletResponse_ instanceof RenderResponse)) throw new UnsupportedOperationException();
    PortletRequestDispatcher prequestDispatcher = portletContext_.getRequestDispatcher( requestURI );
    try {
      prequestDispatcher.include( (RenderRequest) portletRequest_, (RenderResponse) portletResponse_ );
    } catch (PortletException e) {
      getLog().error("error: " , e);
    }
  }

  public void redirect( String sI ) throws IOException {
  }
  
  public void log(String message) { portletContext_.log(message) ; }
  
  public void log(String message, Throwable throwable) {
    portletContext_.log(message, throwable) ;
  }
  
  public String getRequestServletPath() {
    throw new UnsupportedOperationException();
  }
  
  public java.lang.String getAuthType() { return portletRequest_.getAuthType(); }
  
  public java.lang.String getRemoteUser() { return portletRequest_.getRemoteUser() ; }
  
  final public java.security.Principal getUserPrincipal() {
    return portletRequest_.getUserPrincipal() ;
  }

  final public boolean isUserInRole(java.lang.String role) {
    return portletRequest_.isUserInRole(role) ; 
  }
  
  public ExoWindowID getWindowID() { 
    return (ExoWindowID) portletRequest_.getPortletWindowInternal().getWindowID() ; 
  }
  
  private Log getLog() {
    return  LogUtil.getLog("org.exoplatform.portlet.faces.faces");
   }
}
