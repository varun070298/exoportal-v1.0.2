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
 * @version $Id: UIMessageCenterPortlet.java,v 1.5 2004/10/27 03:12:40 tuan08 Exp $
 */
public class UIMessageCenterPortlet extends UIPortlet {
  
  public UIMessageCenterPortlet(UISelectAccount uiSelectAccount,
                                UIAccount uiAccount) throws Exception {
  	setId("UIMessageCenterPortlet") ;	
    setClazz("UIMessageCenterPortlet");
    setRendererType("ChildrenRenderer");
    setDecorator(new BorderDecorator("ic3-border")) ;
    List children = getChildren() ;
    uiAccount.setRendered(true) ;
    uiAccount.setAccount(uiSelectAccount.getSelectAccount()) ;
    children.add(uiAccount) ;
    uiSelectAccount.setRendered(true) ;
    children.add(uiSelectAccount) ;
  }
}