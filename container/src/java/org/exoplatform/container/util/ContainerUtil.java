/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.groovy.GroovyManager;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 28, 2004
 * @version $Id: ContainerUtil.java,v 1.1 2004/10/29 01:55:23 tuan08 Exp $
 */
public class ContainerUtil {
  static public Collection getConfigurationURL(String configuration) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
    Collection c = Collections.list(cl.getResources(configuration)) ;
    return c ;
  }
  
  static public void populate(ExoContainer container , ConfigurationManager conf) {
    Collection serviceConfigurations = conf.getServiceConfigurations();
    if(serviceConfigurations == null) return;
    Iterator  i = serviceConfigurations.iterator() ;
    ClassLoader loader = Thread.currentThread().getContextClassLoader() ;
    while(i.hasNext()) {
      ServiceConfiguration sconf = (ServiceConfiguration) i.next() ;
      String type = sconf.getServiceType() ;
      String key  = sconf.getServiceKey() ;
      try {
        Class classType = loader.loadClass(type) ;
        if(key == null) {
          //container.registerManageableComponentImplementation(classType) ;
          container.registerComponentImplementation(classType) ;
        } else {
          try {
            Class keyType = loader.loadClass(key) ;
            //container.registerManageableComponentImplementation(keyType, classType) ;
            container.registerComponentImplementation(keyType, classType) ;
          } catch (Exception ex) {
            //container.registerManageableComponentImplementation(key, classType) ;
            container.registerComponentImplementation(key, classType) ;
          }
        }
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace() ;
      }
    }
  }

  static public void populateGroovy(ExoContainer container , ConfigurationManager conf) throws Exception {
    Iterator  i = conf.getGroovyServiceConfigurations().iterator() ;
    GroovyManager gmanager = container.getGroovyManager() ;
    while(i.hasNext()) {
      ServiceConfiguration sconf = (ServiceConfiguration) i.next() ;
      String type = sconf.getServiceType() ;
      gmanager.getObject(type) ;
    }
  }
}
