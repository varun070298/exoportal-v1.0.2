/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.share;

import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.*;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: EditPropertiesActionListener.java,v 1.2 2004/09/27 20:44:59 tuan08 Exp $
 */
public class EditPropertiesActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIBasicComponent uiComponent = (UIBasicComponent) event.getSource() ;
    UIPortal uiPortal = (UIPortal)uiComponent.getAncestorOfType(UIPortal.class) ;
    if(uiComponent instanceof UIPage) {
      UIPageForm uiPageForm = 
        (UIPageForm)uiPortal.getPortalComponent(UIPageForm.class) ;
      uiPageForm.setEditingPage((UIPage) uiComponent) ;
      uiPortal.setBodyComponent(uiPageForm) ;
    } else if(uiComponent instanceof UIContainer) {
      UIContainerForm uiContainerForm = 
        (UIContainerForm)uiPortal.getPortalComponent(UIContainerForm.class) ;
      uiContainerForm.setEditingContainer((UIContainer) uiComponent) ;
      uiPortal.setBodyComponent(uiContainerForm) ;
    } else if(uiComponent instanceof UIPortlet) {
      UIPortletForm uiPortletForm = 
        (UIPortletForm)uiPortal.getPortalComponent(UIPortletForm.class) ;
      uiPortletForm.setEditingPortlet((UIPortlet) uiComponent) ;
      uiPortal.setBodyComponent(uiPortletForm) ;
    } else if(uiComponent instanceof UIPortal) {
      UIPortalForm uiPortalForm =(UIPortalForm) uiPortal.getPortalComponent(UIPortalForm.class);
      uiPortalForm.setUIPortal(uiPortal) ;
      uiPortal.setBodyComponent(uiPortalForm) ;
    }
  }
}
