/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.model.Component;
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Date: Aug 11, 2003
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIBasicComponent.java,v 1.10 2004/09/26 02:25:46 tuan08 Exp $
 */
abstract public class UIBasicComponent extends UIExoCommand {
  final static public short  FLOAT_RIGHT = 0 ;
  final static public short  FLOAT_BOTTOM = 1 ;
  
  protected static Log log_ = getLog("org.exoplatform.portal.faces") ;
  final static public  int COMPONENT_VIEW_MODE = 1 ;
  final static public int COMPONENT_EDIT_MODE = 2 ;

  protected String decorator_ ;
  protected String customizedId_  ;
  private short float_ ; 
  protected boolean componentAdminRole_ = false ;
  protected boolean modified_ ;
  protected int mode_   = COMPONENT_VIEW_MODE  ;
  
  public String getCustomizedId() { return customizedId_ ;  }
  public void   setCustomizedId(String id) { customizedId_ = id ; }

  public short getFloat() { return float_ ; }
  public void  setFloat(short val) { float_ = val ;}
  
  abstract public Component getComponentModel() ;
  
  final protected void initBasicComponent(Component componentModel, String defaultStyle) {
    setDecorator(componentModel.getDecorator(), defaultStyle) ;
    setRendererType(componentModel.getRenderer()) ;
    setId(componentModel.getId()) ;
  }
  
  public void updateChange() {
  	Component model = getComponentModel() ;
  	setDecorator(model.getDecorator(), "default") ;
    setRendererType(model.getRenderer()) ;
  }
  
  public String getDecorator() { return decorator_; }
  public void   setDecorator(String decorator, String defaultDecorator) { 
    if(decorator == null || decorator.length() == 0) {
      decorator_ = defaultDecorator ;
    } else {
      decorator_ = decorator ; 
    }
  }
  
  public int getComponentMode() { return mode_ ; }
  public void setComponentMode(int mode) { 
    mode_ = mode ; 
    List children = getChildren() ;
    for(int i = 0; i < children.size(); i++) {
      UIBasicComponent uiChild = (UIBasicComponent)  children.get(i) ; 
      uiChild.setComponentMode(mode) ;
    }
  }
  
  public boolean hasComponentAdminRole() { return componentAdminRole_ ; }
  public void    setComponentAdminRole(boolean b) { 
    componentAdminRole_ = b ; 
    List children = getChildren() ;
    for (int i = 0 ; i <  children.size(); i++) {
      UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
      uiChild.setComponentAdminRole(b) ;
    }
  }
  
  public void   setRendererType(String type) { 
    String rendererType = type ;
    if(type == null || type.length() == 0 || "default".equals(type)) {
      rendererType = getDefaultRendererType() ;
    }
    super.setRendererType(rendererType) ;
  }

  public boolean getRendersChildren(){ return true; }

  public boolean isComponentModified() { return modified_ ; }
  public void setComponentModified(boolean b) {  
  	modified_ = b ;
    setComponentDirty(b) ;
  }
  
  public void clearComponentModified() {
  	modified_ = false ;
  	List children = getChildren() ;
  	for(int i = 0; i < children.size(); i++) {
      Object uichild = children.get(i) ;
      if(uichild instanceof UIBasicComponent) {
  		  ((UIBasicComponent)uichild).clearComponentModified() ;
      }
  	}
  }
  
  public void setComponentDirty(boolean b) {  
    UIBasicComponent uiParent = (UIBasicComponent) getParent() ;
    uiParent.setComponentDirty(b) ;
  }

  final public void decode(FacesContext context) {
    getRenderer(context).decode(context, this) ;
  }
  
  public void setId(String id) {
    if (id == null || id.length() == 0) {
    	id = new String(getIdPrefix()  + Integer.toString(hashCode())); 
    } else {
      setCustomizedId(id);
    }
    super.setId(id) ;
  }
  
  public void removeChild(UIBasicComponent child) {
    List children = getChildren() ;
    children.remove(child) ;
    setComponentModified(true) ;
  }
  
  public String getBaseURL() {
    if (baseURL_ == null) {
      RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
      baseURL_ = rinfo.getOwnerURI();
      baseURL_ += "?" + Constants.COMPONENT_PARAMETER + "=" + getId() ;
    }
    return baseURL_ ;
  }
  
  public void changeLocale(ResourceBundle res) {  }
  
  abstract protected String getDefaultRendererType() ;
  abstract protected String getIdPrefix() ;
  abstract protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) ;
  abstract public    void buildTreeModel(Container parent) ;
  
  final public void encodeBegin(FacesContext context) throws IOException { }
  final public void encodeEnd(FacesContext context) throws IOException { }
  
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    ActionListener[] listener = this.getActionListeners() ;
    for(int i = 0; i < listener.length; i++) {
      if (event.isAppropriateListener(listener[i])) {
        event.processListener(listener[i]);
      }
    }
  }
}
