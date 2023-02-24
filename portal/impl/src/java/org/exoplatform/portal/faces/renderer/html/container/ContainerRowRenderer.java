/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.container;

import java.util.* ;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.portal.faces.component.*;

/**
 * Date: Aug 11, 2003
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContainerRowRenderer.java,v 1.1 2004/07/02 17:52:05 tuan08 Exp $
 */
public class ContainerRowRenderer extends ContainerRenderer {

  protected void renderViewMode(FacesContext context, UIContainer uiContainer) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    List children = uiContainer.getChildren();
    if(children.size() == 0 ) return ;
    w.write("<table class='"); w.write(uiContainer.getDecorator()); w.write("-container'");
    w.write(" id='") ; 
    if(uiContainer.getDisplayTitle() == null)
      w.write(uiContainer.getId());
    else
      w.write(uiContainer.getDisplayTitle());
    w.write("'>") ; 
    int childrenSize = children.size() ;
    for(int i=0 ; i < childrenSize; i++) {
      UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
      if(uiChild.isRendered()) {
        String height = uiChild.getComponentModel().getHeight() ;
        w.write("<tr>") ;
        w.  write("<td");
        if (height != null && height.length() > 0)  {
          w.write(" style='height: "); w.write(height); w.write("'") ;
        }
        w.  write(">");
        uiChild.setFloat(UIBasicComponent.FLOAT_BOTTOM) ;
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
        w.  write("</td>");
        w.write("</tr>") ;
      }
    }
    w.write("</table>") ;
  }
}