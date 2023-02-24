/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import java.util.*;
import java.io.IOException;
import java.io.Writer   ;
import javax.portlet.* ;

import org.apache.commons.logging.Log;
import org.exoplatform.services.wsrp.consumer.*;
import org.exoplatform.services.wsrp.type.*;
/*
 * A part of the business logic of this portlet was taken from the WSRP4J project
 * @author  Tuan Nguyne
 *          tuan08@users.sourceforge.net
 * Tue, Feb 24, 2004 @ 14:35
 */
public class UIProducer {
  private Log log_ ;
  private boolean visible_ ;
  private Producer producer_ ;
  private  PortletDescription[]  offeredPortlets_ ;

  public UIProducer(Producer producer, Log log) throws Exception {
    producer_ = producer ;
    ServiceDescription desc = producer_.getServiceDescription() ;
    offeredPortlets_ = desc.getOfferedPortlets() ;
    log_ = log ;
  }

  public String getId() { return producer_.getID() ; }

  public void setVisible(boolean b) { visible_ = b ; }
  public boolean isVisible() { return visible_  ; }

  public void processAction(ActionRequest request, ActionResponse response) throws Exception {
  }

  public void render(RenderRequest request, RenderResponse response, ResourceBundle res) throws PortletException , IOException {
    Writer w = response.getWriter() ;
    String baseURL = response.createActionURL().toString() ;
    String producerDesc = producer_.getDescription() ;
    if (producerDesc == null) producerDesc = "" ;
    w.write("<table class='UIProducer'>") ;
    w.  write("<tr>") ;
    w.    write("<th colspan='3' height='30'>") ;
    writeProducerLink(w, baseURL) ;
    w.    write("<br/>" + producerDesc) ;
    w.    write("</th>") ;
    w.  write("</tr>") ;
    if (visible_) {
      w.  write("<tr>") ;
      w.    write("<th>") ; w.write(res.getString("UIProducer.header.portlet-title")); w.write("</th>") ;
      w.    write("<th width='*'>") ; w.write(res.getString("UIProducer.header.portlet-description")); w.write("</th>") ;
      w.    write("<th>") ; w.write("-"); w.write("</th>") ;
      w.  write("</tr>") ;
      if(offeredPortlets_ != null){
        for(int i = 0 ; i < offeredPortlets_.length ; i++) {
          PortletDescription pdesc = offeredPortlets_[i] ;
          String clazz = "odd";
          if (i % 2 == 0)
            clazz = "even";
    	    w.write("<tr class='" + clazz + "'>") ;
          w.  write("<td>") ; w.write(getValue(pdesc.getTitle())); w.write("</td>") ;
          w.  write("<td width='*'>") ; w.write(getValue(pdesc.getDescription())); w.write("</td>") ;
          w.  write("<td align='center'>") ;
          writeSelectLink(w, baseURL, pdesc.getPortletHandle(), res) ;
          w.  write("</td>") ;
          w.write("</tr>") ;
        }
      }
    }
    w.write("</table>") ;
  }

  private void writeProducerLink(Writer w,  String baseURL) throws IOException {
    w.write("<a href='" + baseURL + "&producerId=" + producer_.getID() + "&action=selectProducer'>") ;
    w.write(producer_.getName()) ;
    w.write("</a>") ;
  }

  private void writeSelectLink(Writer w, String baseURL, String portletHandle, 
      ResourceBundle res) throws IOException {
    w.write("<a href='" + baseURL + "&producerId=" + producer_.getID() +
            "&portletHandle=" + portletHandle + "&action=selectPortlet'>") ;
    w.write(res.getString("UIProducer.button.select")) ;
    w.write("</a>") ;
  }

  private String getValue(LocalizedString s) {
    if (s == null) return "" ;
    return s.getValue() ;
  }
}
