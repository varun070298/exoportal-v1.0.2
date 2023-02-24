/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.component;

import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.portlets.monitor.portlet.component.model.PortletApplicationData;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortletApplicationMonitor.java,v 1.1 2004/05/05 21:33:13 tuan08 Exp $
 */
public class UIPortletApplicationMonitor extends UIExoComponentBase {
	private PortletApplicationData appData_ ;
    
  public UIPortletApplicationMonitor() {
    setRendererType("PortletApplicationMonitorRenderer");
	}
  
  public void setPortletApplicationData(PortletApplicationData appData) { appData_ = appData;  }
  
  public PortletApplicationData getPortletApplicationData() { return appData_ ;  }
  
  public String getFamily() { 
    return "org.exoplatform.portlets.monitor.portlet.component.UIPortletApplicationMonitor"; 
  }
}