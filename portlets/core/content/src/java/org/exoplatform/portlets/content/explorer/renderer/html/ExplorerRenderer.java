/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.explorer.renderer.html;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.content.explorer.component.UIExplorer;
import org.exoplatform.portlets.content.explorer.component.model.NodeDescriptor;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExplorerRenderer.java,v 1.3 2004/09/05 22:06:15 tuan08 Exp $
 */
public class ExplorerRenderer extends HtmlBasicRenderer {
  private static Parameter CHANGE_NODE = new Parameter(ACTION , UIExplorer.CHANGE_NODE_ACTION) ;

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIExplorer uiExplorer = (UIExplorer) component;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    String currentUri = uiExplorer.getSelectNode().getUri() ;
    String[] path = StringUtils.split(currentUri, "/") ; 
    if(path.length == 0)  {
      path = new String[0] ; 
    }
    String currentPath =   ""  ; 
    Parameter uriParam = new Parameter("uri", "/") ;
    Parameter[] changeNodeParams = { CHANGE_NODE , uriParam} ;
    ResponseWriter w = context.getResponseWriter() ;
    //render header
    w.write("<table class='UIExplorer'>");
    w.  write("<tr>");
    w.    write("<td class='header' colspan='2'>") ;
    linkRenderer_.render(w, uiExplorer,  "/",  changeNodeParams) ;
    String pathSeparator = res.getString("UIExplorer.img.path-separator") ;
    for (int i = 0; i < path.length; i++) {
      currentPath += "/" +  path[i] ; 
      uriParam.setValue(currentPath) ;
      w.write("<img src='"); w.write(pathSeparator) ; w.write("'/>") ;
      linkRenderer_.render(w,uiExplorer, path[i], changeNodeParams) ;
    }
    w.    write("</td>") ;
    w.  write("</tr>");
  	//render body
    w.  write("<tr>");
    w.    write("<td class='tree'>") ;
    renderNodeTree(w, uiExplorer) ;
    w.    write("</td>");
    w.    write("<td>") ;
    renderChildren(context, component) ;
    w.    write("</td>");
    w. write("</tr>");
    w.write("</table>");
  }
  
  final public void  renderNodeTree(ResponseWriter w, UIExplorer uiExplorer) throws IOException {
  	NodeDescriptor node = uiExplorer.getSelectNode() ;
    Parameter uriParam = new Parameter("uri", "../") ;
    Parameter[] changeNodeParams = { CHANGE_NODE , uriParam} ;
    w.write("<div>") ;
    linkRenderer_.render(w, uiExplorer, "[..]", changeNodeParams) ;
    w.write("</div>") ;
    List children = node.getChildren() ;
    for (int i = 0; i < children.size(); i++) {
      NodeDescriptor childNode = (NodeDescriptor) children.get(i) ;
      if (!childNode.isLeafNode()) {
        uriParam.setValue(childNode.getUri()) ;
        w.write("<div style='padding: 2px'>") ;
        w.write(childNode.getIcon()) ;
        linkRenderer_.render(w,uiExplorer, childNode.getName(), changeNodeParams) ;
        w.write("</div>") ;
      }
    }
  }
}