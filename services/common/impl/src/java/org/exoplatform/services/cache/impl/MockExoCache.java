/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache.impl;

import java.io.Serializable ;
import org.exoplatform.services.cache.ExoCache;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 */
public class MockExoCache  implements ExoCache {

  public MockExoCache() {
  }
  
  public String getName() { return "cache" ; }
  
  public Object get(Serializable name) throws Exception {
    return null ;
  }
  
  public Object remove(Serializable name) throws Exception {
    return null ; 
     
  }

  public void  put(Serializable key, Object obj) throws Exception {
  }

  public void   clear() throws Exception {
  }
  
  public int  getCacheSize()  { return 0 ; }
  
  public int  getMaxSize() { return 10 ; }
  public void setMaxSize(int max) {  }
  
  public int getCacheHit()  { return 0  ;}
  
  public int getCacheMiss() { return 0 ; }
}

