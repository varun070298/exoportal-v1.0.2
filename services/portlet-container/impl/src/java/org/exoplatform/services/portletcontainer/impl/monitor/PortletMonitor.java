/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/


package org.exoplatform.services.portletcontainer.impl.monitor;

import org.apache.commons.logging.Log;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.PortletContainerConstants;
import org.exoplatform.services.portletcontainer.monitor.*;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Sep 10, 2003
 * Time: 2:36:19 PM
 */
public class PortletMonitor implements PortletContainerMonitor {

  public static final char SEPARATOR = '/';
  public static Map versionNumberMap = new HashMap();

  private Map runtimeDatas_;
  private Map destroyedPortlets_;
  private Map brokenPortlets_;
  private Log log_;
  private ExoCache globalCache_;
  private CacheService cacheService_;

  public PortletMonitor(LogService logService, CacheService cacheService) throws Exception {
    this.log_ = logService.getLog("org.exoplatform.services.portletcontainer");
    this.cacheService_ = cacheService;
    globalCache_ = cacheService.getCacheInstance(PortletContainerConstants.GLOBAL_SCOPE_CACHE);
    runtimeDatas_ = Collections.synchronizedMap(new HashMap());
    brokenPortlets_ = Collections.synchronizedMap(new HashMap());
    destroyedPortlets_ = Collections.synchronizedMap(new HashMap());
  }

  public Map getPortletRuntimeDataMap() {
    return runtimeDatas_;
  }

  public PortletRuntimeDatasImpl getPortletRuntimeData(String appName, String portletName) {
    return (PortletRuntimeDatasImpl) runtimeDatas_.get(appName + SEPARATOR + portletName);
  }

  public synchronized void registerPortletApp(String portletApplicationName) {
    long versionNumber = 1;
    if (versionNumberMap.get(portletApplicationName) != null) {
      versionNumber = ((Long) versionNumberMap.get(portletApplicationName)).longValue() + 1;
    }
    versionNumberMap.put(portletApplicationName, new Long(versionNumber));
  }

  public long getPortletVersionNumber(String portletAppName) {
    return ((Long) versionNumberMap.get(portletAppName)).longValue();
  }

  public synchronized void register(String portletApplicationName, String portletName) {
    PortletRuntimeData rD = 
      new PortletRuntimeDatasImpl(portletApplicationName, portletName, 
                                  cacheService_, globalCache_, log_);
    runtimeDatas_.put(portletApplicationName + SEPARATOR + portletName, rD);
    brokenPortlets_.remove(portletApplicationName + SEPARATOR + portletName);
    destroyedPortlets_.remove(portletApplicationName + SEPARATOR + portletName);
  }

  public boolean isInitialized(String portletAppName, String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas_.get(portletAppName + 
        SEPARATOR + portletName);
    if (datas == null) {
      return false;
    }
    if (datas.isInitialized()) {
      return true;
    }
    return false;
  }

