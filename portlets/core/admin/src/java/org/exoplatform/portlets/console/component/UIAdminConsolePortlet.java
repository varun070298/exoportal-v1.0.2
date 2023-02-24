/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.console.component;

import org.exoplatform.container.RootContainer;
import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
import org.exoplatform.faces.core.component.UICommandPortlet;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.log.component.UILog;
import org.exoplatform.portlets.log.component.UILogConfig;
import org.exoplatform.portlets.log.component.UILogError;
import org.exoplatform.portlets.monitor.jvm.component.*;
import org.exoplatform.portlets.monitor.portlet.component.UIPortalMonitor;
import org.exoplatform.portlets.monitor.portlet.component.UIPortletContainerMonitor;
import org.exoplatform.portlets.monitor.session.component.UIActionHistory;
import org.exoplatform.portlets.monitor.session.component.UILiveSessions;
import org.exoplatform.portlets.monitor.session.component.UISessionLogManager;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIMonitorPortlet.java,v 1.1 2004/08/02 12:04:26 tuan08 Exp $
 */
public class UIAdminConsolePortlet extends UICommandPortlet {
  final static public String COMPONENT_ID = "componentId" ;
  final static public String CHANGE_COMPONENT_ACTION = "changeComponent" ;
  final static public Parameter CHANGE_COMPONENT = new Parameter(ACTION, CHANGE_COMPONENT_ACTION) ;
  
  public UIAdminConsolePortlet() throws Exception {
    setId("UIAdminConsolePortlet") ;
    setRendererType("AdminConsolePortletRenderer") ; 
    addExoMonitors() ;
    addLogNode() ;
    addChild(UIListCache.class).setRendered(false) ;
    addJVMManagement() ;
   
    addActionListener(ChangeActionListener.class, CHANGE_COMPONENT_ACTION) ;
	}
  
  private void addLogNode() throws Exception {
    UINode uiLogNode  = (UINode)addChild(UINode.class) ;
    uiLogNode.setId("UILogNode") ;
    uiLogNode.setRendererType("SimpleTabRenderer");
    //uiLogNode.setRendererType("ChildrenRenderer");
    uiLogNode.setName("UINode.label.log") ;
    uiLogNode.setRendered(false);
    uiLogNode.addChild(UILog.class);
    uiLogNode.addChild(UILogError.class).setRendered(false) ;
    uiLogNode.addChild(UILogConfig.class).setRendered(false) ;
  }
  
  public void addExoMonitors() throws Exception {
    UINode uiMonitor  = (UINode)addChild(UINode.class) ;
    uiMonitor.setId("UIMonitors");
    uiMonitor.setRendererType("SimpleTabRenderer") ; 
    //setRendererType("ChildrenRenderer") ; 
    uiMonitor.setName("UINode.label.monitors");
  
    UINode uiSessionNode = (UINode)uiMonitor.addChild(UINode.class) ;
    uiSessionNode.setId("UISessionNode") ;
    uiSessionNode.setRendererType("ChildrenRenderer");
    uiSessionNode.addChild(UILiveSessions.class) ;
    uiSessionNode.addChild(UIActionHistory.class).setRendered(false) ;
    
    uiMonitor.addChild(UISessionLogManager.class).setRendered(false) ;
    
    uiMonitor.addChild(UIPortalMonitor.class).setRendered(false) ;
    uiMonitor.addChild(UIPortletContainerMonitor.class).setRendered(false) ;    
  }
  
  public void addJVMManagement() throws Exception {
    UINode jvm  = (UINode)addChild(UINode.class) ;
    jvm.setRendered(false) ;
    jvm.setId("UIJVMManagement");
    jvm.setName("UINode.label.UIJVMManagement");
    jvm.setRendererType("ChildrenRenderer") ; 
    jvm.addChild(UIOSInfo.class) ;
    jvm.addChild(UIJVMRuntimInfo.class).setRendered(false) ;
    JVMRuntimeInfo info = 
      (JVMRuntimeInfo)RootContainer.getComponent(JVMRuntimeInfo.class) ;
    if(info.isManagementSupported()) {
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIProcessInfo")).
          setRendered(false) ;
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIClassInfo")).
          setRendered(false) ;
      
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIMemoryInfo")).
          setRendered(false) ;
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIMemoryManagerInfo")).
          setRendered(false) ;
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIMemoryPoolInfo")).
          setRendered(false) ;
      jvm.addChild(getComponent("org.exoplatform.portlets.monitor.jvm.component.UIGCInfo")).
          setRendered(false) ;
    }
  }
  
  private Class getComponent(String name) {
    try {
      return Class.forName(name) ;
    } catch(Exception ex) {
      return null;
    }
  }
  
  static public class ChangeActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIAdminConsolePortlet uiPortlet = (UIAdminConsolePortlet)event.getComponent() ;
      String componentId = event.getParameter(COMPONENT_ID) ;
      UIExoComponent uiComponent =  uiPortlet.findComponentById(componentId) ;   
      UIExoComponent uiParent = (UIExoComponent) uiComponent.getParent() ;
      if(uiParent != uiPortlet) {
        uiPortlet.setRenderedComponent(uiParent.getId());
      }
      uiParent.setRenderedComponent(componentId) ;
    } 
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.console.component.UIAdminConsolePortlet" ;
  }
}