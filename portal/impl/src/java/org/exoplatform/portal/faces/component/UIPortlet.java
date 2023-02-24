/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.*;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.commons.utils.Formater;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.faces.listener.portlet.PortletActionListener;
import org.exoplatform.portal.faces.listener.share.DeleteActionListener;
import org.exoplatform.portal.faces.listener.share.EditPropertiesActionListener;
import org.exoplatform.portal.faces.listener.share.MoveActionListener;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.services.portal.model.Component;
import org.exoplatform.services.portal.model.Container;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
/**
 * Fri, May 30, 2003 @
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortlet.java,v 1.14 2004/11/01 21:06:49 tuan08 Exp $
 */
public class UIPortlet extends UIBasicComponent {
  public static final String DEFAULT_PORTLET_RENDERER = "PortletRenderer";
  //states
  public static final String MINIMIZED_VIEW = "min_view";
  public static final String NORMAL_VIEW = "normal_view";
  public static final String MAXIMIZED_VIEW = "max_view";
  //modes
  public static final String PORTLET_VIEW_MODE = "view";
  public static final String PORTLET_EDIT_MODE = "edit";
  public static final String PORTLET_CONFIG_MODE = "config";
  public static final String PORTLET_HELP_MODE = "help";
  public static final String PORTLET_WSRP_MODE = "wsrp";
  public static final String PORTLET_MONITOR_MODE = "monitor";
  //actions 
  public static final String ACTION = "action";
  
  private static HashMap supportModes_ ;
  private static HashMap supportStates_ ;

  static {
    supportModes_ = new HashMap(6) ;
    supportModes_.put(PORTLET_VIEW_MODE, PortletMode.VIEW) ;
    supportModes_.put(PORTLET_EDIT_MODE, PortletMode.EDIT) ;
    supportModes_.put(PORTLET_HELP_MODE, PortletMode.HELP) ;
    supportModes_.put(PORTLET_CONFIG_MODE, new PortletMode("config")) ;
    supportModes_.put(PORTLET_WSRP_MODE, new PortletMode("wsrp")) ;
    supportModes_.put(PORTLET_MONITOR_MODE, new PortletMode("monitor")) ;
    
    supportStates_ = new HashMap(5) ;
    supportStates_.put(NORMAL_VIEW , WindowState.NORMAL) ;
    supportStates_.put(MINIMIZED_VIEW , WindowState.MINIMIZED) ;
    supportStates_.put(MAXIMIZED_VIEW , WindowState.MAXIMIZED) ;
  }
  
  private org.exoplatform.services.portal.model.Portlet componentModel_ ;
  private WindowState windowState_ = WindowState.NORMAL; 
  private PortletMode portletMode_ = PortletMode.VIEW ;
  private List  htmlSupportModes_ ;
  private String displayTitle_ ;
  private ExoWindowID windowID_ ;
  private Map renderParameters_ ;
  private boolean updateCache_ ;
  private boolean new_ ;
  private String error_ = null;

  public UIPortlet(String owner, String portletApp, String portletName, String id) {
  	new_ = true ;
    initPortlet(owner, portletApp, portletName, id) ;
    setComponentAdminRole(true) ;
    registerListeners() ;
  }
  
  public UIPortlet(String portletApp, String portletName, String id) {
    this(Constants.ANON_USER, portletApp, portletName, id) ;
  }

  public UIPortlet(org.exoplatform.services.portal.model.Portlet config, 
                   String defaultStyle, String pageRefId) {
		componentModel_ = config ; 
		initBasicComponent(config, defaultStyle) ;
    setDisplayTitle(config.getTitle()) ;
    windowID_ = new ExoWindowID(config.getWindowId()) ;
    windowID_.setConfigurationSource(pageRefId) ;
    portletMode_ = PortletMode.VIEW ;
    windowState_ = WindowState.NORMAL;
    PortalContainer manager = PortalContainer.getInstance() ;
		PortletContainerService portletContainer = 
      (PortletContainerService) manager.getComponentInstanceOfType(PortletContainerService.class);
    String portletId = windowID_.getPortletApplicationName() + "/" + windowID_.getPortletName();
		PortletData portlet = 
      (PortletData) portletContainer.getAllPortletMetaData().get(portletId);
    if(portlet == null) {
      Object[] args = {portletId} ;
      PortalResources appres = 
        (PortalResources)SessionContainer.getComponent(PortalResources.class);
      ResourceBundle res = appres.getApplicationResource();
      error_ = Formater.getDefaultFormater().
                        format(res.getString("UIPortlet.msg.invalid-portlet-id"), args);      
      return ;
    }
    initPortletModes(portlet) ;
    setUpdateCache(true) ;
    setId(windowID_.getUniqueID()); 
    registerListeners() ;
    setRendered(true) ;
  }

