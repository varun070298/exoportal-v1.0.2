/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.renderer.html.BorderDecorator;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIMessageCenterConfig.java,v 1.3 2004/10/27 03:12:40 tuan08 Exp $
 */
public class UIMessageCenterConfig extends UIPortlet {
  
  public UIMessageCenterConfig(UIAccountConfiguration uiAccConfig,
                               UIAccountForm uiAccForm,
                               UIFolderForm uiFolderForm)  {
  	setId("UIMessageCenterConfig") ;	
    setClazz("UIMessageCenterConfig");
    setRendererType("ChildrenRenderer");
    setDecorator(new BorderDecorator("ic3-border")) ;
    List children = getChildren() ;
    uiAccConfig.setRendered(true) ;
    children.add(uiAccConfig);
    uiAccForm.setRendered(false) ;
    children.add(uiAccForm) ;
    uiFolderForm.setRendered(false) ;
    children.add(uiFolderForm) ;
  }
}