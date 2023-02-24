/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.log.renderer.html;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.log.component.UILogMessages;
import org.exoplatform.services.log.LogMessage;
import org.exoplatform.services.log.impl.ExoLog;
import java.io.IOException;


public class LogMessagesRenderer extends HtmlBasicRenderer {
  private static String SCRIPT = 
    "<script type='text/javascript'>\n" +
    "  function showLogDetail(id, button) {\n" +
    "    var block = document.getElementById(id);\n" +
    "    if(block.style.display == 'none') { \n" +
    "      block.style.display = 'block' ; \n" +
    "    } else {\n" +
    "      block.style.display = 'none' ; \n" +
    "    } \n" +
    "  }\n" +
    "</script>\n" ;

  protected String levelAsString( int level ) {
    if (ExoLog.FATAL == level) return "FATAL";
    else if (ExoLog.ERROR == level) return "ERROR";
    else if (ExoLog.WARN == level) return "WARNING";
    else if (ExoLog.INFO == level) return "INFO";
    else if (ExoLog.DEBUG == level) return "DEBUG";
    else if (ExoLog.TRACE == level) return "TRACE";
    return null;
  }

  public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    UILogMessages uiLogMessages = (UILogMessages) component;
    ResponseWriter w = context.getResponseWriter() ;
    //String actionURL = context.getExternalContext().encodeActionURL(null)  ;
    w.write("<table class='UILogMessages'>\n");
    w.write(SCRIPT);
    List list = uiLogMessages.getLogMessages() ;
    for (int i = 0 ; i < list.size(); i++) {
      LogMessage lm = (LogMessage) list.get(i);
      String detail = lm.getDetail() ;
      w.write("<tr>");
      w.  write("<td>"); w.write(lm.getName()); w.write("</td>");
      w.  write("<td>");
      w.    write("[" + levelAsString( lm.getType()) + "]");
      w.  write("</td>");
      w.  write("<td width='100%'>") ;
      if (detail == null) {
        w.    write(lm.getMessage());
      } else {
        w.    write("<a href=\"javascript:showLogDetail('msg-"+ i +"', this)\">") ;
        w.      write(lm.getMessage()) ;
        w.    write("</a>");
        w.    write("<div id='msg-" + i + "' style='display: none'>") ;
        w.      write("<pre>") ;
        w.        write(detail);
        w.      write("</pre>") ;
        w.    write("</div>") ;
      }	
      w.  write("</td>");
      w.write("</tr>\n");
    }
    w.write("</table>");
  }
}