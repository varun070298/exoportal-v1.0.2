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
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletMonitor;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import java.io.IOException;
import org.aspectj.lang.SoftException;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect PortletContentAspect extends PortletBaseAspect {

  public PortletContentAspect() {

  }

  void around(Portlet portlet, RenderRequest request, RenderResponse response) :
       render(portlet, request, response)
  {
    log_.debug("--> render method, call content ");
    proceed(portlet, request, response) ;
    CustomResponseWrapper responseWrapper = ((CustomResponseWrapper)((PortletResponseImp)response).getResponse());
    try{
      responseWrapper.flushBuffer();
      ((RenderOutput) ((PortletResponseImp)response).getOutput()).
          setContent(responseWrapper.getPortletContent());
      ((RenderOutput) ((PortletResponseImp)response).getOutput()).
          setCacheHit(false);
    } catch (IOException e){
       throw new SoftException(e);
    }
  }
}
