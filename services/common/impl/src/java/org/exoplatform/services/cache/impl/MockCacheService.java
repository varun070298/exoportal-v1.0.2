/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache.impl;

import java.util.Collection;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sat, Sep 13, 2003 @  
 * Time: 1:12:22 PM
 */
public class MockCacheService  implements CacheService {
  
  private MockExoCache mockCache_ ;
   
  public MockCacheService() {
    mockCache_ = new MockExoCache();
  }
  
  public ExoCache getCacheInstance(String region) throws Exception {
    return mockCache_ ;
  }
  
  public Collection getAllCacheInstances() throws Exception {
    return null ;
  }
}

