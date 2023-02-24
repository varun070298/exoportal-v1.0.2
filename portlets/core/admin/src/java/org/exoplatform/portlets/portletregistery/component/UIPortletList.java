/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.portletregistery.component;


import java.util.Collection;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletregistery.Portlet;
import org.exoplatform.services.portletregistery.PortletCategory;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 juin 2004
 */
public class UIPortletList extends UIExoCommand{
  private Map allPortletMetaData_;
  private PortletRegisteryService portletRegisteryService;
  private PortletCategoryData portletCategoryData_;

  public UIPortletList(PortletContainerService portletContainerService,
                       PortletRegisteryService portletRegisteryService) {
  	setId("UIPortletList") ;
    setRendererType("AddPortletRenderer") ;
    allPortletMetaData_ = portletContainerService.getAllPortletMetaData() ;
    this.portletRegisteryService = portletRegisteryService;
    addFacesListener(new SaveActionListener().setActionToListen(SAVE_ACTION));
    addFacesListener(new CancelActionListener().setActionToListen(CANCEL_ACTION));
  }

  public Map getAllPortletMetaData() { return allPortletMetaData_ ; }

  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletList" ;
  }

  public void setPortletCategoryData(PortletCategoryData category) {
  	portletCategoryData_ = category ;
  }

  private class SaveActionListener extends ExoActionListener {
    public SaveActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception{
    	UIPortletList uiList = (UIPortletList) event.getComponent() ;
      Map paramValuesMap = 
      	FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap() ;
      String[] portletId = (String[]) paramValuesMap.get("portletId") ;
      PortletCategory portletCategory = portletCategoryData_.getPortletCategory() ;
      for (int i = 0; i < portletId.length; i++) {
        String windowId = portletId[i];
        String[] keys = StringUtils.split(windowId, "/") ;
        Portlet portlet = portletRegisteryService.createPortletInstance();
        portlet.setDisplayName(keys[1]);
        portlet.setPortletApplicationName(keys[0]);
        portlet.setPortletName(keys[1]);
        portletRegisteryService.addPortlet(portletCategory, portlet);
      }
      Collection portlets = portletRegisteryService.getPortlets(portletCategory.getId()) ;
      portletCategoryData_.reset(portletCategory, portlets) ;
      uiList.setRenderedSibling(UIPortletCategories.class) ;
    }
  }

  private class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletRegistry uiPortlet = (UIPortletRegistry) getParent();
      uiPortlet.setRenderedComponent(UIPortletCategories.class);
    }
  }
}