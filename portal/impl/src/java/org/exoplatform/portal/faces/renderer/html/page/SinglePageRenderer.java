/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.page;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.portal.faces.component.UISinglePage;
import org.exoplatform.portal.session.RequestInfo;

/**
 * Date: Aug 11, 2003
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: SinglePageRenderer.java,v 1.4 2004/09/23 12:44:42 tuan08 Exp $
 */
public class SinglePageRenderer extends HtmlBasicRenderer {

  public SinglePageRenderer() {
  }

  public void decode(FacesContext facesContext, UIComponent uiComponent) {
  }

  public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
  }

  public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    ResponseWriter w = facesContext.getResponseWriter();
    UISinglePage uiSinglePage = (UISinglePage) uiComponent ;
    w.write("<html>\n") ;
    w.  write("<head id='head'>\n");
    w.    write(uiSinglePage.getUserCss());
    w.    write("<link rel='stylesheet' type='text/css' href='/skin/default-skin.css'/>\n");
    w.  write("</head>");
    w.  write("<body style='width: auto; height: auto'>") ;
    Iterator iterator = uiSinglePage.getChildren().iterator();
    while (iterator.hasNext()) {
      UIPortlet uiPortlet = (UIPortlet)iterator.next() ; 
      uiPortlet.encodeBegin(facesContext);
      uiPortlet.encodeChildren(facesContext);
      uiPortlet.encodeEnd(facesContext);
    }
    w.  write("<div width='100%' align='center' style='color: white; font-size: 16pt; font-weight: bold'>") ;
    w.    write("<a href='");
          w.write(rinfo.getContextPath()) ;
          w.write("/bookmark.jsp'>Bookmarks</a>") ;
    w.  write("</div>") ;
    w.  write("</body>") ;
    w.write("</html>") ;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
  }
}
