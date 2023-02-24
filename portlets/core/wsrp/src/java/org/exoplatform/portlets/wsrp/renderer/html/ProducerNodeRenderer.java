/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp.renderer.html;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.wsrp.component.UIProducerMenu;
import org.exoplatform.portlets.wsrp.component.UIProducerNode;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 juin 2004
 */
public class ProducerNodeRenderer extends HtmlBasicRenderer {

  final public static String NO_PRODUCER_ICON = "<img class='no-producer-icon' src='/skin/blank.gif'/>";

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIProducerNode producerNode = (UIProducerNode) component;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter();
    List children = component.getChildren();
    w.write("<table class='UIProducerNode'>");
    w.write("<tr>");
    if (!producerNode.hasProducer()) {
      w.write("<td class='no-producer'>");
      w.write(NO_PRODUCER_ICON + res.getString("UIProducerNode.label.no-producers"));
      w.write("</td>");
    } else {
      for (int i = 0; i < children.size(); i++) {
        UIComponent uiChild = (UIComponent) children.get(i);
        if (uiChild instanceof UIProducerMenu) {
          w.write("<td class='UIProducerMenu'>");
        } else {
          w.write("<td width='*'>");
        }
        uiChild.encodeBegin(context);
        uiChild.encodeChildren(context);
        uiChild.encodeEnd(context);
        w.write("</td>");
      }
    }
    w.write("</tr>");
    w.write("</table>");
  }

}
