/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import java.util.*;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect PortletMonitorAspect extends PortletBaseAspect {

  public PortletMonitorAspect() {

  }
  
  void around(Portlet portlet, RenderRequest request, RenderResponse response) :
    target(portlet) && call(void Portlet.render(RenderRequest, RenderResponse)) && args(request, response)
  {
    log_.debug("--> render method, call in monitor aspect");
    proceed(portlet, request, response) ;
  }

  void around(Portlet portlet, ActionRequest request, ActionResponse response) :
    target(portlet) && call(void processAction(ActionRequest, ActionResponse)) && args(request, response)
  {
    log_.debug("--> processAction method, call in monitor aspect");
    proceed(portlet, request, response) ;
  }
}
