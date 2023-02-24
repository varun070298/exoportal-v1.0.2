/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import org.exoplatform.faces.core.Util ;
import org.exoplatform.faces.application.ExoFacesMessage ;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.communication.forum.ForumACL;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.Watcher ;
import org.exoplatform.services.communication.forum.Topic;
import org.exoplatform.services.organization.OrganizationService ;
import org.exoplatform.services.organization.User ;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UITopics.java,v 1.10 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class UITopics extends UIExoCommand {
  public  static final String TOPIC_ID = "topicId";
  private static final String NEW_TOPIC = "newTopic";
  private static final String VIEW_FORUMS = "viewForums";
  private static final String WATCH_TOPIC = "watchTopic";
  private static final String STOP_WATCH_TOPIC  = "stopWatchTopic";
  private static final String WATCH_FORUM = "watchForum";
  private static final String STOP_WATCH_FORUM  = "stopWatchForum";
  
  public static Parameter VIEW_POSTS = new Parameter( ACTION , "viewPosts") ;
  public static Parameter DELETE_TOPIC = new Parameter(ACTION , "deleteTopic") ;
  public static Parameter[] newTopicParams = { new Parameter(ACTION , NEW_TOPIC)} ;
  public static Parameter[] viewForumsParams = { new Parameter(ACTION , VIEW_FORUMS)} ;
  public static Parameter[] watchForumParams = { new Parameter(ACTION , WATCH_FORUM)} ;
  public static Parameter[] stopWatchForumParams = { new Parameter(ACTION , STOP_WATCH_FORUM)} ;
  public static Parameter watchTopicParam =  new Parameter(ACTION , WATCH_TOPIC ) ;
  public static Parameter stopWatchTopicParam = new Parameter(ACTION , STOP_WATCH_TOPIC) ;
  
  private Forum forum_ ;
  private ForumService forumService_ ;
  private OrganizationService orgService_ ;
  private ForumACL acl_ ;
  private UIPageListIterator uiPageIterator_ ;  
  private User user_ ;
//  private long lastAccessTime_ = System.currentTimeMillis() + 24*60*60*1000;//tomorrow todo change on more logical
	private Map visitedTopics_ = new HashMap();

	public UITopics(OrganizationService orgService,
                  ForumService service,ForumACL acl) throws Exception {
    setRendererType("TopicsRenderer") ;
    setId("UITopics") ;       
    uiPageIterator_ = new UIPageListIterator(new PageListDataHandler()) ;
    getChildren().add(uiPageIterator_);
    forumService_ = service ;
    orgService_ = orgService ;
    acl_ = acl ;
    String userName = Util.getRemoteUser();
    if( userName != null ){
    	user_ = orgService_.findUserByName( userName );
    }
    
    addActionListener(ViewPostsActionListener.class, "viewPosts")  ;
    addActionListener(ViewForumsActionListener.class, VIEW_FORUMS)  ;
    addActionListener(NewTopicActionListener.class, NEW_TOPIC);
    addActionListener(DeleteTopicActionListener.class, "deleteTopic")  ;
    addActionListener(WatchTopicActionListener.class, WATCH_TOPIC)  ;
    addActionListener(StopWatchTopicActionListener.class, STOP_WATCH_TOPIC)  ;
    addActionListener(WatchForumActionListener.class, WATCH_FORUM)  ;
    addActionListener(StopWatchForumActionListener.class, STOP_WATCH_FORUM)  ;
  }
  
  public boolean isModerator() { return acl_.hasModeratorRole(forum_) ; }

  public boolean hasViewForumRole() { return acl_.hasViewForumRole(forum_) ; }

  public boolean hasCreateTopicRole() { return acl_.hasCreateTopicRole(forum_) ; }

  public boolean hasReplyTopicRole() { return acl_.hasReplyTopicRole(forum_) ; }
  
  public User getUser() { return user_; }
  
  public Forum getForum() { return forum_ ; }
  
  public boolean setForum(Forum forum) throws Exception {
    if(!acl_.hasViewForumRole(forum)) return false ;
    forum_ = forum ; 
    uiPageIterator_.setPageList(forumService_.getTopics(forum.getId())) ;
    return true ;
  }
  
  public void reload() throws Exception {
    if (forum_ == null) return ;
    uiPageIterator_.setPageList(forumService_.getTopics(forum_.getId())) ;
  }

  public UIPageListIterator getUIPageIterator() { return uiPageIterator_ ; }  
  
  public boolean canDecodeInvalidState()  { return forum_ != null  ;}
	public void visit( String topicId) {
		visitedTopics_.put( topicId, new Date() );
	}
  public boolean hasNewPosts( Topic topic ) {
    boolean retVal = false;
	  Date lastVisitDate = (Date)visitedTopics_.get( topic.getId() );
    if( lastVisitDate != null ) {
      //somebody post new message in during user session but user didn't visit yet this topic
      retVal = lastVisitDate.getTime()  < topic.getLastPostDate().getTime();
    } else if(  user_ != null && user_.getLastLoginTime().getTime() < topic.getLastPostDate().getTime() ){
      //somebody post new message after login of previous user session.
      retVal = true;
    };
	  return retVal;
  }
  static public class ViewPostsActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
	  UITopics uiTopics = (UITopics) event.getComponent() ;
      String topicId = event.getParameter(TOPIC_ID) ;
      Topic topic = uiTopics.forumService_.getTopic(topicId) ;
      UIPosts uiPosts = (UIPosts)uiTopics.getSibling(UIPosts.class) ;
      uiPosts.setUIPostsData(uiTopics.forum_, topic) ;			    
      UIForumPortlet forumPortlet = (UIForumPortlet)uiTopics.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiTopics);      
      forumPortlet.setRenderedComponent(UIPosts.class);   
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
      uiTopics.visit( topicId );
    }
  }
  
  static public class ViewForumsActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UITopics uiTopics = (UITopics) event.getComponent() ;			
      UIForumPortlet forumPortlet = (UIForumPortlet)uiTopics.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiTopics);      
      forumPortlet.setRenderedComponent(UIViewCategories.class);      
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class NewTopicActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UITopics uiTopics = (UITopics) event.getComponent() ;
			UIPostForm uiForm = (UIPostForm) uiTopics.getSibling(UIPostForm.class) ;
			uiForm.setNewTopicInfo(uiTopics.forum_) ;	    
      UIForumPortlet forumPortlet = (UIForumPortlet)uiTopics.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiTopics);      
      forumPortlet.setRenderedComponent(UIPostForm.class);   
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class DeleteTopicActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UITopics uiTopics = (UITopics) event.getComponent() ;
			if(!uiTopics.acl_.hasModeratorRole(uiTopics.forum_)) return ;
			String topicId = event.getParameter(TOPIC_ID) ;
      uiTopics.forumService_.removeTopic(topicId) ;
      uiTopics.reload() ;
		}
  }
  
  static public class WatchTopicActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITopics uiTopics = (UITopics) event.getComponent() ;
      User user = uiTopics.getUser() ;
      Topic topic = uiTopics.forumService_.getTopic(event.getParameter(TOPIC_ID)) ;
      Watcher watcher = uiTopics.forumService_.getWatcher(topic, user.getUserName()) ;
      if(watcher == null) {
        watcher = uiTopics.forumService_.createWatcher(topic) ;
        watcher.setUserName(user.getUserName()) ;
        watcher.setAddress(user.getEmail()) ;
      } 
      UIWatchForm uiForm = (UIWatchForm) uiTopics.getSibling(UIWatchForm.class) ;
      uiForm.setWatcher(watcher) ;
      uiTopics.setRenderedSibling(UIWatchForm.class) ;
    }
  }
  
  static public class StopWatchTopicActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITopics uiTopics = (UITopics) event.getComponent() ;
      Topic topic = uiTopics.forumService_.getTopic(event.getParameter(TOPIC_ID)) ;
      Watcher watcher = uiTopics.forumService_.getWatcher(topic, uiTopics.user_.getUserName()) ;
      InformationProvider iprovider = findInformationProvider(uiTopics);
      if(watcher  != null) {
        uiTopics.forumService_.removeWatcher(watcher) ;
        Object[] args = {topic.getTopic()} ;
        iprovider.addMessage(new ExoFacesMessage("#{UITopics.msg.stop-topic-watch-success}", args)) ;
      } else {
        iprovider.addMessage(new ExoFacesMessage("#{UITopics.msg.stop-topic-watch-fail}")) ;
      }
    }
  }
  
  static public class WatchForumActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITopics uiTopics = (UITopics) event.getComponent() ;
      User user = uiTopics.getUser();
      Watcher watcher = uiTopics.forumService_.getWatcher(uiTopics.forum_, user.getUserName()) ;
      if(watcher == null) {
        watcher = uiTopics.forumService_.createWatcher(uiTopics.forum_) ;
        watcher.setUserName(user.getUserName()) ;
        watcher.setAddress(user.getEmail()) ;
      } 
      UIWatchForm uiForm = (UIWatchForm) uiTopics.getSibling(UIWatchForm.class) ;
      uiForm.setWatcher(watcher) ;
      uiTopics.setRenderedSibling(UIWatchForm.class) ;
    }
  }
  
  static public class StopWatchForumActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITopics uiTopics = (UITopics) event.getComponent() ;
      Watcher watcher = uiTopics.forumService_.getWatcher(uiTopics.forum_, uiTopics.getUser().getUserName()) ;
      InformationProvider iprovider = findInformationProvider(uiTopics);
      if(watcher  != null) {
        uiTopics.forumService_.removeWatcher(watcher) ;
        Object[] args = {uiTopics.forum_.getForumName()} ;
        iprovider.addMessage(new ExoFacesMessage("#{UITopics.msg.stop-forum-watch-success}", args)) ;
      } else {
        iprovider.addMessage(new ExoFacesMessage("#{UITopics.msg.stop-forum-watch-fail}")) ;
      }
    }
  }
}