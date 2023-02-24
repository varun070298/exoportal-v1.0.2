/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.xhtmlmp.container;

import java.util.* ;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.portal.faces.component.*;

/*
 * Date: Aug 11, 2003
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContainerRenderer.java,v 1.1 2004/08/08 19:25:38 tuan08 Exp $
 */
public class ContainerRenderer 
	extends org.exoplatform.portal.faces.renderer.html.container.ContainerRenderer {

  protected void renderViewMode(FacesContext context, UIContainer uiContainer) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    List children = uiContainer.getChildren();
    int childrenSize = children.size() ;
    if(childrenSize == 0 ) return ;
    w.write("<div class='"); w.write(uiContainer.getDecorator()); w.write("-container'");
    w.write(" id='") ; w.write(uiContainer.getId());w.write("'>") ; 
    for(int i=0 ; i < childrenSize; i++) {
      UIBasicComponent uiChild = (UIBasicComponent) children.get(i) ;
      String width = uiChild.getComponentModel().getWidth() ;
      if(uiChild.isRendered()) {
        uiChild.setFloat(UIBasicComponent.FLOAT_RIGHT) ;
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
      }
    }
    w.write("</div>") ;
  }
}