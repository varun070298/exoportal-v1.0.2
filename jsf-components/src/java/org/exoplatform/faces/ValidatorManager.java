/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces;
import java.util.HashMap;
import javax.faces.validator.Validator;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ValueParam;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 13, 2004
 * @version $Id: ValidatorManager.java,v 1.2 2004/09/09 03:22:19 tuan08 Exp $
 */
public class ValidatorManager extends HashMap {
  private boolean cacheValidator_ ;
  
  public ValidatorManager(ConfigurationManager manager) throws Exception {
    ServiceConfiguration sconf = manager.getServiceConfiguration(getClass()) ;
    ValueParam param = sconf.getValueParam("cache.validator") ;
    if(param != null) {
      cacheValidator_ = "true".equals(param.getValue()) ;
    } else {
      cacheValidator_ = false ;
    }
  }
  
  public Validator getValidator(Class clazz)  {
    if(cacheValidator_) return createValidator(clazz) ;
    String key = clazz.getName() ;
    Validator result = (Validator) get(key) ;
    if(result != null) return result ;
    synchronized(this) {
      result = createValidator(clazz) ;
      put(key, result) ;
    }
    return result ;
  }
  
  private Validator createValidator(Class clazz) {
    Validator result = null ;
    try {
      result = (Validator) clazz.newInstance()  ;
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
    return result ;
  }
}