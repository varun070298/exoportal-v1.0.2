/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.faces.UIComponentFactory;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.services.cache.SimpleExoCache;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.model.PageReference;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 14, 2005
 * @version $Id$
 */
public class PortalComponentCache {
  private UIComponentFactory componentFactory_  ;
  private SimpleExoCache cache_ ; 
  
  public PortalComponentCache() {
    componentFactory_ = new UIComponentFactory(null) ;
    //limit to have 20 cached components
    cache_ = new SimpleExoCache(20) ;
  }
  
  public Object getPortalComponent(Class clazz) {
    try {
      Object object  =  cache_.get(clazz.getName()) ;
      if(object == null) {
        object = componentFactory_.createUIComponent(clazz) ;
        cache_.put(clazz.getName(), object)  ;
      }
      return object ;
    } catch (Exception ex) {   
      LogUtil.getLog(getClass()).error( "create component :" + clazz.getName(), ex) ;
    }
    return null ;
  }
  
  public UIPage getUIPage(Node node, UIPortal uiPortal)  {
    SessionContainer scontainer = SessionContainer.getInstance() ;
    try {
      HttpClientInfo info = (HttpClientInfo) scontainer.getMonitor().getClientInfo() ;
      PageReference pref = node.getPageReference(info.getPreferredMimeType()) ;
      if(pref == null ) {
        pref = node.getPageReference(ExoPortal.XHTML_MIME_TYPE) ;
      }
      String referenceId = pref.getPageReference();
      UIPage uiPage = (UIPage)  cache_.get(referenceId) ;
      if (uiPage == null && referenceId != null) {
        
        PortalConfigService configService = 
          (PortalConfigService) PortalContainer.getComponent(PortalConfigService.class) ;
        PortalACL portalACL = configService.getPortalACL() ;
        Page config = configService.getPage(referenceId);
        if(config == null) {
          uiPage = new UIPage() ;
          uiPage.setError("Cannot find the page " +  referenceId) ;
        } else if (!portalACL.hasViewPagePermission(config, uiPortal.getOwner())) {
          uiPage = new UIPage() ;
          uiPage.setError("User do not have the right to access the page " +  referenceId) ;
        } else {
          uiPage = new UIPage(config, "default");
          uiPage.setRendered(true);
          if(uiPortal.hasComponentAdminRole() && 
              portalACL.hasEditPagePermission(config, uiPortal.getOwner())) {
            uiPage.setComponentAdminRole(true) ;
          }
          cache_.put(referenceId, uiPage);
        }
        
      }
      return uiPage ;
    } catch (Throwable t) {
      t = ExceptionUtil.getRootCause(t) ;
      LogUtil.getLog(getClass()).error("Error: ", t) ;
    }
    return null ;
  }
  
  public void removeUIPageFromCache(String referenceId) throws Exception {
    cache_.remove(referenceId);
  }
}
