/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ResourceBundle;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.DataHandlerManager;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class Span extends Text {
  public Span(String value)  { 
    super(value);
  }
  
  public void render(DataHandlerManager manager, 
                     ResourceBundle res, Writer w) throws IOException {
    if(cssClass_ == null) {
      w.write("<span>") ;
    } else {
      w.write("<span class='") ; w.write(cssClass_); w.write("'>") ;
    }
    DataHandler dh = manager.getDataHandler(dataHandlerType_);
    w.write(resolveValueAsString(data_ ,dh, res)) ;
    w.write("</span>") ;
  }
}