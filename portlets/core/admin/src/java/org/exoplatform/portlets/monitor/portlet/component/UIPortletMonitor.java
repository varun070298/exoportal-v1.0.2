/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.component;

import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortletMonitor.java,v 1.1 2004/05/05 21:33:13 tuan08 Exp $
 */
public class UIPortletMonitor extends UIExoComponentBase {
	private PortletRuntimeData runtimeData_ ;
    
  public UIPortletMonitor() {
    setRendererType("PortletMonitorRenderer");
	}
  
  public void setPortletRuntimeData(PortletRuntimeData rd) {
    runtimeData_ = rd ; 
  }
  
  public PortletRuntimeData getPortletRuntimeData() {
    return runtimeData_ ; 
  }
  
  public String getFamily() { 
    return "org.exoplatform.portlets.monitor.portlet.component.UIPortletMonitor"; 
  }
}