/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.container;

import java.util.List;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.servlet.ServletContext;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.PortalContainerInfo;
import org.exoplatform.container.groovy.ExoGroovyComponentAdapter;
import org.exoplatform.container.groovy.GroovyManager;
import org.exoplatform.container.groovy.GroovyManagerListener;
import org.exoplatform.container.groovy.GroovyObject;
import org.exoplatform.container.jmx.MX4JComponentAdapterFactory;
import org.exoplatform.container.monitor.PortalMonitor;
import org.exoplatform.container.util.ContainerUtil;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.Startable;
import org.picocontainer.defaults.DuplicateComponentKeyRegistrationException;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 31, 2003
 * Time: 12:15:28 AM
 */
public class PortalContainer extends ExoContainer {
  final static public String SESSION_CONTAINER_CONFIG = "session.container.config" ;
  private static ThreadLocal currentContainer_ = new ThreadLocal() ;
  
  private MBeanServer mbeanServer ;
  private ServletContext portalServletContext_ ;
  private GroovyManager gmanager_ ;
  private PortalMonitor monitor_ ;
  private boolean started_ = false ;
  
  public PortalContainer(PicoContainer parent,  ServletContext portalContext) {
    super(new MX4JComponentAdapterFactory(),parent)  ;
    portalServletContext_ = portalContext ;
    mbeanServer = MBeanServerFactory.createMBeanServer("portalmx");
    registerComponentInstance(ServletContext.class, portalContext) ;
    monitor_ = new PortalMonitor() ;
    registerComponentInstance(PortalMonitor.class, monitor_) ;
    PortalContainerInfo info = new PortalContainerInfo(portalContext.getServletContextName()) ;
    registerComponentInstance(PortalContainerInfo.class, info);    
  }
  
  public ServletContext getPortalServletContext() {	return portalServletContext_ ; }
  
  public PortalMonitor getMonitor() { return monitor_ ; }
  
  public SessionContainer createSessionContainer(String id, String owner) {
    SessionContainer scontainer = (SessionContainer)this.getComponentInstance(id) ;
    if(scontainer != null) {
      unregisterComponent(id) ;
      scontainer.stop() ;
    }
    scontainer = new SessionContainer(this, id, owner) ;
    ConfigurationManager cService = 
      (ConfigurationManager)this.getComponentInstance(SESSION_CONTAINER_CONFIG);
    ContainerUtil.populate(scontainer, cService) ;
    scontainer.setPortalName(portalServletContext_.getServletContextName()) ;
    registerComponentInstance(id, scontainer) ;
    scontainer.start() ;
    SessionContainer.setInstance(scontainer) ;
    return scontainer ;
  }
  
  public void removeSessionContainer(String id) {
    unregisterComponent(id) ;
  }
  
  public java.util.List getLiveSessions() {
  	return getComponentInstancesOfType(SessionContainer.class) ;
  }
  
  public  MBeanServer getMBeanServer() {  return mbeanServer; }
  
  public GroovyManager getGroovyManager() {  return gmanager_ ;  }
  public void  setGroovyManager(GroovyManager gmanager) { 
    gmanager_ = gmanager ; 
    gmanager_.removeAllListener() ;
    gmanager_.addListener(new PortalGroovyObjectListener()) ;
  }
  
  private ExoContainer getThisContainer() { return this ; }
  
  public static PortalContainer getInstance() {
    PortalContainer container = (PortalContainer) currentContainer_.get() ;
    if(container == null) {
      container = RootContainer.getInstance().getPortalContainer("default") ;
      currentContainer_.set(container) ;
    }
    return container ;
  }
  
  public boolean isStarted() { return started_ ; }
  
  public void start() {
    super.start() ;
    started_ = true ;
  }
  
  public void stop() {
    gmanager_.setDispose(true) ;
    super.stop() ;
    started_ = false  ;
  }
  
  synchronized public ComponentAdapter getComponentAdapterOfType(Class componentType) {
    return super.getComponentAdapterOfType(componentType);
  }
  
  synchronized public List getComponentAdaptersOfType(Class componentType) {
    return super.getComponentAdaptersOfType(componentType) ;
  }
  
  synchronized public ComponentAdapter unregisterComponent(Object componentKey) {
    return super.unregisterComponent(componentKey) ;
  }
  
  synchronized public ComponentAdapter registerComponent(ComponentAdapter componentAdapter) 
    throws DuplicateComponentKeyRegistrationException 
  {
    return  super.registerComponent(componentAdapter) ;
  }
  
  synchronized public List getComponentInstancesOfType(Class componentType) throws PicoException {
    return super.getComponentInstancesOfType(componentType) ;
  }
  
  public static void setInstance(PortalContainer instance) {
    currentContainer_.set(instance) ;
  }
  
  public static Object getComponent(Class key) {
    PortalContainer pcontainer = (PortalContainer)currentContainer_.get() ;
    return pcontainer.getComponentInstanceOfType(key) ;
  }
  
  public class PortalGroovyObjectListener extends GroovyManagerListener {
    public void load(GroovyObject gobject) throws Exception {
      registerComponent(new ExoGroovyComponentAdapter(gobject)) ;
    }
    
    public void reload(GroovyObject gobject) throws Exception {
      registerComponent(new ExoGroovyComponentAdapter(gobject)) ;
      Class type = gobject.getType() ;
      if(Startable.class.isAssignableFrom(type)) {
        //look for the component to force object start
        ExoGroovyComponentAdapter adapter = 
          (ExoGroovyComponentAdapter)getComponentAdapter(gobject.getType()) ;
        adapter.getComponentInstance(getThisContainer()) ;
      }
    }
    
    public void unload(GroovyObject gobject) throws Exception {  
      unregisterComponent(gobject.getType()) ;
      gobject.setObject(null) ;
    }
  }
}