/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.text.template.DataBindingValue;
import org.exoplatform.text.template.DataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Cell extends Text {
  protected String attributes_ ;
  
  public Cell(String value)  { 
    super(value) ;
  }
  
  public Cell addArribute(String name, String value) {
    if(attributes_ == null) attributes_ = "" ;
    attributes_ += " " + name + "='" + value +"'" ;
    return this ;
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    w.write("<td") ; 
    if(cssClass_ != null) {
      w.write(" class='") ;w.write(cssClass_); w.write("'") ;
    }
    if(cssStyle_ != null) {
      w.write(" style='") ;w.write(cssStyle_); w.write("'") ;
    }
    if(attributes_ != null) {
      w.write(attributes_) ;
    }
    w.write(">");
    if(data_ != null) {
      DataHandler dh = manager.getDataHandler(dataHandlerType_) ;
      if(formater_ != null) { 
        formater_.format(w, dh.getValue((DataBindingValue)data_)) ;
      } else {
        w.write(resolveValueAsString(data_ ,dh, res)) ;
      }
    }
    if(children_.length > 0) {
      for(int i = 0 ; i < children_.length; i++) {
        children_[i].render(manager, res, w) ;
      }
    }
    w.write("</td>") ;
  }
  
}