/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.indexing.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIIndexerManagerPortlet.java,v 1.1 2004/09/16 02:48:43 tuan08 Exp $
 */
public class UIIndexerManagerPortlet extends UIPortlet {
  
  public UIIndexerManagerPortlet(UIListIndexer uiListIndexer,
                                 UIFileIndexerPlugin uiFileIndexer) throws Exception {
  	setId("UIIndexerManagerPortlet") ;	
    setClazz("UIIndexerManagerPortlet");
    setRendererType("ChildrenRenderer");
    List children = getChildren() ;
    uiListIndexer.setRendered(true) ;
    children.add(uiListIndexer) ;
    uiFileIndexer.setRendered(false) ;
    children.add(uiFileIndexer) ;
  }
}