/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache;

import java.io.Serializable;
import java.util.* ;
import java.lang.ref.SoftReference;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sat, Sep 13, 2003 @  
 * Time: 1:12:22 PM
 */
public class SimpleExoCache  extends LinkedHashMap implements ExoCache {
	private static int DEFAULT_MAX_SIZE =  100 ;
  
  private String name_ ;
	private int maxSize_ ;
  private int cacheHit_ ;
  private int cacheMiss_ ;
	
	 public SimpleExoCache() {
  	maxSize_  = DEFAULT_MAX_SIZE ;
  }
	
  public SimpleExoCache(int maxSize) {
  	maxSize_  = maxSize ;
  }
  
  public SimpleExoCache(String name, int maxSize) {
    maxSize_  = maxSize ;
    name_ = name ;
  }
  
  public String getName() { return name_ ; }
  public void   setName(String s) {  name_ = s ; } 
  
  public int  getCacheSize()  { return size() ; }
  
  public int  getMaxSize() { return maxSize_ ; }
  public void setMaxSize(int max) { maxSize_ = max ; }
  
  synchronized public Object get(Serializable name)  {
  	SoftReference ref = (SoftReference) super.get(name) ;
  	if(ref !=  null)  { 
      cacheHit_++ ;
      return ref.get() ;
    }
    cacheMiss_++ ;
    return null ; 
  }
  
  synchronized public Object remove(Serializable name)  {
  	SoftReference ref = (SoftReference) super.remove(name) ;
  	if(ref !=  null)  return ref.get() ;
  	return null ;
  }

  synchronized public void put(Serializable name, Object obj)  {
  	SoftReference ref = new SoftReference(obj) ;
  	super.put(name, ref) ;
  }
  
  public int getCacheHit()  { return cacheHit_  ;}
  
  public int getCacheMiss() { return cacheMiss_ ; }
  
  protected boolean removeEldestEntry(Map.Entry eldest) {
    return size() > maxSize_ ;
 }
}