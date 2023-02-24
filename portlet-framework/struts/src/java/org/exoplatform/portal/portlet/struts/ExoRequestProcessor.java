package org.exoplatform.portal.portlet.struts;

import java.io.IOException ;
import java.util.* ;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher ;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.commons.lang.StringUtils ;

import org.apache.struts.action.RequestProcessor ;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;

public class ExoRequestProcessor extends RequestProcessor {
  protected void doForward( String uri, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException 
  {
    // Unwrap the multipart request, if there is one.
    if (request instanceof MultipartRequestWrapper) {
      request = ((MultipartRequestWrapper) request).getRequest();
    }
    CustomRequestWrapper requestWrapper = (CustomRequestWrapper)request;
    int question = uri.indexOf("?") ;   
    String actionPath = null ;
    String params = "" ;
    if (question  > 0) {
      //Map paramMap = requestWrapper.getParameterMap() ;
      actionPath = uri.substring(0, question) ;
      params = uri.substring (question, uri.length()) ;
      //requestWrapper.setParameterMap(parseParams(params)) ;
    } else {
      actionPath = uri ;
    }

    String path = "/struts-controller" ;
    if (actionPath.endsWith(".do")) {
      path += params ;
      requestWrapper.setRedirectedPath(path);
      requestWrapper.setAttribute("exo.forward", actionPath) ;
    } else {
      path = uri ;
    }
    RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
    if (rd == null) {
      throw new ServletException("Cannot find " + path) ;
    }
    requestWrapper.setRedirected(true);
    rd.include(requestWrapper, response);
  }

  private Map parseParams(String params) {
    HashMap map = new HashMap(5) ;
    String[] param = StringUtils.split(params, "&") ;
    for (int i = 0; i < param.length; i++) {
      String[] tmp = StringUtils.split(param[i], "=") ;
      String key = tmp[0] ;
      List values = (List) map.get(key) ;
      if (values == null) {
        values = new ArrayList(3) ;
      }
      values.add(tmp[1]) ;
      map.put(key, values) ;
    }
    Iterator keys = map.keySet().iterator() ;
    Map paramMap = new HashMap() ; 
    while (keys.hasNext())  {
      String key = (String) keys.next() ;
      List list = (List) map.get(key) ;
      String[] values = new String[list.size()] ;
      for (int i = 0; i < list.size(); i++) {
        values[i] = (String) list.get(i) ;
      }
      paramMap.put(key, values) ;
    }
    return paramMap ;
  }
}
