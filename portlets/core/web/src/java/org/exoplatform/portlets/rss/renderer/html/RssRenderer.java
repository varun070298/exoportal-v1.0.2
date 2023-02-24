/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.rss.renderer.html;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.rss.component.*;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: RssRenderer.java,v 1.4 2004/09/02 19:26:10 benjmestrallet Exp $
 */
public class RssRenderer extends  HtmlBasicRenderer {

    private static Parameter[] updatePortletParams =
  	{ new Parameter(FacesConstants.ACTION , UIRss.UPDATE_ACTION) };

  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    String baseURL = context.getExternalContext().encodeActionURL(null) ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    UIRss uiRss = (UIRss) component ;
    Channel channel = uiRss.getChannel() ;
    Item[] items = channel.getItems() ;
    w.write("<div class='UIRss'>") ;
    w.  write("<div class='title'>");
    w.    write(channel.getTitle());
    w.  write("</div>");
    w.  write("<div class='body'>");
    w.    write("<ul>");
    for (int i = 0; i < items.length ; i++ ) {
      w.    write("<li style='text-align: justify'>") ;
      w.      write("<a href='" +items[i].getLink() + "'>");
      w.        write(items[i].getTitle());
      w.      write("</a>") ;
      w.      write("<div class='description'>") ;
      w.        write(items[i].getDescription()) ;
      w.      write("</div>");
      w.    write("</li>");
    }
    w.    write("</ul>");
    w.  write("</div>");
    w.  write("<div class='footer'>");
    linkRenderer_.render(w, uiRss, res.getString("UIRss.img.reload-icon"),updatePortletParams);    
    w.  write("</div>");
    w.write("</div>");
  }
}