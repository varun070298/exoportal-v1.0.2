package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.faces.user.component.UILanguageSelector;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portlets.nav.component.UIBreadcrumbs;
import org.exoplatform.services.portal.model.Node;
/**
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2004/08/05 13:11:13 $
 * @author Fahrid Djebbari
 *  
 */
public class BreadcrumbsRenderer extends HtmlBasicRenderer {
  private static Parameter CHANGE_NODE = new Parameter(FacesConstants.ACTION,
      "changeNode");

  public void encodeBegin(FacesContext context, UIComponent component)
      throws IOException {
  }

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UIBreadcrumbs breadcrumbs = (UIBreadcrumbs) component;
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;

    ResponseWriter writer = context.getResponseWriter();

    String baseURL = breadcrumbs.getBaseURL(context);
    Node selectedNode = portal.getSelectedNode();

    writer.write("<div class='UIBreadcrumbs'>");
    writer.write("<div>");
    this.encodeNode(portal.getRootNode(), selectedNode, baseURL, writer);
    writer.write("</div>");        
    writer.write("<div class='flags'>");
    UILanguageSelector languageSelector = (UILanguageSelector) breadcrumbs.getChildren().get(0);
    languageSelector.encodeBegin(context);
    languageSelector.encodeChildren(context);
    languageSelector.encodeEnd(context);
    writer.write("</div>");
    writer.write("</div>");
  }

  private void encodeNode(Node node, Node selectedNode, String baseURL,
      ResponseWriter writer) throws IOException {
    writer.write("<span>");//class='UIBreadcrumbs'

    if (node == selectedNode) {
      writer.write(node.getResolvedLabel());
    } else {
      Parameter nodeUriParam = new Parameter("uri", "");
      Parameter[] changeNodeParams = { CHANGE_NODE, nodeUriParam };
      nodeUriParam.setValue(node.getUri());
      appendLink(writer, node.getResolvedLabel(), baseURL, changeNodeParams, "");
    }
    writer.write("</span>");
    for (int i = 0; i < node.getChildrenSize(); i++) {
      Node child = node.getChild(i);
      if (selectedNode.getUri().startsWith(child.getUri())) {
        writer.write("<span>&nbsp;>&nbsp;</span>");
        this.encodeNode(child, selectedNode, baseURL, writer);
      }
    }
  }

  public void encodeEnd(FacesContext context, UIComponent component)
      throws IOException {
  }
}