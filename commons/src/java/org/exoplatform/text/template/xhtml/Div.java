/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.text.template.DataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Div extends Text {
  
  public Div()  { 
    super(null) ;
  }
  
  public Div(String text)  { 
    super(text) ;
  }
  
  public void render(XhtmlDataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    if(cssClass_ == null ) {
      w.write("<div>") ;
    } else {
      w.write("<div class='") ; w.write(cssClass_); w.write("'>") ;
    }
    if(data_ != null) {
      DataHandler dh = manager.getDataHandler(dataHandlerType_) ;
      w.write(resolveValueAsString(data_ ,dh, res)) ;
    }
    if(children_.length > 0) {
      for(int i = 0 ; i < children_.length; i++) {
        children_[i].render(manager, res, w) ;
      }
    }
    w.write("</div>") ;
  }
  
}