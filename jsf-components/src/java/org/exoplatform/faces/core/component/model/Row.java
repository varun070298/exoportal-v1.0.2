/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.List ;
import java.util.ArrayList ;
import java.util.ResourceBundle ;
import java.io.IOException  ;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Row.java,v 1.4 2004/07/01 14:20:50 tuan08 Exp $
 */
public class Row implements HtmlFragment {
  protected List cells_ ;
  protected String clazz_ ;
  protected boolean visible_ ;

  public Row() {
    cells_ = new ArrayList() ;
    visible_ = true ;
  }
  
  public Row add(Cell cell) {
    cells_.add(cell) ;
    return this ;
  }
  
  public boolean isVisible() { return visible_ ; }
  public Row setVisible(boolean b) { 
    visible_ = b ; 
    return this ;
  }
  
  final public List getCells() { return cells_ ; } 

  public String getClazz() { return clazz_ ; }
  public Row setClazz(String clazz) { 
    clazz_ = clazz ; 
    return this ;
  }

  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiParent) throws IOException { 
    if(!visible_) return ;
    w.write("<tr") ;
    if(clazz_ != null) { 
      w.write(" class=\"");w.write(clazz_);w.write("\"") ; 
    }
    w.write(">\n") ;
    for (int i = 0 ; i < cells_.size() ; i++) {
      Cell cell = (Cell) cells_.get(i) ;
      cell.render(w, res, uiParent , "td") ;
    }
    w.write("</tr>\n") ;
  }

}