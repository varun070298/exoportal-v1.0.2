/**
 * Copyright 2001-2004 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.portlets.communication.sms.renderer.html;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle ;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.communication.sms.component.UISmsMonitor;


/**
 * @author Ove Ranheim (oranheim@yahoo.no)
 * @since 24.jun.2004 22:05:59
 */
public class SmsMonitorRenderer extends HtmlBasicRenderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        UISmsMonitor uiMonitor = (UISmsMonitor) component;
        uiMonitor.updateCounter();
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter w = context.getResponseWriter();
        UISmsMonitor uiMonitor = (UISmsMonitor) component;
        ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
        String clazz = uiMonitor.getClazz();
        w.write("<div align='center'>");
        w.write("<table");
        if (clazz != null) {
            w.write(" class='");
            w.write(clazz);
            w.write("'");
        }
        w.write(">\n");
        List rows = uiMonitor.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Row row = (Row) rows.get(i);
            row.render(w,res,  uiMonitor);
        }
        w.write("</table>");
        w.write("</div>");
    }

}