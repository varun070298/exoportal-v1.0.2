/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.share;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIPortal;
/**
 * Jun 10, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ShowLastBodyComponentActionListener.java,v 1.2 2004/09/27 18:25:52 tuan08 Exp $
 */
public class ShowLastBodyComponentActionListener extends ExoActionListener {
	public void execute(ExoActionEvent event) throws Exception {
		 UIExoCommand uiComponent = (UIExoCommand)event.getComponent() ;
		 UIPortal uiPortal = (UIPortal) uiComponent.getAncestorOfType(UIPortal.class) ;
     uiPortal.setLastBodyComponent() ;
	}
}