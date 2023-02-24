package org.exoplatform.portlets.nav.renderer.xhtmlmp;

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
import org.exoplatform.services.portal.model.PageReference;
/**
 * @author Fahrid Djebbari
 * @version $Revision: 1.6 $ $Date: 2004/10/08 13:43:52 $
 */
public class SitemapRenderer extends HtmlBasicRenderer {

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    UISitemap sitemap = (UISitemap) component;
    ResponseWriter w = context.getResponseWriter();
    SessionContainer scontainer = SessionContainer.getInstance();
    ExoPortal portal = (ExoPortal)scontainer.getComponentInstanceOfType(ExoPortal.class) ;
    RequestInfo rinfo = (RequestInfo)scontainer.getComponentInstanceOfType(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    String mimeType = sitemap.getPreferedMimeType() ;
    Node root = portal.getRootNode() ;
    w.write("<a href='"); w.write(ownerURI); w.write(root.getUri()); w.write("'>") ;
    w.  write("Home") ;
    w.write("</a>") ;    
  	if (root.getChildrenSize() > 0) w.write("<ul>"); 
    for(int i = 0; i < root.getChildrenSize(); i++){
      Node child = root.getChild(i);
      PageReference pref = child.getPageReference(mimeType) ;
      if(pref == null || pref.isVisible()) {   
        w.write("<li>");
        w.write("<a href='"); w.write(ownerURI); w.write(child.getUri()); w.write("'>") ;
        w.  write(child.getResolvedLabel()) ;
        w.write("</a>") ;   
        encodeNode(child,mimeType, w , ownerURI);
        w.write("</li>");
      }
    }
  	if (root.getChildrenSize() > 0) w.write("</ul>"); 
  }

  public void encodeNode(Node node, String mimeType, ResponseWriter w, String baseURL) throws IOException {
  	if (node.getChildrenSize() > 0) w.write("<ul>"); 
    for (int n = 0; n < node.getChildrenSize(); n++) {
      Node child = node.getChild(n);
      PageReference pref = child.getPageReference(mimeType) ;
      if(pref == null || pref.isVisible()) {
        w.write("<li>");
        w.  write("<a href='"); w.write(baseURL); w.write(child.getUri()); w.write("'>") ;
        w.    write(child.getResolvedLabel()) ;
        w.   write("</a>") ;    
        encodeNode(child, mimeType, w, baseURL);
        w.write("</li>");
      }
    }
    if (node.getChildrenSize() > 0) w.write("</ul>");
  }
}