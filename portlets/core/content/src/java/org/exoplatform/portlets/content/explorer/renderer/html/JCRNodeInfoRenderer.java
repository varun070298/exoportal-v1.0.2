/**
 **************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */
package org.exoplatform.portlets.content.explorer.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.jcr.*;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.version.OnParentVersionAction;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.content.explorer.component.UIJCRNodeInfo;
/**
 * @author <a href="tuan08@users.sourceforge.net">Tuan Nguyen</a>
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: JCRExplorerRenderer.java,v 1.1 2004/11/02 20:02:53 geaz Exp $
 */
public class JCRNodeInfoRenderer extends HtmlBasicRenderer {
  
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIJCRNodeInfo uiInfo = (UIJCRNodeInfo)component;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext());  
    Node currentNode = uiInfo.getCurrentNode() ;
    ResponseWriter w = context.getResponseWriter();
    //render header
    renderNodeDetail(w, res, currentNode) ;
  }
  
  private void renderNodeDetail(ResponseWriter w, ResourceBundle res, Node node) throws IOException {
    // Attrs
    renderNodeAttributes(node, w, res);
    w.write("<br>");
    // Properies
    renderNodeProperties(node, w, res);
    w.write("<br>");
    // Definition
    renderNodeDefinition(node, w, res);
  }
  
  private void renderNodeAttributes(Node node, ResponseWriter w, ResourceBundle res) throws IOException {
    w.write("<table class='detail'>");
    w.  write("<tr>") ;
    w.    write("<th colspan='2'>"); w.write("Details"); w.write("</th>");
    w.  write("</tr>") ;
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Name"); w.write("</td>");
    w.    write("<td>"); w.write((node.getName())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Paths"); w.write("</td>");
    w.    write("<td>");
    StringIterator paths = node.getPaths();
    while(paths.hasNext())  w.write(paths.nextString()+" ");
    w.    write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Primary Node Type"); w.write("</td>");
    w.    write("<td>"); w.write((node.getPrimaryNodeType().getName())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Mixin Node Types"); w.write("</td>");
    w.    write("<td>");
    for(int i=0;i<node.getMixinNodeTypes().length; i++)
      w.write(node.getMixinNodeTypes()[i].getName());
          w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Primary Item"); w.write("</td>");
    w.    write("<td>");
    try {
      w.write((node.getPrimaryItem().getPath()));
    } catch (ItemNotFoundException e) {
    } catch (RepositoryException e) {
      w.write(e.getMessage());
    }
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }
  
  private void renderNodeProperties(Node node, ResponseWriter w, ResourceBundle res) throws IOException {
    w.write("<table>");
    w.  write("<tr>") ;
    w.    write("<th>"); w.write("Property"); w.write("</th>");
    w.    write("<th>"); w.write("Value"); w.write("</th>");
    w.    write("<th>"); w.write("Type"); w.write("</th>");
    w.  write("</tr>") ;
    PropertyIterator properties;
    try {
      properties = node.getProperties();
    } catch (RepositoryException e) {
      throw new IOException("Render Exception " + e);
    }
    Property property;
    Value value;
    while (properties.hasNext()) {
      try {
        property = properties.nextProperty();
        value = property.getValue();
      } catch (RepositoryException e) {
        throw new IOException("Render Exception " + e);
      }
      if(!property.getName().startsWith("jcr:")) continue ;
      w.write("<tr>");
      w.  write("<td width='30%'>"); w.write(property.getName()); w.write("</td>");
      try {
        w.write("<td>"); w.write(value.getString()); w.write("</td>");
      } catch (RepositoryException e) {
        w.write("<td>"); w.write("Format error"); w.write("</td>");
      }
      w.write("<td>"); w.write(PropertyType.nameFromValue(value.getType())); w.write("</td>");
      w.write("</tr>");
    }
    w.write("</table>");
  }
  
  private void renderNodeDefinition(Node node, ResponseWriter w, ResourceBundle res) throws IOException {
    w.write("<table>");
    w.  write("<tr>") ;
    w.    write("<th>"); w.write("Primary Type Definition"); w.write("</th>");
    w.    write("<th>"); w.write(""); w.write("</th>");
    w.  write("</tr>") ;
    NodeDef nodeDef;
    try {
      nodeDef = node.getDefinition();
    } catch (Exception e) {
      throw new IOException("Render Exception " + e);
    }
    w.  write("<tr>");
    w.    write("<td>"); w.write("Name"); w.write("</td>");
    w.    write("<td>"); 
    w.      write((nodeDef.getName() == null) ? "&lt;residual set&gt;" : nodeDef.getName());
    w.    write("</td>");
    w.write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Default primary type"); w.write("</td>");
    w.    write("<td>"); w.write(nodeDef.getDefaultPrimaryType().getName()); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td >"); w.write("Auto create"); w.write("</td>");
    w.    write("<td>"); w.write(Boolean.toString(nodeDef.isAutoCreate())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Mandatory"); w.write("</td>");
    w.    write("<td>"); w.write(Boolean.toString(nodeDef.isMandatory())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Primary item"); w.write("</td>");
    w.    write("<td>"); w.write(Boolean.toString(nodeDef.isPrimaryItem())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("Protected"); w.write("</td>");
    w.    write("<td>"); w.write(Boolean.toString(nodeDef.isReadOnly())); w.write("</td>");
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<td>"); w.write("On Parent Version action"); w.write("</td>");
    w.    write("<td>"); w.write(OnParentVersionAction.nameFromValue(nodeDef.getOnParentVersion()));w.write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }
}