/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.container;

import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIContainer;
import org.exoplatform.portal.faces.component.UIContainerForm;
import org.exoplatform.portal.faces.component.UIPortal;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: AddContainerActionListener.java,v 1.1 2004/09/26 02:25:47 tuan08 Exp $
 */
public class AddContainerActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIBasicComponent uiComponent = (UIBasicComponent) event.getSource() ;
    UIPortal uiPortal = (UIPortal)uiComponent.getAncestorOfType(UIPortal.class) ;
    UIContainer uiContainer = (UIContainer) uiComponent;
    UIContainerForm uiContainerForm = 
    	(UIContainerForm)uiPortal.getPortalComponent(UIContainerForm.class) ;
    uiContainerForm.addNewContainer(uiContainer) ;
    uiPortal.setBodyComponent(uiContainerForm) ;
  }
}
