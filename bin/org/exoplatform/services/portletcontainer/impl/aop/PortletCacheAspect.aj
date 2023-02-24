/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL    All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import java.util.*;
import javax.portlet.*;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionRequestImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.ActionResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.RenderResponseImp;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.CustomResponseWrapper;
import org.exoplatform.services.portletcontainer.impl.monitor.PortletMonitor;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect PortletCacheAspect extends PortletBaseAspect {

	private PortletContainerConf conf;
  private PortletMonitor portletMonitor;

  public PortletCacheAspect() {
  	conf = (PortletContainerConf) PortalContainer.getInstance().
  	    getComponentInstanceOfType(PortletContainerConf.class);
  	portletMonitor = (PortletMonitor)PortalContainer.getInstance().
  	    getComponentInstanceOfType(PortletMonitor.class);
  }

  void around(Portlet portlet, RenderRequest request, RenderResponse response) :
       render(portlet, request, response)
  {
	  if(!conf.isCacheEnable()){
			proceed(portlet, request, response);
      return;
	  }

    log_.debug("--> render method, call cache interceptor");
    RenderRequestImp req = (RenderRequestImp) request;
    RenderResponseImp res = (RenderResponseImp) response;
    if (req.getPortletDatas().getExpirationCache() == null){
      proceed(portlet, request, response);
      return;
    }
    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
	  String uniqueId = req.getPortletWindowInternal().getWindowID().getUniqueID();
	  PortletMode mode = request.getPortletMode();
    WindowState window = request.getWindowState();

    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache());
    log_.debug("Is cache global: " + isCacheGlobal);
    String key = null;
    if(isCacheGlobal){
      key = req.getInput().getWindowID().getOwner() + uniqueId;
    } else {
      key = req.getSession().getId() + req.getRemoteUser() + req.getInput().getWindowID().getOwner() + uniqueId;
    }

    if(req.isRenderRequest()){
      proceed(portlet, request, response);
      updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
      return;      
    }

    if(portletMonitor.needsCacheInvalidation(portletAppName, portletName,
                                             key, mode, window, isCacheGlobal)){
      portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);
    }

    int expirationPeriod = portletMonitor.getCacheExpirationPeriod(portletAppName, portletName);
    long lastAccessTime = portletMonitor.getPortletLastAccessTime(portletAppName, portletName,
                                                                  key, isCacheGlobal);
    long currentAccessTime = System.currentTimeMillis();
    if (expirationPeriod == 0) {
      log_.debug("Expiration period 0 before proceed");
      proceed(portlet, request, response);
      log_.debug("Expiration period 0 after proceed");
    } else if (expirationPeriod == -1) {
      if (portletMonitor.isDataCached(portletAppName, portletName, key, isCacheGlobal)) {
        log_.debug("Use cache : Expiration period -1 data already cached");
        ((RenderOutput) res.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
        ((RenderOutput) res.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
                                                                                portletName, key,
                                                                                isCacheGlobal));
      } else {
        log_.debug("Expiration period -1 data first cached, before proceed");
        proceed(portlet, request, response);
        log_.debug("Expiration period -1 data first cached, after proceed");
        updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
      }
    } else if (currentAccessTime - lastAccessTime > expirationPeriod * 1000) {
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 before proceed");
      proceed(portlet, request, response);
      log_.debug("Expiration period currentAccessTime - lastAccessTime > expirationPeriod * 1000 after proceed");
      updateCache(portletAppName, portletName, key, res, mode, window, isCacheGlobal);
    } else if (0 < currentAccessTime - lastAccessTime &&
        currentAccessTime - lastAccessTime < expirationPeriod * 1000) {
        log_.debug("Use cache : currentAccessTime - lastAccessTime < expirationPeriod * 1000");
        ((RenderOutput) res.getOutput()).setContent(portletMonitor.getCachedContent(portletAppName,
                                                                                    portletName, key,
                                                                                    isCacheGlobal));
        ((RenderOutput) res.getOutput()).setTitle(portletMonitor.getCachedTitle(portletAppName,
                                                                                portletName, key,
                                                                                isCacheGlobal));
    }
  }

  void around(Portlet portlet, ActionRequest request, ActionResponse response) :
       processAction(portlet, request, response)
  {
	  if(!conf.isCacheEnable()){
			proceed(portlet, request, response);
      return;
	  }
    log_.debug("--> processAction method, call cache interceptor");
    ActionRequestImp req = (ActionRequestImp) request;
    if (req.getPortletDatas().getExpirationCache() == null){
      proceed(portlet, request, response);
	  } else {
	    boolean isCacheGlobal = resolveCache(req.getPortletDatas().getGlobalCache());
	    log_.debug("Is cache global: " + isCacheGlobal);
  	  //invalidate cache
	    String portletAppName = req.getPortletWindowInternal().getWindowID().getPortletApplicationName();
    	String portletName = req.getPortletWindowInternal().getWindowID().getPortletName();
	  	String uniqueID = req.getPortletWindowInternal().getWindowID().getUniqueID();
    	String key = req.getSession().getId() + req.getRemoteUser() +
    	    req.getInput().getWindowID().getOwner() + uniqueID;

    	portletMonitor.removeCachedData(portletAppName, portletName, key, isCacheGlobal);

    	proceed(portlet, request, response) ;
	  }
  }

  private void updateCache(String portletAppName, String portletName, String key,
                           PortletResponseImp response, PortletMode mode, WindowState window,
                           boolean isCacheGlobal) {
    log_.debug("Update cache");
    portletMonitor.setCachedTitle(portletAppName, portletName,
                                  key, ((RenderOutput) response.getOutput()).getTitle(),
                                  isCacheGlobal);
    portletMonitor.setCachedContent(portletAppName, portletName,
                                    key, ((RenderOutput) response.getOutput()).getContent(),
                                    isCacheGlobal);
    portletMonitor.setCachedMode(portletAppName, portletName,
                                 key, mode, isCacheGlobal);

    portletMonitor.setCachedWindowState(portletAppName, portletName,
                                        key, window, isCacheGlobal);

    String s = (String)response.getOutput().getProperties().get(RenderResponse.EXPIRATION_CACHE);
    if (s != null) {
      int i = Integer.parseInt(s);
      portletMonitor.setCacheExpirationPeriod(portletAppName, portletName, i);
    }
    portletMonitor.setPortletLastAccessTime(portletAppName, portletName, key,
                                            System.currentTimeMillis(), isCacheGlobal);
  }

  private boolean resolveCache(String s){
    if(s == null)
      return false;
    if("true".equals(s))
      return true;
    else
      return false;
  }


}
