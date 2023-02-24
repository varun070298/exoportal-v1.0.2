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
import org.exoplatform.portlets.portletregistery.component.UIPortletList;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 juin 2004
 */
public class AddPortletRenderer extends HtmlBasicRenderer {
  private static String SCRIPT =
    "<script>\n" +
      "function submitAddPortletForm(action) {\n" +
      "  document.portletForm.elements['"+FacesConstants.ACTION +"'].value = action ;\n" +
      "  document.portletForm.submit();\n" +
      "}\n" +
    "</script>\n" ;

  final public void encodeChildren(FacesContext context, UIComponent component ) throws IOException {
    UIPortletList uiPortletList = (UIPortletList) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    String baseURL = uiPortletList.getBaseURL(context);
    Map allPortletMetaData = uiPortletList.getAllPortletMetaData() ;
    Set keys = allPortletMetaData.keySet();
    w.write("<center>") ;
    w.  write("<form name='portletForm' action='"+baseURL+"' method='post'>") ;
    w.    write("<input type='hidden' name='"+ FacesConstants.ACTION +"' value=''/>");
    w.    write("<table cellspacing='1' cellpadding='3'>");
    w.      write("<tr>");
    w.        write("<th colspan='3'>");
    w.          write(res.getString("UIPortletList.header.title")) ;
    w.        write("</th>");
    w.      write("</tr>");
    Iterator i = keys.iterator() ;
    while(i.hasNext()) {
      String key = (String) i.next() ;
      PortletData portlet = (PortletData) allPortletMetaData.get(key) ;
      String portletTitle = portlet.getPortletName() ;
      String portletDescription = portlet.getDescription("en") ;
      if(portletDescription == null) portletDescription = "N/A" ;
      w.    write("<tr>") ;
      w.      write("<td width='10'>") ;
      w.        write("<input style='width: 15px' type='checkbox' name='portletId'" +
                            " value='" + key +"'/>") ;
      w.      write("</td>");
      w.      write("<td>") ;
      w.        write(portletTitle) ;
      w.      write("</td>") ;
      w.      write("<td style='padding: 2px 20px 2px 10px'>") ;
      w.        write(portletDescription) ;
      w.      write("</td>") ;
      w.    write("</tr>");
    }
    w.      write("<tr>");
    w.        write("<td align='center' colspan='3'>") ;
    w.          write("<a href=\"javascript:submitAddPortletForm('"+SAVE_ACTION +"');\">");
    w.            write(res.getString("UIPortletList.button.save"));
    w.          write("</a> ");
    w.          write("<a href=\"javascript:submitAddPortletForm('"+CANCEL_ACTION +"');\">");
    w.            write(res.getString("UIPortletList.button.cancel"));
    w.          write("</a>");
    w.        write("</td>") ;
    w.      write("</tr>");
    w.    write("</table>");
    w.    write(SCRIPT);
    w.  write("</form>");
    w.write("</center>");
  }
}