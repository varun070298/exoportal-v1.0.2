/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.io.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.context.PortletFacesContext;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIFatalError.java,v 1.6 2004/11/03 04:24:53 tuan08 Exp $
 */
public class UIFatalError extends UIExoComponentBase {
    
  private Throwable exception_  ;

  public UIFatalError(Throwable ex) {
    setId("UIFatalError") ;
    exception_ = ex ;
  }

  public void decode(FacesContext context) {
    PortletFacesContext pcontext = (PortletFacesContext)context ;
    pcontext.destroy() ;
  }
  
  final public void encodeBegin(FacesContext context) throws IOException {
    String baseURL = context.getExternalContext().encodeActionURL(null) ;
    ResponseWriter w = context.getResponseWriter() ;
    w.write("<div>");
    w.  write("The portlet has a fatal error. Click here to restart the portlet") ;
    renderButton(w, baseURL);
    w.write("</div>");
    w.write("<div style='border: 1px dashed black>");
    String message = exception_.getMessage() ;
    if (message != null) {
    	w.write(exception_.getMessage()) ;
    }
    w.write(getStackTrace(exception_));
    w.write("</div>");
    
  }
  
  private void renderButton(ResponseWriter w, String baseURL) throws IOException {
    w.write("<a href='"); w.write(baseURL) ;
    w.write('&'); w.write(FacesConstants.ACTION); w.write("=restart") ;
    w.write("'>");
    w.write("Click");
    w.write("</a>") ;
  }
  
  private String getStackTrace(Throwable t) {
    ByteArrayOutputStream ostream = new ByteArrayOutputStream() ;
    PrintStream pstream = new PrintStream(ostream) ;
    t.printStackTrace(pstream) ;
    return new String(ostream.toByteArray()) ;
  }
  
  final public void encodeChildren(FacesContext context) throws IOException {
  }

  final public void encodeEnd(FacesContext context) throws IOException {
  }
}