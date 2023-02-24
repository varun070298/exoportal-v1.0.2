/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.ActionListenerManager;
import org.exoplatform.faces.UIComponentFactory;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.renderer.html.Decorator;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.log.LogService;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.xhtml.Element;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIExoCommand.java,v 1.36 2004/10/27 02:52:15 tuan08 Exp $
 */
public class UIExoCommand extends UICommand implements UIExoComponent {

  protected String baseURL_ ;
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
    return this; 
  }
  
  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String uicomponent = (String) paramMap.get(UICOMPONENT) ;
    if (uicomponent != null && uicomponent.equals(getId())) {
    	String action = (String) paramMap.get(ACTION) ;
    	if(action != null) {
    		broadcast(new ExoActionEvent(this, action, paramMap)) ;
    	}
    }
  }
  
  public void processDecodes(FacesContext context) {
    List children = getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if (child.isRendered()) {
        child.processDecodes(context) ;
        if (context.getRenderResponse()) {
        	return ;
        }
      }
    }
    decode(context) ;
  }
  
  public void setId(String id) {
    super.setId(id) ;
    baseURL_ = null ;
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
    return ComponentUtil.findRenderedComponentById(this, id) ; 
  }
  
  public Object getChildComponentOfType(Class classType) {
    return ComponentUtil.getChildComponentOfType(this, classType) ;
  }  
  
  public UIComponent getAncestorOfType(Class classType) {
  	return ComponentUtil.getAncestorOfType(this, classType) ;
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

  public List findDescendantsOfType(Class type) {
    return ComponentUtil.findDescendantsOfType(this, type) ;
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
  
  public String getBaseURL() {
    if(baseURL_ != null) return baseURL_ ;
    return getBaseURL(FacesContext.getCurrentInstance());
  }

  static public Log getLog(String name)  {
    PortalContainer manager  = PortalContainer.getInstance();
    LogService service = (LogService) manager.getComponentInstanceOfType(LogService.class) ;
    Log log = service.getLog(name);
    return log ;
  }
  
  
  protected boolean hasOwnerRole(String user) {
    if(user == null) return false ;
  	FacesContext context  = FacesContext.getCurrentInstance() ;
		ExternalContext econtext = context.getExternalContext() ;
		if(user.equals(econtext.getRemoteUser())) {
			return true ;
		}
  	return false ;
  }
  
  protected boolean hasRole(String role) {
  	FacesContext context  = FacesContext.getCurrentInstance() ;
		ExternalContext econtext = context.getExternalContext()  ;
		return econtext.isUserInRole(role) ;
  }
  
  public void addActionListener(Class clazz, String actionToListen)  {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    ActionListenerManager manager = 
      (ActionListenerManager) pcontainer.getComponentInstanceOfType(ActionListenerManager.class) ;
    ExoActionListener result = manager.getActionListener(clazz, actionToListen) ;
    addActionListener(result) ;
  }
  
  public UIExoComponent addChild(Class cl) throws Exception {
    UIExoComponent uiComponent =  UIComponentFactory.createComponent(cl) ;
    getChildren().add(uiComponent) ;
    uiComponent.registerActionListener(this) ;
    uiComponent.registerComponentObserver(this) ;
    return uiComponent ;
  }
  
  public void decorate(FacesContext context) throws IOException {
    if(decorator_ != null) {
      decorator_.decorate(context, this) ;
    } else {
      encodeBegin(context) ;
      encodeChildren(context) ;
      encodeEnd(context) ;
    }
  }
  
  public void registerActionListener(UIExoComponent parent)  {
    
  }
  
  public void registerComponentObserver(UIExoComponent parent)  {
    
  }
  
  public DataHandler getDataHandler(Class  dataType) { 
    throw new RuntimeException("You need to override this method and return at least one default data handler") ;  
  }
  
  public Element getTemplate()  {
    throw new RuntimeException("You need to override this method") ;  
  }
}