/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 9 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPHttpServletResponse implements HttpServletResponse {

  public void addCookie(Cookie arg0) {
  }

  public boolean containsHeader(String arg0) {
    return false;
  }

  public String encodeURL(String arg0) {
    return null;
  }

  public String encodeRedirectURL(String arg0) {
    return null;
  }

  public void sendError(int arg0, String arg1) throws IOException {
  }

  public void sendError(int arg0) throws IOException {
  }

  public void sendRedirect(String arg0) throws IOException {
  }

  public void setDateHeader(String arg0, long arg1) {
  }

  public void addDateHeader(String arg0, long arg1) {
  }

  public void setHeader(String arg0, String arg1) {
  }

  public void addHeader(String arg0, String arg1) {
  }

  public void setIntHeader(String arg0, int arg1) {
  }

  public void addIntHeader(String arg0, int arg1) {
  }

  public void setStatus(int arg0) {
  }

  public String getCharacterEncoding() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return null;
  }

  public void setContentLength(int arg0) {
  }

  public void setContentType(String arg0) {
  }

  public void setBufferSize(int arg0) {
  }

  public int getBufferSize() {
    return 0;
  }

  public void flushBuffer() throws IOException {
  }

  public void resetBuffer() {
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void setLocale(Locale arg0) {
  }

  public Locale getLocale() {
    return null;
  }
  
  //servlet 2.4
  public void setCharacterEncoding(String enc){      
  }
  
  public String getContentType(){
    return null;
  }
  
  //deprecated methods
  public String encodeUrl(String arg0) {
    return null;
  }

  public String encodeRedirectUrl(String arg0) {
    return null;
  }  
  
  public void setStatus(int arg0, String arg1) {
  }  

}
