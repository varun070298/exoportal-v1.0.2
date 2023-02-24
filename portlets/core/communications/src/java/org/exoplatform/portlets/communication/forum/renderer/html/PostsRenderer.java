/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.renderer.html;

import java.io.IOException;
import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.communication.forum.component.UIPosts;
import org.exoplatform.services.communication.forum.Post;
import org.exoplatform.services.grammar.wiki.WikiEngineService;
import org.exoplatform.services.organization.* ;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PostsRenderer.java,v 1.13 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class PostsRenderer extends  HtmlBasicRenderer { 
  private static Parameter QUOTE_POST =   new Parameter(ACTION, "quotePost") ;
  private static Parameter EDIT_POST =  new Parameter(ACTION, "editPost") ;

  private static Parameter[] viewForumsParams_ = {new Parameter(ACTION , "viewForums") } ; 
  private static Parameter[] viewTopicsParams_ = {new Parameter(ACTION , "viewTopics") } ; 
  private static Parameter[] replyParams_ = {new Parameter(ACTION , "reply") } ;
  
  final public void encodeChildren(FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    ExternalContext eContext = context.getExternalContext() ;
    ResourceBundle res = getApplicationResourceBundle(eContext) ;
    UIPosts uiPosts = (UIPosts) component ;
    
    String forumName = uiPosts.getForumName();
    w.write("<table class='UIPosts'>") ;
    w.  write("<tr>");
    w.    write("<td>");
    linkRenderer_.render(w,uiPosts, res.getString("UIPosts.button.forum-index"), viewForumsParams_) ;
    w.write(" >> ");
    linkRenderer_.render(w,uiPosts, forumName, viewTopicsParams_) ;
    w.    write("</td>");
    w.    write("<td align='right'>");
    if(uiPosts.hasReplyTopicRole()) {
      linkRenderer_.render(w,uiPosts, res.getString("UIPosts.button.reply"), replyParams_) ;
    }
    w.    write("</td>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td colspan='2'>");
    addPostsSection(w, res, uiPosts) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.  write("<tr>");
    w.    write("<td>");
    linkRenderer_.render(w,uiPosts, res.getString("UIPosts.button.forum-index"), viewForumsParams_) ;
    w.write(" >> ");
    linkRenderer_.render(w,uiPosts, forumName, viewTopicsParams_) ;
    w.    write("</td>");
    w.    write("<td align='right'>");
    if(uiPosts.hasReplyTopicRole()) {
      linkRenderer_.render(w,uiPosts, res.getString("UIPosts.button.reply"), replyParams_) ;
    } else {
      w.  write(res.getString("UIPosts.button.reply")) ;
    }    
    w.  write("</tr>");    
    w.write("</table>") ;
  }

  private void addPostsSection(ResponseWriter w, ResourceBundle res, 
  		                         UIPosts uiPosts) throws IOException {
    String editButton = res.getString("UIPosts.button.edit") ;
    String quoteButton = res.getString("UIPosts.button.quote") ;
    
    Parameter postIdParam = new Parameter("postId", "") ;
    Parameter[] quoteParams = {QUOTE_POST , postIdParam } ; 
    Parameter[] editParams = { EDIT_POST , postIdParam } ; 

    PageListDataHandler dh = uiPosts.getUIPageIterator().getPageListDataHandler() ;
    boolean hasReplyRole = uiPosts.hasReplyTopicRole() ;
    boolean hasModeratorRole = uiPosts.hasModeratorRole() ;
    String remoteUser = uiPosts.getRemoteUser() ;
    String guestUser  = res.getString("UIPosts.label.guest-user") ;
    WikiEngineService weService_ = uiPosts.getWikiEngineService() ;
    OrganizationService orgService = uiPosts.getOrganizationService() ;
    w.write("<table class='UIPost'>") ;
    w.  write("<tr>");
    w.    write("<th style='width: 200px'>"); w.write(res.getString("UIPosts.header.author"));w.write("</th>");
    w.    write("<th colspan='2'>"); w.write(res.getString("UIPosts.header.messages"));w.write("</th>");
    w.  write("</tr>");
    dh.begin() ;
    while(dh.nextRow()) {
      Post post = (Post) dh.getCurrentObject() ;
      postIdParam.setValue(post.getId()) ;
      String owner = post.getOwner() ;
      if(owner == null ) owner =  guestUser ;
      String avatarURL = null ;
      String signature = null ;
      try {
        UserProfile profile = orgService.findUserProfileByName(owner) ;
        if(profile != null )  {
          Map profileMap = profile.getUserInfoMap() ;
          avatarURL = (String)profileMap.get("user.other-info.avatar.url") ;
          signature = (String)profileMap.get("user.other-info.signature") ;
        }
      } catch (Exception ex) { ex.printStackTrace() ; } 
      if(avatarURL == null) avatarURL = "/user/images/no_pic.gif" ;
      w.write("<tr>");
      w.  write("<td rowspan='3' class='post-owner'>");
      w.    write("<img src='"); w.write(avatarURL) ; w.write("'/><br/>") ;
      w.    write(owner); 
      w.  write("</td>");
      w.  write("<td>"); w.write(post.getSubject()); w.write("</td>");
      w.  write("<td style='width: 100px; text-align: center;'>");
      if (hasReplyRole) {
        linkRenderer_.render(w,uiPosts, quoteButton, quoteParams) ;
      } else {
        w.write(quoteButton);
      }
      w.  write(" - ");
      if (owner.equals(remoteUser) || hasModeratorRole) {
        linkRenderer_.render(w,uiPosts, editButton, editParams) ;
      } else {
        w.write(editButton);
      }
      w.  write("</td>");
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td colspan='2' style='padding: 10px 5px 10px 5px' class='wiki'>");
      String message = weService_.toXHTML(post.getMessage()) ;
      w.    write(message);
      if(signature != null)  {
        renderSignature(w, signature) ;
      }
      w.  write("</td>");
      w.write("</tr>");
      w.write("<tr>");
      w.  write("<td colspan='2'>");
      w.    write(ft_.format(post.getModifiedDate()));
      w.  write("<br>IP: "); w.write(ft_.format(post.getRemoteAddr()));
      w.  write("</td>");
      w.write("</tr>");
    }
    dh.end() ;
    w.  write("<tr>");
    w.    write("<td colspan='3' align='right'>");
    uiPosts.getUIPageIterator().encodeBegin(FacesContext.getCurrentInstance()) ;
    w.    write("</td>");
    w.  write("</tr>");    
    w.write("</table>") ;
  }
  
  private void renderSignature(ResponseWriter w, String signature) throws IOException {
    w.write("<div class='signature'>") ;
    StringBuffer b = new StringBuffer() ;
    char[] buf = signature.toCharArray()  ;
    for(int i = 0; i < buf.length; i++) {
      switch(buf[i]) {
        case '\n' : b.append("<br/>") ; break ;
        case '\r' :  break ;
        case '&' : b.append("&amp;") ; break ;
        case '>' : b.append("&gt;") ; break ;
        case '<' : b.append("&lt;") ; break ;
        case ' ' : {
          b.append(" ") ;
          i++ ;
          while(i < buf.length && buf[i] == ' ') {
            b.append("&nbsp;") ;
            i++ ;
          }
          i-- ;
          break ;
        }
        default: b.append(buf[i]) ; break ;
      }
    }
    w.write(b.toString()) ;
    w.write("</div>") ;
  }
}