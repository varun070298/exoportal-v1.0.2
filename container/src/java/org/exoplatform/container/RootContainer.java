/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.container;

import java.net.URL;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.servlet.ServletContext;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ConfigurationManagerImpl;
import org.exoplatform.container.configuration.MockConfigurationManagerImpl;
import org.exoplatform.container.groovy.GroovyManager;
import org.exoplatform.container.groovy.GroovyManagerContainer;
import org.exoplatform.container.jmx.MX4JComponentAdapterFactory;
import org.exoplatform.container.monitor.jvm.OperatingSystemInfo;
import org.exoplatform.container.monitor.jvm.J2EEServerInfo;
import org.exoplatform.container.util.ContainerUtil;
import org.exoplatform.test.mocks.servlet.MockServletContext;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jul 21, 2004
 * Time: 12:15:28 AM
 */
public class RootContainer extends ExoContainer {
	private static RootContainer singleton_ ;
  private static boolean jdk15_ ;
  
  private  MBeanServer mbeanServer_ ;
  private OperatingSystemInfo osenv_ ;
  private J2EEServerInfo serverenv_ ;

  public RootContainer() {
    super(new MX4JComponentAdapterFactory(), null)  ;
    mbeanServer_ = MBeanServerFactory.createMBeanServer("exomx");
    Runtime.getRuntime().addShutdownHook(new ShutdownThread(this)) ;
  }
  
  public OperatingSystemInfo getOSEnvironment() { 
    if( osenv_ == null) {
      osenv_ = (OperatingSystemInfo) this.getComponentInstanceOfType(OperatingSystemInfo.class) ;
    }
    return osenv_ ; 
  }
  
  public J2EEServerInfo getServerEnvironment() {
    if(serverenv_ == null) {
      serverenv_ = (J2EEServerInfo) this.getComponentInstanceOfType(J2EEServerInfo.class) ;
    }
    return serverenv_ ;
  } 
  
  public PortalContainer getPortalContainer(String name) {
    PortalContainer pcontainer  = (PortalContainer)this.getComponentInstance(name) ;
    if(pcontainer == null) {
      J2EEServerInfo senv = getServerEnvironment() ;
      if("standalone".equals(senv.getServerName())) {
        try {
          MockServletContext scontext = new MockServletContext(name) ;
          pcontainer = new PortalContainer(this, scontext) ;
          ConfigurationManagerImpl cService = new MockConfigurationManagerImpl(scontext) ;
          cService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/configuration.xml")) ;
          cService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/test-configuration.xml")) ;
          cService.processRemoveConfiguration() ;
          pcontainer.registerComponentInstance(ConfigurationManager.class, cService) ;
          
          ConfigurationManagerImpl scService = new MockConfigurationManagerImpl(scontext) ;
          scService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/session/configuration.xml")) ;
          scService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/session/test-configuration.xml")) ;
          scService.processRemoveConfiguration() ;
          pcontainer.registerComponentInstance(PortalContainer.SESSION_CONTAINER_CONFIG, scService) ;
          
          GroovyManagerContainer gcontainer = 
            (GroovyManagerContainer)getComponentInstanceOfType(GroovyManagerContainer.class) ;
          URL url = new URL("file:" + getServerEnvironment().getServerHome() + "/web/portal/src/webapp/WEB-INF/groovy/") ;
          GroovyManager gmanager = gcontainer.getGroovyManager(url) ;
          pcontainer.setGroovyManager(gmanager) ;
          registerComponentInstance(name, pcontainer) ;
          ContainerUtil.populateGroovy(pcontainer, cService) ;
          ContainerUtil.populate(pcontainer, cService) ;
          pcontainer.start() ;
        } catch(Exception ex) {
          ex.printStackTrace() ;
        }
      }
    } 
    return pcontainer ; 
  }
  
  synchronized public PortalContainer createPortalContainer(ServletContext context) {
    try {
      PortalContainer pcontainer =    new PortalContainer(this, context) ; 
      ConfigurationManagerImpl cService = new ConfigurationManagerImpl(context) ;
      cService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/configuration.xml")) ;
      try {
        cService.addConfiguration("war:/conf/configuration.xml") ;
      } catch(Exception ex){}
      cService.processRemoveConfiguration() ;
      pcontainer.registerComponentInstance(ConfigurationManager.class, cService) ;
      
      ConfigurationManagerImpl scService = new ConfigurationManagerImpl(context) ;            
      scService.addConfiguration(ContainerUtil.getConfigurationURL("conf/portal/session/configuration.xml")) ;
      scService.processRemoveConfiguration() ;
      pcontainer.registerComponentInstance(PortalContainer.SESSION_CONTAINER_CONFIG, scService) ;
      
      GroovyManagerContainer gcontainer = 
        (GroovyManagerContainer)getComponentInstanceOfType(GroovyManagerContainer.class) ;
      URL url = new URL("file:" + context.getRealPath("/WEB-INF/groovy") + "/");
      GroovyManager gmanager = gcontainer.getGroovyManager(url) ;
      pcontainer.setGroovyManager(gmanager) ;
      registerComponentInstance(context.getServletContextName(), pcontainer) ;
      PortalContainer.setInstance(pcontainer) ; 
      //ContainerUtil.populateGroovy(pcontainer, cService) ;
      ContainerUtil.populate(pcontainer, cService) ;
      pcontainer.start() ;
      return pcontainer ;
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
    return null ;
  }
  
  synchronized public void removePortalContainer(ServletContext servletContext) {
    this.unregisterComponent(servletContext.getServletContextName()) ;
  }
  
  public MBeanServer getMBeanServer() {  return mbeanServer_; }
  
  public static Object getComponent(Class key) {
    return getInstance().getComponentInstanceOfType(key) ;
  }
  
  
  public static RootContainer getInstance() {
    if(singleton_ == null) {
      synchronized(RootContainer.class) {
        if(singleton_ == null) {
          try {
            Class.forName("java.lang.management.ManagementFactory");
            jdk15_ = true ;
          } catch (Exception ex) {
            jdk15_ = false ;
          }
          try {
            singleton_ = new RootContainer() ;
            ConfigurationManagerImpl service = new ConfigurationManagerImpl(null) ;
            service.addConfiguration(ContainerUtil.getConfigurationURL("conf/configuration.xml")) ;
            if(jdk15_) {
              service.addConfiguration(ContainerUtil.getConfigurationURL("conf/configuration-jvm15.xml")) ;
            }
            if(System.getProperty("maven.exoplatform.dir") != null) {
              service.addConfiguration(ContainerUtil.getConfigurationURL("conf/test-configuration.xml")) ;
            }
            service.processRemoveConfiguration() ;
            singleton_.registerComponentInstance(ConfigurationManager.class, service) ;
            ContainerUtil.populate(singleton_, service) ;
            List initializers = 
              singleton_.getComponentInstancesOfType(RootContainerInitializer.class);
            for(int i = 0; i < initializers.size(); i++) {
              RootContainerInitializer initializer = (RootContainerInitializer) initializers.get(i) ;
                initializer.initialize(singleton_) ;
            }
            singleton_.start() ;
          } catch (Throwable ex) { ex.printStackTrace() ;}
        }
      }
    }
    return singleton_  ;
  }
  
  static public boolean isJDK15() {
    try {
      
    } catch (Exception ex) {
      
    }
    return false ;
  }
  
  static class ShutdownThread extends Thread {
    RootContainer container_ ;
    ShutdownThread(RootContainer container) {
      container_ = container ;
    }
    
    public void run() {
      container_.stop() ;
    }
  }
}