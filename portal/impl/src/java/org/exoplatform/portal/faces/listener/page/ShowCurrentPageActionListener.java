/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.page;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIPortal;
/**
 * Jun 10, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ShowCurrentPageActionListener.java,v 1.1 2004/09/26 02:25:47 tuan08 Exp $
 */
public class ShowCurrentPageActionListener extends ExoActionListener {
	public void execute(ExoActionEvent event) throws Exception {
		 UIExoCommand uiComponent = (UIExoCommand)event.getComponent() ;
		 UIPortal uiPortal = (UIPortal) uiComponent.getAncestorOfType(UIPortal.class) ;
	   uiPortal.setBodyComponent(uiPortal.getCurrentUIPage()) ;
	}
}