/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import java.util.ResourceBundle;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.listener.page.PageActionListener;
import org.exoplatform.services.portal.model.Component;
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;

/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPage.java,v 1.6 2004/09/26 19:14:42 tuan08 Exp $
 */
public class UIPage extends UIContainer {
  final static public String DEFAULT_PAGE_RENDERER = "PageRowRenderer"  ;
  
  private Page componentModel_ ;
  private UIPortlet maximizedPortlet_ ; 
  private boolean dirty_ ;
  private String error_ = null ;

  public UIPage() {
    
  }
  
  public UIPage(Page config, String defaultStyle) {
    componentModel_ = config ;
    initBasicComponent(config, defaultStyle) ;
    List childrenConfig = config.getChildren() ;
    initChildren(childrenConfig, config.getPageId()) ;
    registerListeners();
  }
  
  private void registerListeners() {
    addActionListener(PageActionListener.class, PortalConstants.SAVE_PAGE_ACTION) ;
  }
  
  protected String getIdPrefix() { return "page" ; }
  public String getFamily() { return "org.exoplatform.portal.faces.component.UIPage" ; }
  protected String getSkinName() { return "default" ; } 
  
  /**Override default setDirty in UIBasicComponent*/
  public void setComponentDirty(boolean b) { dirty_ = b ; }
  public boolean isDirty() { return dirty_ ; }
  
  public Component getComponentModel() { return componentModel_ ; }
  public Page      getPageModel() { return componentModel_ ; }
  public Page getEditablePageModel() {
  	if(!modified_) {
  		componentModel_ = (Page)componentModel_.softCloneObject() ;
  	}
  	return componentModel_ ;
  }
  
  public String getViewPermission() { return componentModel_.getViewPermission() ;}
  public String getEditPermission() { return componentModel_.getEditPermission() ;}
  public String getOwner() { return componentModel_.getOwner() ;}
  
  public String getError() { return error_ ;}
  public void   setError(String error) { error_ =  error ; }
  
  public UIPortlet getMaximizedPortlet() { return maximizedPortlet_ ; }
  public void      setMaximizedPortlet(UIPortlet uiPortlet) { maximizedPortlet_ = uiPortlet ; }

  protected String getDefaultRendererType() { return DEFAULT_PAGE_RENDERER ; }

  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return service.getPageDecoratorStyle(renderer, style);
  }
  
  public void setComponentMode(int mode) { 
    if(mode == UIBasicComponent.COMPONENT_EDIT_MODE && !hasComponentAdminRole()) {
      mode = UIBasicComponent.COMPONENT_VIEW_MODE ;   
    }
    mode_ = mode ;
    List children = getChildren() ;
    for(int i = 0; i < children.size(); i++) {
      UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;   
      uiChild.setComponentMode(mode) ;
    }
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
  
  public void changeLocale(ResourceBundle res) {
    String title = componentModel_.getTitle() ;
    if(title == null) {
      setDisplayTitle(getId()) ;
    } else {
      setDisplayTitle(ExpressionUtil.getExpressionValue(res, componentModel_.getTitle())) ;
    }
  }
}