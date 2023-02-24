/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.renderer.html;

import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.workflow.component.UIBPDefinition;
import org.jbpm.model.definition.Definition;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.ArrayList;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 mai 2004
 */
public class BPDefinitionRenderer extends HtmlBasicRenderer {

  private static Parameter MANAGE_START = new Parameter(ACTION, UIBPDefinition.MANAGE_START);

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIBPDefinition uibpDefinition = (UIBPDefinition) component;

    String baseURL = uibpDefinition.getBaseURL(context);

    ResponseWriter w = context.getResponseWriter();
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext());

    Collection bps = new ArrayList();
    try {
      bps = uibpDefinition.getAllDefinition();
    } catch (Exception e) {
      e.printStackTrace() ;
    }
    w.write("<div align='center'>");
    w.write("<table class='UIBPDefinition'>");
    w.write("<tr>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.icon"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.id"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.name"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.description"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.version"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UIBPDefinition.header.start"));
    w.write("</th>");
    if (bps == null)
      return;
    int i = 0;
    if (bps.isEmpty()) {
      w.write("<tr>");
      w.  write("<td>");
      w.  write("<img src='/workflow/images/empty.gif' />");
      w.  write("</td>");
      w.  write("<td colspan='5'>");
      w.  write(res.getString("UIBPDefinition.empty.process.list"));
      w.  write("</td>");
      w.write("</tr>");
    } else {
      Iterator dataIterator = bps.iterator();
      while (dataIterator.hasNext()) {
        Definition definition = (Definition) dataIterator.next();
        String clazz = "odd";
        if (i % 2 == 0)
          clazz = "even";
        Integer version = definition.getVersion();
        if (version == null)
          version = new Integer(-1);
        w.write("<tr class='" + clazz + "'>");
        w.write("<td>");
        if (version.intValue() < 1) {
          w.write("<img src='/workflow/images/no_version.gif' />");
        } else if (version.intValue() == 1) {
          w.write("<img src='/workflow/images/initial_version.gif' />");
        }
        if (version.intValue() > 1) {
          w.write("<img src='/workflow/images/modified_version.gif' />");
        }
        w.write("</td>");
        w.write("<td>");
        w.write(definition.getId().toString());
        w.write("</td>");
        w.write("<td>");
        w.write(definition.getName());
        w.write("</td>");
        String description = definition.getDescription();
        if (description == null)
          description = "N/A";
        w.write("<td>");
        w.write(description);
        w.write("</td>");
        w.write("<td>");
        w.write(version.toString());
        w.write("</td>");
        w.write("<td>");
        Parameter processId = new Parameter("process", definition.getId().toString());
        Parameter[] startProcessParams = {MANAGE_START, processId};
        appendLink(w, res.getString("UIBPDefinition.label.start"), baseURL, startProcessParams, null);
        w.write("</td>");
        w.write("</tr>");
        i++;
      }
    }
    w.write("</table>");
    w.write("</div>");
  }

}
