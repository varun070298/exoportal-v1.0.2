/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.page;

import java.util.* ;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.portal.faces.component.*;

/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PageColumnRenderer.java,v 1.3 2004/08/19 20:30:23 tuan08 Exp $
 */
public class PageColumnRenderer extends PageRenderer {

  protected void renderViewMode(FacesContext context, UIPage uiPage) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    UIPortlet maximizedPortlet = uiPage.getMaximizedPortlet() ;
    w.write("<table class='"); w.write(uiPage.getDecorator()); w.write("-container'");
    w.write(" id='") ; w.write(uiPage.getId());w.write("'>") ;
    if (maximizedPortlet != null) {
      w.write("<tr>") ;
      w.  write("<td style='height: 100%;'>");
      maximizedPortlet.encodeBegin(context);
      maximizedPortlet.encodeChildren(context);
      maximizedPortlet.encodeEnd(context);
      w.  write("</td>");
      w.write("</tr>") ;
    } else {
      List children = uiPage.getChildren();
      int childrenSize = children.size() ;
      w.write("<tr>") ;
      for(int i=0 ; i < childrenSize; i++) {
        UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
        String width = uiChild.getComponentModel().getWidth() ;
        w.write("<td");
        if (width != null && width.length() > 0)  {
          w.write(" style='width: "); w.write(width); w.write("'") ;
        }
        w.  write(">");
        uiChild.setFloat(UIBasicComponent.FLOAT_RIGHT) ;
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
        w.write("</td>");
      }
      w.write("</tr>") ;
    }
    w.write("</table>") ;
  }
}