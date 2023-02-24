/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.util.List;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIAdminContentPortlet.java,v 1.3 2004/08/24 18:44:35 tuan08 Exp $
 */
public class UIAdminContentPortlet extends UIAdminStaticContentPortlet {
  
  public UIAdminContentPortlet(UIContentEditor uiEditor, UIContentConfig uiConfig, 
      UIContentConfigForm uiConfigForm) {
    super(uiConfig, uiConfigForm);
    uiConfig.setModificationAllowed(true);
    List children = getChildren();
		uiEditor.setRendered(false);
		children.add(uiEditor);
	}
}