/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Row extends Element {
  
  public Row()  { }
  
  public Element add(Element element) {
    if(element instanceof Cell ||
       element instanceof HeaderCell) {
      return super.add(element) ;
    }
    throw new RuntimeException("expect element type of Cell or HeaderCell") ; 
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
      if(cssClass_ == null) {
        w.write("<tr>") ;
      } else {
        w.write("<tr class='") ; w.write(cssClass_) ; w.write("'>") ;
      }
      for(int i = 0; i < children_.length; i++) {
        children_[i].render(manager,res, w) ;
      }
      w.write("</tr>") ;
  }
}