/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import javax.faces.component.UIComponent ;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;


/**
 * Jun 22, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ShowExplorerActionListener.java,v 1.1 2004/07/16 09:51:34 oranheim Exp $
 */
public class ShowExplorerActionListener extends ExoActionListener {
	public void execute(ExoActionEvent event) throws Exception {
		UIComponent comp = event.getComponent();
		UIPortlet uiParent = (UIPortlet) comp.getParent() ;
		uiParent.setRenderedComponent(UIExplorer.class) ;
	}
}
