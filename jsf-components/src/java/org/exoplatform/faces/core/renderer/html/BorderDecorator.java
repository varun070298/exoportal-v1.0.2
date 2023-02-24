/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.io.IOException ;
import javax.faces.component.UIComponent ;
import javax.faces.context.FacesContext ;
import javax.faces.context.ResponseWriter;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 26, 2004
 * @version $Id: BorderDecorator.java,v 1.1 2004/10/27 02:52:15 tuan08 Exp $
 */
public class BorderDecorator extends Decorator {
  
  public BorderDecorator(String cssClass) {
    super(cssClass) ;
  }
  
  public void decorate(FacesContext context, UIComponent uiComponent) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    w.write("<table class='") ;w.write(cssClass_); w.write("'>") ;
    w.  write("<tr>");
    w.    write("<td  class='top-left'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.    write("<td  class='top-middle'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.    write("<td  class='top-right'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td  class='middle-left'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.    write("<td>"); 
    render(context, uiComponent) ;
    w.    write("</td>");
    w.    write("<td  class='middle-right'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td  class='bottom-left'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.    write("<td  class='bottom-middle'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.    write("<td  class='bottom-right'>"); w.write("<img src='/skin/blank.gif'/>"); w.write("</td>");
    w.  write("</tr>");
    w.write("</table>") ;
  }
}