/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.renderer.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.content.explorer.component.UIContentViewer;

/**

 * @email:   tuan08@users.sourceforge.net

 * @version: $Id: ImageRenderer.java,v 1.1 2004/07/16 09:51:34 oranheim Exp $

 */
public class ImageRenderer extends HtmlBasicRenderer { 
 
  final public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    UIContentViewer uiDisplayer = (UIContentViewer) component;
    ResponseWriter w = context.getResponseWriter() ;
    w.    write("<div style='margin: auto 0px; '>") ;
    w.      write("<img src='" + uiDisplayer.getContent() + "'/>") ;
    w.    write("</div>") ;
  }
}