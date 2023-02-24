/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.ArrayList ;
import java.util.ResourceBundle ;
import java.io.IOException  ;
import javax.faces.context.ResponseWriter ;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import org.exoplatform.commons.utils.ExpressionUtil;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ListComponentCell.java,v 1.4 2004/06/30 19:52:08 tuan08 Exp $
 */
public class ListComponentCell extends Cell {
  private ArrayList comps_ ;
  
  public ListComponentCell() {
    comps_ = new ArrayList(5) ;
  }

  public ListComponentCell add(UIComponent parent, UIComponent comp) {
    parent.getChildren().add(comp) ;
    comps_.add(comp) ;
    return this ;
  }

  public ListComponentCell add(Object o) {
    comps_.add(o) ;
    return this ;
  }
  
  public ListComponentCell add(HtmlFragment f) {
    comps_.add(f) ;
    return this ;
  }

  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiGrid, String tag) throws IOException { 
    w.write("<") ; w.write(tag) ;
    if(attributes_ != null) {
      int size = attributes_.size() ;
      for(int i = 0; i < size ; i++) {
        Cell.Attribute attr = (Cell.Attribute) attributes_.get(i) ;
        w.write(' ');w.write(attr.name_);w.write("='");w.write(attr.value_);w.write('\'') ;
      }
    }
    w.write('>') ;
    FacesContext context = FacesContext.getCurrentInstance();
    for (int i = 0; i < comps_.size(); i++) {
      Object o =  comps_.get(i) ;
      if (o instanceof UIComponent) {
        UIComponent component = (UIComponent) o;
        component.encodeBegin(context) ;
        component.encodeChildren(context) ;
        component.encodeEnd(context) ;
      } else if (o instanceof HtmlFragment){
      	HtmlFragment f = (HtmlFragment) o ;
        f.render(w, res,  uiGrid) ;
      } else {
        w.write(ExpressionUtil.getExpressionValue(res,o.toString())) ;
      }
    }
    w.write("</") ; w.write(tag) ; w.write(">") ;
  }
}