/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.session;

import java.util.Locale;
import java.util.ResourceBundle;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 11, 2005
 * @version $Id$
 */
public class PortalResources  {
  private static String[] PORTAL_RESOURCES = {"locale.portal.portal"} ;
  private ResourceBundle portalResource_ ;
  private ResourceBundle portalOwnerResource_ ;
  private LocaleConfig localeConfig_ ;
  
  public PortalResources() {
    
  }
  
  public void changeLocaleConfig(String locale, String owner) {
    LocaleConfigService manager = 
      (LocaleConfigService)PortalContainer.getInstance().
                           getComponentInstanceOfType(LocaleConfigService.class) ;
    localeConfig_ = manager.getLocaleConfig(locale);
    portalResource_ = localeConfig_.getMergeResourceBundle(PORTAL_RESOURCES);
    portalOwnerResource_ = localeConfig_.getOwnerResourceBundle(owner) ;
  }
  
  public LocaleConfig getLocaleConfig() { return localeConfig_ ; }
  
  public Locale getLocale() { return localeConfig_.getLocale() ; }
  
  public ResourceBundle getPortalResourceBundle() {
    return portalResource_ ;
  }
  
  public ResourceBundle getPortalOwnerResourceBundle() {
    return portalOwnerResource_ ;
  }

  public ResourceBundle getApplicationResource() {
    return portalResource_ ;
  }

  public ResourceBundle getApplicationOwnerResource() {
    return portalOwnerResource_ ;
  }
  
  public ResourceBundle getApplicationResource(String name)  {
    return localeConfig_.getResourceBundle(name) ;
  }
  
  public ResourceBundle getApplicationOwnerResource(String owner)  {
    return localeConfig_.getOwnerResourceBundle(owner) ;
  }
}