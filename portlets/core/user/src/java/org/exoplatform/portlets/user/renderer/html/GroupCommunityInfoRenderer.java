/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.user.component.UIGroupCommunityInfo;
import org.exoplatform.services.portal.community.CommunityPortal ;
import org.exoplatform.services.portal.community.CommunityNavigation ;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: GroupExplorerRenderer.java,v 1.12 2004/10/22 14:23:31 tuan08 Exp $
 */
public class GroupCommunityInfoRenderer extends  HtmlBasicRenderer {

  final public void encodeBegin(FacesContext context, UIComponent component ) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    UIGroupCommunityInfo uiInfo = (UIGroupCommunityInfo) component ;
    CommunityPortal cp = uiInfo.getCommunityPortal() ;
    CommunityNavigation cn = uiInfo.getCommunityNavigation() ;
    w.write("<table class='UIGrid'>") ;
    w.  write("<tr>");
    w.    write("<th colspan='2'>") ;
    w.      write(res.getString("UIGroupCommunityInfo.header.portal-community")) ;
    w.    write("</th>") ;
    w.  write("</tr>");
    if(cp == null) {
      w.write("<tr>");
      w.  write("<td colspan='2'>") ; w.write(res.getString("UIGroupCommunityInfo.msg.no-community-portal")) ;w.write("</td>") ;
      w.write("</tr>");
    } else {
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.membership")) ; w.write("</td>") ;
      w.  write("<td>") ;  w.write(cp.getMembership()) ; w.write("</td>") ;
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.portal")) ; w.write("</td>") ;
      w.  write("<td>") ;  w.write(cp.getPortal()) ; w.write("</td>") ;
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.priority")) ; w.write("</td>") ;
      w.  write("<td>") ;w.write(Integer.toString(cp.getPriority())) ; w.write("</td>") ;
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.description")) ; w.write("</td>") ;
      w.  write("<td>") ;w.write(ft_.format(cp.getDescription())) ; w.write("</td>") ;
      w.write("</tr>");
    }
    w.  write("<tr>");
    w.    write("<td colspan='2' align='right'>") ;
    linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.add-edit"), 
                         UIGroupCommunityInfo.ADD_EDIT_PORTAL) ;
    if(uiInfo.getCommunityPortal() != null) {
      linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.admin"), 
          UIGroupCommunityInfo.ADMIN_PORTAL) ;      
      linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.delete"), 
                           UIGroupCommunityInfo.DELETE_PORTAL) ;
    }
    w.    write("</td>") ;
    w.  write("</tr>");
    
    w.  write("<tr>");
    w.    write("<th colspan='2'>") ;
    w.      write(res.getString("UIGroupCommunityInfo.header.navigation-community")) ;
    w.    write("</th>") ;
    w.  write("</tr>");
    if(cn == null) {
      w.write("<tr>");
      w.  write("<td colspan='2'>") ; w.write(res.getString("UIGroupCommunityInfo.msg.no-community-navigation")) ;w.write("</td>") ;
      w.write("</tr>");
    } else {
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.membership")) ; w.write("</td>") ;
      w.  write("<td>") ;  w.write(cn.getMembership()) ; w.write("</td>") ;
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.navigation")) ; w.write("</td>") ;
      w.  write("<td>") ;  w.write(cn.getNavigation()) ; w.write("</td>") ;
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td>"); w.write(res.getString("UIGroupCommunityInfo.label.description")) ; w.write("</td>") ;
      w.  write("<td>") ;w.write(ft_.format(cn.getDescription())) ; w.write("</td>") ;
      w.write("</tr>");
    }
    w.  write("<tr>");
    w.    write("<td colspan='2' align='right'>") ;
    linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.add-edit"), 
                         UIGroupCommunityInfo.ADD_EDIT_NAV) ;
    if(uiInfo.getCommunityNavigation() != null) {
      linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.admin"), 
          UIGroupCommunityInfo.ADMIN_NAV) ;      
      linkRenderer_.render(w, uiInfo, res.getString("UIGroupCommunityInfo.button.delete"), 
                           UIGroupCommunityInfo.DELETE_NAV) ;
    }
    w.    write("</td>") ;
    w.  write("</tr>");
    w.write("</table>") ;
  }
}