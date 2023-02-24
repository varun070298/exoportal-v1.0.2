/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.resources.component;

import java.util.* ;
import org.exoplatform.faces.core.component.UIPortlet;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIResourcesPortlet.java,v 1.3 2004/07/31 14:56:22 tuan08 Exp $
 */
public class UIResourcesPortlet extends UIPortlet {
  public UIResourcesPortlet(UIListResources uiListResources, UIResource uiResource) throws Exception {
    try {
    setId("UIResourcesPortlet") ;
    setRendererType("ChildrenRenderer") ;   
    List children = getChildren() ;
    uiListResources.setRendered(true) ;
    children.add(uiListResources) ;
    uiResource.setRendered(false) ;
    children.add(uiResource) ;
    } catch(Throwable t) {
     t.printStackTrace() ; 
    }
  }
}