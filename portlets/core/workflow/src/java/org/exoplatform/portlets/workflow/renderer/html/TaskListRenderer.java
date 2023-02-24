/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.renderer.html;

import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.workflow.component.UITaskList;
import org.exoplatform.portlets.workflow.component.UIWorkflowPortlet;
import org.jbpm.model.definition.Definition;
import org.jbpm.model.execution.Token;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 mai 2004
 */
public class TaskListRenderer extends HtmlBasicRenderer {

  private static Parameter MANAGE_STATE = new Parameter(FacesConstants.ACTION, "manageState");

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UITaskList uiTaskList = (UITaskList) component;
    String baseURL = uiTaskList.getBaseURL(context);
    ResponseWriter w = context.getResponseWriter();
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext());
    String remoteUser = context.getExternalContext().getRemoteUser();
    if (remoteUser == null)
      remoteUser = UIWorkflowPortlet.ANONYMOUS_USER;
    Collection bps = uiTaskList.getTaskList(remoteUser);
    w.write("<div align='center'>");
    w.write("<table class='UITaskList'>");
    w.write("<tr>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.icon"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.id"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.name"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.description"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.definition"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.started"));
    w.write("</th>");
    w.write("<th>");
    w.write(res.getString("UITaskList.header.manage"));
    w.write("</th>");
    if (bps == null)
      return;
    if (bps.isEmpty()) {
      w.write("<tr>");
      w.write("<td>");
      w.write("<img src='/workflow/images/empty.gif' />");
      w.write("</td>");
      w.write("<td colspan='6'>");
      w.write(res.getString("UITaskList.empty.task.list"));
      w.write("</td>");
      w.write("</tr>");
    } else {
      Iterator dataIterator = bps.iterator();
      int i = 0;
      while (dataIterator.hasNext()) {
        Token token = (Token) dataIterator.next();
        Definition def = token.getState().getDefinition();
        String clazz = "odd";
        if (i % 2 == 0)
          clazz = "even";
        w.write("<tr class='" + clazz + "'>");
        w.write("<td>");
        w.write("<img src='/workflow/images/activity.gif' />");
        w.write("</td>");
        w.write("<td>");
        w.write(token.getId().toString());
        w.write("</td>");
        w.write("<td>");
        w.write(token.getState().getName());
        w.write("</td>");
        String description = token.getState().getDescription();
        if (description == null)
          description = "N/A";
        w.write("<td>");
        w.write(description);
        w.write("</td>");
        w.write("<td>");
        w.write(def.getName() + " (" + def.getId() + ")");
        w.write("</td>");
        w.write("<td>");
        w.write(new SimpleDateFormat().format(token.getStart()));
        w.write("</td>");
        w.write("<td>");
        Parameter processId = new Parameter("token", token.getId().toString());
        Parameter[] resolveProcessParams = {MANAGE_STATE, processId};
        appendLink(w, res.getString("UITaskList.label.manage"), baseURL, resolveProcessParams, null);
        w.write("</td>");
        w.write("</tr>");
        i++;
      }
    }
    w.write("</table>");
    w.write("</div>");
  }

}