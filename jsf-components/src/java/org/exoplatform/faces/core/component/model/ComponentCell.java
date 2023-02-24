/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.io.IOException  ;
import java.util.ResourceBundle;
import javax.faces.context.ResponseWriter ;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ComponentCell.java,v 1.3 2004/06/30 19:52:01 tuan08 Exp $
 */
public class ComponentCell extends Cell {
  private UIComponent component_ ;

  public ComponentCell(UIComponent parent, UIComponent comp) {
    component_ = comp ;
    parent.getChildren().add(comp) ;
  }

  public void setComponent(UIComponent parent, UIComponent comp) {
    component_ = comp ;
    parent.getChildren().add(comp) ;
  }

  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiGrid, String cellTag) throws IOException { 
    w.write('<');w.write(cellTag) ;
    if(attributes_ != null) {
      int size = attributes_.size() ;
      for(int i = 0; i < size ; i++) {
        Cell.Attribute attr = (Cell.Attribute) attributes_.get(i) ;
        w.write(' ');w.write(attr.name_);w.write("='");w.write(attr.value_);w.write('\'') ;
      }
    }
    FacesContext context = FacesContext.getCurrentInstance() ;
    w.write('>') ;
    component_.encodeBegin(context) ;
    component_.encodeChildren(context) ;
    component_.encodeEnd(context) ;
    w.write("</");w.write(cellTag);w.write(">") ;
  }
}
