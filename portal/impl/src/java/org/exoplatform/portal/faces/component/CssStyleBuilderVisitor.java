/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.HashMap ;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.core.component.ComponentVisitor;
import org.exoplatform.faces.core.component.UIExoComponent ;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 17, 2004
 * @version $Id$
 */
public class CssStyleBuilderVisitor extends ComponentVisitor {
  private SkinConfigService service_ ;
  private Map cssURLMap_ ;
  
  public CssStyleBuilderVisitor(SkinConfigService service) {
    service_ = service ;
    cssURLMap_ = new HashMap(15) ;
  }
  
  public String  getCSS() {
    StringBuffer sbuf = new StringBuffer() ;
    Iterator styleIterator = cssURLMap_.values().iterator();
    while (styleIterator.hasNext()) {
      String cssLink = (String) styleIterator.next() ;
      sbuf.append(cssLink);
    }
    return sbuf.toString() ;
  }
  
  public  void traverse(UIExoComponent component)  {
    visit(component) ;
    if(component instanceof UIContainer || 
       component instanceof UISinglePage || 
       component instanceof UIPortal) {
      List children = component.getChildren() ;
      for(int i = 0; i < children.size() ; i++) {
        UIExoComponent child = (UIExoComponent) children.get(i);
        traverse(child) ;
      }
    }
  }
  
  protected void visit(UIExoComponent comp) {
    if(comp instanceof UIPortlet) {
      UIPortlet uiPortlet = (UIPortlet)comp ;
      addBasicComponentStyle(uiPortlet) ;
      addPortletComponentStyle(uiPortlet) ;
    } else if(comp instanceof UIBody) {
      Object bodyComponent = ((UIBody)comp).getBodyComponent() ;
      if(bodyComponent instanceof UIPage) {
        traverse((UIPage)bodyComponent) ;
      } else if(bodyComponent instanceof UIPagePreview) {
        UIPagePreview uiPreview = (UIPagePreview) bodyComponent ;
        traverse(uiPreview.getUIPage()) ;
      }
    } else if(comp instanceof UIBasicComponent) {
      addBasicComponentStyle((UIBasicComponent)comp) ;
    }
  }
  
  private void addPortletComponentStyle(UIPortlet comp) {
    String portletStyle = comp.getPortletModel().getPortletStyle() ;
    if (portletStyle != null  && portletStyle.length() > 0 && !"default".equals(portletStyle)) {
      ExoWindowID windowId = comp.getWindowId() ;
      String portletId = windowId.getPortletApplicationName() + "/" +windowId.getPortletName() ;
      Style styleConfig = service_.getPortletStyle(portletId, portletStyle) ;
      if(styleConfig == null) {
        portletId = "default" ;
        styleConfig = service_.getPortletStyle(portletId, portletStyle) ;
      }
      if (styleConfig == null) {
        String cssLink = "expect an style url here for the  , portlet : " +
                          portletId + " style " + portletStyle  + '\n';
        String key =  portletId + "/" + portletStyle ;
        cssURLMap_.put(key , cssLink) ;
      } else {
        String url = styleConfig.getUrl() ;
        String cssLink = "<link rel='stylesheet' type='text/css' href='" + url + "'/>\n" ;
        cssURLMap_.put(url, cssLink) ;
      }
    }
  }
  
  private void addBasicComponentStyle(UIBasicComponent comp) {
    String decorator = comp.getDecorator() ;
    if (decorator != null  && decorator.length() > 0 && !"default".equals(decorator)) {
      String rendererRef = comp.getRendererType() ;
      Style styleConfig = comp.getDecoratorStyle(service_, rendererRef, decorator) ;
     
      if (styleConfig == null) {
        String cssLink = "expect an style url here for the  , renderer: " +
                         rendererRef + " decorator " + decorator  + '\n';
        String key = comp.getIdPrefix() + "/" + rendererRef + "/" + decorator ;
        cssURLMap_.put(key , cssLink) ;
      } else {
        String url = styleConfig.getUrl() ;
        String cssLink = "<link rel='stylesheet' type='text/css' href='" + url + "'/>\n" ;
        cssURLMap_.put(url, cssLink) ;
      }
    }
  }
  
  static public String getCSS(UIExoComponent uiPortal) {
    PortalContainer manager  = PortalContainer.getInstance();
    SkinConfigService service = 
      (SkinConfigService) manager.getComponentInstanceOfType(SkinConfigService.class) ;
    CssStyleBuilderVisitor visitor = new CssStyleBuilderVisitor(service) ;
    visitor.traverse(uiPortal) ;
    return visitor.getCSS() ;
  }
}
