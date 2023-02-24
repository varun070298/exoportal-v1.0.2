/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import java.util.List;
import javax.management.*;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UINavigator.java,v 1.2 2004/08/01 04:18:27 tuan08 Exp $
 */
public class UINavigator extends UIExoCommand {
  static final public String SELECT_ACTION = "select" ;

	public UINavigator() {
		setId("UINavigator") ;
    setClazz("UINavigator") ;
		setRendererType("NavigatorRenderer") ;
		List children = getChildren() ;
		List servers = MBeanServerFactory.findMBeanServer(null) ;
		boolean render = true ;
		for(int i = 0 ; i < servers.size(); i++) {
			MBeanServer server = (MBeanServer) servers.get(i) ;
			UIMBeanServer uiMBeanServer = new UIMBeanServer(server) ;
			uiMBeanServer.setRendered(render) ;
			uiMBeanServer.setId("UIMBeanServer" + i) ;
			children.add(uiMBeanServer)  ;
			render = false ;
		}
    addActionListener(SelectActionListener.class, SELECT_ACTION) ;
	}
  
  public String getFamily() {
    return "org.exoplatform.portlets.jmx.component.UINavigator" ;
  }
  
  public boolean isRendered() { return true ; }
  
  public UIMBeanServer getDefaultUIMBeanServer() {
  	List children = getChildren() ;
    for(int i = 0; i < children.size() ; i++) {
    	UIMBeanServer ui = (UIMBeanServer) children.get(i) ;
      if(ui.isRendered()) return ui ;
    }
    return null ;
  }
  
  static public class SelectActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UINavigator uiParent = (UINavigator)event.getComponent() ;
      String mserver = event.getParameter("mserver") ;
      UIMBeanServer uiMServer =(UIMBeanServer) uiParent.findComponent(mserver) ;
      uiParent.setRenderedComponent(mserver) ;
      UIListMBean uiListMBean = (UIListMBean) uiParent.getSibling(UIListMBean.class) ;
      uiListMBean.setMBeanServerDomain(uiMServer.getMBeanServer(), uiMServer.getRootDomain()) ;
      uiParent.setRenderedSibling(UIListMBean.class) ;
    }
  }
}