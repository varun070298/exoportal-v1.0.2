/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.share;

import java.util.List;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.component.*;


/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: MoveActionListener.java,v 1.1 2004/09/26 02:25:48 tuan08 Exp $
 */
public class MoveActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIBasicComponent uiComponent = (UIBasicComponent) event.getSource() ;
    String action = event.getAction() ;
    if (PortalConstants.MOVE_UP_ACTION.equals(action)) {
      moveUp(uiComponent) ;
    } else if (PortalConstants.MOVE_DOWN_ACTION.equals(action)) {
      moveDown(uiComponent) ;
    }
  }
  
  public void moveUp(UIBasicComponent uiComponent) {
    UIBasicComponent uiParent = (UIBasicComponent) uiComponent.getParent();
    List children = uiParent.getChildren() ;
		for (int i = 0; i < children.size(); i++) {
			UIBasicComponent uiChild = (UIBasicComponent) children.get(i);
			if (uiChild == uiComponent) {
				if(i != 0) {
					Object tmp = children.remove(i);
          children.add(i - 1, tmp);
          uiParent.setComponentModified(true) ;
        }
				return;
      }
    }
  }

  public void moveDown(UIBasicComponent uiComponent) {
    UIBasicComponent uiParent = (UIBasicComponent) uiComponent.getParent();
    List children = uiParent.getChildren() ;
		for (int i = 0; i < children.size(); i++) {
			UIBasicComponent uiChild = (UIBasicComponent) children.get(i);
			if (uiChild == uiComponent) {
				if(i != children.size() - 1) {
					Object tmp = children.remove(i);
          children.add(i + 1, tmp);
          uiParent.setComponentModified(true) ;
        }
				return;
			}
		}
  }
}