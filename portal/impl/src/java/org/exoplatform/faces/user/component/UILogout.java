/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.faces.user.component;

import javax.faces.context.ExternalContext;
import javax.portlet.ActionResponse;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.component.model.ListComponentCell;
import org.exoplatform.faces.core.component.model.Cell;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.filter.AdminRequestFilter;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portal.session.PortalResources ;
import org.exoplatform.services.portletcontainer.pci.Output;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 1 mai 2004
 */
public class UILogout extends UIGrid {
  public static final String LOGOUT_ACTION = "logout";
  public static final Parameter[] LOGOUT_PARAMS = {new Parameter(ACTION, LOGOUT_ACTION)} ; 

  private String fullName_;
  private String logoutPath_;

  public UILogout() throws Exception {
    setId("UILogout");
    setClazz("UILogout");
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class) ;
    String accessibility = rinfo.getAccessibility() ;
    if (RequestInfo.ADMIN_ACCESS.equals(accessibility)) {
      PortalResources presources = 
        (PortalResources)SessionContainer.getComponent(PortalResources.class) ;
      String s = "<a href='" + rinfo.getOwnerURI() + '?' + 
                 AdminRequestFilter.ACTION + '=' + AdminRequestFilter.RETURN + "'>" +
                 presources.getPortalResourceBundle().getString("UILogout.button.back") + 
                 "</a>" ;
      add(new Row().
          add(new Cell(s).addAlign("center")));
    } else {
      logoutPath_ = rinfo.getContextPath() ;
      fullName_ = rinfo.getPortalOwner();
      add(new Row().
          add(new ListComponentCell().
              add(new Button("#{UILogout.button.logout}",  LOGOUT_PARAMS)).
              addAlign("center")));
      addActionListener(LogoutActionListener.class , LOGOUT_ACTION) ;
    }
    
  }


  public String getFullName() {  return fullName_; }
  
  static public class LogoutActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
		  UILogout comp = (UILogout) event.getComponent() ;
		  ExternalContext eContext = comp.getFacesContext().getExternalContext();
	    ActionResponse response = (ActionResponse) eContext.getResponse();
	    response.addProperty(Output.LOGOUT, "true");
	    response.sendRedirect(comp.logoutPath_);
		}
	}
}