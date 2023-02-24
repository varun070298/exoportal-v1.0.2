/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import org.exoplatform.commons.debug.ObjectDebuger;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.jmx.component.UIOperation;

/**
 * Jul 29, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: OperationRenderer.java,v 1.2 2004/08/01 18:56:11 tuan08 Exp $
 */
public class OperationRenderer extends HtmlBasicRenderer{
	public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
		UIOperation uiOperation = (UIOperation) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    MBeanOperationInfo operationInfo =  uiOperation.getMBeanOperationInfo() ;
    Object result = uiOperation.getResult() ;
    String returnData = "" ;
    if(result != null) returnData = ObjectDebuger.asString(result) ; 
    ResponseWriter w = context.getResponseWriter() ;
    String baseURL = uiOperation.getBaseURL(context) ;
    w.write("<form name='operation' action='"); w.write(baseURL); w.write("' method='post'>\n") ;
    w.write("<input type='hidden'  name='"+ ACTION + "' value=''/>") ;
    w.write("<table class='"); w.write(uiOperation.getClazz()); w.write("'>") ;
    w.  write("<tr>") ;
    w.    write("<th colspan='2'>"); 
    w.      write(res.getString("UIOperation.header.operation"));  w.write(operationInfo.getName()); 
    w.    write("</th>") ;
    w.  write("</tr>"); 
    w.  write("<tr>") ;
    w.    write("<td>");w.write(res.getString("UIOperation.label.description"));w.write("</td>") ;
    w.    write("<td>"); w.write(operationInfo.getDescription()); w.write("</td>") ;
    w.  write("</tr>"); 
    w.  write("<tr>") ;
    w.    write("<td>");w.write(res.getString("UIOperation.label.return-type"));w.write("</td>") ;
    w.    write("<td>"); w.write(operationInfo.getReturnType()); w.write("</td>") ;
    w.  write("</tr>"); 
    MBeanParameterInfo[] params = operationInfo.getSignature() ;
    for(int i = 0 ; i < params.length; i++) {
      String index = Integer.toString(i) ;
    	w.write("<tr>") ;
    	w. write("<td><label>");w.write(res.getString("UIOperation.label.parameter")); w.write(index); w.write("</label></td>") ;
      w. write("<td><input name='parameter' value=''/></td>") ;
    	w.write("</tr>"); 
    }
    w. write("<tr>") ;
    w.   write("<td colspan='2' align='center'>") ;
    w.     write("<a href=\"javascript:submitOperation('" + UIOperation.EXECUTE_ACTION + "')\">" + res.getString("UIOperation.button.execute") + "</a>") ;
    w.     write("<a href=\"javascript:submitOperation('" + CANCEL_ACTION + "')\">" + res.getString("UIOperation.button.cancel") + "</a>") ;
    w.   write("</td>") ;
    w.write("</tr>"); 
    w.  write("<tr>") ;
    w.    write("<td colspan='2'>"); 
    w.      write("<pre>"); w.write(returnData); w.write("</pre>"); 
    w.    write("</td>") ;
    w.  write("</tr>"); 
    w. write(getScript()) ;
    w.write("</table>") ; 
    w.write("</form>") ;
	}	
  
   private String getScript() {
    String script = 
     "<script type=\"text/javascript\">\n" +
     "  function  submitOperation(action) {\n" +
     "    document.operation.elements['"+ ACTION + "'].value =  action ;\n" +
     "    document.operation.submit();\n" +
     "  }\n" +
     "</script>\n" ;
     return script ;
  }
}