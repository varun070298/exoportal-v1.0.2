/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.*;
import org.exoplatform.services.portletcontainer.pci.model.*;


/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class PortletApplicationRegisterImpl implements
    PortletApplicationRegister {
  
  private Collection listeners_;
  private LogService logService_;
  private Log log;
  private PortletApplicationsHolder holder_;
  
  public PortletApplicationRegisterImpl(PortletApplicationsHolder holder,      
      LogService logService) {
    logService_ = logService ;
    listeners_ = new ArrayList();
    holder_ = holder;    
    this.log = logService_.getLog(getClass());
  }   
  
  public void addPortletLyfecycleListener(PortletLifecycleListener listener) {
    listeners_.add(listener);
  }  

  public void registerPortletApplication(ServletContext servletContext,
      PortletApp portletApp_, Collection roles)
      throws PortletContainerException {
    String portletAppName = servletContext.getServletContextName();
    log.debug("send pre deploy event for portlet app : "
        + servletContext.getServletContextName());
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator
          .next();
      portletLifecycleListener.preDeploy(portletAppName, portletApp_,
          servletContext);
    }
    holder_.registerPortletApplication(portletAppName, portletApp_, roles);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    RootContainer rootContainer = RootContainer.getInstance();
    try {
      rootContainer
          .registerComponentImplementation(
              portletAppName,
              cl
                  .loadClass("org.exoplatform.services.portletcontainer.impl.PortletApplicationProxy"));
    } catch (ClassNotFoundException e) {
      log.error("Class not found", e);
      throw new PortletContainerException("Class not found", e);
    }
    PortletApplicationProxy proxy = (PortletApplicationProxy) rootContainer
        .getComponentInstance(servletContext.getServletContextName());
    proxy.setApplicationName(servletContext.getServletContextName());
    proxy.load();
    log.debug("send post deploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator
          .next();
      portletLifecycleListener.postDeploy(portletAppName, portletApp_,
          servletContext);
    }
  }

  public void removePortletApplication(ServletContext servletContext)
      throws PortletContainerException {
    PortletApp portletApp = holder_.getPortletApplication(servletContext
        .getServletContextName());
    if (portletApp == null)
      return;
    String portletAppName = servletContext.getServletContextName();
    log.debug("send pre undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator
          .next();
      portletLifecycleListener.preUndeploy(portletAppName, portletApp,
          servletContext);
    }
    RootContainer.getInstance().unregisterComponent(portletAppName);
    removeMessageListeners(portletAppName, portletApp);
    removeFilters(portletAppName, portletApp);
    holder_.removePortletApplication(portletAppName);
    log.debug("send post undeploy event");
    for (Iterator iterator = listeners_.iterator(); iterator.hasNext();) {
      PortletLifecycleListener portletLifecycleListener = (PortletLifecycleListener) iterator
          .next();
      portletLifecycleListener.postUndeploy(portletAppName, portletApp,
          servletContext);
    }
  }

  private void removeMessageListeners(String portletAppName,
      PortletApp portletApplication) {
    log.debug("remove message listener entered");
    List portlets = portletApplication.getPortlet();
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      List listeners = portlet.getMessageListener();
      for (Iterator iterator1 = listeners.iterator(); iterator1.hasNext();) {
        MessageListener messageListenerData = (MessageListener) iterator1
            .next();
        String key = portletAppName + Constants.MESSAGE_LISTENER_ENCODER
            + messageListenerData.getListenerName();
        RootContainer.getInstance().unregisterComponent(key);
      }
    }
  }

  private void removeFilters(String portletAppName, PortletApp portletApp) {
    log.debug("remove filters entered");
    List portlets = portletApp.getPortlet();
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      List filters = portlet.getFilter();
      for (Iterator iterator1 = filters.iterator(); iterator1.hasNext();) {
        Filter portletFilterData = (Filter) iterator1.next();
        String key = portletAppName + Constants.FILTER_ENCODER
            + portletFilterData.getFilterName();
        RootContainer.getInstance().unregisterComponent(key);
      }
    }
  }

}