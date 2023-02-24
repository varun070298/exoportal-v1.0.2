/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.config.impl;

import java.util.* ;
import org.exoplatform.container.configuration.* ;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.config.ConfigurationService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 5, 2004
 * @version $Id$
 */
public class ConfigurationServiceImpl implements ConfigurationService {
  static private String[] MAPPING =
  {
    "org/exoplatform/services/config/impl/ConfigurationDataImpl.hbm.xml"
  } ;
  
  private ConfigurationManager manager_ ;
  private HibernateService hservice_ ;
  private XStream xstream_ ; 
  
  public ConfigurationServiceImpl(ConfigurationManager manager,
                                  HibernateService hservice) {
    manager_ = manager ;
    hservice_ = hservice ;
    hservice_.addMappingFiles(MAPPING) ;
    xstream_ = new XStream(new XppDriver());
  }
  
  public Object getServiceConfiguration(Class serviceType) throws Exception {
    ConfigurationDataImpl impl = 
      (ConfigurationDataImpl) hservice_.findOne(ConfigurationDataImpl.class, serviceType.getName()) ;
    Object obj = null ;
    if(impl == null) {
      obj = loadDefaultConfig(serviceType) ;
      saveServiceConfiguration(serviceType, obj) ;
    } else {
      obj = xstream_.fromXML(impl.getData()) ;
    }
    return obj;
  }

  public void saveServiceConfiguration(Class serviceType, Object config) throws Exception {
    ConfigurationDataImpl impl = 
      (ConfigurationDataImpl) hservice_.findOne(ConfigurationDataImpl.class, serviceType.getName()) ;
    String xml = xstream_.toXML(config) ;
    if(impl == null) {
      impl = new ConfigurationDataImpl();
      impl.setServiceType(serviceType.getName()) ;
      impl.setData(xml) ;
      hservice_.create(impl) ;
    } else {
      impl.setData(xml) ;
      hservice_.update(impl) ;
    }
  }

  public void removeServiceConfiguration(Class serviceType) throws Exception {
    hservice_.remove(serviceType, serviceType.getName()) ;
  }
  
  private Object loadDefaultConfig(Class serviceType) throws Exception {
    ServiceConfiguration sconf = manager_.getServiceConfiguration(serviceType) ;
    Iterator i = sconf.values().iterator() ;
    ObjectParam param = (ObjectParam) i.next() ;
    return param.getObject() ;
  }
}
