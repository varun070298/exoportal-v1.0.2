/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.indexing.component;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 14, 2004
 * @version $Id: ShowListIndexerPluginActionListener.java,v 1.1 2004/09/16 02:48:43 tuan08 Exp $
 */
public class ShowListIndexerPluginActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIExoCommand uiCommand = (UIExoCommand) event.getComponent() ;
    uiCommand.setRenderedSibling(UIListIndexer.class) ;
  }
}