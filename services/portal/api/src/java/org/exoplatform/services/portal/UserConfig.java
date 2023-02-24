/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal;

import java.util.* ;
import org.exoplatform.services.portal.model.*;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class UserConfig {
  public PortalConfig  portalConfig_ ;
  public List  groupConfigs_ ;
  
  public UserConfig (PortalConfig pconfig, List gconfigs) {
    portalConfig_ = pconfig ;
    groupConfigs_ = gconfigs ;
  }
  
  public PortalConfig getUserPortalConfig()   {
    return portalConfig_ ;
  
  }
  
  public List  getGroupConfigs() {
    return groupConfigs_ ;
  }
}
