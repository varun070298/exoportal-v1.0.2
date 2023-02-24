/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.listener.share;

import java.util.List;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.faces.component.UIBody;
import org.exoplatform.portal.faces.component.UIContainer;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 16, 2004
 * @version $Id: DeleteActionListener.java,v 1.1 2004/09/26 02:25:48 tuan08 Exp $
 */
public class DeleteActionListener extends ExoActionListener  {
  public void execute(ExoActionEvent event) throws Exception {
    UIBasicComponent uiComponent = (UIBasicComponent) event.getSource() ;
  	// Cannot remove the container that has the Body component
    if(uiComponent instanceof UIContainer) {
      if(uiComponent.findComponentById(UIBody.COMPONENT_ID) != null) {
        InformationProvider iprovider = findInformationProvider(uiComponent) ;
        iprovider.addMessage(new ExoFacesMessage("#{UIContainer.msg.remove-body-container}")) ;
        return ;
      }
    }
    UIBasicComponent uiParent = (UIBasicComponent)uiComponent.getParent() ;
    List children = uiParent.getChildren() ;
    children.remove(uiComponent) ;
    uiParent.setComponentModified(true) ;
  }
}