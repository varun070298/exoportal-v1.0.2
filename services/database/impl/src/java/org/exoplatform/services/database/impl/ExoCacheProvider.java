/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import net.sf.hibernate.cache.Cache;
import net.sf.hibernate.cache.CacheException;
import net.sf.hibernate.cache.CacheProvider;
import net.sf.hibernate.cache.Timestamper;

import java.util.Properties ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
/**
 * Jul 17, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoCacheProvider.java,v 1.1 2004/08/29 21:47:58 benjmestrallet Exp $
 */
public class ExoCacheProvider implements CacheProvider {
  
  public ExoCacheProvider() {
    
  }
  
  public Cache buildCache(String name, Properties properties) throws CacheException {
    try {
      PortalContainer container = PortalContainer.getInstance() ;
      CacheService cservice = 
        (CacheService)container.getComponentInstanceOfType(CacheService.class) ;
    	ExoCache cache = cservice.getCacheInstance(name) ;
      cache.setMaxSize(1000) ;
    	return new ExoCachePlugin(cache);
    } catch (Exception ex) {
      ex.printStackTrace() ;
    	throw new CacheException("Cannot instanstiate cache provider") ;
    }
  }

  public long nextTimestamp() {  return Timestamper.next(); } 
}