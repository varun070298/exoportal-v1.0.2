/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.sms.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 11:15:20 AM
 */
public class UISmsMonitorPortlet extends UIPortlet {
    
	public UISmsMonitorPortlet(UISmsMonitor uiForm) {
	    setRendererType("ChildrenRenderer");
	    setId("sms-monitor-portlet");
	    List children = getChildren();
	    uiForm.setRendered(true);
	    children.add(uiForm);
	}
	
}
