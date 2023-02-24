/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.groovy;

import java.net.URL;
import java.util.* ;
import org.picocontainer.Startable ;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ValueParam;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class GroovyManagerContainer implements Startable {
  private Map managers_ ;
  private Thread scanner_ ;
  
  public GroovyManagerContainer(ConfigurationManager cService) throws Exception {
    ServiceConfiguration sconf = cService.getServiceConfiguration(getClass()) ;
    ValueParam  param = sconf.getValueParam("check.modified.period") ;
    long period = Long.parseLong(param.getValue()) ;
    if(period > 0) {
      scanner_ = new ScannerThread(period) ;
      scanner_.start() ;
    }
    managers_ = new HashMap() ;
  }
  
  public GroovyManager getGroovyManager(URL classpath) throws Exception {
    String id = classpath.toString() ;
    GroovyManager manager = (GroovyManager) managers_.get(id) ;
    if(manager == null) {
      synchronized(managers_) {
        manager = new GroovyManager(classpath) ;
        managers_.put(id, manager) ;
      }
    }
    return  manager ; 
  }
  
  public GroovyManager removeGroovyManager(URL classpath) throws Exception {
    synchronized (managers_) {
      GroovyManager manager = (GroovyManager) managers_.remove(classpath.toString()) ;
      return manager ;
    }
  }
  
  public void start() {}
  public void stop() {
    if(scanner_ != null) scanner_.interrupt();  
  }
  
  public class ScannerThread extends Thread {
    private long period_ ;
    
    public ScannerThread(long period) {
      setPriority(MIN_PRIORITY) ;
      period_ = period ;
    }
    
    public void run() {
      long checkPeriod = 20000 ;
      while (true) {
        try {
          sleep(checkPeriod) ;
          if(managers_.size() == 0) return ;
          checkPeriod = period_ ;
          Iterator i = managers_.values().iterator() ;
          while(i.hasNext()) {
            GroovyManager gmanager = (GroovyManager) i.next() ;
            if(gmanager.isDispose()) {
              i.remove() ;
            } else { 
              gmanager.checkModifiedObjects() ;
            }
          }
        } catch (InterruptedException ex) {
          return ;
        } catch (Throwable ex) {
          ex.printStackTrace() ;
        }
      }
    }
  }
}