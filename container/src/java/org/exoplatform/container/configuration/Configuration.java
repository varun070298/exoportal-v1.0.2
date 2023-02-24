/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.util.* ;

/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Configuration.java,v 1.3 2004/10/29 01:55:23 tuan08 Exp $
 */
public class Configuration {
	private Map globalParam_  = new HashMap();
  private Map serviceConfiguration_ = new HashMap() ;
  private Map groovyServiceConfiguration_ = new HashMap() ;
  private List importConfiguration_ = new ArrayList() ; 
  private List removeConfiguration_ = new ArrayList() ; 
  
  public ValuesParam  getInitParam(String name) {
    return (ValuesParam) globalParam_.get(name) ;
  }
  
  public void addInitParam(ValuesParam param) {
    globalParam_.put(param.getName(), param) ;
  }
  
  public ValuesParam  removeInitParam(String name) {
    return (ValuesParam) globalParam_.remove(name) ;
  }
  
  public ServiceConfiguration getServiceConfiguration(String service) {
    if(service.endsWith(".groovy")) {
      return (ServiceConfiguration) groovyServiceConfiguration_.get(service) ;
    }
    return (ServiceConfiguration) serviceConfiguration_.get(service) ;
  }
  
  public Collection getServiceConfigurations() {
    return  serviceConfiguration_.values();
  }
  
  public Collection getGroovyServiceConfigurations() {
    return  groovyServiceConfiguration_.values();
  }
  
  public void addServiceConfiguration(ServiceConfiguration sconf) {
    String type = sconf.getServiceType() ;
    if(type.endsWith(".groovy")) {
      groovyServiceConfiguration_.put(sconf.getServiceType(), sconf) ;
    } else {
      String key = sconf.getServiceKey() ;
      if(key == null) key = sconf.getServiceType() ;
      serviceConfiguration_.put(key, sconf) ;
    }
  }
  
  public ServiceConfiguration removeServiceConfiguration(String service) {
    return (ServiceConfiguration) serviceConfiguration_.remove(service) ;
  }
  
  public void addImportConfiguration(String url) {
    importConfiguration_.add(url) ;
  }
  
  public List getImportConfiguration() { return importConfiguration_ ; }
  
  public void addRemoveConfiguration(String type) {
    removeConfiguration_.add(type) ;
  }
  
  public List getRemoveConfiguration() { return removeConfiguration_ ; }
  
  Map getGlobalParam() { return globalParam_ ; }
  
  Map getServiceConfigurationMap() { return serviceConfiguration_ ; }
  
  Map getGroovyServiceConfigurationMap() { return groovyServiceConfiguration_ ; }
  
  void mergeConfiguration(Configuration other) {
    globalParam_.putAll(other.getGlobalParam()) ;
    serviceConfiguration_.putAll(other.getServiceConfigurationMap()) ;
    groovyServiceConfiguration_.putAll(other.getGroovyServiceConfigurationMap()) ;
    removeConfiguration_.addAll(other.getRemoveConfiguration()) ;
  }
}
