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
 * @version $Id: ThreeImagePatternButton.java,v 1.1 2004/11/01 14:52:22 tuan08 Exp $
 */
public class ThreeImagePatternButton extends ButtonRenderer {
  private String buttonClass_  ;
  private String selectButtonClass_ ;
  
  public ThreeImagePatternButton(String buttonClass, String selectButtonClass) {
    buttonClass_ = buttonClass ;
    selectButtonClass_ = selectButtonClass ;
  }
  
  public void renderSelect(ResponseWriter w, UIExoComponent component, 
                           String text, Parameter[] params)  throws IOException {
    render(w, component, text, selectButtonClass_, params, null) ;
  }
  
  public void render(ResponseWriter w, UIExoComponent component, 
                     String text, Parameter[] params)  throws IOException {
    render(w, component, text, buttonClass_, params, null) ;
  }
  
  protected void writeText(ResponseWriter w, String text) throws IOException {
    w.write("<span class='left'/> </span>");
    w.write("<span class='middle'>");
    w.write(text);
    w.write("</span>");
    w.write("<span class='right'/> </span>");
  }
}