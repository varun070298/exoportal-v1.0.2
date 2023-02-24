package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.portal.model.Node;
/**
 * @author Fahrid Djebbari
 * @version $Revision: 1.6 $ $Date: 2004/09/28 18:19:23 $
 */
public class HorizontalMenuRenderer extends HtmlBasicRenderer {

  final public static String LEFT_SELECTED_TAB =
      "<img class='left-selected-tab' src='/skin/blank.gif'/>";
  final public static String RIGHT_SELECTED_TAB =
      "<img class='right-selected-tab' src='/skin/blank.gif'/>";
  final public static String HOME_ICON = "<img class='home-icon' src='/skin/blank.gif' alt='Home'/>";

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    ResponseWriter w = context.getResponseWriter();
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    Node root = portal.getRootNode();
    w.write("<script>");
    w.write("function _showmenu(id){");
    w.write("	  if (document.getElementById) {");
    w.write("		  document.getElementById(id).style.display=\"block\";");
    w.write("		} else if (document.all) {");
    w.write("		  document.all[id].style.display=\"block\";");
    w.write("		} else if (document.layers) {");
    w.write("		  document.layers[id].display=\"block\";");
    w.write("		} }");

    w.write("function _hidemenu(id){");
    w.write("	  if (document.getElementById) {");
    w.write("		  document.getElementById(id).style.display=\"none\";");
    w.write("		} else if (document.all) {");
    w.write("		  document.all[id].style.display=\"none\";");
    w.write("		} else if (document.layers) {");
    w.write("		  document.layers[id].display=\"none\";");
    w.write("		} }");
    w.write("</script>");

    w.write("<div class='UIHorizontalMenu'>");
    w.  write("<dl class='home-dl'>");
    w.    write("<dt>");
    w.      write("<a href='"); w.write(ownerURI); w.write("/'>") ;
    w.        write(HOME_ICON) ;
    w.      write("</a>") ;
    w.write("    </dt>");
    w.write("    <dd/>");
    w.write("  </dl>");
    encodeNode(root, 2, w, ownerURI);
    w.write("</div>");
  }

  public void encodeNode(Node root, int level, ResponseWriter w, 
                         String ownerURI)
      throws IOException {
    if (level > 0) {
      for (int n = 0; n < root.getChildrenSize(); n++) {
        Node node = root.getChild(n);
        if(!node.isVisible()) continue ;
        String jsName = "node_" + node.getUri().replace('/', '_');
        w.write("<dl onmouseover=\"_showmenu('" + jsName + "');\" onmouseout=\"_hidemenu('" + jsName + "');\">");
        if (node.isSelectedPath()) {
          w.  write("<dt>");
          w.    write("<span> ");
          w.      write(LEFT_SELECTED_TAB);
          w.      write("<a class='selected' href='"); w.write(ownerURI); w.write(node.getUri()); w.write("'>") ;
          w.        write(node.getResolvedLabel()) ;
          w.      write("</a>") ;
          w.      write(RIGHT_SELECTED_TAB);
          w.    write("</span>");
        } else {
          w.write("	<dt>");
          w.      write("<a href='"); w.write(ownerURI); w.write(node.getUri()); w.write("'>") ;
          w.        write(node.getResolvedLabel()) ;
          w.      write("</a>") ;
        }
        w.write("	</dt>");
        w.write("	<dd id=\"" + jsName + "\" onmouseover=\"_showmenu('" + jsName +
            "');\" onmouseout=\"_hidemenu('" + jsName + "');\">");        
        w.write("		<ul>");
        for (int p = 0; p < node.getChildrenSize(); p++) {
          Node child = node.getChild(p);
          if(!child.isVisible()) continue ;
          w.    write("<li>");
          w.      write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
          w.        write(child.getResolvedLabel()) ;
          w.      write("</a>") ;
          w.    write("</li>");
        }
        w.write("		</ul>");
        w.write("	</dd>");
        w.write("</dl>");
      }
    }
  }
}