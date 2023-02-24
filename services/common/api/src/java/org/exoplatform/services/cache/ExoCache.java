/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.cache;

import java.io.Serializable;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface ExoCache  {
	
  public String getName() ;
  public Object get(Serializable name) throws Exception ;
  public Object remove(Serializable name) throws Exception ;
  public void   put(Serializable name, Object obj) throws Exception ;
  public void   clear() throws Exception ;
  public int  getCacheSize() ;
  public int  getMaxSize() ;
  public void setMaxSize(int max) ;
  
  public int getCacheHit() ;
  public int getCacheMiss() ;
}
