package org.exoplatform.portlets.nav.component;

import java.util.List;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.PortalConstants;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.services.portal.model.Node;
/**
 * @version $Revision: 1.8 $ $Date: 2004/10/19 13:20:03 $
 * @author Tuan Nguyen
 */
public class UIMenu extends UINavigation {
  final static public String VIEW_MODE_RENDERER = "ExoMenuRenderer" ;
  final static public String EDIT_PORTAL_MODE_RENDERER = "ExoEditPortalModeMenuRenderer" ;
  final static public String EDIT_PAGE_MODE_RENDERER = "ExoEditPageModeMenuRenderer" ;
  final static public String EDIT_NAVIGATION_MODE_RENDERER = "ExoEditNavigationModeMenuRenderer" ;
  
  final static public Parameter[] addNodeParams = {new Parameter(ACTION, PortalConstants.ADD_NODE_ACTION) };
  final static public Parameter[] saveNavigationParams = {new Parameter(ACTION, PortalConstants.SAVE_NODE_ACTION) };
  final static public Parameter[] browsePageParams = {new Parameter(ACTION, PortalConstants.BROWSE_PAGE_ACTION) };
  final static public Parameter[] savePageParams = {new Parameter(ACTION, PortalConstants.SAVE_PAGE_ACTION) };
  final static public Parameter[] editPortalParams = {new Parameter(ACTION, PortalConstants.EDIT_PORTAL_ACTION) };
  final static public Parameter[] savePortalParams = {new Parameter(ACTION, PortalConstants.SAVE_PORTAL_ACTION) };
  private UIToolbar uiToolbarView_;
  private UIToolbar uiToolbarPortal_;
  private UIToolbar uiToolbarNav_;
  private UIToolbar uiToolbarPage_;
  private boolean showToolBar_ ;
  
	public UIMenu(ResourceBundle res) throws Exception {
		super(res) ;
		setId("UIMenu");
		setRendererType(VIEW_MODE_RENDERER);
    
    List children = getChildren() ;
    uiToolbarView_ = new UIToolbar("UIToolbarView");  
    uiToolbarView_.setRendered(true);
    ExoPortal portal = (ExoPortal) SessionContainer.getComponent(ExoPortal.class) ;
    
    if(portal.hasEditPortalCapability()) {
      uiToolbarView_.addRightButton(new Button("#{UIMenu.button.portal-mode}", editPortalModeParams));
      uiToolbarPortal_ = new UIToolbar("UIToolbarPortal");
      uiToolbarPortal_.addLeftButton(new Button("#{UIMenu.button.edit-portal-properties}", editPortalParams));
      uiToolbarPortal_.addLeftButton(new Button("#{UIMenu.button.save-portal}", savePortalParams));
      uiToolbarPortal_.addRightButton(new Button("#{UIMenu.button.view-mode}", viewModeParams));
      uiToolbarPortal_.setRendered(true);
    }
    
    if(portal.hasEditNavigationCapability()) {
      uiToolbarView_.addRightButton(new Button("#{UIMenu.button.navigation-mode}", editNavigationModeParams));
      uiToolbarNav_ = new UIToolbar("UIToolbarNav");
      uiToolbarNav_.addLeftButton(new Button("#{UIMenu.button.browse-pages}", browsePageParams));
      uiToolbarNav_.addLeftButton(new Button("#{UIMenu.button.save-navigation}", saveNavigationParams));
      uiToolbarNav_.addRightButton(new Button("#{UIMenu.button.view-mode}", viewModeParams));
      uiToolbarNav_.setRendered(true);
      
      uiToolbarView_.addRightButton(new Button("#{UIMenu.button.page-mode}", editPageModeParams));
      uiToolbarPage_ = new UIToolbar("UIToolbarPage");
      uiToolbarPage_.addLeftButton(new Button("#{UIMenu.button.browse-pages}", browsePageParams));
      uiToolbarPage_.addLeftButton(new Button("#{UIMenu.button.save-page}", savePageParams));
      uiToolbarPage_.addRightButton(new Button("#{UIMenu.button.view-mode}", viewModeParams));
      uiToolbarPage_.setRendered(true);
    }
    
    showToolBar_ = portal.hasEditPortalCapability() || portal.hasEditNavigationCapability() ;
    if(showToolBar_) {
      children.add(uiToolbarView_);
    }
    addActionListener(ForwardPortalEventActionListener.class, PortalConstants.SAVE_PAGE_ACTION) ;            
    addActionListener(ForwardPortalEventActionListener.class, PortalConstants.EDIT_PORTAL_ACTION) ;
    addActionListener(ForwardPortalEventActionListener.class, PortalConstants.SAVE_PORTAL_ACTION) ;
    
    addActionListener(ForwardNodeEventActionListener.class, PortalConstants.ADD_NODE_ACTION) ;
    addActionListener(ForwardPortalEventActionListener.class, PortalConstants.BROWSE_PAGE_ACTION) ;
    addActionListener(ForwardNodeEventActionListener.class, PortalConstants.MOVE_UP_NODE_ACTION) ;
    addActionListener(ForwardNodeEventActionListener.class, PortalConstants.MOVE_DOWN_NODE_ACTION) ;
    addActionListener(ForwardNodeEventActionListener.class, PortalConstants.DELETE_NODE_ACTION) ;
    addActionListener(ForwardNodeEventActionListener.class, PortalConstants.EDIT_NODE_ACTION) ;
    addActionListener(ForwardPortalEventActionListener.class, PortalConstants.SAVE_NODE_ACTION) ;
	}
	
