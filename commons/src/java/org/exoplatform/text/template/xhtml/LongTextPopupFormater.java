/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template.xhtml;

import java.io.IOException;
import java.io.Writer;
import org.exoplatform.text.template.ObjectFormater;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 3, 2005
 * @version $Id$
 */
public class LongTextPopupFormater implements ObjectFormater {
  private int maxLength_ ;
  
  public LongTextPopupFormater(int textSize) {
    maxLength_ = textSize ;
  }
  
  public void   format(Writer w, Object obj) throws IOException {
    String value  = obj.toString() ;
    if(value.length() < maxLength_) {
      w.write(value) ;
    } else {
      w.write("<span title='");
      w.write(value.toString()) ;
      w.write("'>") ;
      w.write(value.substring(0 , maxLength_)) ;
      w.write(".....") ;
      w.write("</span>") ;
    }
  }
}