/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;


/**
 * Jul 29, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIJMXPortlet.java,v 1.2 2004/08/01 04:18:27 tuan08 Exp $
 */
public class UIJMXPortlet extends UIPortlet {

  public UIJMXPortlet(UINavigator uiNavigator, UIListMBean uiListMBean,
                      UIMBean uiMBean, UIOperation uiOperation) {
    setId("UIJMXPortlet") ;
    setClazz("UIJMXPortlet");
    setRendererType("JMXPortletRenderer") ;
    List children = getChildren() ;
    uiNavigator.setRendered(true) ;
    children.add(uiNavigator) ;
    uiListMBean.setRendered(true) ;
    UIMBeanServer uiMBeanServer = uiNavigator.getDefaultUIMBeanServer() ;
    uiListMBean.setMBeanServerDomain(uiMBeanServer.getMBeanServer(), 
                                     uiMBeanServer.getRootDomain()) ;
    children.add(uiListMBean) ;
    uiMBean.setRendered(false) ;
    children.add(uiMBean) ;
    uiOperation.setRendered(false) ;
    children.add(uiOperation) ;
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.jmx.component.UIJMXPortlet" ;
  }
}