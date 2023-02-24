/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.event.MessageListener;
import org.exoplatform.services.portletcontainer.event.PortletMessage;
import org.exoplatform.services.portletcontainer.impl.PortletApplicationsHolder;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.services.portletcontainer.impl.event.MessageEventImpl;
import org.exoplatform.services.portletcontainer.pci.model.Portlet;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;


/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 * tuan08@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 2:13:09 AM
 */
public class PortletContextImpl implements PortletContext, ExoPortletContext {
  private ServletContext servletContext_;
  private Log log;

  public PortletContextImpl(ServletContext scontext) {
    this.log = LogUtil.getLog("org.exoplatform.services.portletcontainer");
    servletContext_ = scontext;
  }

  public String getServerInfo() {
    return servletContext_.getServerInfo();
  }

  public PortletRequestDispatcher getRequestDispatcher(String path) {
    if (path == null || !path.startsWith("/"))
      return null;
    RequestDispatcher rD = null;
    try {
      rD = servletContext_.getRequestDispatcher(path);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(rD, path);
  }

  public PortletRequestDispatcher getNamedDispatcher(String name) {
    RequestDispatcher rD = null;
    try {
      rD = servletContext_.getNamedDispatcher(name);
      if (rD == null)
        return null;
    } catch (IllegalArgumentException e) {
      log.error("Can not lookup request dispatcher", e);
      return null;
    }
    return new PortletRequestDispatcherImp(rD, name);
  }

  public InputStream getResourceAsStream(String path) {
    return servletContext_.getResourceAsStream(path);
  }

  public int getMajorVersion() {
    return 1;//servletContext_.getMajorVersion() ;
  }

  public int getMinorVersion() {
    return 0;//servletContext_.getMinorVersion() ;
  }

  public String getMimeType(String file) {
    return servletContext_.getMimeType(file);
  }

  public String getRealPath(String path) {
    return servletContext_.getRealPath(path);
  }

  public java.util.Set getResourcePaths(String path) {
    return servletContext_.getResourcePaths(path);
  }

  public java.net.URL getResource(String path) throws MalformedURLException {
    if (!path.startsWith("/"))
      throw new MalformedURLException("path must start with /");
    return servletContext_.getResource(path);
  }

  public java.lang.Object getAttribute(String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    return servletContext_.getAttribute(name);
  }

  public void removeAttribute(java.lang.String name) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    servletContext_.removeAttribute(name);
  }

  public void setAttribute(String name, Object value) {
    if (name == null) {
      throw new IllegalArgumentException("The attribute name cannot be null");
    }
    //when the value is null, should have the same effect as removeAttribute (Spec)
    if (value == null) {
      servletContext_.removeAttribute(name);
    } else {
      servletContext_.setAttribute(name, value);
    }
  }

  public Enumeration getAttributeNames() {
    return servletContext_.getAttributeNames();
  }

  public String getInitParameter(String name) {
    if (name == null)
      throw new IllegalArgumentException("argument must not be null");
    return servletContext_.getInitParameter(name);
  }

  public Enumeration getInitParameterNames() {
    return servletContext_.getInitParameterNames();
  }

  public String getPortletContextName() {
    return servletContext_.getServletContextName();
  }

  public ServletContext getWrappedServletContext() {
    return servletContext_;
  }

  public void send(String portletToAccessName, PortletMessage portletMessage,
                   PortletRequest portletRequest) throws PortletException {
    try {
      if (portletRequest instanceof RenderRequest) {
        throw new PortletException("can not send message in render method");
      }
      PortalContainer manager = PortalContainer.getInstance();
      PortletApplicationsHolder service =
          (PortletApplicationsHolder) manager.getComponentInstanceOfType(PortletApplicationsHolder.class);
      PortletApp portletApp = service.getPortletApplication(servletContext_.getServletContextName());
      List portlets = portletApp.getPortlet();
      for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
        Portlet portlet = (Portlet) iterator.next();
        String portletName = portlet.getPortletName();
        if (portletName.equals(portletToAccessName)) {
          List messageListeners = portlet.getMessageListener();
          for (Iterator iterator1 = messageListeners.iterator(); iterator1.hasNext();) {
            org.exoplatform.services.portletcontainer.pci.model.MessageListener messageListenerData = 
              (org.exoplatform.services.portletcontainer.pci.model.MessageListener) iterator1.next();
            String listenerName = messageListenerData.getListenerName();
            String listenerClass = messageListenerData.getListenerClass();
            String key = servletContext_.getServletContextName() + Constants.MESSAGE_LISTENER_ENCODER + listenerName;
            MessageListener listener = (MessageListener) manager.getComponentInstance(key);
            if (listener == null) {
              ClassLoader cl = Thread.currentThread().getContextClassLoader();
              manager.registerComponentImplementation(key, cl.loadClass(listenerClass));
              listener = (MessageListener) manager.getComponentInstance(key);
            }
            listener.messageReceived(new MessageEventImpl(portletMessage,
                (ActionRequest) portletRequest));
          }
          break;
        }
      }
    } catch (Exception ex) {
      log.error("Can not send message", ex);
      throw new PortletException(ex);
    }
  }

  public boolean isSessionShared() {
    PortletContainerConf conf = (PortletContainerConf) PortalContainer.getInstance().
        getComponentInstanceOfType(PortletContainerConf.class);
    return conf.isSharedSessionEnable();
  }

  public void log(java.lang.String msg) {
    servletContext_.log(msg);
  }

  public void log(java.lang.String message, java.lang.Throwable throwable) {
    servletContext_.log(message, throwable);
  }
}
