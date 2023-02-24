/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.container;

import java.util.List;
import org.exoplatform.container.monitor.SessionMonitor;
import org.exoplatform.container.monitor.SessionMonitorListenerStack;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jul 18, 2004
 * Time: 12:15:28 AM
 */
public class SessionContainer extends ExoContainer {
  private static ThreadLocal threadLocal_ = new ThreadLocal() ;
  
  private String portalName_ ;
  private SessionMonitor monitor_ ;
  private String id_ ;
  
  public SessionContainer(PortalContainer parent, String id, String owner) {
    super(new SimpleComponentAdapterFactory(), parent)  ;
    id_ = id ;
    SessionMonitorListenerStack stack = 
      (SessionMonitorListenerStack)parent.getComponentInstanceOfType(SessionMonitorListenerStack.class);
    monitor_ = new SessionMonitor(stack, owner);
    registerComponentInstance(monitor_) ;
    List factories = 
      parent.getComponentInstancesOfType(SessionContainerInitializer.class);
    for(int i = 0; i < factories.size(); i++) {
      SessionContainerInitializer initializer = 
        (SessionContainerInitializer) factories.get(i) ;
        initializer.initialize(this) ;
    }
  }
  
  public String getId() { return id_ ; }
  
  public String getOwner() { return monitor_.getSessionOwner() ; }
  
  public SessionMonitor getMonitor() { return monitor_ ; }
  
  public String getPortalName() { return portalName_ ; }
  public void   setPortalName(String name) { portalName_ = name ; }
 
  static public SessionContainer getInstance() { 
    return (SessionContainer)threadLocal_.get() ; 
  }
  
  static public void setInstance(SessionContainer scontainer) {
  	threadLocal_.set(scontainer) ;
  }
  
  public void startActionLifcycle()  {
    threadLocal_.set(this) ;
  }
  
  public void endActionLifcycle()  {
    threadLocal_.set(null) ;
  }
  
  public void stop() {
    if(monitor_ == null) return  ;
    monitor_.getListeners().onStop(monitor_) ;
    monitor_ = null ;
    super.stop() ;
  }
  
  public static Object getComponent(Class key) {
    SessionContainer scontainer = (SessionContainer)threadLocal_.get() ;
    return scontainer.getComponentInstanceOfType(key) ;
  }
}