/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle ;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.faces.core.Util ;
import org.exoplatform.faces.application.ExoFacesMessage;
/**
 * Jun 9, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Information.java,v 1.5 2004/09/21 03:41:33 tuan08 Exp $
 */
public class Information {
  private List messages_ ;
  
  public Information() {
    messages_ = new ArrayList(5) ;
  }
  
  public boolean hasMessage() {  return messages_.size() > 0 ;}
  public List getMessages() { return messages_ ; }
  public void addMessage(FacesMessage  message) { messages_.add(message) ; }
  public void clearMessages() { messages_.clear() ; }
  
  static public void renderBodyInformation(FacesContext context, InformationProvider iprovider) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    List messages = iprovider.getMessages() ;
    ResourceBundle res = Util.getApplicationResourceBundle();
    w.write("<table width='100%'>") ;
    for (int i = 0; i < messages.size() ; i++) {
      ExoFacesMessage  m = (ExoFacesMessage) messages.get(i) ;
      String detail = m.getDetail(res) ;
      if(detail == null) detail = "" ;
      w.write("<tr>") ;
      if(m.getSeverity() == FacesMessage.SEVERITY_ERROR) {
        w.write("<td class='portlet-msg-error'>") ;
        w.  write("error: ") ; w.write(m.getSummary(res)) ; w.write("<br/>") ;
      } else if (m.getSeverity() == FacesMessage.SEVERITY_WARN) {
        w.write("<td class='portlet-msg-alert'>") ;
        w. write("warning: ") ; w.write(m.getSummary(res)) ; w.write("<br/>") ;
      } else {
        w.write("<td class='portlet-msg-info'>") ;
        w. write("info: ") ; w.write(m.getSummary(res)) ;w.write("<br/>") ;
      }
      w.   write(detail) ;
      w.  write("</td>") ;
      w.write("</tr>") ;
    }
    w.  write("<tr>") ;
    w.    write("<td align='center'>") ;
    String baseUrl = HtmlBasicRenderer.getBaseURL(context);
    HtmlBasicRenderer.appendLink(w, "Back", baseUrl, HtmlBasicRenderer.EMPTY_PARAMS, null) ;
    w.    write("<td>") ;
    w.  write("</tr>") ;
    w.write("</table>") ;
    iprovider.clearMessages() ;
    iprovider.setDisplayMessageType(InformationProvider.FOOTER_MESSAGE_TYPE) ;
  }
  
  
  static public void renderFooterInformation(FacesContext context, InformationProvider iprovider) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    List messages = iprovider.getMessages() ;
    ResourceBundle res = Util.getApplicationResourceBundle();
    w.write("<div style='color: red;'>") ;
    for (int i = 0; i < messages.size() ; i++) {
      try {
      ExoFacesMessage m = (ExoFacesMessage) messages.get(i) ;
      if(m.getSeverity() == FacesMessage.SEVERITY_ERROR) {
        w.write("error: ") ; w.write(m.getSummary(res)) ; w.write("<br/>") ;
      } else if (m.getSeverity() == FacesMessage.SEVERITY_WARN) {
        w.write("warning: ") ; w.write(m.getSummary(res)) ; w.write("<br/>") ;
      } else {
        w.write("info: ") ; w.write(m.getSummary(res)) ;w.write("<br/>") ;
      }
      } catch (Throwable t) { t.printStackTrace() ;}
    }
     w.write("</div>") ;
    iprovider.clearMessages() ;
    iprovider.setDisplayMessageType(InformationProvider.FOOTER_MESSAGE_TYPE) ;
  }
}
