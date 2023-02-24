/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.user.component;

import java.util.Collection;
import javax.faces.component.UIComponent;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.services.resources.LocaleConfigService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIUserInfo.java,v 1.10 2004/07/02 20:48:33 tuan08 Exp $
 */
public class UILanguageSelector extends UIExoCommand {
  public static final String  COMPONENT_FAMILY =  "org.exoplatform.faces.user.component.UILanguageSelector" ;
  public static final String CHANGE_ACTION = "change";
  public static final String LOCALE_NAME = "localeName";
  public static final Parameter CHANGE_PARAM = new Parameter(ACTION, CHANGE_ACTION) ; 

  private Collection configs_ ;
  
  public UILanguageSelector(LocaleConfigService manager) throws Exception {
    setId("UILanguageSelector") ;
    setRendererType("LanguageSelectorRenderer") ;
    configs_ = manager.getLocalConfigs() ;
    addActionListener(ChangeLocaleActionListener.class, CHANGE_ACTION) ;
  }
  
  public Collection getLocaleConfigs() { return configs_ ; }
  
  public String getFamily() {    return COMPONENT_FAMILY  ;  }
  
  static public class ChangeLocaleActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      String localeName = event.getParameter(LOCALE_NAME) ;
      UIComponent portal = (UIComponent) SessionContainer.getComponent(ExoPortal.class) ;
      ExoActionEvent portalEvent = 
        new ExoActionEvent(portal, PortalConstants.CHANGE_LANGUAGE_ACTION) ;
      portalEvent.addParameter(PortalConstants.LANGUAGE_PARAM, localeName) ;
      portal.queueEvent(portalEvent) ;
    }
  }
}