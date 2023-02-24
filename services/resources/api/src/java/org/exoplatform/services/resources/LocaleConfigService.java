 /**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.resources;

import java.util.Collection;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public interface LocaleConfigService {
  
  public LocaleConfig getDefaultLocaleConfig() ;
  
  public LocaleConfig getLocaleConfig(String lang);
  
  public Collection getLocalConfigs();
  
}
