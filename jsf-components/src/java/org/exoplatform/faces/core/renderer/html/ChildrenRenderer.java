/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.  *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.util.* ;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer ;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ChildrenRenderer.java,v 1.1 2004/06/03 22:51:36 tuan08 Exp $
 */
public  class ChildrenRenderer extends Renderer {

  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {    
  }

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    List children = component.getChildren() ;
    for (int i = 0; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i) ;
      if (child.isRendered()) {
        child.encodeBegin(context) ;
        child.encodeChildren(context) ;
        child.encodeEnd(context) ;
      }
    }
  }

  final public void encodeEnd( FacesContext context, UIComponent component ) throws IOException {
  }
}