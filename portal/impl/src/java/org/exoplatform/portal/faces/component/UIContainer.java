/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.listener.container.AddContainerActionListener;
import org.exoplatform.portal.faces.listener.container.AddPortletActionListener;
import org.exoplatform.portal.faces.listener.container.ChangeTabActionListener;
import org.exoplatform.portal.faces.listener.container.PlaceBodyActionListener;
import org.exoplatform.portal.faces.listener.share.DeleteActionListener;
import org.exoplatform.portal.faces.listener.share.EditPropertiesActionListener;
import org.exoplatform.portal.faces.listener.share.MoveActionListener;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.model.Body;
import org.exoplatform.services.portal.model.Component;
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.model.Portlet;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;

/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIContainer.java,v 1.10 2004/11/03 01:19:45 tuan08 Exp $
 */
public class UIContainer extends UIBasicComponent {
  final static public String DEFAULT_CONTAINER_RENDERER = "ContainerRowRenderer" ;  
  
  private Container componentModel_ ;
  private int selectedComponent_ = 0;   
  private String displayTitle_ ;
	
  public UIContainer() {
    registerListeners() ;
  }
  
  public UIContainer(Container config) {
    componentModel_ = config ;
    initBasicComponent(componentModel_, "default") ; 
    setDisplayTitle(config.getTitle()) ;
    setComponentMode(UIBasicComponent.COMPONENT_EDIT_MODE) ;
    registerListeners() ;
  }
  
  public UIContainer(Container config, String defaultStyle, String configurationSource) {
    componentModel_ = config ;
    initBasicComponent(config, defaultStyle) ;
    setDisplayTitle(config.getTitle()) ;
    List childrenConfig = config.getChildren() ;
    initChildren(childrenConfig, configurationSource) ;
    registerListeners() ;
   }
  
  private void registerListeners() {
    addActionListener(EditPropertiesActionListener.class, PortalConstants.EDIT_ACTION) ;
    addActionListener(DeleteActionListener.class, PortalConstants.DELETE_ACTION) ;
    addActionListener(MoveActionListener.class, PortalConstants.MOVE_UP_ACTION) ;
    addActionListener(MoveActionListener.class, PortalConstants.MOVE_DOWN_ACTION) ;
    
    addActionListener(AddContainerActionListener.class, PortalConstants.ADD_CONTAINER_ACTION) ;
    addActionListener(AddPortletActionListener.class, PortalConstants.ADD_PORTLET_ACTION) ;
    addActionListener(PlaceBodyActionListener.class, PortalConstants.PLACE_BODY_ACTION) ;
    addActionListener(ChangeTabActionListener.class, PortalConstants.CHANGE_CONTAINER_TAB_ACTION) ;
  }
  
  final protected void initChildren(List childrenConfig, String configurationSource) {
    List children = getChildren() ;
    for (int i = 0; i < childrenConfig.size() ; i++) {
      Object childConfig =  childrenConfig.get(i) ;
      if (childConfig instanceof Portlet) {
        Portlet portletConfig = (Portlet) childConfig ;
        UIPortlet uiPortlet = 
          new UIPortlet(portletConfig, getDecorator(), configurationSource) ;
        children.add(uiPortlet) ;
      } else  if (childConfig instanceof Container) {
        Container containerConfig = (Container) childConfig ; 
        UIContainer uiContainer = 
        	new UIContainer(containerConfig, getDecorator(), configurationSource) ;
        children.add(uiContainer) ;
      } else  if (childConfig instanceof Body) {
        Body bodyConfig = (Body) childConfig ; 
        UIBody uiBody = new UIBody(bodyConfig, getDecorator()) ;
        children.add(uiBody) ;
      }
    }
  }
  
  public Component getComponentModel() { return componentModel_ ; }
  public Container getContainerModel() { return componentModel_ ; }
  public Container getEditableContainerModel() {
  	if(!modified_) {
  		componentModel_ = (Container)componentModel_.softCloneObject() ;
  	}
  	return componentModel_ ;
  }
  
  public String getDisplayTitle() { return displayTitle_ ;  }
  public void   setDisplayTitle(String s) { 
    if(s == null || s.length() == 0) s = getId() ;
    displayTitle_  = s; 
  }
  
  protected String getIdPrefix() { return "c" ; }
  public String getFamily() { return "org.exoplatform.portal.faces.component.UIContainer" ; }
  protected String getSkinName() { return "default" ; } 
  protected String getDefaultRendererType() { return  DEFAULT_CONTAINER_RENDERER ; }
  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return service.getContainerDecoratorStyle(renderer, style) ;
  }

  public void processDecodes(FacesContext context) {
    decode(context) ;
  }
  
  public void buildTreeModel(Container parent) {
  	componentModel_.getChildren().clear() ;
  	List uichildren = getChildren() ;
  	for(int i = 0; i < uichildren.size(); i++) {
  		UIBasicComponent uiChild =(UIBasicComponent) uichildren.get(i) ;
  		uiChild.buildTreeModel(componentModel_) ;
  	}
  	if(parent != null) parent.getChildren().add(componentModel_) ;
  }
  
  public int getSelectedComponent() {return selectedComponent_;}
  public void setSelectedComponent(int selectedComponent) {
    selectedComponent_ = selectedComponent;
  }
  
  public void changeLocale(ResourceBundle res) {   
    String title = componentModel_.getTitle() ;
    if(title == null) {
      displayTitle_ = getId() ;
    } else {
      displayTitle_ = ExpressionUtil.getExpressionValue(res, componentModel_.getTitle()) ;
    }    
  }
  
  
  public String getBaseURL() {
    if (baseURL_ == null) {
      RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
      if(getAncestorOfType(UIPage.class) == null) {
        baseURL_ = rinfo.getOwnerURI();
        baseURL_ += "?" + Constants.COMPONENT_PARAMETER + "=" + getId() ;
      } else {
        baseURL_ = rinfo.getPageURI();
        baseURL_ += "?" + Constants.COMPONENT_PARAMETER + "=" + getId() ;
      }
    }
    return baseURL_ ;
  }
}