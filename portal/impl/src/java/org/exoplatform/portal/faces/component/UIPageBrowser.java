/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import org.exoplatform.commons.exception.ExoPermissionException;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.Page;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPageBrowser.java,v 1.9 2004/09/28 18:19:21 tuan08 Exp $
 */
public class UIPageBrowser extends UINode {
  static final public String  UI_NAVIGATION_NODE_FORM = "UINavigationNodeForm" ;
  static final public String  UI_PAGE = "UIPage" ;
  
  private PortalConfigService service_ ;
  private String returnModule_ ;
  
  public UIPageBrowser(PortalConfigService service) throws Exception {
    setId("UIPageBrowser") ;
    setRendererType("ChildrenRenderer") ;
    service_ = service ;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class) ;
    String owner = rinfo.getPortalOwner() ;
    
    UIPageList uiPageList = (UIPageList) addChild(UIPageList.class) ;
    uiPageList.queryDescriptions(owner, null, null) ;
    addChild(UIPageModelForm.class).setRendered(false) ;
  }
  
  public PortalConfigService getPortalConfigService() { return service_ ; }
  
  public void setReturnModule(String name) { returnModule_ = name ;} 
  
  public void goBack() {
    UIPortal uiPortal =  (UIPortal) getAncestorOfType(UIPortal.class);
  	if(UI_NAVIGATION_NODE_FORM.equals(returnModule_)) {
  		 UIPageNodeForm uiForm = 
  	  	(UIPageNodeForm)uiPortal.getPortalComponent(UIPageNodeForm.class);
      uiPortal.setBodyComponent(uiForm) ;
    } else {
      uiPortal.setBodyComponent(uiPortal.getCurrentUIPage()) ;
    }
  } 
  
  public void setSelectNodeReferencePage(String refPage) throws Exception  {
    UIPortal uiPortal =  (UIPortal) getAncestorOfType(UIPortal.class);
    Page page = service_.getPage(refPage) ;
    PortalACL acl = service_.getPortalACL() ;
    if(!acl.hasViewPagePermission(page, uiPortal.getOwner())) {
      throw new ExoPermissionException("selectPage", "onwer") ;
    }
  	if(UI_NAVIGATION_NODE_FORM.equals(returnModule_)) {
  	  UIPageNodeForm uiNavigationNodeForm = 
  	  	(UIPageNodeForm)uiPortal.getPortalComponent(UIPageNodeForm.class);
  	  uiNavigationNodeForm.returnReferencePage(refPage) ;
      uiPortal.setBodyComponent(uiNavigationNodeForm) ;
    } else {
      Node selectNode = uiPortal.getSelectedNode() ;
      selectNode.getPageReference(ExoPortal.XHTML_MIME_TYPE).setPageReference(refPage) ;
      uiPortal.setSelectedNode(selectNode) ;
    }
  }
}