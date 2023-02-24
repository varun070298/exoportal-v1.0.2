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
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
import org.aspectj.lang.SoftException;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect PortletSecurityAspect extends PortletBaseAspect {

  public PortletSecurityAspect() {

  }
  
  void around(Portlet portlet, RenderRequest request, RenderResponse response) :
       render(portlet, request, response)
  {
    log_.debug("--> render method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) request;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!request.isSecure())
        throw new SoftException(new Throwable("Need a secure transport layer"));
    }
    proceed(portlet, request, response) ;
  }

  void around(Portlet portlet, ActionRequest request, ActionResponse response) :
       processAction(portlet, request, response)
  {
    log_.debug("--> processAction method, call security interceptor");
    PortletRequestImp req = (PortletRequestImp) request;
    String portletName = req.getPortletDatas().getPortletName();
    boolean needSecure = req.needsSecurityContraints(portletName);
    if (needSecure) {
      if(!request.isSecure())
        throw new SoftException(new Throwable());
    }
    proceed(portlet, request, response) ;
  }
}
