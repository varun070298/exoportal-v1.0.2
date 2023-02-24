/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 9 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpServletRequest implements HttpServletRequest {
  
  private HttpSession session;
  private Map parameters;
  private Map attributes;
  
  public WSRPHttpServletRequest(HttpSession session){
    this.session = session;
    this.parameters = new HashMap();
    this.attributes = new HashMap();
  }

  public String getAuthType() {
    return null;
  }

  public Cookie[] getCookies() {
    return null;
  }

  public long getDateHeader(String arg0) {
    return 0;
  }

  public String getHeader(String arg0) {
    return null;
  }

  public Enumeration getHeaders(String arg0) {
    return null;
  }

  public Enumeration getHeaderNames() {
    return null;
  }

  public int getIntHeader(String arg0) {
    return 0;
  }

  public String getMethod() {
    return null;
  }

  public String getPathInfo() {
    return null;
  }

  public String getPathTranslated() {
    return null;
  }

  public String getContextPath() {
    return null;
  }

  public String getQueryString() {
    return null;
  }

  public String getRemoteUser() {
    return null;
  }

  public boolean isUserInRole(String arg0) {
    return false;
  }

  public Principal getUserPrincipal() {
    return null;
  }

  public String getRequestedSessionId() {
    return null;
  }

  public String getRequestURI() {
    return null;
  }

  public StringBuffer getRequestURL() {
    return null;
  }

  public String getServletPath() {
    return null;
  }

  public HttpSession getSession(boolean arg0) {
    return session;
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public boolean isRequestedSessionIdValid() {
    return false;
  }

  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  public Object getAttribute(String arg0) {
    return attributes.get(arg0);
  }

  public Enumeration getAttributeNames() {
    return Collections.enumeration(attributes.keySet());
  }

  public String getCharacterEncoding() {
    return null;
  }

  public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
  }

  public int getContentLength() {
    return 0;
  }

  public String getContentType() {
    return null;
  }

  public ServletInputStream getInputStream() throws IOException {
    return null;
  }

  public String getParameter(String arg0) {
    return (String) parameters.get(arg0);
  }

  public Enumeration getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  public String[] getParameterValues(String arg0) {
    Collection c = parameters.values();
    String[] array = new String[c.size()];
    int i = 0;
    for (Iterator iter = c.iterator(); iter.hasNext(); i++) {      
      array[i] = (String) iter.next();
    }
    return array;
  }

  public Map getParameterMap() {
    return Collections.unmodifiableMap(parameters);
  }

  public String getProtocol() {
    return null;
  }

  public String getScheme() {
    return null;
  }

  public String getServerName() {
    return null;
  }

  public int getServerPort() {
    return 0;
  }

  public BufferedReader getReader() throws IOException {
    return null;
  }

  public String getRemoteAddr() {
    return null;
  }

  public String getRemoteHost() {
    return null;
  }

  public void setAttribute(String arg0, Object arg1) {
    attributes.put(arg0, arg1);
  }

  public void removeAttribute(String arg0) {
    attributes.remove(arg0);
  }

  public Locale getLocale() {
    return null;
  }

  public Enumeration getLocales() {
    return null;
  }

  public boolean isSecure() {    
    return false;
  }

  public RequestDispatcher getRequestDispatcher(String arg0) {   
    return null;
  }
  
  //servlet 2.4 method
  public int getLocalPort(){
    return 0;
  }

  public String getLocalAddr(){
    return "local adress";
  }

  public String getLocalName(){
    return "Local name";
  }

  public int getRemotePort(){
   return 0;
  }
  
  public void setParameter(String string, String string2) {
    parameters.put(string, string2); 
  }    

  //depracated methods
  public String getRealPath(String arg0) {    
    return null;
  }
  
  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  

}
