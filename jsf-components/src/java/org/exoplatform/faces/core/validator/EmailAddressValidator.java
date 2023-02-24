/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.validator;

import java.util.regex.*;
import javax.faces.context.FacesContext; 
import javax.faces.component.UIComponent ; 
import javax.faces.validator.Validator ;
import javax.faces.validator.ValidatorException ;
import org.exoplatform.faces.core.component.UIInput;
import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.application.ExoFacesMessage ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 13, 2004
 * @version $Id: EmptyStringValidator.java,v 1.4 2004/08/19 14:53:44 tuan08 Exp $
 */
public class EmailAddressValidator implements Validator {
  static private final String EMAIL_REGEX =
    "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*";

  public void validate(FacesContext context, 
                       UIComponent component, 
                       Object value) throws ValidatorException {
    if(component instanceof UIForm) {
      UIForm.StringField field = (UIForm.StringField) value ;
      if(!field.getValue().matches(EMAIL_REGEX)) {
        field.setError(true) ;
        Object[] args = {field.getName(), field.getValue()} ;
        throw new ValidatorException(new ExoFacesMessage("#{EmailAddressValidator.msg.invalid-email}", args)) ;
      }
    } else {
      String s = (String) value ;
      if(!s.matches(EMAIL_REGEX)) {
        UIInput uiInput = (UIInput) component ;
        Object[] args = {uiInput.getName(), s} ;
        throw new ValidatorException(new ExoFacesMessage("#{EmailAddressValidator.msg.invalid-email}", args)) ;
      }
    }
  }
}