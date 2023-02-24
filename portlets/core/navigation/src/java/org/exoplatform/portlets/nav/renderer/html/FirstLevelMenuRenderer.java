/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portlets.nav.component.UIFirstLevelMenu;
import org.exoplatform.portlets.nav.component.UINavigation;
import org.exoplatform.services.portal.model.Node;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 sept. 2004
 */
public class FirstLevelMenuRenderer extends HtmlBasicRenderer {
  final static protected Parameter LOGOUT = new Parameter(ACTION, UIFirstLevelMenu.LOGOUT_ACTION);

  public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    UIFirstLevelMenu uiMenu = (UIFirstLevelMenu) uiComponent;
    ResponseWriter w = context.getResponseWriter();
    ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
    Node rootNode = portal.getRootNode();
    w.write("<div class='UIFirstLevelMenu'>");
    w.write("<div class='" + rootNode.getName() + "'>");
    Parameter nodeUriParam = new Parameter("uri", rootNode.getUri());
    Parameter[] changeToRootNodeParams = {UINavigation.CHANGE_NODE, nodeUriParam};
    render(w, uiMenu, rootNode.getResolvedLabel(), changeToRootNodeParams);
    w.write("</div>");
    for (int i = 0; i < rootNode.getChildrenSize(); i++) {
      Node node = rootNode.getChild(i);
      nodeUriParam = new Parameter("uri", node.getUri());
      Parameter[] changeNodeParams = {UINavigation.CHANGE_NODE, nodeUriParam};
      w.write("<div class='" + node.getName() + "'>");
      render(w, uiMenu, node.getResolvedLabel(), changeNodeParams);
      w.write("</div>");
    }
    if (context.getExternalContext().getRemoteUser() != null) {
      w.write("<div class='logout'>");
      Parameter[] logout = {LOGOUT};
      render(w, uiMenu, "logout", logout);
      w.write("</div>");
    }
    w.write("</div>");
  }

  private void render(ResponseWriter w, UIExoComponent component, String text, Parameter[] params)
      throws IOException {
    w.write("<a");
    w.write(" href='");
    w.write(component.getBaseURL());
    for (int i = 0; i < params.length; i++) {
      w.write("&amp;");
      w.write(params[i].getName());
      w.write('=');
      w.write(params[i].getValue());
    }
    w.write("'");
    w.write(">");
    w.write("<span>");
    w.write(text);
    w.write("</span>");
    w.write("</a>");
  }
}