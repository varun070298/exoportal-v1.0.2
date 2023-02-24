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

 * @version: $Id: UIHtmlTextArea.java,v 1.5 2004/08/17 00:13:28 tuan08 Exp $

 */
public class UIHtmlTextArea extends UIStringInput {
  private String width_ ;
  private String height_ ;
  private boolean setFormScript_ ;

  public UIHtmlTextArea(String name, String text, String width, String height) {
    super(name, text) ;
    width_ = width ;
    height_ = height ;
    setFormScript_ = false ;
  }

  public void encodeBegin(FacesContext context) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    String value = value_ ;
    if (value == null) value = "" ;
    w.write("<textarea id='"); w.write(name_); w.write("'") ;
    w.write(" name='"); w.write(name_); w.write("'") ;
    if (getClazz() != null) {
      w.write(" class='"); w.write(getClazz()); w.write("'") ;
    }
    w.write('>') ;
    w.write(value) ;
    w.write("</textarea>") ;
    if(!setFormScript_) {
      UISimpleForm uiForm = (UISimpleForm) getParent() ;
      uiForm.setEnhancedScript(name_ , width_ , height_) ;
      setFormScript_ =  true ;
    }
  }
}