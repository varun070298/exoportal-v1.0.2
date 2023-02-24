/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp.component;


import java.util.List;

import org.exoplatform.faces.core.component.UIPortlet;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 5 juin 2004
 */
public class UIWSRPAdminPortlet extends UIPortlet{

	public UIWSRPAdminPortlet(UIProducerNode uiProducerNode,
			                      UIProducerForm uiProducerForm) {
		setId("UIWSRPAdminPortlet") ;
		setClazz("UIWSRPAdminPortlet") ;
		setRendererType("SimpleTabRenderer") ;
		List children = getChildren() ;
		uiProducerNode.setRendered(true) ;
		children.add(uiProducerNode) ;
		uiProducerForm.setRendered(false) ;
		children.add(uiProducerForm) ;
	}

}
