/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.ResourceBundle ;
import java.io.IOException;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.renderer.html.SimpleFormRenderer;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: FormButton.java,v 1.11 2004/08/05 04:10:10 tuan08 Exp $
 */
public class FormButton  implements HtmlFragment {
  
  protected String  label_;
  protected String  action_;
  protected String  clazz_;
  protected short supportMode_ ;  
  private boolean visible_ = true;

  public FormButton(String name, String action) {
    label_ = name;
    action_ = action;
    supportMode_ = UISimpleForm.ALL_MODE ;
  }
  
  public FormButton(String name, String action, short mode) {
    label_ = name;
    action_ = action;
    supportMode_ = mode ;
  }

  public short getSupportMode() { return supportMode_ ; }
  public void  setSupportMode(short mode) { supportMode_ = mode ; } 
  
  public String getLabel() { return label_ ; }
  public void setLabel(String label) {
    label_ = label;
  }

  public String getAction() { return action_ ;}
  public void setAction(String action) {
    action_ = action;
  }
  
  public void setVisible(boolean visible) {  visible_ = visible;}
  public boolean isVisible(){return visible_ = true;}  
  
  public String getClazz() { return clazz_ ; }
  public void setClass(String s) {
    clazz_ = s;
  }
  
  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiParent) throws IOException {
    if(!visible_) return;
    UISimpleForm uiForm = (UISimpleForm) uiParent ;
    if(supportMode_ == UISimpleForm.ALL_MODE || supportMode_ == uiForm.getMode()) {
      SimpleFormRenderer renderer = 
        (SimpleFormRenderer)uiParent.getComponentRenderer(FacesContext.getCurrentInstance()) ;
      renderer.renderFormButton(this, res, w, uiParent) ;
    }    
  }
  
}