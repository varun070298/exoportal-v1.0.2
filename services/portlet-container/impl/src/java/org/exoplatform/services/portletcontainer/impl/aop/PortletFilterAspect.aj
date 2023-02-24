/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import org.aspectj.lang.SoftException;

import java.util.*;
import org.exoplatform.Constants;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletContext;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.pci.model.Filter ;
import org.exoplatform.services.portletcontainer.impl.filter.PortletFilterConfigImpl;
import org.exoplatform.container.PortalContainer;


import org.exoplatform.services.portletcontainer.filter.PortletFilter;
import org.exoplatform.services.portletcontainer.filter.PortletFilterChain;
import org.exoplatform.services.portletcontainer.impl.filter.PortletFilterChainImpl;
import java.io.IOException;


import org.exoplatform.services.portletcontainer.filter.PortletFilterConfig;
import org.exoplatform.container.PortalContainer;


/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect PortletFilterAspect extends PortletBaseAspect {



  public PortletFilterAspect() {
  }
  
  void around(Portlet portlet, RenderRequest request, RenderResponse response) :
       render(portlet, request, response)
  {
    log_.debug("--> render method, call portlet filter aspect");

		PortalContainer manager = PortalContainer.getInstance();
		PortletRequestImp req = (PortletRequestImp) request;
		PortletContext portletContext = req.getPortletConfig().getPortletContext();
		String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
		List filters = req.getPortletDatas().getFilter();
	  if(filters.isEmpty()) {
	    proceed(portlet, request, response) ;
	    return;
	  }
	  try {
			Collection filterInstances = new ArrayList();
			for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
				Filter portletFilter = (Filter) iterator.next();
		  	String filterName = portletFilter.getFilterName();
				String filterClass = portletFilter.getFilterClass();
				String key = portletAppName + Constants.FILTER_ENCODER + filterName;
				PortletFilter filter = (PortletFilter) manager.getComponentInstance(key);
				if (filter == null){
				  ClassLoader cl = Thread.currentThread().getContextClassLoader();
				  manager.registerComponentImplementation(key, cl.loadClass(filterClass));
					filter = (PortletFilter) manager.getComponentInstance(key);
					filter.init(new PortletFilterConfigImpl(filterName, portletFilter.getInitParam(), portletContext));
				}
				filterInstances.add(filter);
			}
    	PortletFilterChainImpl invoker = new PortletFilterChainImpl();
    	invoker.invoke(request, response, filterInstances, portlet, false);
	  } catch (Exception e){
	    throw new SoftException(e);
	  }
  }

  void around(Portlet portlet, ActionRequest request, ActionResponse response) :
       processAction(portlet, request, response)
  {
    log_.debug("--> processAction method, call portlet filter aspect");

		PortalContainer manager = PortalContainer.getInstance();
		PortletRequestImp req = (PortletRequestImp) request;
		PortletContext portletContext = req.getPortletConfig().getPortletContext();
		String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
		List filters = req.getPortletDatas().getFilter();
	  if(filters.isEmpty()){
			proceed(portlet, request, response) ;
	    return;
	  }
	  try {
			Collection filterInstances = new ArrayList();
			for (Iterator iterator = filters.iterator(); iterator.hasNext();) {
				Filter portletFilter = (Filter) iterator.next();
			  String filterName = portletFilter.getFilterName();
				String filterClass = portletFilter.getFilterClass();
				String key = portletAppName + Constants.FILTER_ENCODER + filterName;
				PortletFilter filter = (PortletFilter) manager.getComponentInstance(key);
				if (filter == null){
				  ClassLoader cl = Thread.currentThread().getContextClassLoader();
				  manager.registerComponentImplementation(key, cl.loadClass(filterClass));
					filter = (PortletFilter) manager.getComponentInstance(key);
					filter.init(new PortletFilterConfigImpl(filterName, portletFilter.getInitParam(), portletContext));
				}
				filterInstances.add(filter);
			}
    	PortletFilterChainImpl invoker = new PortletFilterChainImpl();
    	invoker.invoke(request, response, filterInstances, portlet, true);
	  } catch (Exception e){
	    throw new SoftException(e);
	  }
  }




}

