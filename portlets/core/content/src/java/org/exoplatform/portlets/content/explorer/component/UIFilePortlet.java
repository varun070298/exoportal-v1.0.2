/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import org.exoplatform.faces.core.component.UIPortlet;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIFilePortlet.java,v 1.3 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIFilePortlet extends UIPortlet {
  
  public UIFilePortlet() throws Exception {
    getChildren().add(new UIFileExplorer()) ;
    setRendererType("ChildrenRenderer") ;
  }
}