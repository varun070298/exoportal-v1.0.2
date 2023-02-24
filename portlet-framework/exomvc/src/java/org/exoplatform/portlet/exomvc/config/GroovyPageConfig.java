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
public class GroovyPageConfig extends PageConfig  {
  private String groovyScript_ ;
  
  public String  getGroovyScript() { return groovyScript_ ; }
  public GroovyPageConfig setGroovyScript(String s) {
    groovyScript_ = s ;
    return this ;
  }
  
  public Page  getPageObject(Configuration configuration) throws Exception  {
    Page page = (Page) configuration.getGroovyResourceManager().
                                     getGroovyManager().
                                     getObject(groovyScript_) ;
    page.setConfiguration(configuration) ;
    page.setPageName(getPageName()) ;
    return page ;
  }
}