/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.ComponentVisitor;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.session.PortalResources;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 17, 2004
 * @version $Id$
 */
public class ChangeLocaleVisitor extends ComponentVisitor {
  private ResourceBundle res_ ;
  
  public ChangeLocaleVisitor() {    
  }
  
  /* set default locale */
  public void changeLocale(UIPortal uiPortal) {
    String lang = uiPortal.getPortalConfigModel().getLocale() ;
    FacesContext context = FacesContext.getCurrentInstance() ;
    HttpServletRequest request = 
      (HttpServletRequest) context.getExternalContext().getRequest();
    String langParam = request.getParameter(PortalConstants.LANGUAGE_PARAMETER) ;
    if(langParam != null) lang = langParam ;
    if (lang == null || lang.length() == 0) {
      Locale requestLocale = request.getLocale() ;
      lang = requestLocale.getLanguage() ;
    }
    changeLocale(uiPortal, lang) ;
  }
  
  public void changeLocale(UIPortal uiPortal, String locale) {
    PortalResources  presources = 
      (PortalResources) SessionContainer.getComponent(PortalResources.class) ;
    presources.changeLocaleConfig(locale, uiPortal.getOwner()) ;
    res_ = presources.getApplicationOwnerResource() ;
    visit(uiPortal) ;
    traverse(uiPortal.getRootContainer());
  }
  
  public void changeLocale(UIPage uiPage) {    
    PortalResources  presources = 
      (PortalResources) SessionContainer.getComponent(PortalResources.class) ;
    if(uiPage.getPageModel() == null) {      
      res_ = presources.getLocaleConfig().getOwnerResourceBundle(uiPage.getOwner()) ;
      traverse(uiPage) ;
      return;
    }
    visit( uiPage) ;    
    List children = uiPage.getChildren() ;
    for(int i = 0; i < children.size() ; i++) {
      UIExoComponent child = (UIExoComponent) children.get(i);
      visit(child) ;
    }
  }
  
  public void changeLocale(UISinglePage comp, String locale) {
    PortalResources  presources = 
      (PortalResources) SessionContainer.getComponent(PortalResources.class) ;
    presources.changeLocaleConfig(locale, "anonymous") ;
    res_ = presources.getApplicationOwnerResource() ;
    List children = comp.getChildren() ;
    for(int i = 0; i < children.size() ; i++) {
      UIExoComponent child = (UIExoComponent) children.get(i);
      visit(child) ;
    }
  }
  
  public  void traverse(UIExoComponent component)  {
    visit(component) ;
    if(component instanceof UIContainer) {
      List children = component.getChildren() ;
      for(int i = 0; i < children.size() ; i++) {
        UIExoComponent child = (UIExoComponent) children.get(i);
        traverse(child) ;
      }
    }
  }
  
  public void visit(UIExoComponent comp) {     
    if(comp instanceof UIBasicComponent) {   
      UIBasicComponent uiComp = (UIBasicComponent) comp ;
      uiComp.changeLocale(res_) ;
    }     
  }
}