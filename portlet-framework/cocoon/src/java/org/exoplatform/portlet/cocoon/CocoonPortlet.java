/**
 **************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.portlet.cocoon;


import javax.portlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequestWrapper;
import org.exoplatform.portlet.commons.servlet.BufferedServletResponse;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.*;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;
import java.io.IOException;
import java.io.Writer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.Constants;
//import javax.servlet.ServletOutputStream;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author Gennady Azarenkov
 * @version $Id: CocoonPortlet.java 596 2005-01-28 15:45:37Z kravchuk $
 */

public class CocoonPortlet extends GenericPortlet {
  private String servletName;
  private Log log;
  public void init(PortletConfig config) throws PortletException {
    super.init(config);
    PortalContainer manager = PortalContainer.getInstance();
        LogService logService =
              (LogService) manager.getComponentInstanceOfType(LogService.class);
    log = logService.getLog(this.getClass());

    servletName = config.getInitParameter("servlet-name");
    if (servletName == null)
      servletName = config.getPortletName();

    log.debug("servletName ["+servletName+"]");


  }

  public void render(RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
    doView(request, response);
  }

  public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
      WindowState state = request.getWindowState();
    String path = "/";
    //    System.out.println("---->>>>>>>Cocoon Portlet in View Mode!");
    CustomRequestWrapper requestWrapper = null;
    ExoPortletContext context = (ExoPortletContext) request.getPortletSession().getPortletContext();
    boolean isSharedSessionEnable = context.isSessionShared();
    try {
      requestWrapper = ((CustomRequestWrapper) ((HttpServletRequestWrapper) request).getRequest());
      BufferedServletResponse responseWrapper = new BufferedServletResponse((RenderResponseImp) response);
      PortletContextImpl portletContext = (PortletContextImpl) getPortletContext();
      ServletContext scontext = portletContext.getWrappedServletContext();
      RequestDispatcher dispatcher = null;
      String requestUrl = requestWrapper.getParameter("url");
      System.out.println("Portlet RequestURI: " + requestUrl);
      log.debug("Portlet RequestURI: " + requestUrl);
      dispatcher = scontext.getNamedDispatcher(servletName);
      if (requestUrl != null)
        path += requestUrl;
      // System.out.println("Real Path: " + scontext.getRealPath(path));
      log.debug("Real Path: " + scontext.getRealPath(path));

      responseWrapper.flushBuffer();
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(path);
      if (isSharedSessionEnable) {
        PortletSessionImp pS = (PortletSessionImp) request.getPortletSession();
        requestWrapper.setSharedSession(pS.getSession());
        requestWrapper.setContextPath(request.getContextPath());
      }
      dispatcher.include(requestWrapper, responseWrapper);
      response.setContentType("text/html");


      ByteArrayOutputStream  byteOutput = new ByteArrayOutputStream();
      new URLRewriter().rewrite(

                new ByteArrayInputStream(responseWrapper.toByteArray()),
                //response.getPortletOutputStream(),
                byteOutput,
                getBaseURI(),requestUrl);
      Writer writer = response.getWriter();
      writer.write(byteOutput.toString());

    } catch (Exception e) {
      e.printStackTrace();
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      if (requestWrapper != null)
        requestWrapper.setRedirected(false);
      if (isSharedSessionEnable) {
        requestWrapper.setSharedSession(null);
      }
    }
  }

  //URI (path + query string) of current string
  private String getBaseURI(){
      RequestInfo rinfo =
            (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
      String path = rinfo.getOwnerURI();


      ArrayList paramsArray = new ArrayList();

      //query string
      if (rinfo.getTargetComponentId() != null){
          paramsArray.add(
            Constants.COMPONENT_PARAMETER + "="+rinfo.getTargetComponentId());
      }

      if (rinfo.getPortletWindowState() != null){
          paramsArray.add(
            Constants.WINDOW_STATE_PARAMETER+"="+rinfo.getPortletWindowState());
      }

      if (rinfo.getPortletMode() != null){
          paramsArray.add(
            Constants.PORTLET_MODE_PARAMETER+"="+rinfo.getPortletMode());
      }

      //add query to path
      if (! paramsArray.isEmpty()){
          path += "?" + paramsArray.get(0);
          for (int i = 1; i < paramsArray.size(); i++){
              path += "&" + paramsArray.get(i);
          }
      }

      return path;
  }

}
