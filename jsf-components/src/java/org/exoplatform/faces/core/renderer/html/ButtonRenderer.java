/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.io.IOException;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.model.Parameter;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 28, 2004
 * @version $Id: ButtonRenderer.java,v 1.4 2004/11/01 14:52:22 tuan08 Exp $
 */
public class ButtonRenderer extends LinkRenderer {
  
  public void renderSelect(ResponseWriter w, UIExoComponent component, 
                           String text, Parameter[] params)  throws IOException {
    render(w, component, text, "select-button", params, null) ;
  }
  
  public void render(ResponseWriter w, UIExoComponent component, 
                     String text, Parameter[] params)  throws IOException {
    render(w, component, text, "button",  params, null) ;
  }
}