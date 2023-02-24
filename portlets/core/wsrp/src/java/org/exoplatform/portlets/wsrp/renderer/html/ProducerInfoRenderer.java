/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.wsrp.renderer.html;

import java.util.* ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.wsrp.component.*;
import org.exoplatform.portlets.wsrp.component.model.ProducerData;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.CookieProtocol;
import org.exoplatform.services.wsrp.type.ServiceDescription;
import java.io.IOException;


public class ProducerInfoRenderer extends HtmlBasicRenderer {

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIProducerInfo uiProducerInfo  = (UIProducerInfo) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    ProducerData producerData = uiProducerInfo.getProducerData() ;
    Producer producer = producerData.getProducer() ;
    ServiceDescription desc = null;
    try {
      desc = producer.getServiceDescription();
    } catch (WSRPException e) {
      e.printStackTrace();
    }
    w.write("<table class='UIProducerInfo'>") ;
    w.  write("<tr>") ;
    w.    write("<th colspan='2'>"); w.write(res.getString("UIProducerInfo.label.producer-properties")) ; w.write("</th>") ;
    w.  write("</tr>") ;
    w.write("<tr>") ;
    w.  write("<td class='label'>"); w.write(res.getString("UIProducerInfo.label.producer-name")) ; w.write("</td>") ;
    w.  write("<td>"); w.write(producerData.getProducerName()) ; w.write("</td>") ;
    w.write("</tr>") ;
    w.write("<tr>") ;
    w.  write("<td class='label'>"); w.write(res.getString("UIProducerInfo.label.requires-registration")) ; w.write("</td>") ;
    String answer = "N/A";
    if(desc != null)
      answer = Boolean.toString(desc.isRequiresRegistration());
    w.  write("<td>"); w.write(answer) ; w.write("</td>") ;
    w.write("</tr>") ;
    w.write("<tr>") ;
    w.  write("<td class='label'>"); w.write(res.getString("UIProducerInfo.label.requires-init-cookie")); w.write("</td>") ;
    answer = "none";
    if(desc != null) {
      CookieProtocol cookie = desc.getRequiresInitCookie();
      if(cookie != null)
        answer = cookie.getValue();
    }
    w.  write("<td>"); w.write(answer) ; w.write("</td>") ;
    w.write("</tr>") ;
    w.write("</table>") ;
  }
}
