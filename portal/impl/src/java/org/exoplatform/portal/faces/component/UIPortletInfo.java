/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Style;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletregistery.Portlet;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
import org.exoplatform.services.portletregistery.PortletRole;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 juin 2004
 */
public class UIPortletInfo extends UIExoCommand {
  public static final String SELECT_PORTLET = "selectPortlet";

  protected Portlet portlet_ ;
  private UIContainer portletContainer_ ;
  private SkinConfigService skinService_ ;
  private PortletRegisteryService registryService_ ;
  private PortalComponentIDGenerator idGenerator_ ;
  
  public UIPortletInfo() {
    setRendererType("PortletInfoRenderer");
    setId("UIPortletInfo");
	}

  public UIPortletInfo(SkinConfigService service,
  		                 PortletRegisteryService portletRegisteryService,
                       PortalComponentIDGenerator idGenerator) {
    this() ;
    idGenerator_ = idGenerator ;
    skinService_ =  service ;
    registryService_ = portletRegisteryService ;
    addActionListener(SelectPortletActionListener.class, SELECT_PORTLET) ;
	}
  
  public String getFamily() {
    return "org.exoplatform.portal.faces.component.UIPortletInfo";
  }

  public UIContainer getPortletContainer() { return portletContainer_ ; }
  public void setPortletContainer(UIContainer container) {
  	portletContainer_ = container ;
  }
  
  public void setPortletData(Portlet portlet) { portlet_  = portlet; }

  public Portlet getPortletData() { return portlet_ ;}
  
  static public class SelectPortletActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletInfo uiPortletInfo = (UIPortletInfo) event.getComponent() ;
    	ExternalContext econtext = FacesContext.getCurrentInstance().getExternalContext() ;
    	Collection temp = 
    		uiPortletInfo.registryService_.getPortletRoles(uiPortletInfo.portlet_.getId());
    	Iterator i = temp.iterator() ;
    	boolean hasRole = false ;
    	while(i.hasNext() && !hasRole) {
    		PortletRole role = (PortletRole) i.next() ;
    		hasRole = econtext.isUserInRole(role.getPortletRoleName()) ;
    	}
    	
    	if (!hasRole) {
    		InformationProvider iprovider = findInformationProvider(uiPortletInfo) ;
    		iprovider.addMessage(new ExoFacesMessage("#{UIPortletInfo.msg.require-role}")) ;
    		return ;
    	}
    	
    	UIPortal uiPortal = (UIPortal) uiPortletInfo.getAncestorOfType(UIPortal.class) ;
    	String appName = uiPortletInfo.portlet_.getPortletApplicationName() ;
    	String portletName = uiPortletInfo.portlet_.getPortletName() ;
    	String portletId = appName + "/" + portletName ;
      String styleName = "default" ;
      String rendererName = "default" ;
      List styles = uiPortletInfo.skinService_.getPortletStyles(portletId) ;
      if(styles != null) {
         styleName = ((Style) styles.get(0)).getName() ;
       }
       String id = uiPortletInfo.idGenerator_.generatePortletId(uiPortal, portletName) ;
       UIPortlet uiPortlet = new UIPortlet(uiPortal.getOwner(), appName, portletName, id);
       uiPortlet.setComponentMode(UIBasicComponent.COMPONENT_EDIT_MODE) ;
       org.exoplatform.services.portal.model.Portlet model = uiPortlet.getEditablePortletModel() ;
       model.setRenderer(rendererName) ;
       model.setPortletStyle(styleName) ;
       model.setDecorator("default") ;
       uiPortletInfo.portletContainer_.getChildren().add(uiPortlet) ;
       uiPortletInfo.portletContainer_.setComponentModified(true) ;
       UIPage uiPage = (UIPage)uiPortlet.getAncestorOfType(UIPage.class) ;
       if(uiPage != null) {
         ExoWindowID windowId = uiPortlet.getWindowId() ;
         windowId.setConfigurationSource(uiPage.getPageModel().getPageId()) ;
       }
       uiPortal.setLastBodyComponent() ;
    }
  }
}