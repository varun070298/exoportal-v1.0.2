/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.user.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.exoplatform.container.PortalContainer ;
import org.exoplatform.services.organization.OrganizationService ;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.UIInput;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 13, 2004
 * @version $Id: EmptyStringValidator.java,v 1.4 2004/08/19 14:53:44 tuan08 Exp $
 */
public class ValidUserValidator implements Validator {
  public void validate(FacesContext context,  UIComponent component, 
                       Object value) throws ValidatorException {
    String s = (String) value ;
    if(s == null || s.length() == 0) {
      UIInput uiInput = (UIInput) component ;
      Object[] args = {uiInput.getName(), s} ;
      throw new ValidatorException(new ExoFacesMessage("#{ValidUserValidator.msg.empty-input}", args)) ;
    }
    PortalContainer container = PortalContainer.getInstance() ;
    OrganizationService service =  
      (OrganizationService) container.getComponentInstanceOfType(OrganizationService.class) ;
    try {
      if(service.findUserByName(s) == null)  {
        UIInput uiInput = (UIInput) component ;
        Object[] args = {uiInput.getName(), s} ;
        throw new ValidatorException(new ExoFacesMessage("#{ValidUserValidator.msg.invalid-username}", args)) ;
      }
    } catch(Exception ex) {
      throw new ValidatorException(new ExoFacesMessage(ex.getMessage())) ;
    }
  }
}	