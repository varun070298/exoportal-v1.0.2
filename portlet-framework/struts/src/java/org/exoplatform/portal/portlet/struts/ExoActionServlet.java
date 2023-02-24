package org.exoplatform.portal.portlet.struts;

import java.io.IOException ;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet ;
import org.apache.struts.util.RequestUtils;

public class ExoActionServlet extends ActionServlet {
  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String requestUri = (String) request.getAttribute("exo.forward") ;
    if (requestUri != null) {
      request.setAttribute("javax.servlet.include.servlet_path", requestUri) ;
    }
    RequestUtils.selectModule(request, getServletContext());
    getRequestProcessor(getModuleConfig(request)).process(request, response);
  }

  protected void initServlet() throws ServletException {
    super.initServlet(); 
    getServletContext().setAttribute(Globals.SERVLET_KEY, "*.do");
  }
}
