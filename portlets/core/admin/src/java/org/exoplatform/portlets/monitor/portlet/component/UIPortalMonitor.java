/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.component;

import java.util.* ;
import org.exoplatform.container.monitor.PortalMonitor;
import org.exoplatform.faces.core.component.UIExoCommand;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortalMonitor.java,v 1.3 2004/08/02 12:04:26 tuan08 Exp $
 */
public class UIPortalMonitor extends UIExoCommand {
    
  private PortalMonitor service_ ;
     
  public UIPortalMonitor(PortalMonitor service) {
    setRendererType("PortalMonitorRenderer");
    setId("UIPortalMonitor") ;
    setClazz("UIGrid") ;
    service_ = service ;
	}
  
  public List getRequestMonitorData() {
  	return service_.getRequestMonitorData() ;
  }
   
  public String getRequestCounter() {
    return Long.toString(service_.getRequestCounter()) ; 
  }
  
  public String getAverageExecutionTime() {
    return Long.toString(service_.averageExecutionTime()) ; 
  }
  
  public String getMinExecutionTime() {
    return Long.toString(service_.minExecutionTime()) ; 
  }
  
  public String getMaxExecutionTime() {
    return Long.toString(service_.maxExecutionTime()) ; 
  }
  
  public String getFamily() { 
    return "org.exoplatform.portlets.monitor.portlet.component.UIPortalMonitor"; 
  }
}