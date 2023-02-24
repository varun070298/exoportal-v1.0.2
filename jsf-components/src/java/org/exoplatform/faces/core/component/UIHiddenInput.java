/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.io.IOException  ;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIHiddenInput.java,v 1.3 2004/10/05 14:40:48 tuan08 Exp $
 */
public class UIHiddenInput extends UIExoComponentBase {
  protected String name_ ;
  protected String value_ ;
  protected String validateErrorMessage_ ;

  public UIHiddenInput(String name, String value) {
    name_ = name ;
    value_ = value ;
  }

  final public String getName() { return name_; }
  final public UIHiddenInput setName(String name) { 
    name_ = name ; 
    return this ;
  }

  final public String getValue() { return value_; }
  final public UIHiddenInput setValue(String value) { 
    value_ = value ; 
    return this ;
  }
  
  public String getValidateErrorMessage() { return validateErrorMessage_ ; }


  final public void processDecodes(FacesContext context) {
    decode(context) ;
  }

  public void encodeBegin(FacesContext context) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    String value = value_ ;
    if (value == null) value = "" ;
    w.write("<input type='hidden' name='"); w.write(name_); w.write("'"); 
    w.write(" value='"); w.write(value); w.write("'") ;
    w.write("/>") ;
  }

  public void encodeChildren(FacesContext context) {
  }

  public void encodeEnd(FacesContext context) {
  }
}
