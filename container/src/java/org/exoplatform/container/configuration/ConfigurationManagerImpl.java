/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.servlet.ServletContext;

/**
 * Jul 19, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: ConfigurationServiceImpl.java,v 1.8 2004/10/30 02:29:51 tuan08
 *           Exp $
 */
public class ConfigurationManagerImpl implements ConfigurationManager {
  final static public String WAR_CONF_LOCATION = "/WEB-INF";

  protected Configuration configurations_;

  private ServletContext scontext_;

  public ConfigurationManagerImpl(ServletContext context) {
    scontext_ = context;
  }

  public void addConfiguration(String url) throws Exception {
    addConfiguration(getURL(url));
  }

  public void addConfiguration(Collection urls) throws Exception {
    Iterator i = urls.iterator();
    while (i.hasNext()) {
      URL url = (URL) i.next();
      addConfiguration(url);
    }
  }

  public void addConfiguration(URL url) throws Exception {
    Configuration conf = XMLParser.parse(url.openStream());
    if (configurations_ == null)
      configurations_ = conf;
    else
      configurations_.mergeConfiguration(conf);
    List urls = conf.getImportConfiguration();
    for (int i = 0; i < urls.size(); i++) {
      String uri = (String) urls.get(i);
      try {
        InputStream is = getURL(uri).openStream();
        conf = XMLParser.parse(is);
        configurations_.mergeConfiguration(conf);
      } catch (Exception ex) {
        System.err.println("Error: " + ex.getMessage());
        System.err.println("Error: cannot process the configuration" + url);
      }
    }
  }

  public void processRemoveConfiguration() {
    if(configurations_ == null) return;
    List list = configurations_.getRemoveConfiguration();
    for (int i = 0; i < list.size(); i++) {
      String type = (String) list.get(i);
      configurations_.removeServiceConfiguration(type);
    }
  }

  public ValuesParam getGlobalInitParam(String name) throws Exception {
    return configurations_.getInitParam(name);
  }

  public ServiceConfiguration getServiceConfiguration(String service)
      throws Exception {
    return configurations_.getServiceConfiguration(service);
  }

  public ServiceConfiguration getServiceConfiguration(Class clazz)
      throws Exception {
    return getServiceConfiguration(clazz.getName());
  }

  public Collection getServiceConfigurations() {
    if(configurations_ == null) return null;
    return configurations_.getServiceConfigurations();
  }

  public Collection getGroovyServiceConfigurations() {
    return configurations_.getGroovyServiceConfigurations();
  }

  public URL getResource(String url, String defaultURL) throws Exception {
    return null;
  }

  public URL getResource(String uri) throws Exception {
    return getURL(uri);
  }

  public InputStream getInputStream(String url, String defaultURL)
      throws Exception {
    return null;
  }

  public InputStream getInputStream(String uri) throws Exception {
    URL url = getURL(uri);
    return url.openStream();
  }

  protected URL getURL(String url) throws Exception {
    if (url.startsWith("jar:")) {
      String path = removePrefix("jar:/", url);
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      return cl.getResource(path);
    } else if (url.startsWith("war:")) {
      if (scontext_ == null) {
        throw new Exception("unsupport war uri in this configuration service");
      }
      String path = removePrefix("war:", url);
      return scontext_.getResource(WAR_CONF_LOCATION + path);
    } else if (url.startsWith("file:")) {
      url = resolveSystemProperties(url);
      return new URL(url);
    }
    return null;
  }

  private String resolveSystemProperties(String input) {
    final int NORMAL = 0;
    final int SEEN_DOLLAR = 1;
    final int IN_BRACKET = 2;    
    if (input == null)
      return input;
    char[] chars = input.toCharArray();
    StringBuffer buffer = new StringBuffer();
    boolean properties = false;
    int state = NORMAL;
    int start = 0;
    for (int i = 0; i < chars.length; ++i) {
      char c = chars[i];
      if (c == '$' && state != IN_BRACKET)
        state = SEEN_DOLLAR;
      else if (c == '{' && state == SEEN_DOLLAR) {
        buffer.append(input.substring(start, i - 1));
        state = IN_BRACKET;
        start = i - 1;
      }
      else if (state == SEEN_DOLLAR)
        state = NORMAL;
      else if (c == '}' && state == IN_BRACKET) {        
        if (start + 2 == i) {
          buffer.append("${}");
        }       
        else {
          String value = null;
          String key = input.substring(start + 2, i);
          value = System.getProperty(key);
          if (value != null) {
            properties = true;
            buffer.append(value);
          }
        }
        start = i + 1;
        state = NORMAL;
      }
    }
    if (properties == false)
      return input;
    if (start != chars.length)
      buffer.append(input.substring(start, chars.length));
    return buffer.toString();

  }

  public boolean isDefault(String value)  {
     return value == null || value.length() == 0 || "default".equals(value) ;
  }
  
  protected String removePrefix(String prefix, String url) {
    return url.substring(prefix.length(), url.length());
  }
}