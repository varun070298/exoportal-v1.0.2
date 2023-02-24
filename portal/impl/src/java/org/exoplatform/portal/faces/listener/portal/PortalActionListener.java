/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.portal;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.*;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.PortalConfig;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: PortalActionListener.java,v 1.3 2004/09/27 20:44:59 tuan08 Exp $
 */
public class PortalActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIPortal uiPortal = (UIPortal) event.getSource() ;
    String portalAction = event.getAction() ;
    if (PortalConstants.SAVE_PORTAL_ACTION.equals(portalAction)) {
      if(!uiPortal.isDirty()) return ;
      PortalConfigService service = 
        (PortalConfigService)PortalContainer.getInstance().
                             getComponentInstanceOfType(PortalConfigService.class) ;
      UIContainer uiLayout = (UIContainer) uiPortal.getChildren().get(0) ;
      uiLayout.buildTreeModel(null) ;
      PortalConfig componentModel = uiPortal.getPortalConfigModel() ;
      componentModel.setLayout(uiLayout.getContainerModel()) ;
      service.savePortalConfig(componentModel) ;
      uiPortal.setComponentDirty(false) ;
      uiPortal.clearComponentModified() ;
    } else  if (PortalConstants.EDIT_PORTAL_ACTION.equals(portalAction)) {
      UIPortalForm uiPortalForm = 
        (UIPortalForm)uiPortal.getPortalComponent(UIPortalForm.class) ;
      uiPortalForm.setUIPortal(uiPortal);
      uiPortal.setBodyComponent(uiPortalForm) ;
    } else  if (PortalConstants.CHANGE_LANGUAGE_ACTION.equals(portalAction)) {
      String localeName = event.getParameter(PortalConstants.LANGUAGE_PARAM) ;
      new ChangeLocaleVisitor().changeLocale(uiPortal, localeName) ;
      new ChangeLocaleVisitor().changeLocale(uiPortal.getCurrentUIPage()) ;
    }
  }
}