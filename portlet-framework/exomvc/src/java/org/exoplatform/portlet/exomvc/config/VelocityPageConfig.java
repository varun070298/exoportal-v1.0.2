/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.VelocityPage;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class VelocityPageConfig extends PageConfig {
  private String velocityTemplate_ ;
  private String pageClassName_ = "org.exoplatform.portlet.mvc.VelocityPage";
  private VelocityPage cache_ ;
  
  public String getTemplate() { return velocityTemplate_ ; }
  public VelocityPageConfig setTemplate(String className) {
    velocityTemplate_ = className ;
    return this ;
  }
  
  public String getPageClassName() { return pageClassName_ ; }
  public VelocityPageConfig setPageClassName(String s) { 
    pageClassName_ = s ;
    return this ;
  }
  
  public Page getPageObject(Configuration configuration) throws Exception    {
    if(cache_ != null) return cache_ ;
    synchronized(configuration) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
      Class clazz = cl.loadClass(pageClassName_) ;
      cache_ = (VelocityPage) clazz.newInstance() ;
      cache_.setConfiguration(configuration) ;
      cache_.setPageName(getPageName()) ;
      cache_.setTemplate(velocityTemplate_) ;
    }
    return cache_ ;
  }
}