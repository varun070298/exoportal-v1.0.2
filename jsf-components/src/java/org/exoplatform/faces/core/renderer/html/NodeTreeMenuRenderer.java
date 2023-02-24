/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.util.List ;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.Node;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.model.Parameter;


public  class NodeTreeMenuRenderer extends NodeMenuRenderer {
  
  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UINode uiNode = (UINode) component ; 
    
    ExternalContext eContext = context.getExternalContext() ;
    String baseURL = eContext.encodeActionURL("") ;
    ResponseWriter w = context.getResponseWriter() ;
    String clazz = uiNode.getClazz() ;
    w.write("<table class='") ; w.write(clazz) ; w.write("'>") ; 
    w.write("<tr>");
    w.	write("<td class='UITreeMenu'>") ;
    w.		write("<div>"); w.write(uiNode.getName()); w.write("</div>");
    renderNode(w, uiNode, baseURL) ;
    w.	write("</td style='vertical-align: top;'>") ;
    w.	write("<td>") ;
    List children = uiNode.getChildren() ;
    for(int i = 0; i < children.size(); i++) {
    	UIComponent uiChild = (UIComponent) children.get(i);
    	if(uiChild.isRendered()) {
    		uiChild.encodeBegin(context) ;
    		uiChild.encodeChildren(context) ;
    		uiChild.encodeEnd(context) ;
    	}
    }
    w.	write("</td>");
    w.write("</tr>");
    w.write("</table>");
  }
  
  public void renderNode(ResponseWriter w, Node node, String baseURL) throws IOException {
    List children = node.getChildren();
    if (children.size() == 0) return;
    Parameter nodeIdParam = new Parameter("tabId", "");
    Parameter[] changeNodeParams = {SELECT_TAB, nodeIdParam};
    w.write("<ul>");
    for (int i = 0; i < children.size(); i++) {
    	Object o = children.get(i) ;
    	if (o instanceof Node) {
    		Node child = (Node) o;
        w.write("<li>");
      	nodeIdParam.setValue(child.getId());
      	appendLink(w, child.getName(), baseURL, changeNodeParams, "");
      	if (child.getChildren().size() > 0) {
      		renderNode(w, child, baseURL);
      	}
      	w.write("</li>");
    	}
    }
    w.write("</ul>");
  }
}	