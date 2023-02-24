/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL    All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import javax.portlet.* ;
import org.apache.commons.logging.Log ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.LogService;
/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
abstract aspect PortletBaseAspect {
  protected Log log_ = ((LogService)PortalContainer.getInstance().getComponentInstanceOfType(LogService.class)).getLog("org.exoplatform.services.portletcontainer");
  
  protected pointcut render(Portlet portlet, RenderRequest request, RenderResponse response) :
    target(portlet) && call(void Portlet.render(RenderRequest, RenderResponse)) && args(request, response) ;

  protected pointcut processAction(Portlet portlet, ActionRequest request, ActionResponse response) :
    target(portlet) && call(void Portlet.processAction(ActionRequest, ActionResponse)) && args(request, response) ;
}
