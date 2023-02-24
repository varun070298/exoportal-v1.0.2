/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.portlet.struts;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.helper.URLEncoder;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletContextImpl;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletSessionImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomRequestWrapper;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;

import javax.portlet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 3, 2003 3:53:21 PM
 * @version $rev
 */
public class ExoStrutsPortlet extends GenericPortlet {
  private static Log log_ = LogUtil.getLog("org.exoplatform.portal.portlet");
  private String defaultPage_ ;
  private String contextPath_ ;
  
  public void init(PortletConfig config) throws PortletException {
    super.init(config);
    defaultPage_ = config.getInitParameter("default-page") ;
    if(defaultPage_ == null) defaultPage_ = "/index.html" ; 
    contextPath_ = "/" + config.getPortletContext().getPortletContextName() ;
  }

  public void render (RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
    response.setTitle(getTitle(request));
    WindowState state = request.getWindowState();
    if ( ! state.equals(WindowState.MINIMIZED)) {
      PortletMode mode = request.getPortletMode();
      if (mode.equals(PortletMode.VIEW)) {
        doView (request, response);
      } else if (mode.equals(PortletMode.EDIT)) {
        doEdit (request, response);
      } else if (mode.equals(PortletMode.HELP)) {
        doHelp (request, response);
      } else {
        throw new PortletException("unknown portlet mode: " + mode);
      }
    }
  }

  public void doView(RenderRequest request, RenderResponse response)
    throws PortletException, IOException
  {
    doRender(request, response) ;
  }

  public void doHelp(RenderRequest request, RenderResponse response)
    throws PortletException, IOException
  {
    doRender(request, response) ;
  }

  public void doEdit(RenderRequest request, RenderResponse response)
    throws PortletException, IOException
  {
    doRender(request, response) ;
  }

  public void doConfig(RenderRequest request, RenderResponse response)
    throws PortletException, IOException
  {
    doRender(request, response) ;
  }

  protected void doRender(RenderRequest request, RenderResponse response) throws PortletException, IOException {
    log_.debug( "$$$ dispatch to servlet acion $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" );
    String path = "/struts-controller" ;
    CustomRequestWrapper requestWrapper = null;

    ExoPortletContext context = (ExoPortletContext) request.getPortletSession().getPortletContext();
    boolean isSharedSessionEnable = context.isSessionShared();
    requestWrapper = ((CustomRequestWrapper) ((HttpServletRequestWrapper)request).getRequest());
    CustomResponseWrapper responseWrapper = (CustomResponseWrapper) ((HttpServletResponseWrapper)response).getResponse();
    try {      
      PortletContextImpl portletContext = (PortletContextImpl) getPortletContext() ;
      ServletContext scontext = portletContext.getWrappedServletContext() ;
      RequestDispatcher dispatcher = null;  
      
      String requestUri = requestWrapper.getParameter("requestUri") ;
      log_.debug("URI requested : " + requestUri);
      if(requestUri == null) {
        dispatcher = scontext.getRequestDispatcher(defaultPage_) ;  
      } else {
        requestUri = requestUri.substring(contextPath_.length() , requestUri.length()) ;
        if (requestUri.endsWith(".do")) {
          dispatcher = scontext.getRequestDispatcher(path) ;  
          requestWrapper.setAttribute("exo.forward", requestUri) ;
        } else {
          dispatcher = scontext.getRequestDispatcher(requestUri) ;  
        }
      }
      responseWrapper.flushBuffer();
      requestWrapper.setRedirected(true);
      requestWrapper.setRedirectedPath(path);
      URLEncoder urlencoder = 
        new StrutsURLEncoder(contextPath_, response.createRenderURL().toString()) ; 
      responseWrapper.setURLEncoder(urlencoder) ;
      if(isSharedSessionEnable){
        PortletSessionImp pS = (PortletSessionImp)request.getPortletSession();        
        requestWrapper.setSharedSession(pS.getSession());
        requestWrapper.setContextPath(request.getContextPath());        
      }
      dispatcher.include(requestWrapper, responseWrapper);      
    } catch (ServletException e) {
      throw new PortletException("Problems occur when using PortletDispatcher", e);
    } finally {
      if(requestWrapper != null)
        requestWrapper.setRedirected(false);
      if(isSharedSessionEnable){               
        requestWrapper.setSharedSession(null);
      }        
      responseWrapper.setURLEncoder(null) ;
    }    
    log_.debug( "$$$ end dispatch to struts controller $$$$$$$$$$$$$$$$$$$$$$$$" );
  }

  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException  {

  }
}
