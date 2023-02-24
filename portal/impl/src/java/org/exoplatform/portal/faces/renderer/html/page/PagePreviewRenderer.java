/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.page;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPagePreview;
/**
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PagePreviewRenderer.java,v 1.5 2004/09/29 17:44:51
 *           benjmestrallet Exp $
 */
public class PagePreviewRenderer extends HtmlBasicRenderer {

  final public void encodeChildren(FacesContext context, UIComponent component)
      throws IOException {
    UIPagePreview uiComponent = (UIPagePreview) component;
    ResponseWriter w = context.getResponseWriter();
    w.write("<div class='UIPagePreview'>");
    UIComponent uiToolbar = uiComponent.getUIToolbar();
    uiToolbar.encodeBegin(context);
    uiToolbar.encodeChildren(context);
    uiToolbar.encodeEnd(context);
    w.write("<div class='page'>");    

    List children = component.getChildren();
    for (int i = 0; i < children.size(); i++) {
      UIComponent uiChild = (UIComponent) children.get(i);
      if (uiChild.isRendered() && !(uiChild instanceof UIToolbar)) {
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
      }
    }

    w.write("</div>");
    w.write("</div>");
  }
}