  public void initPortlet(String owner, String portletApp, String portletName, String id) {
		componentModel_  = new org.exoplatform.services.portal.model.Portlet() ;
		componentModel_.setTitle(portletName) ;
    displayTitle_ = portletName;
		initBasicComponent(componentModel_, "default") ;
    windowID_ = new ExoWindowID( owner + ":/" + portletApp + "/" + portletName + "/" + id);
    componentModel_.setWindowId(windowID_.generatePersistenceId()) ;
    PortalContainer manager = PortalContainer.getInstance() ;
		PortletContainerService portletContainer = 
      (PortletContainerService) manager.getComponentInstanceOfType(PortletContainerService.class);
    String portletId = portletApp + "/" + portletName;
		PortletData portlet = (PortletData) portletContainer.getAllPortletMetaData().get(portletId);
    initPortletModes(portlet) ;
    setUpdateCache(true) ;
    setId(windowID_.getUniqueID()); 
  }
  
  private void initPortletModes(PortletData portlet) {
    boolean adminRole = hasRole(CheckRoleInterceptor.ADMIN) ;
    List supportsList = portlet.getSupports() ;
    htmlSupportModes_ = new ArrayList() ;
    for (int i = 0; i < supportsList.size(); i++) {
      Supports supports = (Supports) supportsList.get(i) ;
      String mimeType = supports.getMimeType() ;
      if ("text/html".equals(mimeType)) {
        List modes = supports.getPortletMode() ;
        for (int j =0 ; j < modes.size() ; j++) {
          String mode =(String)modes.get(j) ;
          mode = mode.toLowerCase() ;
          if("config".equals(mode)) { 
            if(adminRole) htmlSupportModes_.add(mode) ;
          } else {
            htmlSupportModes_.add(mode) ;
          }
        }
        break ;
      }
    }
  }
  
  private void registerListeners() {
    addActionListener(EditPropertiesActionListener.class, PortalConstants.EDIT_ACTION) ;
    addActionListener(DeleteActionListener.class, PortalConstants.DELETE_ACTION) ;
    addActionListener(MoveActionListener.class, PortalConstants.MOVE_UP_ACTION) ;
    addActionListener(MoveActionListener.class, PortalConstants.MOVE_DOWN_ACTION) ;
    addActionListener(PortletActionListener.class, "portletAction") ;
  }
  
  public Component getComponentModel() { return componentModel_ ; }
  public org.exoplatform.services.portal.model.Portlet getPortletModel() { return componentModel_ ; }
  public org.exoplatform.services.portal.model.Portlet getEditablePortletModel() {
  	if(!modified_) {
  		componentModel_ = 
  			(org.exoplatform.services.portal.model.Portlet)componentModel_.softCloneObject() ;
  	}
  	return componentModel_ ;
  }
  
  public boolean isNew() { return new_ ; }
  public void    setComponentModified(boolean b) {
  	if(b == false) new_ = false ;
  	super.setComponentModified(b) ;
  }
  
  public void clearComponentModified() {
  	modified_ = false ;
  	new_ = false ;
  }
  
  public boolean hasError() { return error_ != null ; }
  public String getErrorMessage() { return error_ ; }
  
  protected String getIdPrefix() { return "p" ; }
  public String getFamily() { return "org.exoplatform.portal.faces.component.UIPortlet" ; }
  protected String getSkinName() { 
    return windowID_.getPortletApplicationName() + "/" + windowID_.getPortletName(); 
  } 
  
  protected String getDefaultRendererType() { return DEFAULT_PORTLET_RENDERER ; }
  
  protected Style getDecoratorStyle(SkinConfigService service, String renderer, String style) {
    return service.getPortletDecoratorStyle(renderer, style);
  }

  public String getDisplayTitle() { return displayTitle_ ; }
  public void   setDisplayTitle(String s) { 
    if(s == null || s.length() == 0) s = getId() ;
    displayTitle_  = s; 
  }
  
  public WindowState getWindowState() { return windowState_ ; }
  public void setWindowState(WindowState state) { windowState_ = state ;}
  public void setWindowState(String state) { 
    windowState_ = (WindowState) supportStates_.get(state) ;
    if(windowState_  == null ) windowState_ = WindowState.NORMAL ; 
  }

  public PortletMode getPortletMode() { return portletMode_ ; }
  public void setPortletMode(PortletMode mode) { portletMode_ = mode ;}
  public void setPortletMode(String mode) { 
    mode = mode.toLowerCase() ;
    portletMode_ = (PortletMode) supportModes_.get(mode) ; 
    if (portletMode_ == null)  portletMode_ = PortletMode.VIEW ;
  }

  public ExoWindowID getWindowId() { return windowID_; }
  
  public List getHtmlSupportModes() { return htmlSupportModes_ ; }

  public Map getRenderParameters() { return renderParameters_; }
  public void setRenderParameters(Map map) { renderParameters_ = map ; }

  public boolean getUpdateCache() { return updateCache_ ; }
  public void    setUpdateCache(boolean b) {  updateCache_ = b ; }
  
  public void changeLocale(ResourceBundle res) { 
    displayTitle_ = ExpressionUtil.getExpressionValue(res, componentModel_.getTitle()) ;
    setUpdateCache(true) ;   
  }
  
  public void buildTreeModel(Container parent) {
  	parent.getChildren().add(componentModel_) ;
  }
}