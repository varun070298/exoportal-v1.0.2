/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import org.exoplatform.portlet.exomvc.Page;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class POJOPageConfig extends PageConfig {
  private String className_ ;
  private Page cache_ ;
  
  public String getClassName() { return className_ ; }
  public POJOPageConfig setClassName(String className) {
    className_ = className ;
    return this ;
  }
  
  public Page getPageObject(Configuration configuration) throws Exception    {
    if(cache_ != null) return cache_ ;
    synchronized(configuration) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
      Class clazz = cl.loadClass(className_) ;
      cache_ = (Page) clazz.newInstance() ;
      cache_.setConfiguration(configuration) ;
      cache_.setPageName(getPageName()) ;
    }
    return cache_ ;
  }
}