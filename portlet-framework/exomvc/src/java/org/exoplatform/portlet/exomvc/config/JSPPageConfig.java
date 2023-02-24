/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.JSPPage;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class JSPPageConfig extends PageConfig {
  private String jspPage_ ;
  private String pageClassName_ = "org.exoplatform.portlet.mvc.JSPPage";
  private JSPPage cache_ ;
  
  public String getJSPPage() { return jspPage_ ; }
  public JSPPageConfig setJSPPage(String className) {
    jspPage_ = className ;
    return this ;
  }
  
  public String getPageClassName() { return pageClassName_ ; }
  public JSPPageConfig setPageClassName(String s) { 
    pageClassName_ = s ;
    return this ;
  }
  
  public Page getPageObject(Configuration configuration) throws Exception    {
    if(cache_ != null) return cache_ ;
    synchronized(configuration) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
      Class clazz = cl.loadClass(pageClassName_) ;
      cache_ = (JSPPage) clazz.newInstance() ;
      cache_.setConfiguration(configuration) ;
      cache_.setPageName(getPageName()) ;
      cache_.setJSPPage(jspPage_) ;
    }
    return cache_ ;
  }
}