/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp.renderer.html;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.wsrp.component.UIProducerMenu;
import org.exoplatform.portlets.wsrp.component.model.ProducerData;
import org.exoplatform.services.wsrp.type.PortletDescription;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 juin 2004
 */
public class ProducerMenuRenderer extends HtmlBasicRenderer {

  private static Parameter SELECT_PRODUCER = new Parameter(FacesConstants.ACTION, "selectProducer");
  private static Parameter SHOW_PRODUCER = new Parameter(FacesConstants.ACTION, "showProducer");
  private static Parameter SHOW_PORTLET = new Parameter(FacesConstants.ACTION, "showPortlet");
  final public static String EXPAND = "<img class='expand' src='/skin/blank.gif'/>";
  final public static String COLLAPSE = "<img class='collapse' src='/skin/blank.gif'/>";

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIProducerMenu uiMenu = (UIProducerMenu) component;
    ResponseWriter w = context.getResponseWriter();
    String baseURL = context.getExternalContext().encodeActionURL(null);
    Parameter producerParam = new Parameter("producerName", "");
    Parameter portletParam = new Parameter("portletName", "");
    Parameter[] selectProducersParams = {SELECT_PRODUCER, producerParam};
    Parameter[] showProducerParams = {SHOW_PRODUCER, producerParam};
    Parameter[] showPortletMonitorParams = {SHOW_PORTLET, producerParam, portletParam};
    Collection producerDatas = uiMenu.getProducers();
    Iterator prodIterator = producerDatas.iterator();
    w.write("<table>");
    while (prodIterator.hasNext()) {
      ProducerData prodData = (ProducerData) prodIterator.next();
      String prodpName = prodData.getProducerName();
      producerParam.setValue(prodpName);
      String sign = EXPAND;
      if (prodData.isSelect()) sign = COLLAPSE;
      w.write("<tr>");
      w.write("<th>");
      appendLink(w, sign, baseURL, selectProducersParams, "");
      appendLink(w, prodpName, baseURL, showProducerParams, "");
      w.write("</th>");
      w.write("</tr>");
      if (prodData.isSelect()) {
        PortletDescription[] portletDescriptions = prodData.getOfferedPortlets();
        if (portletDescriptions != null) {
          for (int i = 0; i < portletDescriptions.length; i++) {
            PortletDescription portletDescription = portletDescriptions[i];
            String portletName = portletDescription.getDisplayName().getValue();
            portletParam.setValue(portletDescription.getPortletHandle());
            w.write("<tr>");
            w.write("<td>");
            appendLink(w, portletName, baseURL, showPortletMonitorParams, "");
            w.write("</td>");
            w.write("</tr>");
          }
        }
      }
    }
    w.write("</table>");
  }
}
