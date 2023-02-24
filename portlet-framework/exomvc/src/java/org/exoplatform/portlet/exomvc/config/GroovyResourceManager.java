/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import java.net.URL;
import javax.portlet.PortletContext;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.groovy.GroovyManager;
import org.exoplatform.container.groovy.GroovyManagerContainer;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 */
public class GroovyResourceManager {
  private GroovyManager gmanager_ ;
  
  public GroovyResourceManager(PortletContext context, String repo ) throws Exception {
    RootContainer root = RootContainer.getInstance() ;
    GroovyManagerContainer container = 
      (GroovyManagerContainer)root.getComponentInstanceOfType(GroovyManagerContainer.class) ;
    String realpath =  context.getRealPath(repo + "/groovy") + "/" ;
    URL classpath = root.getOSEnvironment().createURL(realpath) ;
    container.removeGroovyManager(classpath) ;
    gmanager_ = container.getGroovyManager(classpath) ;
  }
  
  public GroovyManager getGroovyManager() { return gmanager_ ; }
  
  public void destroy() {
    gmanager_.setDispose(true) ;
  }
}
