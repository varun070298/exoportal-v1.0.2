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
 * @version $Id: LinkRenderer.java,v 1.2 2004/10/13 03:26:54 tuan08 Exp $
 */
public class LinkRenderer {
  public void renderSelect(ResponseWriter w, UIExoComponent component, 
                           String text, Parameter[] params)  throws IOException {
    render(w, component, text, "select-link", params, null) ;
  }
  
  public void render(ResponseWriter w, UIExoComponent component, 
                     String text, Parameter[] params)  throws IOException {
    render(w, component, text, "link", params, null) ;
  }
  
  public void render(ResponseWriter w, UIExoComponent component, 
                     String text, String clazz, Parameter[] params)  throws IOException {
    render(w, component, text, clazz, params, null) ;
  }
  
  public void render(ResponseWriter w, UIExoComponent component, 
                     String text, String clazz, Parameter[] params, 
                     String tooltip)  throws IOException {
    w.write("<a");  
    if(clazz != null) {
      w.write(" class='"); w.write(clazz); w.write("'");
    }
    w.write(" href='");
    w.write(component.getBaseURL());
    for (int i = 0; i < params.length; i++) {
      w.write("&amp;");
      w.write(params[i].getName());
      w.write('=');
      w.write(params[i].getValue());
    }
    w.write("'");
    if (tooltip == null || tooltip.length() == 0) {
      w.write(">");
    } else {
      w.write(" alt='"); w.write(tooltip); w.write("'");
      w.write(" title='"); w.write(tooltip); w.write("'>");
    }
    writeText(w, text) ;
    w.write("</a>");
  } 
  
  protected void writeText(ResponseWriter w, String text) throws IOException {
    w.write(text);
  }
}