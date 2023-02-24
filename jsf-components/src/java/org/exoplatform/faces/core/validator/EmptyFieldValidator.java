/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.UIInput;
import org.exoplatform.faces.core.component.UIForm;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 13, 2004
 * @version $Id: EmptyStringValidator.java,v 1.4 2004/08/19 14:53:44 tuan08 Exp $
 */
public class EmptyFieldValidator implements Validator {
  public void validate(FacesContext context, 
                       UIComponent component, 
                       Object value) throws ValidatorException {
    if(component instanceof UIForm) {
      if(value instanceof UIForm.IntegerField) {
      } else if(value instanceof UIForm.StringField) {
        UIForm.StringField field = (UIForm.StringField) value ;
        String s = field.getValue() ;
        if(s == null || s.length() == 0) {
          Object[] args = {field.getName()} ;
          field.setError(true) ;
          throw new ValidatorException(new ExoFacesMessage("#{EmptyStringValidator.msg.empty-input}", args)) ;
        }
      }
    } else {
      String s = (String) value ;
      if(s == null || s.length() == 0) {
        UIInput uiInput = (UIInput) component ;
        Object[] args = {uiInput.getName()} ;
        throw new ValidatorException(new ExoFacesMessage("#{EmptyStringValidator.msg.empty-input}", args)) ;
      }
    }
  }
}	