 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portal.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.services.portletcontainer.bundle.ResourceBundleDelegate;
import org.exoplatform.services.resources.ResourceBundleService;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class ResourceBundleDelegateImpl implements ResourceBundleDelegate {
  
  private static final String CUSTOM_PROPERTIES_PATH = "locale.custom.custom";
  private static final String PORTAL_PROPERTIES_PATH = "locale.portal.portal";
  
  private ResourceBundleService resourceBundleService;
  
  public ResourceBundleDelegateImpl(ResourceBundleService resourceBundleService) {    
    this.resourceBundleService = resourceBundleService;
  }
  
  public ResourceBundle lookupBundle(String portletBundleName, Locale locale){            
    String[] bundles = { PORTAL_PROPERTIES_PATH, portletBundleName, CUSTOM_PROPERTIES_PATH};        
    return resourceBundleService.getResourceBundle(bundles, locale);          
  }

}
