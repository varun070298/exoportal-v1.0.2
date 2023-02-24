/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.portletcontainer.impl.servlet;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.helper.PortletWindowInternal;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationHandler;
import org.exoplatform.services.portletcontainer.impl.PortletContainerDispatcher;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.exoplatform.services.portletcontainer.pci.Output;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 13:01:42
 */
public class ServletWrapper extends HttpServlet{

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);

  }

  protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
                  throws ServletException, java.io.IOException{
    PortalContainer manager = PortalContainer.getInstance();
    LogService logService = (LogService) RootContainer.getInstance().
        getComponentInstanceOfType(LogService.class);
    PortletApplicationHandler handler = (PortletApplicationHandler) manager.
        getComponentInstanceOfType(PortletApplicationHandler.class);
    Log log = logService.getLog("org.exoplatform.services.portletcontainer");    
    log.debug("Service method of ServletWrapper entered");
    log.debug("Encoding used : " + servletRequest.getCharacterEncoding());    
    boolean isToGetBundle = false;
    Boolean b = (Boolean)servletRequest.getAttribute(PortletContainerDispatcher.IS_TO_GET_BUNDLE);
    if(b != null)                         
      isToGetBundle = b.booleanValue();                
    if(isToGetBundle){
      log.debug("Get bundle");
      String portletAppName = (String) servletRequest.getAttribute(PortletContainerDispatcher.PORTLET_APPLICATION_NAME);
      String portletName = (String) servletRequest.getAttribute(PortletContainerDispatcher.PORTLET_NAME);
      ResourceBundle bundle = handler.getBundle(portletAppName, portletName,                                                   
                                                ((Locale)servletRequest.getAttribute(PortletContainerDispatcher.LOCALE_FOR_BUNDLE)));
      servletRequest.setAttribute(PortletContainerDispatcher.BUNDLE, bundle);
      return; 
    }
    
    PortletWindowInternal windowInfo = (PortletWindowInternal) servletRequest.getAttribute(PortletContainerDispatcher.WINDOW_INFO);
    Input input = (Input) servletRequest.getAttribute(PortletContainerDispatcher.INPUT);
    Output output = (Output) servletRequest.getAttribute(PortletContainerDispatcher.OUTPUT);
		boolean isAction = ((Boolean)servletRequest.getAttribute(PortletContainerDispatcher.IS_ACTION)).booleanValue();
		try {			      
			handler.process(getServletContext(), servletRequest,
							        servletResponse, input, output, windowInfo, isAction);
		} catch (PortletContainerException e) {
      log.error("An error occured while processing the portlet request", e);
			throw new ServletException("An error occured while processing the portlet request", e);
		}
	}
}
