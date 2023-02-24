/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.xhtmlmp.page;

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
 * @version: $Id: PageRenderer.java,v 1.1 2004/08/08 19:25:39 tuan08 Exp $
 */
public class PageRenderer 
	extends org.exoplatform.portal.faces.renderer.html.page.PageRenderer {

  protected void renderViewMode(FacesContext context, UIPage uiPage) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    UIPortlet maximizedPortlet = uiPage.getMaximizedPortlet() ;
    w.write("<div class='"); w.write(uiPage.getDecorator()); w.write("-page'");
    w.write(" id='") ; w.write(uiPage.getId());w.write("'>") ;
    if (maximizedPortlet != null) {
      maximizedPortlet.encodeBegin(context);
      maximizedPortlet.encodeChildren(context);
      maximizedPortlet.encodeEnd(context);
    } else {
      List children = uiPage.getChildren();
      int childrenSize = children.size() ;
      for(int i=0 ; i < childrenSize; i++) {
        UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
      }
    }
    w.write("</div>") ;
  }
}