  public boolean isShowAdminButton() { return showToolBar_; }
  
	public void setViewModeRenderer() {  
    setRendererType(VIEW_MODE_RENDERER);
    List children = getChildren() ;
    children.remove(0);
    children.add(uiToolbarView_);
  }
	
	public void setEditPortalModeRenderer() {  
    setRendererType(EDIT_PORTAL_MODE_RENDERER);
    List children = getChildren() ;
    children.remove(0);
    children.add(uiToolbarPortal_);    
  }
	
	public void setEditPageModeRenderer() {  
    setRendererType(EDIT_PAGE_MODE_RENDERER );
    List children = getChildren() ;
    children.remove(0);
    children.add(uiToolbarPage_);    
  }
	
	public void setEditNavigationModeRenderer() {  
    setRendererType(EDIT_NAVIGATION_MODE_RENDERER );
    List children = getChildren() ;
    children.remove(0);
    children.add(uiToolbarNav_);    
  }
  
  public UIToolbar getUIToolbarNav() {
    return uiToolbarNav_;
  }
  
  public UIToolbar getUIToolbarPage() {
    return uiToolbarPage_;
  }

  public UIToolbar getUIToolbarPortal() {
    return uiToolbarPortal_;
  }
    
  public UIToolbar getUIToolbarView() {
    return uiToolbarView_;
  }
  
  static public class ForwardPortalEventActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      ExoPortal portal = (ExoPortal) SessionContainer.getComponent(ExoPortal.class) ;
      ExoActionEvent portalEvent = new ExoActionEvent((UIComponent)portal, getActionName()) ;
      portal.queueEvent(portalEvent) ;
    }
  }
  
  static public class ForwardNodeEventActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      ExoPortal portal = (ExoPortal) SessionContainer.getComponent(ExoPortal.class) ;
      ExoActionEvent portalEvent = new ExoActionEvent((UIComponent)portal, getActionName()) ;
      String nodeURI  =event.getParameter(PortalConstants.NODE_URI) ;
      Node node = portal.getRootNode().findNode(nodeURI) ;
      portal.setSelectedNode(node) ;
      portalEvent.addParameter(PortalConstants.NODE_URI, nodeURI) ;
      portal.queueEvent(portalEvent) ;
    }
  }
  
  public String getFamily( ) { return "org.exoplatform.portlets.nav.component.UIMenu" ; }
  
}