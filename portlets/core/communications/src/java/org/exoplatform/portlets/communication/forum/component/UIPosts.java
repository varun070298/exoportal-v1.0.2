/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.communication.forum.ForumACL;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.Post;
import org.exoplatform.services.communication.forum.Topic;
import org.exoplatform.services.grammar.wiki.WikiEngineService ;
import org.exoplatform.services.organization.OrganizationService ;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPosts.java,v 1.9 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class UIPosts extends UIExoCommand {
  private UIPageListIterator uiPageIterator_ ;
  private Topic topic_ ;
  private Forum forum_  ;
  private ForumService service_ ;
  private ForumACL acl_ ;  
  private WikiEngineService wikiEngineService_ ;
  private OrganizationService orgService_ ;
    
  private static final String REPLY = "reply";
  private static final String VIEW_FORUMS = "viewForums";          

  public static Parameter[] replyParams = { new Parameter(ACTION , REPLY)} ;
  public static Parameter[] viewForumsParams = { new Parameter(ACTION , VIEW_FORUMS)} ;
   
  public UIPosts(WikiEngineService weService, 
                 OrganizationService orgService,  
                 ForumService service, ForumACL acl) throws Exception {
    setRendererType("PostsRenderer") ;
    setId("UIPosts") ;
    wikiEngineService_ = weService ;
    orgService_ = orgService ;
    uiPageIterator_ = new UIPageListIterator(new PageListDataHandler()) ;
    getChildren().add(uiPageIterator_);
    service_ = service ;
    acl_ = acl ;            
    
    addActionListener(ViewTopicsActionListener.class, "viewTopics")  ;
    addActionListener(ViewForumsActionListener.class, VIEW_FORUMS)  ;
    addActionListener(QuotePostActionListener.class, "quotePost")  ;
    addActionListener(EditPostActionListener.class, "editPost")  ;
    addActionListener(ReplyActionListener.class, REPLY)  ;
  }
  
  
  public String getForumName() { return forum_.getForumName(); }
   
  public Topic getTopic() { return topic_ ; } 

  public WikiEngineService getWikiEngineService() { return wikiEngineService_ ; }
  
  public OrganizationService  getOrganizationService() { return orgService_ ; }
  
  public void setUIPostsData(Forum forum, Topic topic) throws Exception {
    forum_ = forum ;
    topic_ = topic ; 
    uiPageIterator_.setPageList(service_.getPosts(topic_.getId())) ;
  }
  
  public void reload() throws Exception {
    uiPageIterator_.setPageList(service_.getPosts(forum_.getId())) ;
  }
  
  public boolean hasModeratorRole() { return acl_.hasModeratorRole(forum_) ; }

  public boolean hasViewForumRole() { return acl_.hasViewForumRole(forum_) ; }

  public boolean hasCreateTopicRole() { return acl_.hasCreateTopicRole(forum_) ; }

  public boolean hasReplyTopicRole() { return acl_.hasReplyTopicRole(forum_) ; }
  
  public String getRemoteUser() { return acl_.getRemoteUser() ; } 

  public UIPageListIterator getUIPageIterator() { return uiPageIterator_ ; }
  
  public boolean canDecodeInvalidState()  { return forum_ != null  || topic_ != null; }  
  
  static public class ViewTopicsActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;			
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UITopics.class);
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class ViewForumsActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;			
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UIViewCategories.class);
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class NewTopicActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;
			UITopics uiTopics = (UITopics) uiPosts.getSibling(UITopics.class) ;
	    Forum forum = uiTopics.getForum() ;
	    UIPostForm uiForm = (UIPostForm) uiPosts.getSibling(UIPostForm.class) ;
	    uiForm.setNewTopicInfo(forum) ;
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UIPostForm.class);  
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class QuotePostActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;
			String postId = event.getParameter("postId") ;
      Post post = uiPosts.service_.getPost(postId) ;
      UIPostForm uiForm = (UIPostForm) uiPosts.getSibling(UIPostForm.class) ;
      uiForm.setQuotePostInfo(uiPosts.topic_, post) ;
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UIPostForm.class);    
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class EditPostActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;
			String postId = event.getParameter("postId") ;
      Post post = uiPosts.service_.getPost(postId) ;
      UIPostForm uiForm = (UIPostForm) uiPosts.getSibling(UIPostForm.class) ;
      uiForm.setEditPostInfo(uiPosts.topic_, post) ;			      
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UIPostForm.class);       
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class ReplyActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPosts uiPosts = (UIPosts) event.getComponent() ;
			UIPostForm uiForm = (UIPostForm) uiPosts.getSibling(UIPostForm.class) ;
	    uiForm.setNewPostInfo(uiPosts.topic_) ;			
      UIForumPortlet forumPortlet = (UIForumPortlet)uiPosts.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiPosts);      
      forumPortlet.setRenderedComponent(UIPostForm.class);
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
}