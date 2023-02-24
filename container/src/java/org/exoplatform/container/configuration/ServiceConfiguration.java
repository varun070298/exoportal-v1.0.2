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
 * @version: $Id: ServiceConfiguration.java,v 1.3 2004/10/28 15:35:23 tuan08 Exp $
 */
public class ServiceConfiguration extends HashMap {
  private String key ;
  private String type ;
  
  public ServiceConfiguration() {
  }
  
  public String getServiceKey() { return key ; }
  public void   setServiceKey(String s) { key = s  ;  }
  
  public String getServiceType() { return type ; }
  public void   setServiceType(String s) { type = s  ;  }
  
  public ValuesParam  getValuesParam(String name) {
  	return (ValuesParam) get(name) ;
  }
  
  public ValueParam  getValueParam(String name) {
    return (ValueParam) get(name) ;
  }
  
  public PropertiesParam  getPropertiesParam(String name) {
    return (PropertiesParam) get(name) ;
  }
  
  public ObjectParam  getObjectParam(String name) {
    return (ObjectParam) get(name) ;
  }
  
  public Parameter getParameter(String name) {
    return (Parameter) get(name) ;
  }
  
  public void addParameter(Parameter param) {
    put(param.getName(), param) ;
  }
  
  public Parameter  removeParameter(String name) {
    return (Parameter) remove(name) ;
  }
}