/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import org.exoplatform.services.idgenerator.IDGeneratorService;
import org.exoplatform.container.configuration.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 2, 2004
 * @version $Id: PortalComponentIDGenerator.java,v 1.1 2004/11/03 01:19:44 tuan08 Exp $
 */
public class PortalComponentIDGenerator {
  final static public String UUID_ALGORITHM = "uuid" ;
  final static public String EXO_ALGORITHM = "exo" ;
  
  private IDGeneratorService idservice_ ;
  private String algorithm_ ;
  
  public PortalComponentIDGenerator(IDGeneratorService idservice, 
                                    ConfigurationManager cservice) throws Exception {
    idservice_ = idservice ;
    ServiceConfiguration sconf = cservice.getServiceConfiguration(getClass()) ;
    ValueParam param = sconf.getValueParam("algorithm") ;
    algorithm_ = param.getValue() ;
  }
  
  public String generatePortletId(UIPortal uiPortal, String portletName) {
    if(UUID_ALGORITHM.equals(algorithm_)) {
      return "P" + idservice_.generateStringID(portletName) ;
    }
    String id = portletName ;
    if(uiPortal.findComponentById(id) != null ||
        uiPortal.getCurrentUIPage().findComponentById(id) != null) {
      id = "P" + idservice_.generateStringID(portletName) ;
    }
    return id ;
  }
  
  public String generateContainerId(UIPortal uiPortal, String containerName) {
    if(UUID_ALGORITHM.equals(algorithm_)) {
      return "P" + idservice_.generateStringID(containerName) ;
    }
    return containerName ;
  }
  
  public String generatePageId(UIPortal uiPortal, String pageName) {
    if(UUID_ALGORITHM.equals(algorithm_)) {
      return "P" + idservice_.generateStringID(pageName) ;
    }
    return pageName ;
  }
}