/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component ;

import java.util.List;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.user.component.UILogin;
import org.exoplatform.faces.user.component.UILogout;
import org.exoplatform.portal.session.RequestInfo;
/**
 * Wed, Dec 22, 2003 @ 23:14
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIIC3LoginPortlet.java,v 1.1 2004/11/01 15:21:42 tuan08 Exp $
 */
public class UIWelcomePortlet extends UIPortlet {
	
	public UIWelcomePortlet(UILogin uiLogin, 
                          UILogout uiLogout) throws Exception {
		setClazz("UIWelcomePortlet");
    setRendererType("ChildrenRenderer");
    List children = getChildren() ;
    RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class) ;
    if (RequestInfo.PUBLIC_ACCESS.equals(rinfo.getAccessibility())) {
      children.add(uiLogin) ;
    } else {
      children.add(uiLogout) ;
    }    
	}
}
