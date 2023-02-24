/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.nav.component;

import java.util.ResourceBundle;
import javax.faces.context.ExternalContext;
import javax.portlet.ActionResponse;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portletcontainer.pci.Output;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 sept. 2004
 */
public class UIFirstLevelMenu extends UINavigation {
  public static final String LOGOUT_ACTION = "logout";
  private String logoutPath_;

	public UIFirstLevelMenu(ResourceBundle res) throws Exception {
		super(res) ;
		setId("uiFirstLevelMenu");
		setRendererType("FirstLevelMenuRenderer");
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    logoutPath_ = rinfo.getContextPath() ;
    addActionListener(LogoutActionListener.class , LOGOUT_ACTION) ;
	}

  public String getFamily( ) { return "org.exoplatform.portlets.nav.component.UIFirstLevelMenu" ; }

  static public class LogoutActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
		  UIFirstLevelMenu comp = (UIFirstLevelMenu) event.getComponent() ;
		  ExternalContext eContext = comp.getFacesContext().getExternalContext();
	    ActionResponse response = (ActionResponse) eContext.getResponse();
	    response.addProperty(Output.LOGOUT, "true");
	    response.sendRedirect(comp.logoutPath_);
		}
	}
}
