/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.jmx.component.model.MBeanDomain;

;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIMBeanServer.java,v 1.2 2004/08/01 04:18:27 tuan08 Exp $
 */
public class UIMBeanServer extends UIExoCommand {
  static final public String SELECT_DOMAIN_ACTION = "selectDomain" ;
  static final public String SELECT_MBEAN_ACTION = "selectMBean" ;
  
	private MBeanServer mserver_ ;
  private MBeanDomain rootDomain_ ;
  private String name_ ;
  
  public UIMBeanServer(MBeanServer server) {
    mserver_ = server ;
    rootDomain_ = new MBeanDomain("") ;
    Set names = mserver_.queryNames(null, null) ;
    Iterator i = names.iterator() ;
    while(i.hasNext()) {
    	ObjectName oname = (ObjectName)i.next()  ;
      rootDomain_.addObjectName(oname) ;
    }
    name_ = server.getDefaultDomain() ;
    addActionListener(SelectDomainActionListener.class, SELECT_DOMAIN_ACTION) ;
    addActionListener(SelectMBeanActionListener.class, SELECT_MBEAN_ACTION) ;
  }
  
  public MBeanServer getMBeanServer() { return mserver_ ; }
  
  public MBeanDomain getRootDomain() { return rootDomain_ ; }
  
  public String getName() { return name_ ; }
  
  public String getFamily() {
    return "org.exoplatform.portlets.jmx.component.UIMBeanServer" ;
  }
  
  static public class SelectDomainActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIMBeanServer uiServer = (UIMBeanServer)event.getComponent() ;
      String domain = event.getParameter("domain") ;
      MBeanDomain mdomain = uiServer.rootDomain_.findMBeanDomain(domain) ;
      UIJMXPortlet uiPortlet = (UIJMXPortlet) uiServer.getAncestorOfType(UIJMXPortlet.class) ;
      UIListMBean uiListMBean = (UIListMBean) uiPortlet.getChildComponentOfType(UIListMBean.class) ;
      uiListMBean.setMBeanServerDomain(uiServer.getMBeanServer(), mdomain) ;
      uiPortlet.setRenderedComponent(UIListMBean.class) ;
      mdomain.setSelect(!mdomain.isSelect()) ;
    }
  }
  
  static public class SelectMBeanActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIMBeanServer uiServer = (UIMBeanServer)event.getComponent() ;
      String mbean = event.getParameter("mbean") ;
      ObjectName name = uiServer.rootDomain_.findMBeanObjectname(mbean) ;
      UIJMXPortlet uiPortlet = (UIJMXPortlet) uiServer.getAncestorOfType(UIJMXPortlet.class) ;
      UIMBean uiMBean = (UIMBean) uiPortlet.getChildComponentOfType(UIMBean.class) ;
      uiMBean.setUIMBeanData(uiServer.mserver_, name) ;
      uiPortlet.setRenderedComponent(uiMBean.getId()) ;
    } 
  }
}