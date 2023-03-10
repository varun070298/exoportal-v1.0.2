/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.config.impl;

import org.exoplatform.services.config.ConfigurationData ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 5, 2004
 * @version $Id$
 * @hibernate.class  table="EXO_SERVICE_CONFIG"
 * @hibernate.cache  usage="read-write"
 */
public class ConfigurationDataImpl implements ConfigurationData {
  private String serviceType ;
  private String data ;
  
  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String getServiceType() {   return serviceType  ;}
  public void setServiceType(String s) {serviceType = s; }

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() {  return data ; }
  public String setData(String s) {  return data = s; }
  
}
