/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import org.exoplatform.Constants;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.UIComponentFactory;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.renderer.html.Decorator;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.xhtml.Element;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIExoComponentBase.java,v 1.28 2004/10/27 02:52:15 tuan08 Exp $
 */
public class UIExoComponentBase extends UIComponentBase implements UIExoComponent {
  private String baseURL_ ;
  private String clazz_;
  private Decorator decorator_ ;
  private transient Renderer cacheRenderer_ ;

  public String getClazz() { return clazz_ ; }
  public UIExoComponent setClazz(String clazz) { 
    clazz_ = clazz  ; 
    return this ;
  }

  public String getFamily() { return UIExoComponent.COMPONENT_FAMILY ; }
  
  public boolean canDecodeInvalidState()  { return true ; }
  
  public UIExoComponent setDecorator(Decorator decorator) { 
    decorator_ = decorator ; 
    return this ;
  }
  
  public void processDecodes(FacesContext context) {
    List children = getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if (child.isRendered()) {
        child.processDecodes(context) ;
        if (context.getRenderResponse()) 	return ;
      }
    }
    decode(context) ;
  }
  
  public void setRendererType(String type) {
    cacheRenderer_ = null ;
    super.setRendererType(type) ;
  }
  
  protected Renderer getRenderer(FacesContext context) {
    if(cacheRenderer_ == null) {
      cacheRenderer_ = Util.getRenderer(context, getRendererType(), getFamily());
    }
    return cacheRenderer_  ;
  }
  
  public UIExoComponent findComponentById(String id) {
    return ComponentUtil.findComponentById(this, id) ;
  }
  
  public UIExoComponent findRenderedComponentById(String id) {
    return  ComponentUtil.findRenderedComponentById(this, id) ;
  }
  
  
  public UIComponent getAncestorOfType(Class classType) {
    return ComponentUtil.getAncestorOfType(this, classType) ;
  }  
  
  public Object getChildComponentOfType(Class classType) {
    return ComponentUtil.getChildComponentOfType(this, classType) ;
  }
  
  public void setRenderedComponent(String id) {
    ComponentUtil.setRenderedComponent(this, id) ;
  }
  
  public void setRenderedComponent(Class type) {
    ComponentUtil.setRenderedComponent(this, type) ;
  }
  
  public void setRenderedSibling(Class type) {
    ComponentUtil.setRenderedSibling(this, type) ;
  }
  
  public UIComponent getSibling(Class type) {
    return ComponentUtil.getSibling(this, type) ;
  }
  
  public String getBaseURL(FacesContext context) {
    if (baseURL_ == null) {
      ExternalContext econtext = context.getExternalContext() ;
      if(econtext instanceof PortletExternalContext) {
        baseURL_ = econtext.encodeActionURL("") ;
        baseURL_ += Constants.AMPERSAND + UICOMPONENT + "=" + getId() ;
      } else {
        RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
        baseURL_ = rinfo.getOwnerURI();
        baseURL_ += "?" + UICOMPONENT + "=" + getId() ;
      }
    }
    return baseURL_ ;
  }
  
  public String getBaseURL() { return getBaseURL(FacesContext.getCurrentInstance()); }

  public void decorate(FacesContext context) throws IOException {
    if(decorator_ != null) {
      decorator_.decorate(context, this) ;
    } else {
      encodeBegin(context) ;
      encodeChildren(context) ;
      encodeEnd(context) ;
    }
  }
  
  public UIExoComponent addChild(Class cl) throws Exception {
    UIExoComponent uiComponent =  UIComponentFactory.createComponent(cl) ;
    getChildren().add(uiComponent) ;
    uiComponent.registerActionListener(this) ;
    uiComponent.registerComponentObserver(this) ;
    return uiComponent ;
  }
  
  public void registerActionListener(UIExoComponent parent)  {
    
  }
  
  public void registerComponentObserver(UIExoComponent parent)  {
    
  }
  
  public DataHandler getDataHandler(Class classType) { 
    throw new RuntimeException("You need to override this method and return at least one default data handler") ; 
  }
  
  public Element getTemplate()  {
    throw new RuntimeException("You need to override this method") ;  
  }
}
