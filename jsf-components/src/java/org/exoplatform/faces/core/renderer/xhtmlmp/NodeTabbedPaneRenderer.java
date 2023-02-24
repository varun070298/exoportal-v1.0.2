/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.xhtmlmp;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.Node;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.faces.core.component.model.Parameter;


public  class NodeTabbedPaneRenderer 
	extends org.exoplatform.faces.core.renderer.html.NodeTabbedPaneRenderer {
  
  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UINode uiNode = (UINode) component ; 
    Parameter  tabIdParam = new Parameter("tabId" , "");
    Parameter[] params = {SELECT_TAB , tabIdParam} ;

    List children = uiNode.getChildren() ;
    if(children.size() == 0 ) return ;
    UIComponent uiSelectTab = null ;
    ExternalContext eContext = context.getExternalContext() ;
    String baseURL = eContext.encodeActionURL(null) ;
    ResponseWriter w = context.getResponseWriter() ;
    String clazz = uiNode.getClazz() ;
    //w.write("<div class='") ; w.write(clazz) ; w.write("'>") ; 
    if (children.size() > 1) {
      //w.write("<div>") ; 
    	for (int i = 0; i < children.size(); i++) {
    		UIComponent child = (UIComponent) children.get(i) ;
    		Node tab = (Node) child ;
    		tabIdParam.setValue(child.getId()) ;
    		if (child.isRendered()) {
    			uiSelectTab = child ;
    			appendLink(w, tab.getName(), baseURL, params, "") ;
    		} else {
    			appendLink(w, tab.getName(), baseURL, params, "") ;
    		}
    	}
    	//w.write("</div>") ; 
    } else {
			uiSelectTab = (UIComponent) children.get(0) ;
    }
    uiSelectTab.encodeBegin(context) ;
    uiSelectTab.encodeChildren(context) ;
    uiSelectTab.encodeEnd(context) ;
    //w.write("</div>");
  }
}