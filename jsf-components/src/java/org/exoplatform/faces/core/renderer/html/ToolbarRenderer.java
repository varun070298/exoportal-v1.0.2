 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.faces.core.renderer.html;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.component.model.Button;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class ToolbarRenderer extends HtmlBasicRenderer {
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    UIToolbar toolbar = (UIToolbar) component  ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter();
    
    Collection leftButtons = toolbar.getLeftButtons();
    Collection rightButtons = toolbar.getRightButtons();
    
    w.write("<div class='UIToolbar'>"); 
    w.  write("<div class='toolbar-left'>");
    for (Iterator iter = leftButtons.iterator(); iter.hasNext();) {
      Button button = (Button) iter.next();
      button.render(w, res, toolbar, null);
    }
    w.  write("</div>");
    w.  write("<div class='toolbar-right'>");
    for (Iterator iter = rightButtons.iterator(); iter.hasNext();) {
      Button button = (Button) iter.next();
      button.render(w, res, toolbar, null);
    }    
    w.  write("</div>");
    w.write("</div>");
  }

}
