/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UITextArea.java,v 1.8 2004/07/02 02:35:21 tuan08 Exp $
 */
public class UITextArea extends UIStringInput {

  public static String COMPONENT_FAMILY = "org.exoplatform.faces.core.component.UITextArea";
  public static String TEXTAREA_RENDERER = "TextAreaRenderer";
    
  private String cols_ = "40";
  private String rows_ = "20";
  
  public UITextArea(String name, String text) {
    super(name, text) ;
    setRendererType(TEXTAREA_RENDERER);
  }
  
  /* 
   * @see javax.faces.component.UIComponent#getFamily()
   */
  public String getFamily() {
      return COMPONENT_FAMILY;
  }
  
  public String getCols() {
      return cols_;
  }
  
  public UITextArea setCols(String cols) {
      cols_ = cols;
      return this;
  }
  
  public String getRows() {
      return rows_;
  }
  
  public UITextArea setRows(String rows) {
      rows_ = rows;
      return this;
  }
}
