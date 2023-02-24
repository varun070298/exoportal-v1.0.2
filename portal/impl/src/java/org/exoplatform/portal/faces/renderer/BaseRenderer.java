/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 ***************************************************************************/
package org.exoplatform.portal.faces.renderer;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIBasicComponent;
import org.exoplatform.portal.session.RequestInfo;
/**
 * Created by The eXo Platform SARL        .
 * Date: Mar 15, 2003
 * Time: 11:30:40 AM
 */
abstract public class BaseRenderer extends HtmlBasicRenderer {
  
  public void decode(FacesContext context, UIComponent uiComponent) {
    RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
    String portalAction = rinfo.getPortalAction();
    if(portalAction != null) {
      UIBasicComponent basicComponent = (UIBasicComponent) uiComponent ;
      Map paramMap = context.getExternalContext().getRequestParameterMap() ;
      basicComponent.broadcast(new ExoActionEvent(uiComponent, portalAction, paramMap)) ;
    } else {
      decodeComponentAction(context, uiComponent) ;
    }
  }
  
  protected void decodeComponentAction(FacesContext context, UIComponent uiComponent) {
  }
  
}