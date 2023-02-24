/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.xhtmlmp.page;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.portal.faces.component.UISinglePage;
import java.io.IOException;
import java.util.Iterator;

/**
 * Date: Jul 02, 2004
 * @author : Moron Fran�ois
 * @email:   francois.moron@rd.francetelecom.com
 * 
 */
public class SinglePageRenderer extends HtmlBasicRenderer {

  public SinglePageRenderer() {
  }

  public void decode(FacesContext facesContext, UIComponent uiComponent) {
  }

  public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
  }

  public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    ResponseWriter w = facesContext.getResponseWriter();
    UISinglePage uiSinglePage = (UISinglePage) uiComponent ;
    w.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
    w.write("<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\"\n");
    w.write("\"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\" >\n\n");
    w.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n") ;
    w.write("<head id='head'>\n");
    w.    write(uiSinglePage.getUserCss());
    w.    write("<link rel='stylesheet' type='text/css' href='/skin/default-skin.css'/>\n");
    w.write("</head>");
    w.write("<body style='width: auto; height: auto'>") ;
    Iterator iterator = uiSinglePage.getChildren().iterator();
    while (iterator.hasNext()) {
      UIPortlet uiPortlet = (UIPortlet)iterator.next() ; 
      uiPortlet.encodeBegin(facesContext);
      uiPortlet.encodeChildren(facesContext);
      uiPortlet.encodeEnd(facesContext);
    }
    w.  write("<div width='100%' align='center' style='color: white; font-size: 16pt; font-weight: bold'>") ;
    w.  write("</div>") ;
    w.  write("</body>") ;
    w.write("</html>") ;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
  }
}
