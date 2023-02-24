package org.exoplatform.portal.faces.renderer.html.portlet;


import javax.faces.context.ResponseWriter;
import javax.portlet.WindowState;
import org.exoplatform.portal.faces.component.UIPortlet;
import org.exoplatform.services.portal.model.Portlet;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class BoxPortletRenderer extends PortletRenderer {

  protected void renderPortletHeaderBar(ResponseWriter w, UIPortlet uiPortlet, ResourceBundle res, 
                                        String portletTitle, String baseUrl) throws IOException {
  	Portlet model = uiPortlet.getPortletModel() ;
    w.write("<tr>");
    //top left corner
    w.  write("<td  class='top-left-corner'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>");
    //title part
    w.  write("<td class='left-portlet-info-bar'>");
    if (model.getShowInfoBar()) {
      w.  write(portletTitle);
    }
    w.  write("</td>\n");
    //info bar with no title
    w.  write("<td align='right' class='right-portlet-info-bar'>");
    if (model.getShowInfoBar()) {
      //icon actions
      if(model.getShowPortletMode()) {
        List modes = uiPortlet.getHtmlSupportModes() ;
        if (uiPortlet.hasComponentAdminRole()) {
          for (int i =0 ; i < modes.size() ; i++) {
            String mode = (String) modes.get(i) ;
            renderModeLink(w, uiPortlet,res,  mode, baseUrl) ;
          }
        } else {
          for (int i =0 ; i < modes.size() ; i++) {
            String mode = (String) modes.get(i) ;
            if("help".equals(mode)) {
              renderModeLink(w, uiPortlet, res,  mode, baseUrl) ;
            }
          }
        }
      }
      if (model.getShowWindowState()) {
        renderWindowStateLink(w, uiPortlet, res,  WindowState.MINIMIZED, baseUrl) ;
        renderWindowStateLink(w, uiPortlet, res, WindowState.MAXIMIZED, baseUrl) ;
      }
    }
    w.  write("</td>");
    //top right corner
    w.  write("<td class='top-right-corner'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>");
    w.write("</tr>");
  }

  protected void renderPortletBody(ResponseWriter w, UIPortlet uiPortlet,
                                   String content) throws IOException {
    w.write("<tr>");
    w.  write("<td class='left-border'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>\n");
    w.  write("<td colspan='2' width='*' height='100%' class='");
    w.write(uiPortlet.getDecorator()); w.write("-portlet'>");
    w.    write(content);
    w.  write("</td>\n");
    w.  write("<td class='right-border'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>");
    w.write("</tr>\n");
  }

  protected void renderPortletFooterBar(ResponseWriter w, UIPortlet uiPortlet,
                                        String portletTitle, String baseUrl) throws IOException {
    w.write("<tr>");
    //bottom left corner class='bottom-left-corner'
    w.  write("<td class='bottom-left-corner'>");
    w.    write("<img  src='/skin/blank.gif'/>");
    w.  write("</td>\n");
    //footer bar
    w.  write("<td class='portlet-footer-bar'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>\n");
    w.  write("<td class='portlet-footer-bar'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>\n");
    //bottom right corner
    w.  write("<td class='bottom-right-corner'>");
    w.    write("<img src='/skin/blank.gif'/>");
    w.  write("</td>");
    w.write("</tr>");
  }
}