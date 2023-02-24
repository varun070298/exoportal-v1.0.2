 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.resources.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class LocaleConfigServiceImpl implements LocaleConfigService {

  private LocaleConfig defaultConfig_ ;
  private Map configs_ ;
  
  public LocaleConfigServiceImpl(ConfigurationManager confService) throws Exception {
    configs_ = new HashMap(10) ;
    ServiceConfiguration sconf = confService.getServiceConfiguration(getClass()) ;
    String confResource = sconf.getValueParam("locale.config.file").getValue() ;
    InputStream is = confService.getInputStream(confResource) ;
    parseConfiguration(is) ;
  }
  
  public LocaleConfig getDefaultLocaleConfig()  { return defaultConfig_ ; }
  
  public LocaleConfig getLocaleConfig(String lang) {
    LocaleConfig config = (LocaleConfig) configs_.get(lang) ;
    if(config != null) return config ;
    return defaultConfig_ ;
  }
  
  public Collection getLocalConfigs() { return configs_.values() ; }
  
  private void parseConfiguration(InputStream is) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringComments(true);
    factory.setCoalescing(true);
    factory.setNamespaceAware(false);
    factory.setValidating(false);
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.parse(is);
    NodeList nodes = document.getElementsByTagName("locale-config") ;
    for(int i = 0;  i < nodes.getLength() ; i++) {
      Node node = nodes.item(i) ;
      NodeList children = node.getChildNodes() ;
      LocaleConfig config = new LocaleConfigImpl() ;
      for(int j = 0; j < children.getLength(); j++ ) {
        Node element = children.item(j) ;
        if("locale".equals(element.getNodeName()))  {
          config.setLocale(element.getFirstChild().getNodeValue()) ;
        } else  if("output-encoding".equals(element.getNodeName()))  {
          config.setOutputEncoding(element.getFirstChild().getNodeValue()) ;
        } else  if("input-encoding".equals(element.getNodeName()))  {
          config.setInputEncoding(element.getFirstChild().getNodeValue()) ;
        } else  if("description".equals(element.getNodeName()))  {
          config.setDescription(element.getFirstChild().getNodeValue()) ;
        }
      }
      configs_.put(config.getLanguage(), config) ;
      if(i == 0) defaultConfig_ = config ;
    }
  }
}
