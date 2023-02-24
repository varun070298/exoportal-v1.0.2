/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.renderer.html;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.communication.forum.component.UITopics;
import org.exoplatform.services.communication.forum.Topic;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: TopicsRenderer.java,v 1.11 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class TopicsRenderer extends  HtmlBasicRenderer { 

  final public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    ExternalContext eContext = context.getExternalContext() ;
    ResourceBundle res = getApplicationResourceBundle(eContext) ;
    UITopics uiTopics = (UITopics) component ;
        
    w.write("<table width='100%'>") ;
    w.  write("<tr>");
    w.    write("<td>");
    linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.forum-index"), UITopics.viewForumsParams) ;
    w.    write("</td>");
    w.    write("<td align='right'>");
    if(uiTopics.hasCreateTopicRole()) {
      linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.new-topic"), UITopics.newTopicParams) ;
    }
    w.    write("</td>");    
    w.  write("</tr>");
    w.write("</table>") ;    
    addTopicsSection(context, w, res, uiTopics) ;
    w.write("<table width='100%'>") ;
    w.  write("<tr>");
    w.    write("<td>");
    linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.forum-index"), 
                         UITopics.viewForumsParams) ;
    w.    write("</td>");
    w.    write("<td align='right'>");
    if(uiTopics.getUser() != null) {
      linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.watch-forum"), 
                           UITopics.watchForumParams) ;
      linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.stop-watch-forum"), 
                           UITopics.stopWatchForumParams) ;
    }
    if(uiTopics.hasCreateTopicRole()) {
      linkRenderer_.render(w,uiTopics, res.getString("UITopics.button.new-topic"), UITopics.newTopicParams) ;
    }
    w.    write("</td>");    
    w.  write("</tr>");
    w.write("</table>") ;      
  }

  private void addTopicsSection(FacesContext context, ResponseWriter w, 
                                ResourceBundle res, UITopics uiTopics) throws IOException {
    Parameter topicIdParam = new Parameter("topicId" , "");
    Parameter[] viewTopicParams = { UITopics.VIEW_POSTS , topicIdParam} ;
    Parameter[] deleteTopicParams = {  UITopics.DELETE_TOPIC , topicIdParam} ;
    Parameter[] watchTopicParams = {  UITopics.watchTopicParam , topicIdParam} ;
    Parameter[] stopWatchTopicParams = {  UITopics.stopWatchTopicParam , topicIdParam} ;
    String delButton = res.getString("UITopics.button.delete");
    String watchTopicButton = res.getString("UITopics.button.watch-topic");
    String stopWatchTopicButton = res.getString("UITopics.button.stop-watch-topic");
    String topicIcon = res.getString("UITopics.img.topic-icon");
    String guestUser  = res.getString("UIPosts.label.guest-user") ;
    PageListDataHandler dh = uiTopics.getUIPageIterator().getPageListDataHandler();
    w.write("<table class='UITopics'>") ;
    w.  write("<tr>");
    w.    write("<th width='*' colspan='2'>"); 
    w.      write(res.getString("UITopics.header.topics")); 
    w.    write("</th>");
    w.    write("<th width='75'>"); 
    w.      write(res.getString("UITopics.header.replies")); 
    w.    write("</th>");
    w.    write("<th width='75'>"); 
    w.      write(res.getString("UITopics.header.author")); 
    w.    write("</th>");
    w.    write("<th width='75'>"); 
    w.      write(res.getString("UITopics.header.views")); 
    w.    write("</th>");
    w.    write("<th width='200'>"); 
    w.      write(res.getString("UITopics.header.last-post")); 
    w.    write("</th>");
    w.    write("<th width='25'>"); w.write("-"); w.write("</th>");
    w.  write("</tr>");
    dh.begin() ;
    while(dh.nextRow()) {
      Topic topic = (Topic) dh.getCurrentObject() ;
      topicIdParam.setValue(topic.getId()) ;
      String owner = topic.getOwner() ;
      if(owner == null) owner =  guestUser ;
      String lastPostUser = topic.getLastPostBy() ;
      if(lastPostUser == null) lastPostUser =  guestUser ;
      boolean hasNewPosts = uiTopics.hasNewPosts( topic );
      w.write("<tr>");
      w.  write("<td width='40'>");
        w.write(topicIcon);
        if(hasNewPosts) {
          w.write(res.getString("UITopics.img.newest-reply-icon"));
        }
      w.write("</td>");
      w.  write("<td style='text-align: left'>");
      linkRenderer_.render(w,uiTopics
			  , hasNewPosts ? "<b>" + topic.getTopic() + "</b>" : topic.getTopic()
			  , viewTopicParams) ;
      w.    write("<br/>"); w.write(topic.getDescription()) ;
      w.  write("</td>");
      w.  write("<td>");w.write(Integer.toString(topic.getPostCount())); w.write("</td>");
      w.  write("<td>");  w.write(owner);w.write("</td>");
      w.  write("<td>"); w.write("N/A"); w.write("</td>");
      w.  write("<td>");
      w.    write(ft_.format(topic.getLastPostDate()));
      w.    write("<br/>");
      w.    write(lastPostUser);
      w.  write("</td>");
      w.  write("<td width='60' valign='middle'>");
      if(uiTopics.getUser() != null) {
        linkRenderer_.render(w,uiTopics, watchTopicButton, watchTopicParams) ;
        linkRenderer_.render(w,uiTopics, stopWatchTopicButton, stopWatchTopicParams) ;
      } else {
        w.write(watchTopicButton);
        w.write(stopWatchTopicButton);
      }
      if (uiTopics.isModerator()) {
        linkRenderer_.render(w,uiTopics, delButton, deleteTopicParams) ;
      }
      w.  write("</td>");
      w.write("</tr>");
    }
    dh.end() ;
    w.  write("<tr>");
    w.    write("<td colspan='7' style='text-align: right;'>");
    uiTopics.getUIPageIterator().encodeBegin(context) ;
    w.    write("</td>");
    w.  write("</tr>");
    w.write("</table>");
  }
}