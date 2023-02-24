/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.text.template.CollectionDataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Rows extends Element {
  private boolean showHeader_ ;
  private String cssHeaderClass_ ;
  private String evenRowClass_ = "odd";
  private String oddRowClass_ = "even";
  
  public Rows()  { }
  
  public Rows(String odd, String even)  { 
    evenRowClass_ = even ;
    oddRowClass_ = odd ;
  }
  
  public Element add(Element element) {
    if(element instanceof Column) {
      return super.add(element) ;
    }
    throw new RuntimeException("expect element type of Column") ; 
  }
  
  public Element setShowHeader(boolean b) {
    showHeader_ = b ;
    return this ;
  }
  
  public Element setCssHeaderClass(String s) {
    cssHeaderClass_ = s ;
    return this ;
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    if(showHeader_) {
      if(cssHeaderClass_ == null) {
        w.write("<tr>") ;
      } else {
        w.write("<tr class='") ; w.write(cssHeaderClass_) ; w.write("'>") ;
      }
      for(int i = 0; i < children_.length; i++) {
        Column column = (Column) children_[i] ;
        column.renderHeader(res, w) ;
      }
      w.write("</tr>") ;
    }
    
    CollectionDataHandler dh = (CollectionDataHandler)manager.getDataHandler(dataHandlerType_) ;
    boolean even = true ;
    dh.begin() ;
    while(dh.nextRow()) {
      String css = oddRowClass_ ;
      if(even) css = evenRowClass_ ;
      if(css == null) {
        w.write("<tr>") ;
      } else {
        w.write("<tr class='") ; w.write(css) ; w.write("'>") ;
      }
      for(int i = 0 ; i < children_.length; i++) {
        Column column = (Column) children_[i] ;
        column.renderCell(manager, res, w) ;
      }
      w.write("</tr>") ;
      even = !even ;
    }
    dh.end() ;
  }
}