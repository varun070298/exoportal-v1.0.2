/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.io.InputStream ;
import java.net.URL;
import java.util.Collection;
/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ConfigurationService.java,v 1.3 2004/10/29 01:55:23 tuan08 Exp $
 */
public interface ConfigurationManager {
  public ValuesParam getGlobalInitParam(String name) throws Exception ;
  public ServiceConfiguration getServiceConfiguration(String service) throws Exception ;
  public ServiceConfiguration getServiceConfiguration(Class clazz) throws Exception ;
  public Collection getServiceConfigurations()  ;
  public Collection getGroovyServiceConfigurations()  ;
  public void addConfiguration(String url) throws Exception ;
  public void addConfiguration(Collection urls) throws Exception ;
  public void addConfiguration(URL url) throws Exception ;
  public URL getResource(String url, String defaultURL) throws Exception ;
  public URL getResource(String url) throws Exception ;
  public InputStream getInputStream(String url, String defaultURL) throws Exception  ;
  public InputStream getInputStream(String url) throws Exception  ;
  
  public boolean isDefault(String value) ;
}