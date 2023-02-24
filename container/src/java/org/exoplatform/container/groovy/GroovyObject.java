/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy;

import groovy.lang.GroovyClassLoader;
import java.net.URL;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class GroovyObject {
  private String resource_ ;
  private Object object_ ;
  private Class type_ ;
  private boolean reloadable_ = false ;
  private long loadTime_ = 1;
  
  public GroovyObject(String resource) {
    resource_ = resource ;
    loadTime_ = System.currentTimeMillis() ;
  }
  
  public String getGroovyResource()  { return resource_ ; }
  
  public Object getObject() {  return object_ ;}
  public void   setObject(Object object) {  
    loadTime_ = System.currentTimeMillis() ;
    object_ = object ;
    if(object != null) { 
      type_ =  object.getClass() ;
    }  else {
      type_ = null ; 
    }
  }
  
  public boolean isReloadable() { return reloadable_ ; }
  public long getLoadTime() { return loadTime_ ; }
  
  public Class getType() { return type_ ; }
  
  synchronized public void setType(GroovyClassLoader gcl) throws Exception {
    URL url = gcl.getResource(resource_) ;
    if(url.toString().startsWith("file:")) reloadable_ = true ;
    else reloadable_ = false ;
    type_ =  gcl.parseClass(url.openStream()) ;
    object_ = null ;
  }
}