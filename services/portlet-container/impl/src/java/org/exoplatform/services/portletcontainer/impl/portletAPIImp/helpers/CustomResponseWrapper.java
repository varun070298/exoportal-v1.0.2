/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 8:22:50 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.exoplatform.services.portletcontainer.helper.URLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomResponseWrapper extends HttpServletResponseWrapper {
  
  private PrintWriter tmpWriter;
  private ByteArrayOutputStream output;
  private ByteArrayServletOutputStream servletOutput;
  private URLEncoder urlEncoder_ ;  
  private CharArrayWriter charArrayWriter;
  private boolean writerAlreadyCalled;
  private boolean outputStreamAlreadyCalled;


  public CustomResponseWrapper(HttpServletResponse httpServletResponse) {
    super(httpServletResponse);
  }

  public void fillResponseWrapper(HttpServletResponse httpServletResponse) {
    super.setResponse(httpServletResponse);
    output = new ByteArrayOutputStream();
    servletOutput = new ByteArrayServletOutputStream(output);

    charArrayWriter = new CharArrayWriter();
    tmpWriter = new PrintWriter(charArrayWriter);
    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;
  }

  public void emptyResponseWrapper() {
    output = null;   
    tmpWriter = null;
    servletOutput = null;
    writerAlreadyCalled = false;
    outputStreamAlreadyCalled = false;    
  }

  public void finalize() throws Throwable {
    super.finalize();
    servletOutput.close();
    output.close();
    tmpWriter.close();
  }

  public char[] getPortletContent() {    
    return charArrayWriter.toCharArray();
  }

  public PrintWriter getWriter() throws IOException {
    if(outputStreamAlreadyCalled)
      throw new IllegalStateException("the PrintWriter object has already been called");
    writerAlreadyCalled = true;
    return tmpWriter;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    if(writerAlreadyCalled)
      throw new IllegalStateException("the output stream has already been called");
    outputStreamAlreadyCalled = true;
    return servletOutput;
  }

  public byte[] toByteArray() {
    return output.toByteArray();
  }

  public void flushBuffer() throws IOException {
    tmpWriter.flush();
    servletOutput.flush();
  }

  public void reset() {
    output.reset();
  }

  public void close() throws IOException {
    tmpWriter.close();
  }
  
  public int getBufferSize() {
    return 0;
  }  

  public String encodeURL(String url) {    
    if (urlEncoder_ == null) return super.encodeURL(url) ;
    return urlEncoder_.encodeURL(url) ;
  }
  
  public void setURLEncoder(URLEncoder encoder) {
    urlEncoder_ = encoder ;
  }
  
  private static class ByteArrayServletOutputStream extends ServletOutputStream {
    ByteArrayOutputStream baos;
    public ByteArrayServletOutputStream(ByteArrayOutputStream baos) {
      this.baos = baos;
    }
    public void write(int i) throws IOException {
      baos.write(i);
    }
  }
}