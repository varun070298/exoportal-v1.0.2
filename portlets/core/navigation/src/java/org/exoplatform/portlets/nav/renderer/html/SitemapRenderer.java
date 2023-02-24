package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portlets.nav.component.UISitemap;
import org.exoplatform.services.portal.model.Node;
/**
 * @author Fahrid Djebbari
 * @version $Revision: 1.3 $ $Date: 2004/09/23 16:22:14 $
 */
public class SitemapRenderer extends HtmlBasicRenderer {
  private static int MAX_LEVEL = 3;
  private static int COLUMNS_NUMBER = 3;

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    SessionContainer scontainer = SessionContainer.getInstance() ;
    ExoPortal portal = (ExoPortal)scontainer.getComponentInstanceOfType(ExoPortal.class) ;
    RequestInfo rinfo = (RequestInfo)scontainer.getComponentInstanceOfType(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    Node root = portal.getRootNode();

    w.write("<div class=\"UISitemap\">");
    int trigger = Math.abs(root.getChildrenSize()/COLUMNS_NUMBER) + 1;
    for(int i = 0; i < COLUMNS_NUMBER; i++){
      String clazz = "odd";
      if (i % 2 == 0)
        clazz = "even";
      w.  write("<div class='" + clazz + "'>");
      int startIndex = i * trigger;
      for (int n = startIndex; n < startIndex + trigger; n++) {
        Node child = root.getChild(n);
        if (child == null)
          break;
        w.  write("<ul class='first-level'>");
        w.    write("<li>");
        w.  write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
        w.      write(child.getResolvedLabel()) ;
        w.   write("</a>") ;
        encodeNode(child, MAX_LEVEL, w, ownerURI);
        w.    write("</li>");
        w.  write("</ul>");
      }
      w.  write("</div>");
    }
    w.write("</div>");
  }

  public void encodeNode(Node node, int level, ResponseWriter w, String ownerURI) throws IOException {
    if (level > 0) {
      w.write("<ul class='nth-level'>");
      for (int n = 0; n < node.getChildrenSize(); n++) {
        Node child = node.getChild(n);
        w.write("<li>");
        w.  write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
        w.      write(child.getResolvedLabel()) ;
        w.   write("</a>") ;
        this.encodeNode(child, level - 1, w, ownerURI);
        w.write("</li>");
      }
      w.write("</ul>");
    }
  }
}