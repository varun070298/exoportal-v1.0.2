/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.renderer.html;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.portletregistery.component.UIPortletRole;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juin 2004
 */
public class PortletRoleRenderer extends HtmlBasicRenderer{

  final public static String ADD_ROLE_ICON = "<img class='add-role-icon' src='/skin/blank.gif'/>";
  final public static String REMOVE_ROLE_ICON = "<img class='remove-role-icon' src='/skin/blank.gif'/>";

  private static String SCRIPT =
    "<script>\n" +
      "function submitAddPortletRoleForm(action) {\n" +
      "  document.portletRoleForm.elements['"+FacesConstants.ACTION +"'].value = action ;\n" +
      "  document.portletRoleForm.submit();\n" +
      "}\n" +
    "</script>\n" ;

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIPortletRole uiPortletRole = (UIPortletRole) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    String baseURL = uiPortletRole.getBaseURL(context);
    Collection availableRoles = uiPortletRole.getAvailableRoles() ;
    Collection currentRoles = uiPortletRole.getCurrentRoles();
    w.write("<center>") ;
    w.  write("<form name='portletRoleForm' action='"+baseURL+"' method='post'>") ;
    w.    write("<input type='hidden' name='"+ FacesConstants.ACTION +"' value=''/>");
    w.    write("<table class='UIPortletRole'>");
    w.      write("<tr>");
    w.        write("<th colspan='3'>");
    w.          write(res.getString("UIPortletRole.header.title")) ;
    w.        write("</th>");
    w.      write("</tr>");
    w.      write("<tr>");
    w.        write("<td>");
    w.          write(res.getString("UIPortletRole.label.available-roles")) ;
    w.        write("</td>");
    w.        write("<td>");
    w.          write("&nbsp;&nbsp;") ;
    w.        write("</td>");
    w.        write("<td>");
    w.          write(res.getString("UIPortletRole.label.current-roles")) ;
    w.        write("</td>");
    w.      write("</tr>");
    w.    write("<tr>") ;
    w.      write("<td>") ;
    w.        write("<select multiple size='5' name='" + UIPortletRole.AVAILABLE_ROLE_SELECT + "'>") ;
    if(availableRoles != null){
      for (Iterator iterator = availableRoles.iterator(); iterator.hasNext();) {
        String availableRole = (String) iterator.next();
        w.      write("<option>" + availableRole + "</option>");
      }
    }
    w.        write("</select>") ;
    w.      write("</td>");
    w.      write("<td class='arrows'>") ;
    w.        write("<div>");
    w.          write("<a href=\"javascript:submitAddPortletRoleForm('"+ UIPortletRole.ADD_ROLE +"');\">");
    w.            write(ADD_ROLE_ICON);
    w.          write("</a> ");
    w.        write("</div>");
    w.        write("<div>");
    w.          write("<a href=\"javascript:submitAddPortletRoleForm('"+UIPortletRole.REMOVE_ROLE +"');\">");
    w.            write(REMOVE_ROLE_ICON);
    w.          write("</a> ");
    w.        write("</div>");
    w.      write("</td>") ;
    w.      write("<td>") ;
    w.        write("<select multiple size='5' name='" + UIPortletRole.CURRENT_ROLE_SELECT + "'>") ;
    if(currentRoles != null){
      for (Iterator iterator = currentRoles.iterator(); iterator.hasNext();) {
        String currentRole = (String) iterator.next();
        w.      write("<option>" + currentRole + "</option>");
      }
    }
    w.        write("</select>") ;
    w.      write("</td>");
    w.    write("</tr>");
    w.      write("<tr>");
    w.        write("<td align='center' colspan='3'>") ;
    w.          write("<a href=\"javascript:submitAddPortletRoleForm('"+SAVE_ACTION +"');\">");
    w.            write(res.getString("UIPortletRole.button.save"));
    w.          write("</a> ");
    w.          write("<a href=\"javascript:submitAddPortletRoleForm('"+CANCEL_ACTION +"');\">");
    w.            write(res.getString("UIPortletRole.button.cancel"));
    w.          write("</a>");
    w.        write("</td>") ;
    w.      write("</tr>");
    w.    write("</table>");
    w.    write(SCRIPT);
    w.  write("</form>");
    w.write("</center>");
  }
}
