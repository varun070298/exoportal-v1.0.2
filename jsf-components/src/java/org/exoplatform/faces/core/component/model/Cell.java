/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.* ;
import java.io.IOException  ;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.commons.utils.ExpressionUtil ;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Cell.java,v 1.3 2004/06/30 19:52:01 tuan08 Exp $
 */
public class Cell {
  protected List attributes_ ;
  protected String value_ ;
  
  public Cell() {
    
  }
  
  public Cell (String value) {
    value_ = value ;
  }
  
  public Cell(int value) {
    value_ = Integer.toString(value) ;
  }

  public Cell(long value) {
    value_ = Long.toString(value) ;
  }

  public Cell(float value) {
    value_ = Float.toString(value) ;
  }

  public Cell(double value) {
    value_ = Double.toString(value) ;
  }

  public Cell(Date value) {
    value_ = value.toString() ;
  }

  public Cell setValue(String value) {
    if(value == null) value_ ="" ;
    else value_ = value ;
    return this ;
  }
  
  public Cell setValue(Date value) {
    if(value == null) value_ = "" ;
    else value_ = value.toString() ;
    return this ;
  }

  public Cell addAttribute(String name, String value) {
    if(attributes_ == null) attributes_ = new ArrayList(3) ;
    attributes_.add(new Attribute(name, value)) ;
    return this ;
  }

  public Cell addClazz(String clazz) {
    addAttribute("class", clazz) ;
    return this ;
  }

  public Cell addStyle(String style) {
    addAttribute("style", style) ;
    return this ;
  }

  public Cell addColspan(String s) {
    addAttribute("colspan", s) ;
    return this ;
  }

  public Cell addRowspan(String s) {
    addAttribute("rowspan", s) ;
    return this ;
  }

  public Cell addWidth(String w) {
    addAttribute("width", w) ;
    return this ;
  }

  public Cell addAlign(String s) {
    addAttribute("align", s) ;
    return this ;
  }

  public Cell addValign(String s) {
    addAttribute("valign", s) ;
    return this ;
  }

  public Cell addHeight(String h) {
    addAttribute("height", h) ;
    return this ;
  }

  public class Attribute {
    String name_ ;
    String value_ ;

    public Attribute(String name, String value) {
      name_ = name ;
      value_ = value ;
    }
  }
  
  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiGrid, String tag) throws IOException { 
    w.write('<'); w.write(tag) ;
    if(attributes_ != null) {
      int size = attributes_.size() ;
      for(int i = 0; i < size ; i++) {
        Cell.Attribute attr = (Cell.Attribute) attributes_.get(i) ;
        w.write(" "); w.write(attr.name_); w.write("=\""); w.write(attr.value_); w.write('\"') ;
      }
    }
    w.write('>') ;
    w.write(ExpressionUtil.getExpressionValue(res,value_)) ;
    w.write("</"); w.write(tag); w.write(">") ;
  }
}