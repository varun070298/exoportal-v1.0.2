/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL. All rights reserved.         *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.util.List;

import org.exoplatform.faces.core.component.UIPortlet;

/**
 * @author benjaminmestrallet
 */
public class UIAdminStaticContentPortlet extends UIPortlet {

  public UIAdminStaticContentPortlet(UIContentConfig uiConfig, 
      UIContentConfigForm uiConfigForm) {
    setRendererType("ChildrenRenderer");
    List children = getChildren();
    uiConfigForm.setRendered(false);
    children.add(uiConfigForm);
    uiConfig.setRendered(true);
    uiConfig.setModificationAllowed(false);
    children.add(uiConfig);
  }

}