/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.* ;
import java.io.IOException  ;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.validator.Validator;
import org.exoplatform.faces.core.component.model.*;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIRadioBox.java,v 1.5 2004/08/23 15:05:41 benjmestrallet Exp $
 */
public class UIRadioBox extends UIInput {
  private static int VERTICAL_ALIGN = 1 ;
  private static int HORIZONTAL_ALIGN = 2 ;

  protected String value_ ;
  private List options_ ;
  private int align_ ;

  public UIRadioBox(String name, String value) {
    name_ = name ;
    value_ = value ;
    align_ = HORIZONTAL_ALIGN ;
  }

  public UIRadioBox(String name, String value, List options) {
    name_ = name ;
    value_ = value ;
    options_ = options ;
    align_ = HORIZONTAL_ALIGN ;
  }

  final public String getValue() { return value_ ; }

  final public UIRadioBox setValue(String s) { 
    value_ = s ; 
    return this ;
  }
  
  final public List getOptions() { return options_ ; }
  final public UIRadioBox setOptions(List options) { 
    options_ = options ; 
    return this ;
  }

  final public UIRadioBox setAlign(int val) { 
    align_ = val ;
    return this ;
  }
  
  public UIRadioBox addValidator(Validator validator) {
    addComponentValidator(validator) ;
    return this ;
  }
  
  public  UIRadioBox addValidator(Class clazz) {
    addComponentValidator(clazz) ;
    return this ;
  }

  public void decode(FacesContext context) {
    if (!editable_ || readonly_) return ;
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String value = (String) paramMap.get(name_) ;
    if (value != null) {
      value_ = value ;
    }
  }
  
  final public  void processValidators(FacesContext context) {
    processValidators(context, value_) ;
  }

  public void encodeBegin(FacesContext context) throws IOException {
    ResponseWriter w =  context.getResponseWriter() ;
    if(value_ == null) {
      SelectItem si = (SelectItem) options_.get(0) ;
      value_ = si.value_ ;
    }
    for(int i=0; i < options_.size(); i++) {
      SelectItem si = (SelectItem) options_.get(i) ;
      String checked = "" ;
      if (si.value_.equals(value_)) checked = " checked" ;
      w.write("<input class='radio' type='radio'");
      if (!editable_ || readonly_) {
        w.write(" readonly='readonly'");
      }
      w.write(checked); w.write(" name='"); w.write(name_); w.write("'") ;
      w.write(" value='"); w.write(si.value_);
      w.write("'/>");
      w.write(si.display_);
      if (align_ == VERTICAL_ALIGN) w.write("<br/>");
    }
  }
}
