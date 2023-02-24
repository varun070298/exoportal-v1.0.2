/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.util.* ;

import org.exoplatform.services.database.*;
import org.exoplatform.services.log.LogService;

import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.container.configuration.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 29, 2004
 * @version $Id: HibernateServiceContainerImpl.java,v 1.1 2004/10/30 02:27:52 tuan08 Exp $
 */
public class HibernateServiceContainerImpl implements HibernateServiceContainer {
  private HibernateService defaultService_ ;
  private Map services_ ;
  
  public HibernateServiceContainerImpl(DatabaseService dbService,  
                                       LogService lservice,
                                       HibernateService defaultService, 
                                       ConfigurationManager confService) throws Exception {
    defaultService_ = defaultService ;
    services_ = new HashMap() ;
    ServiceConfiguration sconf = 
      confService.getServiceConfiguration(HibernateServiceContainer.class) ;
    Iterator i = sconf.values().iterator() ;
    while(i.hasNext()) {
      PropertiesParam param =  (PropertiesParam) i.next() ;
      String name = param.getName() ;
      if(name.equals(defaultService_.getServiceName())) {
        throw new ExoMessageException("HibernateServiceContainer.duplciate-service-name") ; 
      }
      HibernateServiceImpl service = new HibernateServiceImpl(dbService, lservice) ;
      service.configure(param.getName(), param.getProperties()) ;
      services_.put(name, service) ;
    }
  }
  
  public HibernateService getHibernateService()  { return defaultService_ ; }
  
  public HibernateService getHibernateService(String name) {
    return (HibernateService)services_.get(name);
  }
  
  public void closeAllSessions(){
    defaultService_.closeSession();
    Collection values = services_.values();
    for (Iterator iter = values.iterator(); iter.hasNext();) {
      HibernateService service = (HibernateService) iter.next();
      service.closeSession();      
    }    
  }
}
