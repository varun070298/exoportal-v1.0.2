/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.io.File;
import java.net.URL;
import javax.servlet.ServletContext;
/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: MockConfigurationServiceImpl.java,v 1.11 2004/10/29 01:55:23 tuan08 Exp $
 */
public class MockConfigurationManagerImpl extends ConfigurationManagerImpl {
  private String confDir_ ;
  
  public MockConfigurationManagerImpl(ServletContext context) throws Exception {
    super(context) ;
  	File currentDir = new File("") ;
    String path =  currentDir.getAbsolutePath() ;
    int idx = path.indexOf("exoplatform") ;
    if(idx < 0) {
      path = System.getProperty("maven.exoplatform.dir") + "/" ;
      confDir_ = path + "/web/portal/src/webapp/WEB-INF" ;
    } else {
      path = path.substring(0, idx) ;
      path = path.replace('\\', '/') ;
      confDir_ = path + "exoplatform/web/portal/src/webapp/WEB-INF" ;
    }
  }
  
  protected URL getURL(String uri) throws Exception {
    if(uri.startsWith("jar:")) {
      String path = removePrefix("jar:/", uri) ;
      ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
      return cl.getResource(path) ;
    } else if(uri.startsWith("war:")) {
      String path = removePrefix("war:", uri) ;
      URL url =  new URL("file:" + confDir_ + path) ;
      return url ;
    }  else if(uri.startsWith("file:")) {
      return new URL(uri) ; 
    }
    return null ;
  }
}