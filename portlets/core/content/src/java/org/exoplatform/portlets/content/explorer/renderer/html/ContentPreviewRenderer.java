/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.renderer.html;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.commons.utils.HtmlUtil;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.SimpleFormRenderer;
import org.exoplatform.portlets.content.explorer.component.UIContentEditor;
import java.io.IOException;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ContentPreviewRenderer.java,v 1.1 2004/07/16 09:51:34 oranheim Exp $
 */
public class ContentPreviewRenderer extends SimpleFormRenderer { 
  private static Parameter[] editCurrentParams_ = 
    { new Parameter(FacesConstants.ACTION , UIContentEditor.EDIT_CURRENT_CONTENT_ACTION) };

  final public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    UIContentEditor uiEditor = (UIContentEditor) component ;
    String content = uiEditor.getContent() ;
    String mime = uiEditor.getContentType() ;
    if ("text/plain".equals(mime)) content = "<pre>" + content + "</pre>" ;
    else if ("text/xml".equals(mime)) content = "<pre>" + HtmlUtil.showXmlTags(content) + "</pre>";
    w.write("<table width='100%'>");
    w.  write("<tr>");
    w.    write("<td style='padding: 10px 10px 10px 10px' valign='top'>");
    w.      write(content);
    w.    write("</td>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td align='center' valign='center'>");
    linkRenderer_.render(w,uiEditor, "Back" , editCurrentParams_) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }
}