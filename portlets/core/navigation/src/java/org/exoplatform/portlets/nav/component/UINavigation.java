package org.exoplatform.portlets.nav.component;

import java.util.ResourceBundle;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.model.Node;

/**
 * @version $Revision: 1.11 $ $Date: 2004/09/27 18:26:58 $
 * @author Fahrid Djebbari
 */
public class UINavigation extends UIExoCommand {
  final public static Parameter CHANGE_NODE = new Parameter(ACTION, "changeNode");
  final static public Parameter CHANGE_MODE =   new Parameter(ACTION, "changeMode");
  final static public Parameter[] viewModeParams = {CHANGE_MODE,  new Parameter("mode", "normal") };
  final static public Parameter[] editNavigationModeParams = 
    {CHANGE_MODE,  new Parameter("mode", "edit-navigation") };
  final static public Parameter[] editPortalModeParams =  {CHANGE_MODE,  new Parameter("mode", "edit-portal") };
  final static public Parameter[] editPageModeParams =  {CHANGE_MODE,  new Parameter("mode", "edit-page") };
  
	final static public String DEFAULT_RENDERER_TYPE = "HorizontalMenuRenderer" ;
  
  protected Node rootNode_ ;
  private boolean adminRole_ = false ;
  private boolean showReturn_  = false ;
  private String preferedMimetype_ ;
  
	public UINavigation(ResourceBundle res) throws Exception {
		setId("UINavigation");
		setRendererType(DEFAULT_RENDERER_TYPE);
		setTransient(false);
    HttpClientInfo info = 
      (HttpClientInfo) SessionContainer.getInstance().getMonitor().getClientInfo() ;
    preferedMimetype_ = info.getPreferredMimeType() ;
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    rootNode_ = portal.getRootNode() ;
    RequestInfo rinfo = (RequestInfo) SessionContainer.getComponent(RequestInfo.class) ;
    String accessibility = rinfo.getAccessibility() ;
    if(RequestInfo.PRIVATE_ACCESS.equals(accessibility)) {
    	adminRole_ = true ;
    } else if (RequestInfo.ADMIN_ACCESS.equals(accessibility)) {
      adminRole_ = true ;   showReturn_ = true ;
    } else {
    	adminRole_  = false ;
    }
    
    addActionListener(ChangeNodeActionListener.class, "changeNode") ;
    addActionListener(ChangeModeActionListener.class, "changeMode") ;
	}
  
  public Node getRootNode() { return rootNode_ ; }
  public Node getSelectedNode()  { 
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    return  portal.getSelectedNode() ; 
  }
  
  public String getPreferedMimeType() { return preferedMimetype_ ; }

  public boolean hasAdminRole() { return adminRole_ ; }
  
  public boolean getShowReturn() { return showReturn_ ; }
  
  public void setViewModeRenderer() {  }
  
  public void setEditPortalModeRenderer() {  }
  
  public void setEditPageModeRenderer() {  }
  
  public void setEditNavigationModeRenderer() {  }
  
  public String getFamily( ) { return "org.exoplatform.portlets.nav.component.UINavigation" ; }
  
	static public class ChangeNodeActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			String uri = event.getParameter("uri");
      ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
			Node node = portal.getRootNode().findNode(uri) ;
			portal.setSelectedNode(node) ;
		}
	}
	
  static public class ChangeModeActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
      UINavigation uiNavigation = (UINavigation) event.getSource() ;
			String mode = event.getParameter("mode");
      ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
			if("edit-navigation".equals(mode)) {
			  portal.setPortalMode(ExoPortal.PORTAL_EDIT_NAVIGATION_MODE) ;
        uiNavigation.setEditNavigationModeRenderer() ;
			} else if("edit-portal".equals(mode)) {
				portal.setPortalMode(ExoPortal.PORTAL_EDIT_MODE) ;
        uiNavigation.setEditPortalModeRenderer() ;
			} else if("edit-page".equals(mode)) {
				portal.setPortalMode(ExoPortal.PORTAL_EDIT_PAGE_MODE) ;
        uiNavigation.setEditPageModeRenderer() ;
			} else {
				portal.setPortalMode(ExoPortal.PORTAL_VIEW_MODE) ;
        uiNavigation.setViewModeRenderer() ;
			}
		}
	}
}