/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache.impl;

import java.util.Collection;
import java.util.HashMap ;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.cache.SimpleExoCache;
import org.picocontainer.Startable ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sat, Sep 13, 2003 @  
 * Time: 1:12:22 PM
 */
public class SimpleCacheService  implements CacheService, Startable {
  private HashMap cacheMap_ ; 
  
  public SimpleCacheService() {
    cacheMap_ = new HashMap() ;
  }
  
  public ExoCache getCacheInstance(String region) throws Exception {
    if( region == null || region.length() == 0) {
      throw new Exception ("region cannot be empty"); 
    }
    ExoCache cache = (ExoCache) cacheMap_.get(region) ; 
    if (cache == null) {
      synchronized (cacheMap_) {
        cache = new SimpleExoCache(region, 100) ;
        cacheMap_.put(region, cache) ;     
      }
    }
    return cache ;
  }
  
  public Collection getAllCacheInstances() throws Exception  {
    return cacheMap_.values() ;
  }
  
  public void start() { }
  public void stop() {} 
}

