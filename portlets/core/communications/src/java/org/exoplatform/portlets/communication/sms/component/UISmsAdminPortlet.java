/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 21, 2004 11:15:20 AM
 */
public class UISmsAdminPortlet extends UIPortlet {
    
    public UISmsAdminPortlet(UISmsAdminForm uiForm) {
	    setRendererType("ChildrenRenderer");
	    setId("sms-admin-portlet");
	    List children = getChildren();
	    uiForm.setRendered(true);
	    children.add(uiForm);
    }

}
