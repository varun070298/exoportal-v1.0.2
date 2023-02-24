/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.container;

import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIBody;
import org.exoplatform.portal.faces.component.UIContainer;
import org.exoplatform.portal.faces.component.UIPage;
import org.exoplatform.portal.faces.component.UIPortal;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: PlaceBodyActionListener.java,v 1.1 2004/09/26 02:25:47 tuan08 Exp $
 */
public class PlaceBodyActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIContainer uiContainer = (UIContainer) event.getSource();
    Object ancestor = uiContainer.getAncestorOfType(UIPage.class) ;
    if(ancestor != null) {
      //cannot put the body in page
      return ;
    }
		UIPortal uiPortal = (UIPortal)uiContainer.getAncestorOfType(UIPortal.class) ;
		UIBody uiBody = uiPortal.findUIBody() ;
	  UIContainer uiBodyParent =(UIContainer) uiBody.getParent() ;
		uiBodyParent.removeChild(uiBody) ;
		uiContainer.getChildren().add(uiBody) ;
  }
}