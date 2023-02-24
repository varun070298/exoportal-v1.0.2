/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.faces.user.component;

import javax.faces.context.ExternalContext;
import javax.portlet.ActionResponse;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.container.SessionContainer ;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 1 mai 2004
 */
public class UILogin extends UISimpleForm {
  public static final String LOGIN_ACTION = "login";
  public static final String REGISTER_ACTION = "register";
  public static final String LOGOUT_ACTION = "logout";

  private UIStringInput userNameInput_;
  private UIStringInput passwordInput_;

  private String loginPath_;
  private SecurityService securityService_;
  private OrganizationService organizationService_;

  public UILogin(SecurityService service, OrganizationService organizationService) throws Exception {
    super("loginForm", "post", null);
    setId("UILogin");
    setRendererType("SimpleFormRenderer");
    setClazz("UILogin");
    securityService_ = service;
    organizationService_ = organizationService;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    loginPath_ = rinfo.getContextPath() + "/faces/private/";
    
    userNameInput_ = new UIStringInput("userName", "");
    passwordInput_ = new UIStringInput("password", "", "onkeypress='var keycode; if (window.event) { keycode = window.event.keyCode; } else if (event) { keycode = event.which; } else { return true; } if (keycode == 13) { submit_loginForm(\"login\"); return false; } else { return true; }'");
    passwordInput_.setType(UIStringInput.PASSWORD);
    add(new Row().
        add(new LabelCell("#{UILogin.label.user-name}")).
        add(new ComponentCell(this, userNameInput_)));
    add(new Row().
        add(new LabelCell("#{UILogin.label.password}")).
        add(new ComponentCell(this, passwordInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UILogin.button.login}", LOGIN_ACTION)).            
            addColspan("2").addAlign("center")));
    addActionListener(LoginActionListener.class , LOGIN_ACTION) ;
  }

  public String getLoginPath() {  return loginPath_; }
  
  static public class LoginActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
		  UILogin comp = (UILogin) event.getComponent() ;     
			String userName = comp.userNameInput_.getValue() ;
			String password = comp.passwordInput_.getValue();
			InformationProvider iprovider = findInformationProvider(comp) ;
			boolean hasError = false ;
			if(userName == null || userName.length() == 0){
	      iprovider.addMessage(new ExoFacesMessage("#{UILogin.msg.empty-login}")) ;
	      hasError = true ;
	    }
	    if(password == null || password.length() == 0){
	      iprovider.addMessage(new ExoFacesMessage("#{UILogin.msg.empty-password}")) ;
	      hasError = true ;
	    }
	    if(hasError) return ;
	    if(comp.securityService_.authenticate(userName, password)) {
		User user = comp.organizationService_.findUserByName(userName);
	        userName = user.getUserName();
	    	ExternalContext eContext =comp.getFacesContext().getExternalContext();
				ActionResponse response = (ActionResponse) eContext.getResponse();
				response.addProperty(Output.LOGIN, userName);
				response.addProperty(Output.PASSWORD, password);
				response.sendRedirect(comp.loginPath_ + userName);
	    } else {
	      iprovider.addMessage(new ExoFacesMessage("#{UILogin.msg.unknow-user}")) ;
	    }
		}
	}
}