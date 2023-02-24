/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.validator.EmailAddressValidator;
import org.exoplatform.faces.core.validator.EmptyFieldValidator;
import org.exoplatform.container.PortalContainer ;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIAccountForm.java,v 1.14 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIAccountForm extends UIForm {
  private static String USERNAME = "username" , PASSWORD1X = "password1x" ,
                        PASSWORD2X = "password2x" , EMAIL = "email" ,
                        FIRSTNAME = "firstname" , LASTNAME = "lastname" ;
  private User user_ ;
  
  public UIAccountForm() throws Exception {
    super("accountForm") ;
    setId("UIAccountForm") ;
    addContainer("#{UIAccountForm.header.new-account}").
      add(new StringField(USERNAME, "#{UIAccountForm.label.user-name}", "")).
      add(new StringPasswordField(PASSWORD1X, "#{UIAccountForm.label.password1x}", "")).
      add(new StringPasswordField(PASSWORD2X, "#{UIAccountForm.label.password2x}", "")).
      add(new StringField(FIRSTNAME, "#{UIAccountForm.label.first-name}", "")).
      add(new StringField(LASTNAME, "#{UIAccountForm.label.last-name}", "")).
      add(new StringField(EMAIL, "#{UIAccountForm.label.email}", "").
          setValidator(EmailAddressValidator.class));
    addFieldValidator(EmptyFieldValidator.class);
    addFormValidator(ConfirmPasswordValidator.class);
    addButton(new Button("#{UIAccountForm.button.save}", SAVE_ACTION)) ;
  }
   
  public void customizeNewAccountForm() {
    addActionListener(NewAccountListener.class, SAVE_ACTION) ;
  }
  
  public void customizeUpdateAccountForm() {
    addActionListener(UpdateAccountListener.class, SAVE_ACTION) ;
    getStringField(USERNAME).setEditable(false);
  }
  
  public void setEditingUser(String userName) throws Exception {
    OrganizationService service =
      (OrganizationService) PortalContainer.getComponent(OrganizationService.class) ;
    user_ = service.findUserByName(userName) ;
    setEditingUser(user_) ;
  }
  
  public void setEditingUser(User user) throws Exception {
    user_ = user;
    setStringFieldValue(USERNAME, user_.getUserName());
    setStringFieldValue(PASSWORD1X, user_.getPassword());
    setStringFieldValue(PASSWORD2X, user_.getPassword());
    setStringFieldValue(FIRSTNAME, user_.getFirstName());
    setStringFieldValue(LASTNAME, user_.getLastName());
    setStringFieldValue(EMAIL, user_.getEmail());
  }
  
  static public class ConfirmPasswordValidator implements Validator {
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
      UIAccountForm uiForm = (UIAccountForm) component ;
      String p1 = uiForm.getStringFieldValue(PASSWORD1X) ;
      String p2 = uiForm.getStringFieldValue(PASSWORD2X) ;
      if(!p1.equals(p2)) {
        throw new ValidatorException(new ExoFacesMessage("#{UIAccountForm.msg.password-error}")) ;
      }
    }
  }
  
  static public class NewAccountListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  	  UIAccountForm uicomp = (UIAccountForm) event.getComponent() ;    
  	  InformationProvider iprovider = findInformationProvider(uicomp);
  		String userName = uicomp.getStringFieldValue(USERNAME) ;
      OrganizationService service =
        (OrganizationService) PortalContainer.getComponent(OrganizationService.class) ;
      User newUser = service.findUserByName(userName) ;
      if (newUser == null) {
        newUser = service.createUserInstance() ;
        newUser.setUserName(userName) ;
        newUser.setPassword(uicomp.getStringFieldValue(PASSWORD1X)) ;
        newUser.setFirstName(uicomp.getStringFieldValue(FIRSTNAME)) ;
        newUser.setLastName(uicomp.getStringFieldValue(LASTNAME)) ;
        newUser.setEmail(uicomp.getStringFieldValue(EMAIL)) ;
        service.createUser(newUser);
        uicomp.resetFields() ;
        iprovider.addMessage(new ExoFacesMessage("#{UIAccountForm.msg.create-user-success}")) ;
      } else {
        iprovider.addMessage(new ExoFacesMessage("#{UIAccountForm.msg.user-already-exist}")) ;
      }
    }
  }
  
  static public class UpdateAccountListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  	  UIAccountForm  uicomp = (UIAccountForm) event.getComponent() ;
      uicomp.user_.setPassword(uicomp.getStringFieldValue(PASSWORD1X)) ;
      uicomp.user_.setFirstName(uicomp.getStringFieldValue(FIRSTNAME)) ;
      uicomp.user_.setLastName(uicomp.getStringFieldValue(LASTNAME)) ;
      uicomp.user_.setEmail(uicomp.getStringFieldValue(EMAIL)) ;
      OrganizationService service =
        (OrganizationService) PortalContainer.getComponent(OrganizationService.class) ;
      service.saveUser(uicomp.user_) ;
      InformationProvider iprovider = findInformationProvider(uicomp);
      iprovider.addMessage(new ExoFacesMessage("#{UIAccountForm.msg.update-user-success}")) ;
    }
  }
}