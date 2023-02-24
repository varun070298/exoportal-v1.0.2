/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;



/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since Jun 24, 2004 11:12:56 AM
 */
public class UISmsHelpPortlet extends UIPortlet {

    public UISmsHelpPortlet(UISmsHelpContent uiHelp) {
	    setRendererType("ChildrenRenderer");
	    setId("sms-help-portlet");
	    List children = getChildren();
	    uiHelp.setRendered(true);
	    children.add(uiHelp);
    }
    
    

}
