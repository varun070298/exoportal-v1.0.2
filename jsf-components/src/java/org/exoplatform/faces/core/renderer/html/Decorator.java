/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.io.IOException ;
import javax.faces.component.UIComponent ;
import javax.faces.context.FacesContext ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 26, 2004
 * @version $Id: Decorator.java,v 1.1 2004/10/27 02:52:15 tuan08 Exp $
 */
abstract public class Decorator {
  protected String cssClass_ ;
  
  public Decorator(String cssClass) {
    cssClass_ = cssClass ;
  }
  
  abstract public void decorate(FacesContext context, UIComponent uiComponent) throws IOException ;
  
  protected void render(FacesContext context, UIComponent uiComponent) throws IOException {
    uiComponent.encodeBegin(context) ;
    uiComponent.encodeChildren(context) ;
    uiComponent.encodeEnd(context) ;
  }
}