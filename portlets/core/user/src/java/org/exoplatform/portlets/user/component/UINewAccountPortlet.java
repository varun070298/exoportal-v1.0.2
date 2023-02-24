/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component ;

import org.exoplatform.faces.core.component.UIPortlet;
/**
 * Wed, Dec 22, 2003 @ 23:14
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIIC3LoginPortlet.java,v 1.1 2004/11/01 15:21:42 tuan08 Exp $
 */
public class UINewAccountPortlet extends UIPortlet {
	
	public UINewAccountPortlet() throws Exception  {
    setRendererType("ChildrenRenderer");
    UIAccountForm comp = (UIAccountForm)addChild(UIAccountForm.class) ;
    comp.customizeNewAccountForm() ;
	}
}
