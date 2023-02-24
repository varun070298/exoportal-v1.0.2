 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portal.faces.listener.container;

import java.util.List;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIContainer;
import org.exoplatform.portal.faces.renderer.html.container.TabRenderer;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class ChangeTabActionListener extends ExoActionListener{

  public void execute(ExoActionEvent event) throws Exception {
    UIContainer container = (UIContainer) event.getSource() ;
    String id = event.getParameter(TabRenderer.TAB_PARAMETER);
    List children = container.getChildren();
    for (int i = 0; i < children.size(); i++) {
      UIBasicComponent component = (UIBasicComponent)children.get(i);
      if(id.equals(component.getId())){
        container.setSelectedComponent(i);
        return;
      }              
    }
  }

}
