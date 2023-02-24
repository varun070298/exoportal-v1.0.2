/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.renderer.html;

import java.util.Iterator  ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import java.io.IOException;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContentEditorRenderer.java,v 1.1 2004/07/16 09:51:34 oranheim Exp $
 */
public class ContentEditorRenderer extends HtmlBasicRenderer { 
  
  final public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    w.write("<center>");
    Iterator i = component.getFacets().values().iterator() ;
    while (i.hasNext()) {
      UIComponent child = (UIComponent) i.next() ;
      if (child.isRendered()) {
        child.encodeBegin(context) ;
        child.encodeChildren(context) ;
        child.encodeEnd(context) ;
      }
    }
    w.write("</center>");
  }
}
