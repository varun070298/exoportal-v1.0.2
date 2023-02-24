/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.component;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Page;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class UIPagePreview extends UIExoCommand {
  public static Parameter[] saveParams = { new Parameter(ACTION , SAVE_ACTION)} ;
  public static Parameter[] editParams = { new Parameter(ACTION , EDIT_ACTION)} ;
  public static Parameter[] viewParams = { new Parameter(ACTION , "view")} ;
  public static Parameter[] cancelParams = { new Parameter(ACTION , CANCEL_ACTION)} ;
  
  public UIPagePreview() {
    setRendererType("PagePreviewRenderer");
    setId("UIPagePreview");
    
    List children = getChildren() ;    
    UIToolbar uiToolbar = new UIToolbar("UIPagePreviewToolbar");
    uiToolbar.addLeftButton(new Button("#{UIPagePreview.button.view}", viewParams));
    uiToolbar.addLeftButton(new Button("#{UIPagePreview.button.edit}", editParams));
    uiToolbar.addLeftButton(new Button("#{UIPagePreview.button.save}", saveParams));
    uiToolbar.addRightButton(new Button("#{UIPagePreview.button.cancel}", cancelParams));                       
    uiToolbar.setRendered(true);
    children.add(uiToolbar);    
        
    addActionListener(ViewActionListener.class, "view") ;
    addActionListener(EditActionListener.class, EDIT_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
	}

  public boolean setPage(UIPortal uiPortal, String pageId) throws Exception {
    PortalContainer pcontainer  = PortalContainer.getInstance();
    PortalConfigService service = 
      (PortalConfigService) pcontainer.getComponentInstanceOfType(PortalConfigService.class) ;
    Page config = service.getPage(pageId);
    PortalConfigService configService = 
      (PortalConfigService) PortalContainer.getComponent(PortalConfigService.class) ;
    PortalACL portalACL = configService.getPortalACL() ;
    if(!portalACL.hasViewPagePermission(config, uiPortal.getOwner())) {
      uiPortal.addMessage(new ExoFacesMessage("#{UIPagePreview.msg.no-view-permission}"));
      return false ;
    }
    UIPage uiPage = new UIPage(config, "default") ;
    new ChangeLocaleVisitor().changeLocale(uiPage) ;
    PortalACL acl = service.getPortalACL() ;
    if( acl.hasEditPagePermission(config, uiPortal.getOwner())) {
      uiPage.setComponentAdminRole(true) ;
    } 
    uiPage.setRendered(true) ;
    List children = getChildren() ;
    if(children.size() > 1) children.remove(1) ;
    children.add(uiPage) ;
    return true ;
  }
  
  public UIPage getUIPage() { return (UIPage)getChildren().get(1) ; }
  
  public String getFamily() {
    return "org.exoplatform.portal.faces.component.UIPagePreview";
  }
  
  public UIComponent getUIToolbar() {  return  (UIComponent)getChildren().get(0) ; }  
  
  public void processDecodes(FacesContext context) {
    List children = getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if (child.isRendered() && child instanceof UIToolbar) {
        child.processDecodes(context) ;
        if (context.getRenderResponse()) {
          return ;
        }
      }
    }    
    decode(context) ;
  }
  
  static public class ViewActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPagePreview uiComponent = (UIPagePreview) event.getSource() ;
      UIPage uiPage = (UIPage)uiComponent.getChildren().get(1) ;
      uiPage.setComponentMode(UIBasicComponent.COMPONENT_VIEW_MODE) ;
    }
  }
  
  static public class EditActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPagePreview uiComponent = (UIPagePreview) event.getSource() ;
      UIPage uiPage = (UIPage)uiComponent.getChildren().get(1) ;
      uiPage.setComponentMode(UIBasicComponent.COMPONENT_EDIT_MODE) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPagePreview uiComponent = (UIPagePreview) event.getSource() ;
      UIPortal uiPortal = (UIPortal) uiComponent.getAncestorOfType(UIPortal.class) ;
      UIPageBrowser uiPageBrowser = 
        (UIPageBrowser)uiPortal.getPortalComponent(UIPageBrowser.class);
      uiPortal.setBodyComponent(uiPageBrowser);
    }
  }
  
  static public class SaveActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPagePreview uiComponent = (UIPagePreview) event.getSource() ;
      UIPage uiPage = (UIPage)uiComponent.getChildren().get(1) ;
      uiPage.buildTreeModel(null) ;
      PortalConfigService service = 
        (PortalConfigService)PortalContainer.getInstance().
                             getComponentInstanceOfType(PortalConfigService.class) ;
      service.savePage(uiPage.getPageModel()) ;
      uiPage.clearComponentModified()  ;
      uiPage.setComponentDirty(false) ;
    }
  }

}