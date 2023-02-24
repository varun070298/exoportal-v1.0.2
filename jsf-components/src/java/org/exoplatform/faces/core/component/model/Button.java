/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.ResourceBundle ;
import java.io.IOException;

import javax.faces.context.ResponseWriter;
import org.exoplatform.Constants;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.commons.utils.ExpressionUtil ;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Button.java,v 1.10 2004/10/18 22:06:09 benjmestrallet Exp $
 */
public class Button implements HtmlFragment {
  protected String  label_ ;
  protected String  clazz_ ;
  protected String parameters_ ;
  protected boolean activate_ ;
  private boolean visible_ = true;
  
  public Button(String name, Parameter[] params) {
    label_ = name ;
    StringBuffer b = new StringBuffer() ;
    for(int i = 0; i < params.length; i++) {
      b.append(Constants.AMPERSAND);
      b.append(params[i].getName()).append('=').append(params[i].getValue());
    }
    parameters_ = b.toString() ;
    activate_ = true ;
  }
  
  public void setActivate(boolean b) { activate_ = b ; }
  public void setVisible(boolean visible) {  visible_ = visible;}
  public boolean isVisible(){return visible_;}
  
  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiParent) throws IOException {
    if(!visible_) return;
      if (activate_) {
        String baseURL =  uiParent.getBaseURL() ; 
        w.write("<a");
        if (clazz_ != null) {
          w.write(" class='"); w.write(clazz_); w.write("'");
        }
        w.write(" href='"); w.write(baseURL); w.write(parameters_); w.write("'>");
        w.write(ExpressionUtil.getExpressionValue(res,label_));
        w.write("</a>");
      } else {
        w.write("<span style='border: 1px groove'>");w.write(label_); w.write("<span>");
      }    
  }
  
  public void render(ResponseWriter w, ResourceBundle res, 
                     UIExoComponent uiParent, String objectId) throws IOException {
    if(!visible_) return;
    if (activate_) {
      String baseURL =  uiParent.getBaseURL() ; 
      w.write("<a");
      if (clazz_ != null) {
        w.write(" class='"); w.write(clazz_); w.write("'");
      }
      w.write(" href='"); w.write(baseURL); w.write(parameters_) ;
      if(objectId != null) {
        w.write(Constants.AMPERSAND + "objectId="); w.write(objectId) ;
      }
      w.write("'>");
      w.write(ExpressionUtil.getExpressionValue(res,label_));
      w.write("</a>");
    } else {
      w.write("<span style='border: 1px groove'>");
      w.write(ExpressionUtil.getExpressionValue(res,label_));
      w.write("<span>");
    }    
  }  
}