  public synchronized void init(String portletAppName, String portletName, int cacheExpirationTime) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletAppName + SEPARATOR + portletName);
    datas.setInitialized(true);
    datas.setCacheExpirationPeriod(cacheExpirationTime);
  }

  public synchronized void brokePortlet(String portletAppName, String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas_.get(portletAppName + 
        SEPARATOR + portletName);
    if (datas == null) {
      datas = (PortletRuntimeData) destroyedPortlets_.get(portletAppName + SEPARATOR + portletName);
      destroyedPortlets_.remove(portletAppName + SEPARATOR + portletName);
    }
    runtimeDatas_.remove(portletAppName + SEPARATOR + portletName);
    brokenPortlets_.put(portletAppName + SEPARATOR + portletName, datas);
  }

  public boolean isBroken(String portletAppName, String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) brokenPortlets_.get(portletAppName + SEPARATOR +
        portletName);
    if (datas != null) {
      return true;
    } 
    return false ;
  }

  public boolean isDestroyed(String portletAppName, String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) destroyedPortlets_.get(portletAppName + SEPARATOR +
        portletName);
    if (datas != null) {
      return true;
    }
    return false;
  }

  public boolean isAvailable(String portletApplicationName, String portletName, long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null) {
      return false;
    }
    return datas.isAvailable(l);
  }
  
  public boolean isAvailable(String portletApplicationName, String portletName) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null) {
      return false;
    }
    return datas.isAvailable(System.currentTimeMillis());
  }  
  
  public long whenAvailable(String portletApplicationName, String portletName){
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
    get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null) {
      return -1;
    }
    if(datas.isAvailable(System.currentTimeMillis()))
      return 0;
    else 
      return datas.whenAvailable();
  }

  public boolean isInitialisationAllowed(String portletApplicationName, String portletName, long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null) {
      return false;
    }
    return datas.isInitialisationAllowed(l);
  }

  public synchronized void destroy(String portletApplicationName, String portletName) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas_.
        get(portletApplicationName + SEPARATOR + portletName);
    if (datas == null) {
      return;//already destroyed or broke
    }
    runtimeDatas_.remove(portletApplicationName + SEPARATOR + portletName);
    destroyedPortlets_.put(portletApplicationName + SEPARATOR + portletName, datas);
  }

  public void setLastAccessTime(String portletAppName, String portletName, long l) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletAppName + SEPARATOR + portletName);
    if (datas == null) {//look in broken portlets
      datas = (PortletRuntimeDatasImpl) brokenPortlets_.get(portletAppName + SEPARATOR + portletName);
    }
    if (datas == null) {//look in destroyed portlets
      datas = (PortletRuntimeDatasImpl) destroyedPortlets_.get(portletAppName + SEPARATOR + portletName);
    }
    datas.setLastAccessTime(l);
  }

  public void setLastInitFailureAccessTime(String portletAppName, String portletName, long l) {
    PortletRuntimeData datas = (PortletRuntimeData) runtimeDatas_.
        get(portletAppName + SEPARATOR + portletName);
    datas.setLastInitFailureAccessTime(l);
  }

  public void setLastFailureAccessTime(String portletAppName, String portletName, long l) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    datas.setLastFailureAccessTime(l);
  }

  public void setUnavailabilityPeriod(String portletAppName, String portletName, int unavailableSeconds) {
    PortletRuntimeDatasImpl datas = (PortletRuntimeDatasImpl) runtimeDatas_.
        get(portletAppName + SEPARATOR + portletName);
    datas.setUnavailabilityPeriod(unavailableSeconds * 1000);
  }

  public boolean isDataCached(String portletApplicationName, String portletName,
                              String key, boolean isCacheGlobal) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletApplicationName + SEPARATOR + portletName);
    return datas.isDataCached(key, isCacheGlobal);
  }

  public void removeCachedData(String portletApplicationName, String portletName,
                               String key, boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletApplicationName + SEPARATOR + portletName);
    datas.removeCachedData(key, isCacheGlobal);
  }

  public int getCacheExpirationPeriod(String portletApplicationName, String portletName) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletApplicationName + SEPARATOR + portletName);
    return datas.getCacheExpirationPeriod();
  }

  public void setCacheExpirationPeriod(String portletAppName, String portletName, int i) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    datas.setCacheExpirationPeriod(i);
  }

  public long getPortletLastAccessTime(String portletAppName, String portletName,
                                       String key, boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null) {
      return ((CachedDataImpl) cachedData).getLastAccessTime();
    }
    return 0;
  }

  public void setPortletLastAccessTime(String portletAppName, String portletName,
                                       String key, long lastAccessTime,
                                       boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setLastAccessTime(lastAccessTime);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else {
      cachedData.setLastAccessTime(lastAccessTime);
    }
  }

  public void setCachedTitle(String portletAppName, String portletName,
                             String key, String title, boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setTitle(title);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else {
      cachedData.setTitle(title);
    }
  }

  public String getCachedTitle(String portletAppName, String portletName,
                               String key, boolean isCacheGlobal) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null) {
      return cachedData.getTitle();
    }
    return null;
  }

  public void setCachedContent(String portletAppName, String portletName,
                               String key, char[] content,
                               boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setContent(content);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else {
      cachedData.setContent(content);
    }
  }

  public char[] getCachedContent(String portletAppName, String portletName,
                                 String key, boolean isCacheGlobal) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData != null) {
      return cachedData.getContent();
    }
    return null;
  }

  public void setCachedMode(String portletAppName, String portletName,
                            String key, PortletMode mode,
                            boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setMode(mode);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else {
      cachedData.setMode(mode);
    }
  }

  public void setCachedWindowState(String portletAppName, String portletName,
                                   String key, WindowState window,
                                   boolean isCacheGlobal) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedDataImpl cachedData = (CachedDataImpl) datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      cachedData = new CachedDataImpl();
      cachedData.setWindowState(window);
      datas.setCachedData(key, cachedData, isCacheGlobal);
    } else {
      cachedData.setWindowState(window);
    }
  }

  public boolean needsCacheInvalidation(String portletAppName, String portletName,
                                        String key, PortletMode mode,
                                        WindowState window,
                                        boolean isCacheGlobal) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    CachedData cachedData = datas.getCachedData(key, isCacheGlobal);
    if (cachedData == null) {
      return false;
    }
    if (cachedData.getMode() != mode || cachedData.getWindowState() != window) {
      return true;
    }
    return false;
  }

  public void setInitializationTime(String portletAppName, String portletName, long accessTime) {
    PortletRuntimeDatasImpl datas = 
      (PortletRuntimeDatasImpl) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    datas.setInitializationTime(accessTime);
  }

  public long getInitializationTime(String portletAppName, String portletName) {
    PortletRuntimeData datas = 
      (PortletRuntimeData) runtimeDatas_.get(portletAppName + SEPARATOR + portletName);
    return datas.getInitializationTime();
  }
}