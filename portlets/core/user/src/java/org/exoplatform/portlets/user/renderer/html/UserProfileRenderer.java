/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.renderer.html;

import java.util.* ;
import java.util.ResourceBundle  ;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.commons.utils.Formater;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.user.component.UIUserProfileSummary;
import java.io.IOException;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserProfileRenderer.java,v 1.8 2004/07/22 17:08:18 tuan08 Exp $
 */
public class UserProfileRenderer extends  HtmlBasicRenderer {
  private static Parameter[] backParams = { new Parameter(ACTION , BACK_ACTION)} ;
  static private Formater ft_ = Formater.getFormater(null) ;
   
  final public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter() ;
    UIUserProfileSummary uiUserProfile = (UIUserProfileSummary) component ;
    Map userInfo = uiUserProfile.getUserInfoMap() ;
    String title = res.getString("UIUserProfile.header.viewing-user-info") ;
    w.write("<table class='UIUserProfile'>");
    w.  write("<tr>");
    w.    write("<th colspan='2' class='header'>");
    w.      write(title);
    w.    write("</th>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td width='50%'>");
    addSection(w, res, userInfo, "UIUserProfile.header.personal-info", UIUserProfileSummary.PERSONAL_INFO_KEYS) ;
    addSection(w, res, userInfo, "UIUserProfile.header.user-home-info", UIUserProfileSummary.HOME_INFO_KEYS) ;
    addSection(w, res, userInfo, "UIUserProfile.header.user-businese-info", UIUserProfileSummary.BUSINESE_INFO_KEYS) ;
    w.    write("</td>");
    w.    write("<td class='avatar'>");
    String avatarURL = (String)userInfo.get("user.other-info.avatar.url") ;
    if(avatarURL == null) avatarURL = "/user/images/no_pic.gif" ;
    w.      write("<img src='");w.write(avatarURL); w.write("'/>") ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("<tr>");
    w.  write("<td class='footer' colspan='2'>");
    if(uiUserProfile.showBackButton()) {
      linkRenderer_.render(w,uiUserProfile, res.getString("UIUserProfile.button.back"),backParams) ;
    }
    w.  write("</td>");
    w.write("</tr>");
    w.write("</table>");
  }

  private void addSection(ResponseWriter w, ResourceBundle res, Map userInfo,
                          String titleKey, String[] keys) throws IOException {
    w.write("<table class='section'>");
    w.  write("<tr>");
    w.    write("<th colspan='2'>"); w.write(res.getString(titleKey)); w.write("</th>") ;
    w.  write("</tr>") ;
    for (int i = 0 ; i < keys.length; i++) {
      if (i % 2 == 0) {  
        w.write("<tr class='even'>");
      } else {
        w.write("<tr class='odd'>");
      }
      w.  write("<td class='label'>");w.write(res.getString(keys[i])) ;w.write("</td>");
      w.  write("<td>");w.write(ft_.format((String) userInfo.get(keys[i])));w.write("</td>");
      w.write("</tr>");
    }
    w.write("</table>") ;
  }
}