/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.container.monitor.SessionMonitor;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.model.Information;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.listener.page.PageActionListener;
import org.exoplatform.portal.faces.listener.portal.NodeActionListener;
import org.exoplatform.portal.faces.listener.portal.PortalActionListener;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortal.java,v 1.36 2004/10/27 02:53:05 tuan08 Exp $
 */
public class UIPortal extends UIBasicComponent 
  implements InformationProvider, ExoPortal {
  public static final String DEFAULT_RENDERER_TYPE = "PortalRenderer";
  
  private String userCss_ ;
  private Node root_ ;
  private Node selectNode_ ;
  private boolean modified_ ;
  private PortalConfig componentModel_ ;
  private boolean dirty_ = false ;
  private PortalACL portalACL_ ;
  private Information information_ = new Information() ;
  private String portalOwner_ ;
  private boolean queueEvent_ ; 
  private int portalMode_ ;
  
	public UIPortal(PortalConfigService configService, 
                  SessionMonitor monitor, RequestInfo rinfo) throws Exception {
    portalACL_ = configService.getPortalACL() ;
    portalOwner_ = rinfo.getPortalOwner() ;
    componentModel_ =  configService.getPortalConfig(portalOwner_) ;
    initBasicComponent(componentModel_, "default") ;
    HttpClientInfo clientInfo =(HttpClientInfo) monitor.getClientInfo();
    List children = getChildren() ;
    root_ = configService.getNodeNavigation(getOwner())  ;
    root_.setVisible(portalACL_, rinfo.getPortalOwner(), clientInfo.getRemoteUser()) ;
    selectNode_ = root_.findNode(rinfo.getPageName()) ;
    if(selectNode_ == null) selectNode_ = root_ ;
    UIContainer uiPortalLayout = null ;
    if(ExoPortal.XHTMLMP_MIME_TYPE.equals(clientInfo.getPreferredMimeType())) {
      uiPortalLayout = 
        new UIContainer(componentModel_.getMobilePortalLayout(), "default", ExoWindowID.MOBILE_PORTAL_CONFIG);
    } else {
      uiPortalLayout = 
        new UIContainer(componentModel_.getLayout(), "default", ExoWindowID.DEFAULT_PORTAL_CONFIG);
    }
		children.add(uiPortalLayout) ;
    new ChangeLocaleVisitor().changeLocale(this) ;
   
    if(RequestInfo.PRIVATE_ACCESS.equals(rinfo.getAccessibility()) ||
       RequestInfo.ADMIN_ACCESS.equals(rinfo.getAccessibility())){
      setComponentAdminRole(true) ;
    } else {
      setComponentAdminRole(false) ;   
    }
    registerListeners() ;
    UIBody uiBody = findUIBody(uiPortalLayout) ;
    uiBody.initDefaultBodyComponent() ;
    userCss_ = CssStyleBuilderVisitor.getCSS(getRootContainer()) ;
    setId("portal") ;
	}
  
  private void registerListeners() {
    addActionListener(NodeActionListener.class, PortalConstants.EDIT_NODE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.ADD_NODE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.DELETE_NODE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.SAVE_NODE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.BROWSE_PAGE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.MOVE_UP_NODE_ACTION) ;
    addActionListener(NodeActionListener.class, PortalConstants.MOVE_DOWN_NODE_ACTION) ;
    
    addActionListener(PageActionListener.class, PortalConstants.SAVE_PAGE_ACTION) ;
    
    addActionListener(PortalActionListener.class, PortalConstants.EDIT_PORTAL_ACTION) ;
    addActionListener(PortalActionListener.class, PortalConstants.SAVE_PORTAL_ACTION) ;
    addActionListener(PortalActionListener.class, PortalConstants.CHANGE_LANGUAGE_ACTION) ;
  }
    
  public String getFamily() { return "org.exoplatform.portal.faces.component.UIPortal" ; }
  protected String getIdPrefix() { return "portal" ; }
  
  public Component getComponentModel() { return componentModel_ ; }
  public PortalConfig getPortalConfigModel() { return componentModel_ ; }
  public PortalConfig getEditablePortalConfigModel() {
  	if(!modified_) {
  		componentModel_ = (PortalConfig)componentModel_.softCloneObject() ;
  	}
  	return componentModel_ ;
  }
  
  public String getOwner() { return portalOwner_ ; }

  public String getUserCss() { return userCss_ ; } 
  
  protected String getDefaultRendererType() { return DEFAULT_RENDERER_TYPE ; }

  /**Override default set editable in UIBasicComponent*/
  //public void setComponentAdminRole(boolean b) { componentAdminRole_ = b ; }
  
  public boolean isDirty() { return dirty_ ; }
  /**Override default setDirty in UIBasicComponent*/
  public void setComponentDirty(boolean b) { dirty_ = b ; }
  
  public void setPortalViewlMode() {
    setSelectedNode(selectNode_) ;
    setComponentMode(UIBasicComponent.COMPONENT_VIEW_MODE);
    findUIBody().setBodyMode(UIBasicComponent.COMPONENT_VIEW_MODE);
  }
  
  public boolean hasEditPortalCapability() { 
    SessionMonitor monitor = 
      (SessionMonitor)SessionContainer.getComponent(SessionMonitor.class) ;
    return hasComponentAdminRole() && 
           portalACL_.hasEditPortalPermission(componentModel_, monitor.getRemoteUser()) ;
  }
  
  public void setPortalEditMode() {
  	if(hasEditPortalCapability()) {
  		setComponentMode(UIBasicComponent.COMPONENT_EDIT_MODE);
  		findUIBody().setBodyMode(UIBasicComponent.COMPONENT_VIEW_MODE);
  		mode_ = UIBasicComponent.COMPONENT_VIEW_MODE ;
  	}
  }
  
  public void setPortalEditPageMode() {
    setComponentMode(UIBasicComponent.COMPONENT_VIEW_MODE);
    setSelectedNode(getSelectedNode()) ;
    findUIBody().setBodyMode(UIBasicComponent.COMPONENT_EDIT_MODE) ;
  }
  
  public boolean hasEditNavigationCapability() { 
    SessionMonitor monitor = 
      (SessionMonitor)SessionContainer.getComponent(SessionMonitor.class) ;
    return hasComponentAdminRole() && 
           portalACL_.hasEditNodePermission(root_, getOwner(), monitor.getRemoteUser());
  }
  
  public void setPortalEditNavigationMode() {
  	if(hasEditNavigationCapability()) {
  		setComponentMode(UIBasicComponent.COMPONENT_VIEW_MODE);
  		findUIBody().setBodyMode(UIBasicComponent.COMPONENT_VIEW_MODE);
  		mode_ = UIBasicComponent.COMPONENT_EDIT_MODE ;
  	}
  }
  
  public int   getPortalMode() { return portalMode_ ; }
  public void  setPortalMode(int mode) { 
    portalMode_ = mode ;
    if(mode == PORTAL_EDIT_MODE) setPortalEditMode() ;
    else if(mode == PORTAL_EDIT_PAGE_MODE) setPortalEditPageMode() ;
    else if(mode == PORTAL_EDIT_NAVIGATION_MODE) setPortalEditNavigationMode() ; 
    else setPortalViewlMode() ;
  }
  
  public Node  getRootNode() { return root_ ; }
  public Node  getSelectedNode() { return selectNode_ ; }
  public void  setSelectedNode(Node node) {
    if(selectNode_!= null) {
      selectNode_.setSelectedPath(false) ;
      selectNode_ = node ;
      selectNode_.setSelectedPath(true) ;
      PortalComponentCache  cache = 
        (PortalComponentCache) SessionContainer.getComponent(PortalComponentCache.class) ; 
      UIPage uiPage = cache.getUIPage(selectNode_, this) ;
      new ChangeLocaleVisitor().changeLocale(uiPage) ;
      setBodyComponent(uiPage) ;
    }
  }
  
  public void  removeSelectedNode() {
    Node parent = selectNode_.getParent() ;
    if (parent != null && !selectNode_.isShare()) {
      parent.removeChild(selectNode_.getUri()) ;
      setSelectedNode(parent) ;
    }
  }
  
  public Object getPortalComponent(Class clazz) {
    PortalComponentCache  cache = 
      (PortalComponentCache) SessionContainer.getComponent(PortalComponentCache.class) ;
    return cache.getPortalComponent(clazz) ;
  }
  
  public UIBody findUIBody(UIContainer container) {
    List children = container.getChildren();
    int childrenSize = children.size() ;
    UIBody uiBody = null ;
    for(int i = 0; i < childrenSize; i++) {
      Object child = children.get(i) ;
      if(child instanceof UIBody) {
        uiBody = (UIBody) child ;
      } else if (child instanceof UIContainer) {
        uiBody = findUIBody((UIContainer) child) ;
      }
      if(uiBody != null) return uiBody ;
    }
    return null ;
  }
  
  final public UIBody findUIBody() { return findUIBody(getRootContainer()) ; }
  
  final public UIContainer getRootContainer() { return (UIContainer) getChildren().get(0) ; }
  
  public UIPage getCurrentUIPage() { 
    UIBody uiBody = findUIBody() ;
    if(uiBody.getBodyComponent() instanceof UIPage) {
      return (UIPage)uiBody.getBodyComponent() ;
    }
    PortalComponentCache  cache = 
      (PortalComponentCache) SessionContainer.getComponent(PortalComponentCache.class) ;
    return cache.getUIPage(selectNode_, this) ; 
  }
  
  public void setBodyComponent(UIComponent component) { 
    UIBody uiBody = findUIBody() ;
    uiBody.setBodyComponent(component) ;
    if(portalMode_ == ExoPortal.PORTAL_EDIT_PAGE_MODE) {
      uiBody.setBodyMode(UIBasicComponent.COMPONENT_EDIT_MODE) ;
    } else {
      uiBody.setBodyMode(UIBasicComponent.COMPONENT_VIEW_MODE) ;
    }
    userCss_ = CssStyleBuilderVisitor.getCSS(getRootContainer()) ;
  }
  
  public void setLastBodyComponent() { 
    UIContainer uiLayout = (UIContainer)getChildren().get(0) ;
    UIBody uiBody = findUIBody(uiLayout) ;
    uiBody.setLastBodyComponent() ;
    userCss_ = CssStyleBuilderVisitor.getCSS(getRootContainer()) ;
  }
  
  final public void processDecodes(FacesContext context) {
    RequestInfo pparams = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
		String targetComponent = pparams.getTargetComponentId();
		if (targetComponent != null) {
			UIComponent uiTargetComponent = (UIComponent)findComponentById(targetComponent);
      if(uiTargetComponent == null) {
        //log_.info("Cannot find the the target component id: " + targetComponent) ; 
      } else {
        if(uiTargetComponent == this) decode(context)  ;
        else uiTargetComponent.processDecodes(context);
      }
		} else {
			UIBody uiBody = findUIBody((UIContainer) getChildren().get(0));
			uiBody.processDecodes(context);
		}
  }
  
  final public  void processValidators(FacesContext context) {
    RequestInfo pparams = (RequestInfo) SessionContainer.getComponent(RequestInfo.class);
    if(pparams.getPortalAction() != null)  {
      super.processValidators(context) ;
    }
  }

  protected String getComponentIdPrefix() { return "" ; }
  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return null ;
  }
  
  public void changeLocale(ResourceBundle res) {
    root_.visit(new ResolveLabelVisitor(res)) ;
  }
  
  public void buildTreeModel(Container parent) { }
  
  public int   getDisplayMessageType() { return BODY_MESSAGE_TYPE  ;}
  public void  setDisplayMessageType(int type) {  }
  public void  addMessage(FacesMessage  message) { information_.addMessage(message);  }
  public void  clearMessages() {  information_.clearMessages() ;}
  public List getMessages() { return information_.getMessages() ; }
  public boolean  hasMessage() {  return information_.hasMessage() ;}
  
  public boolean hasQueuEvent() { return queueEvent_  ; }
  public void    setQueueEvent(boolean b) { queueEvent_ = b ;}
  public void queueEvent(javax.faces.event.FacesEvent event) {
    queueEvent_ = true ;
    super.queueEvent(event) ;
  }
  
  class ResolveLabelVisitor implements NodeVisitor {
    ResourceBundle res_ ;
    
    public ResolveLabelVisitor(ResourceBundle res) {    res_ = res ;   }
    
    public void visit(Node node) {    node.setResolvedLabel(res_) ;   }   
  }
}