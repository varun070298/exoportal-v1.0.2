/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.io.Serializable;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.services.cache.ExoCache;

import net.sf.hibernate.cache.CacheException;
import net.sf.hibernate.cache.Timestamper;

/**
 * Jul 17, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoCachePlugin.java,v 1.1 2004/08/29 21:47:58 benjmestrallet Exp $
 */
public class ExoCachePlugin implements net.sf.hibernate.cache.Cache { 
  private ExoCache cache_ ;
  
  public ExoCachePlugin(ExoCache cache) {
  	cache_ = cache ;
  }

  public Object get(Object key) throws CacheException {
    //System.out.println("::::::::::::::::::::::::::::: get() key = " + key) ;
  	try {
  		return cache_.get((Serializable)key) ;
  	} catch (Exception ex) {
  		throw new CacheException(ex) ; 
  	}
  }
  
  public void put(Object key, Object value) throws CacheException {
    //System.out.println("::::::::::::::::::::::::::::: put() key = " + key + " value " + value) ;
  	try {
  		cache_.put((Serializable)key, value) ;
  	} catch (Exception ex) {
  		throw new CacheException(ex) ; 
  	}
  }
  
  public void remove(Object key) throws CacheException {
    //System.out.println("::::::::::::::::::::::::::::: remove() key = " + key) ;
  	try {
  		cache_.remove((Serializable)key) ;
  	} catch (Exception ex) {
  		throw new CacheException(ex) ; 
  	}
  }

  public void clear() throws CacheException {
    //System.out.println(ExpceptionUtil.getExoStackTrace(new Exception())) ;
    try {
      cache_.clear() ;
    } catch (Exception ex) {
      throw new CacheException(ex) ; 
    }
  }

  public void destroy() throws CacheException {
  	//System.out.println(ExpceptionUtil.getExoStackTrace(new Exception())) ;
  }

  public void lock(Object key) throws CacheException {
  }


  public void unlock(Object key) throws CacheException {
  }

  public long nextTimestamp() {  return Timestamper.next(); }

  /**
   * Returns the lock timeout for this cache.
   */
  public int getTimeout() {  return Timestamper.ONE_MS * 60000; }
}