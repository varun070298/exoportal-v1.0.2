/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portal.model.Component;
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Date: Aug 11, 2003
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UISinglePage.java,v 1.7 2004/10/22 23:49:42 tuan08 Exp $
 */
public class UISinglePage extends UIBasicComponent {
  
  public static final String DEFAULT_RENDERER_TYPE = "SinglePageRenderer";

  private String userCss_ ;

  public UISinglePage() {
    try {
      FacesContext context = FacesContext.getCurrentInstance() ;
      HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
      String[] portletNames = request.getParameterValues("portletName") ;
      String style = request.getParameter("style") ;
      List children = getChildren() ;
      if (portletNames != null) {
        for (int i = 0; i < portletNames.length; i++) {
          String[] keys = StringUtils.split(portletNames[i], "/") ;
          String portletApp =  keys[0] ;
          String portletName = keys[1] ;
          String id = "window-" + portletName ;
          if (keys.length == 3) id = keys[2] ;
          UIPortlet uiPortlet = new UIPortlet(portletApp, portletName, id) ;
          if(style != null && style.length() > 0) {
          	uiPortlet.getPortletModel().setPortletStyle(style) ;
          }
          children.add(uiPortlet) ;
        }
      } else {
        log_.warn("NO PORTLET NAME IS FOUND");
      }
      setRendererType(getRendererType());
      setId("single-page");
    } catch (Throwable t) {
      t.printStackTrace() ;
    }
    
    new ChangeLocaleVisitor().changeLocale(this, "en") ;
    userCss_ = CssStyleBuilderVisitor.getCSS(this) ;
  }
  
  public Component getComponentModel() { return null ;}
  
  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return null ;
  }
  protected String getComponentIdPrefix() { return "" ; }
  public String getFamily() { return "org.exoplatform.portal.faces.component.UISinglePage" ; }
  public String getIdPrefix() { return "sp" ; }
  protected String getSkinName() { return "default" ; } 
  public String getUserCss() { return userCss_ ; } 
  protected String getDefaultRendererType() { return DEFAULT_RENDERER_TYPE ; }

  public String getRendererType() {
  		/*
  		 * TODO : Test of user-agent and MIME type or markup parameters found
  		 * in ExoPortalInfo instance (no yet implemented) to choose correct
  		 * renderer type
  		 */
  		return DEFAULT_RENDERER_TYPE;
  }

  public void processDecodes(FacesContext context) {
    List children = getChildren() ;
    for (int i = 0; i < children.size() ; i++) {
      UIComponent child = (UIComponent) children.get(i) ;
      if (context.getRenderResponse()) return ;
      child.processDecodes(context) ;
    }
  }

  public boolean getRendersChildren(){
    return true;
  }
  
  public void buildTreeModel(Container parent) {
  	
  }
}