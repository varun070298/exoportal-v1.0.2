/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core;

import java.util.ResourceBundle;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.portal.session.PortalResources;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 4, 2004
 * @version $Id: Util.java,v 1.5 2004/10/23 17:46:26 tuan08 Exp $
 */
public class Util {
  static public Renderer getRenderer(FacesContext context, String type, String family) { 
    if(type != null) {
      HttpClientInfo info = 
        (HttpClientInfo) SessionContainer.getInstance().getMonitor().getClientInfo();
      if(HttpClientInfo.MOBILE_BROWSER_TYPE.equals(info.getPreferredMimeType())) {
        return getXHTMLMPRenderer(context,type, family) ; 
      } 
      return getXHTMLRenderer(context,type, family) ; 
    }
    return null ;
  }
  
  static public Renderer getXHTMLMPRenderer(FacesContext context, String type, String family) { 
    RenderKitFactory rkFactory = 
      (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = rkFactory.getRenderKit(null, UIExoComponent.XHTMLMP_KIT);
    Renderer renderer =  renderKit.getRenderer(family, type);
    if(renderer == null) {
      renderer = getXHTMLRenderer(context,type, family) ; 
    }
    return renderer ;
  }
  
  static public Renderer getXHTMLRenderer(FacesContext context, String type, String family) { 
    RenderKitFactory rkFactory = 
      (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = 
      rkFactory.getRenderKit(context, context.getViewRoot().getRenderKitId());
    return renderKit.getRenderer(family, type);
  }
  
  static public ResourceBundle getApplicationResourceBundle() {
    FacesContext context = FacesContext.getCurrentInstance() ;
    javax.faces.context.ExternalContext  externalContext = context.getExternalContext() ;
    if(externalContext instanceof PortletExternalContext) {
      PortletExternalContext econtext = (PortletExternalContext) externalContext ;
      return econtext.getApplicationResourceBundle() ;
    } 
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    return appres.getApplicationResource();
  }

  static public InformationProvider findInformationProvider(UIComponent src) {
    InformationProvider provider = null ;
    while(provider == null) {
      if(src instanceof InformationProvider) {
        provider = (InformationProvider) src ;
      } else if (src instanceof UIViewRoot){
        return null ;
      } else {
        src = src.getParent() ;
      }
    }
    return provider ;
  }
  
  static public PhaseId getActionPhaseId(String action) {
    if(action.startsWith("PhaseId[1].")) {
      return PhaseId.APPLY_REQUEST_VALUES ;
    }
    return PhaseId.INVOKE_APPLICATION ;
  }
  
  static public String encodeActionPhase(String action , PhaseId phaseId) {
    if(PhaseId.APPLY_REQUEST_VALUES == phaseId) {
      action = "PhaseId[1]." + action ;
    }
    return action ;
  }
  
  static public  String getRemoteUser() {
    FacesContext context  = FacesContext.getCurrentInstance() ;
    ExternalContext econtext = context.getExternalContext()  ;
    return econtext.getRemoteUser() ;
  }
  
  static public boolean hasRole(String role) {
    FacesContext context  = FacesContext.getCurrentInstance() ;
    ExternalContext econtext = context.getExternalContext()  ;
    return econtext.isUserInRole(role) ;
  }
}