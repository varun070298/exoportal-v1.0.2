/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.commons.utils.IOUtil;

/**
 * Created by The eXo Platform SARL        .
 * Sun, Dec 28, 2003 @ 14:57 
 * @author: Tuan Nguyen
 * @version: $Id: FileContentServlet.java,v 1.3 2004/06/23 03:19:01 tuan08 Exp $
 * @email: tuan08@yahoo.com
 */
public class FileContentServlet extends HttpServlet {
  static private int LENGTH = "/content/file".length() + 1;
  private String reposistory_ = null  ;
  
  public void init(ServletConfig config) throws ServletException {
    ServletContext context = config.getServletContext() ;
    reposistory_ = context.getInitParameter("file.portlet.reposistory") ;
    if(reposistory_ == null || reposistory_.length() ==0 || reposistory_.equals("default")) {
      reposistory_=  context.getRealPath("/") ;
    }
    if(reposistory_.endsWith("/")) {
      reposistory_ = reposistory_.substring(0, reposistory_.length() - 1) ;
    }
  }
  
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletOutputStream os = response.getOutputStream() ;
    try {
      //long headerValue = request.getDateHeader("If-Modified-Since");
      //System.out.println("if modified since....... " + headerValue);
      String servletPath = request.getPathInfo() ;
      String path = reposistory_ + servletPath ;
      if(path == null) {
        response.setContentType("text/plain");
        os.println("path: " + path + " is not found") ;
      } else {
        String mimeType = MimeTypeManager.getInstance().getMimeTypeByOfFile(path).getMimeType() ;
        //System.out.println("mime type = " + mimeType);
        response.setContentType(mimeType);
        byte[] buf = IOUtil.getFileContentAsBytes(path) ;
        //System.out.println("buf length = " + buf.length);
        //response.setHeader("Cache-Control", "public, private max-age=600, s-maxage=120");
        //System.out.println("write bin content");
        os.write(buf) ;
      }
    } catch (Exception ex) {
      String s = ExceptionUtil.getStackTrace(ex, 20) ;
      os.println(s) ;
    }
  }
}
