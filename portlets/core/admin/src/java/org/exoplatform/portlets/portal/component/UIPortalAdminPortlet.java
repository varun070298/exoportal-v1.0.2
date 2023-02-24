/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.portal.component;

import org.exoplatform.faces.core.component.UIPortlet;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 18, 2004
 * @version $Id$
 */
public class UIPortalAdminPortlet extends UIPortlet {
  public UIPortalAdminPortlet(UIAvailablePortal uiAvailablePortal) {
    setId("UIPortalAdminPortlet") ;
    setRendererType("ChildrenRenderer") ;
    getChildren().add(uiAvailablePortal) ;
  }
}