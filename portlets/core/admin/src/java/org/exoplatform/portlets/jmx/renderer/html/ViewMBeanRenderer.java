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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.jmx.component.UIMBean;


/**
 * Jul 29, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ViewMBeanRenderer.java,v 1.3 2004/08/28 18:59:20 tuan08 Exp $
 */
public class ViewMBeanRenderer extends HtmlBasicRenderer {
  private static Parameter executeParam = new Parameter(ACTION , UIMBean.EXECUTE_ACTION) ;
  
	public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
		UIMBean uiMBean = (UIMBean) component ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
		MBeanInfo minfo = uiMBean.getMBeanInfo() ;
		ResponseWriter w = context.getResponseWriter() ;
		MBeanAttributeInfo[] attrs = minfo.getAttributes();
    ObjectName name = uiMBean.getObjectName() ;
    MBeanServer server =  uiMBean.getMBeanServer();
		try {
			w.write("<table class='"); w.write(uiMBean.getClazz()); w.write("'>") ;
      w.  write("<tr>") ;
      w.    write("<th colspan='2'>") ; w.write(res.getString("UIMBean.header.mbean"));  w.write("</th>") ;
      w.  write("</tr>") ;
      w.  write("<tr>") ;
      w.    write("<td>") ; w.write(res.getString("UIMBean.label.type"));  w.write("</td>") ;
      w.    write("<td>") ; w.write(minfo.getClassName());  w.write("</td>") ;
      w.  write("</tr>") ;
      w.  write("<tr>") ;
      w.    write("<td>") ; w.write(res.getString("UIMBean.label.description"));  w.write("</td>") ;
      w.    write("<td>") ; w.write(ft_.format(minfo.getDescription())); w.write("</td>") ;
      w.  write("</tr>") ;
      w.  write("<tr>") ;
      w.    write("<td>") ; w.write(res.getString("UIMBean.label.dependencies"));  w.write("</td>") ;
      w.    write("<td>") ; w.write(getDependencies(minfo)); w.write("</td>") ;
      w.  write("</tr>") ;
      w.  write("<tr>") ;
      w.    write("<td colspan='2'>") ;
      for (int i = 0; i < attrs.length; i++) {
      	Object value = "" ;
      	if (attrs[i].isReadable()) {
      		value = server.getAttribute(name, attrs[i].getName());
      	}
      	renderAttribute(w, attrs[i], value) ;
      }
      w.    write("</td>") ;
      w.  write("</tr>") ;
      MBeanOperationInfo[] methods = minfo.getOperations();
      w.  write("<tr>") ;
      w.    write("<td colspan='2'>") ;
      for (int j = 0; j < methods.length; j++) {
        renderOperation(w, uiMBean, methods[j], res) ;
      }
      w.    write("</td>") ;
      w.  write("</tr>") ;
			w.write("</table>") ;
		} catch (Exception x) {
			x.printStackTrace();
		}
	}	
  
	private void renderAttribute(ResponseWriter w, 
                               MBeanAttributeInfo attr, Object value) throws Exception {
    if(value == null) value = "" ;
    w.write("<div>") ;
    w. write("Attribute Name: "); w.write(attr.getName()) ;
    w. write("<br/>Attribute Type: "); w.write(attr.getType()) ;
    w. write("<br/>Attribute Readable: "); w.write(Boolean.toString(attr.isReadable())) ;
    w. write("<br/>Attribute Writable: "); w.write(Boolean.toString(attr.isWritable())) ;
    w. write("<br/>Attribute Value: "); w.write(value.toString()) ;
    w.write("</div>") ;
	}
  
  private void renderOperation(ResponseWriter w, UIMBean uiMBean,
                               MBeanOperationInfo method, ResourceBundle res) throws Exception {
    w.write("<table class='method'>") ;
    w. write("<tr>") ;
    w.  write("<td>" + res.getString("UIMBean.label.method")); w.write(method.getName()) ;w.write("</td>") ;
    w.  write("<td style='text-align: right; border: none'>");
    if(uiMBean.canExecute(method)) {
      Parameter[] params = {executeParam, new Parameter("methodId" , Integer.toString(method.hashCode()))} ;
    	linkRenderer_.render(w, uiMBean, res.getString("UIMBean.button.execute"), params) ;
    } else {
      w.  write(res.getString("UIMBean.label.na"));
    }
    w.  write("</td>") ;
    w. write("</tr>") ;
    w. write("<tr>") ;
    w.  write("<td colspan='2'>" + res.getString("UIMBean.label.parameter") + ":") ;
    MBeanParameterInfo[] params =  method.getSignature() ;
    for(int k = 0 ; k < params.length; k++) {
    	w.write("<li>"); 
      w.write(params[k].getName()); w.write(" - ") ;w.write(params[k].getType()) ;
      w.write("</li>"); 
    }
    w.  write("</td>") ;
    w. write("</tr>") ;
    w. write("<tr>") ;
    w.  write("<td colspan='2'>" + res.getString("UIMBean.label.return-type")); w.write(method.getReturnType()) ; w.write("</td>") ;
    w. write("</tr>") ;
    w.write("</table>") ;
  }
  
  private String getDependencies(MBeanInfo minfo) {
    if(!minfo.getClassName().startsWith("exo")) return "" ;
    StringBuffer b = new StringBuffer() ;
    MBeanConstructorInfo[] constructors = minfo.getConstructors() ;
    MBeanConstructorInfo constructor = null ;
    for(int i = 0; i < constructors.length; i++) {
    	if(constructor == null) constructor = constructors[i] ;
      if(constructor.getSignature().length < constructors[i].getSignature().length) {
        constructor = constructors[i] ;
      }
    }
    if(constructor == null) return "" ;
    MBeanParameterInfo[] params = constructor.getSignature() ;
    for(int i = 0 ; i < params.length; i++) {
    	b.append(params[i].getType()).append(" ") ;
    }
    return b.toString() ;
  }
}