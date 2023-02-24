/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.renderer.html;

import java.util.* ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.monitor.portlet.component.UIPortletMenu;
import java.io.IOException;

/**
 * Apr 28, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletContainerMonitorRenderer.java,v 1.3 2004/06/03 22:58:59 tuan08 Exp $
 **/
public class PortletContainerMonitorRenderer extends HtmlBasicRenderer {

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    List children = component.getChildren() ;    
    w.write("<table class='UIPortletContainerMonitor'>") ;
    w.  write("<tr>") ;
    for(int i = 0; i < children.size(); i++) {
      UIComponent uiChild = (UIComponent) children.get(i);
    	if (uiChild instanceof UIPortletMenu) {
    		w.write("<td class='UIPortletMenu'>") ;
    	} else {
     		w.write("<td width='*'>") ;
     	}
     	uiChild.encodeBegin(context) ;
     	uiChild.encodeChildren(context) ;
     	uiChild.encodeEnd(context) ;
     	w.  write("</td>") ;
    }
    w.  write("</tr>") ;
    w.write("</table>") ;
  }
}