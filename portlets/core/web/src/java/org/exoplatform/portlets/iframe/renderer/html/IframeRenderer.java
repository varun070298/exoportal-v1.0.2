/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.iframe.renderer.html;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.iframe.component.*;
import java.io.IOException;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: IframeRenderer.java,v 1.2 2004/04/07 02:28:29 tuan08 Exp $
 */
public class IframeRenderer extends  HtmlBasicRenderer { 

  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    UIIframe uiIframe = (UIIframe) component ;
    w.write("<iframe style='border: none' src='"+ uiIframe.getFrameSource() + "'" +
                   " width='"+ uiIframe.getFrameWidth() + "'" + 
                   " height='"+uiIframe.getFrameHeight()+"'" +
                   " scrolling='auto' frameborder='0'>") ;
    w.  write("[Your user agent does not support frames or is currently configured " +
              "not to display frames. However, you may visit") ;
    w.write("</iframe>");
  }
}
