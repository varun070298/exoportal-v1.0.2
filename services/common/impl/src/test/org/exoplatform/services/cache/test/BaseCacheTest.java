/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache.test;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.test.BasicTestCase;
/*
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: BaseCacheTest.java,v 1.3 2004/04/10 17:21:36 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class BaseCacheTest extends BasicTestCase {
  
  CacheService service_ ;

  public BaseCacheTest(String name) {
    super(name);
  }

  public void testCacheService() throws Exception {
    ExoCache cache = service_.getCacheInstance("exo") ;
    cache.put("test", "this is a test") ;
    String ret = (String) cache.get("test"); 
    assertTrue("Return value from cache is not null", ret != null) ;

  }
  
  protected String getDescription() {
    return "Test Cache Service" ;
  }
}
