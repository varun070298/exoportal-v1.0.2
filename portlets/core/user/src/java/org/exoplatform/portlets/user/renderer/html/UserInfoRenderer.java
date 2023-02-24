/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.renderer.html;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.user.component.UIUserInfo;
import org.exoplatform.services.organization.Membership;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserInfoRenderer.java,v 1.15 2004/10/21 15:25:17 tuan08 Exp $
 */
public class UserInfoRenderer extends  HtmlBasicRenderer {
  private static Parameter DELETE_MEMBERSHIP = new Parameter(ACTION , "deleteMembership") ;
  private static Parameter[] backParams = {new Parameter(ACTION , "back")} ;

  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
 
    UIUserInfo uiUserInfo = (UIUserInfo) component ;
    w.write("<table class='UIUserInfo'>");
    w.  write("<tr>");
    w.    write("<td class='form'>");
    uiUserInfo.getUIAccountForm().encodeChildren(context) ;
    w.    write("</td>");
    w.    write("<td class='user-roles'>");
    addUserRoleInfo(context, res, uiUserInfo) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td colspan='2' class='footer'>");
    linkRenderer_.render(w,uiUserInfo, res.getString("UIUserInfo.button.back"), backParams) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }

  private void addUserRoleInfo(FacesContext context,
                               ResourceBundle res, UIUserInfo uiUserInfo) throws IOException {
    ResponseWriter w  = context.getResponseWriter() ;
    Iterator memberships = uiUserInfo.getMemberships().iterator() ;
    Parameter membershipIdParam = new Parameter("membershipId", "") ;
    Parameter[] deleteMembershipParams = { DELETE_MEMBERSHIP , membershipIdParam } ;
    w.write("<table>");
    w.  write("<tr>");
    w.    write("<th>");
    w.      write(res.getString("UIUserInfo.header.membership"));
    w.    write("</th>");
    w.    write("<th>");
    w.      write(res.getString("UIUserInfo.header.membership-type"));
    w.    write("</th>");
    w.    write("<th>");
    w.      write(res.getString("UIUserInfo.header.group-id"));
    w.    write("</th>");
    w.    write("<th>-</th>");
    w.  write("</tr>") ;
    int i = 0;
    while (memberships.hasNext()) {
      Membership membership = (Membership) memberships.next();
      String clazz = "odd";
      if (i % 2 == 0)   clazz = "even";
      w.write("<tr  class='" + clazz + "'>") ;
      w.  write("<td>") ; w.write("Membership") ; w.write("</td>") ;      
      w.  write("</td>"); w.write("<td>"); w.write(membership.getMembershipType()); w.write("</td>") ;
      w.  write("<td>") ; w.write(membership.getGroupId()) ; w.write("</td>") ;
      if(uiUserInfo.hasAdminRole()) {
        w.write("<td>");
        membershipIdParam.setValue(membership.getId()) ;
        linkRenderer_.render(w, uiUserInfo, res.getString("UIUserInfo.button.delete"), deleteMembershipParams) ;
        w.write("</td>");
      } else {
        w.write("<td> - </td>");
      }
      w.write("</tr>") ;
      i++;
    }
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td class='form' colspan='4'>");
    uiUserInfo.getUIMembershipForm().encodeChildren(context) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }
}