/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import javax.faces.component.UIComponent;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIBody.java,v 1.8 2004/09/27 18:25:51 tuan08 Exp $
 */
public class UIBody extends UIBasicComponent {
	public static final String COMPONENT_ID = "UIBody";
  public static final String DEFAULT_RENDERER = "ChildrenRenderer";
  
  private Body componentModel_ ;
  private UIComponent uiBodyComponent_ ;
  private UIComponent uiLastBodyComponent_ ;
  
  public UIBody(Body config, String defaultStyle) {
    componentModel_ = config ;
    initBasicComponent(config, defaultStyle) ;
    setId(COMPONENT_ID) ;
  }
  
  protected String getIdPrefix() { return "rc" ; }
  //public String getFamily() { return "org.exoplatform.portal.faces.component.UIBody" ; }
  protected String getSkinName() { return "default" ; } 
  protected String getDefaultRendererType() { return DEFAULT_RENDERER ; }
  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return null ;
  }

  public Component getComponentModel() { return componentModel_ ; }
  public Body getEditableBodyModel() {
  	if(!modified_) {
  		componentModel_ = (Body)componentModel_.softCloneObject() ;
  	}
  	return componentModel_ ;
  }
  
  public void setComponentMode(int mode) {
    mode_ = mode ;
  }
  
  public void setBodyMode(int mode) {
  	List children = getChildren() ;
  	for (int i = 0; i < children.size(); i++ ) {
  		Object o = children.get(i) ;
  		if(o instanceof UIBasicComponent) {
  			UIBasicComponent uiChild = (UIBasicComponent) o ;
  			uiChild.setComponentMode(mode) ;
  		}
  	}
  }
  
  public UIComponent getBodyComponent() { return uiBodyComponent_ ; }
  public void setBodyComponent(UIComponent component) {
    uiBodyComponent_ = component ;
    List children = getChildren() ;
    if(children.size() > 0)  {
      uiLastBodyComponent_ = (UIComponent) children.remove(0);
    }
    children.add(component) ;
    boolean renderSibling = true ;
    if (component instanceof UIPage) {
      UIPage uiPage = (UIPage) component ;
      if(Page.MAXIMIZE_STATE.equals(uiPage.getPageModel().getState())) {
        renderSibling = false ;
      }
    } else {
      renderSibling = false ;
    }
    
    List siblings = getParent().getChildren() ;
    for (int i = 0; i < siblings.size() ; i++) {
      UIComponent uiSibling = (UIComponent) siblings.get(i) ;
      uiSibling.setRendered(renderSibling) ;
    }
    this.setRendered(true) ;
  }

  public UIComponent getLastBodyComponent() { return uiLastBodyComponent_ ; }
  public void        setLastBodyComponent() { 
    if(uiLastBodyComponent_ != null) {
      setBodyComponent(uiLastBodyComponent_) ;
    }
  }
  
  
  public void initDefaultBodyComponent() {
    UIPortal uiPortal = (UIPortal) getAncestorOfType(UIPortal.class) ;
    if(Body.PAGE_NODE_TYPE.equals(componentModel_.getComponentType())) {
      Node selectedNode = uiPortal.getSelectedNode() ;
      Node selectNode = selectedNode.findNode(componentModel_.getComponentId()) ;
      if(selectNode == null) selectNode = selectedNode ;
      uiPortal.setSelectedNode(selectNode) ;
    }
  }
  
  public void buildTreeModel(Container parent) {
  	parent.getChildren().add(componentModel_) ;
  }
}