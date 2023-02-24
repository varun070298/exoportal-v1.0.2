/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.component;

import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 19, 2005
 * @version $Id$
 */
public interface ExplorerListener {
  public void  onChange(UIExplorer uiExplorer, NodeDescriptor node)  ;
  public void  onRemove(UIExplorer uiExplorer, NodeDescriptor node)  ;
  public void  onModify(UIExplorer uiExplorer, NodeDescriptor node)  ;
  public void  onAddChild(UIExplorer uiExplorer, NodeDescriptor node)  ;
}
