/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL. All rights reserved.         *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import org.exoplatform.portlets.content.display.component.model.ContentConfig;

/**
 * @author benjaminmestrallet
 */
public class UIStaticContentPortlet extends UIContentPortlet {
  
  public UIStaticContentPortlet() throws Exception {
    super();
  }
  
  protected UIContentTab createUIContentTab(ContentConfig contentConfig) throws Exception{
    return new UIStaticContentTab(contentConfig);
  }
  
}
