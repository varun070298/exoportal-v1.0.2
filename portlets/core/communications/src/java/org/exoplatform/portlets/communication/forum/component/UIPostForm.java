/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.ResourceBundle;
import javax.faces.context.FacesContext ;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UITextArea ;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.Post;
import org.exoplatform.services.communication.forum.Topic;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPostForm.java,v 1.12 2004/10/20 23:39:59 benjmestrallet Exp $
 */
public class UIPostForm extends UISimpleForm {
  private ForumService service_ ;
  private UIStringInput subject_ ;
  private UITextArea    message_ ;
  private Post post_ ;
  private Topic topic_ ;
  private Forum forum_ ; 
  
  public UIPostForm(ForumService service) throws Exception {
    super("postForm", "post", null) ;
    setId("UIPostForm") ;
    setClazz("UIPostForm");
    service_ = service ;    
    message_ = new UITextArea("message", "") ;
    subject_ = new UIStringInput("subject", "") ;
        
    add(new HeaderRow().
        add(new Cell("#{UIPostForm.header.title}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPostForm.label.subject}")).
        add(new ComponentCell(this, subject_))) ;
    add(new Row().
        add(new LabelCell("#{UIPostForm.label.message}").addValign("top")).
        add(new ComponentCell(this, message_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIPostForm.button.save}", SAVE_ACTION)).
            add(new FormButton("#{UIPostForm.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION)  ;
    addActionListener(SaveActionListener.class, SAVE_ACTION)  ;
  }
  
  public void setNewTopicInfo(Forum forum) {
    forum_ = forum ;
    topic_ = null ;
    post_ = null ;
    subject_.setText("") ;
    message_.setText("") ;
  }

  public void setNewPostInfo(Topic topic) {
    forum_ = null ;
    topic_ = topic ;
    post_ = null ;
    subject_.setText("") ;
    message_.setText("") ;
  }

  public void setQuotePostInfo(Topic topic, Post post) {
    forum_ = null ;
    topic_ = topic ;
    post_ = null ;
    subject_.setText(post.getSubject()) ;
    message_.setText(post.getMessage()) ;
  }

  public void setEditPostInfo(Topic topic, Post post) {
    forum_ = null ;
    topic_ = topic ;
    post_ = post ;
    subject_.setText(post.getSubject()) ;
    message_.setText(post.getMessage()) ;
  }
  
  public boolean canDecodeInvalidState()  { return forum_ != null  || topic_ != null; }
  
  static public class CancelActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPostForm uiForm = (UIPostForm) event.getComponent() ;
      UIForumPortlet forumPortlet = (UIForumPortlet)uiForm.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(uiForm);      
			if (uiForm.topic_ == null) 	{              
        forumPortlet.setRenderedComponent(UITopics.class);                        
      } else 	{
        forumPortlet.setRenderedComponent(UIPosts.class);        
      }
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIPostForm uiForm = (UIPostForm) event.getComponent() ;
			 String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() ;
       String subject = uiForm.subject_.getText() ;
       String message = uiForm.message_.getText() ;
       if(subject == null || subject.length() == 0) {
         ResourceBundle res = event.getApplicationResourceBundle() ;
         subject = res.getString("UIPostForm.msg.no-subject");
       }
       Topic topic = uiForm.topic_ ;
       boolean isNewTopic = false  ;
       if (topic == null) {
         topic = uiForm.service_.createTopicInstance() ;
         topic.setOwner(user) ;
         topic.setModifiedBy(user) ;
         topic.setTopic(subject) ;
         topic.setDescription("") ;
         uiForm.service_.addTopic(uiForm.forum_, topic) ;
         isNewTopic = true ;
       }
       
       boolean isNewPost = false ;
       Post post = uiForm.post_ ;
       if(post == null) {
         post = uiForm.service_.createPostInstance() ;
         post.setOwner(user) ;
         isNewPost = true ;
       }

       post.setModifiedBy(user) ;
       post.setSubject(subject) ;
       post.setMessage(message) ;
       post.setRemoteAddr(SessionContainer.getInstance().getMonitor().getIPAddress()) ;
       if(isNewPost) uiForm.service_.addPost(topic, post) ;
       else  uiForm.service_.updatePost(post) ;
       UITopics uiTopics = (UITopics)uiForm.getSibling(UITopics.class) ;
       if(isNewTopic || isNewPost) {
         uiTopics.reload() ;
       }
			 uiTopics.visit( topic.getId() );
       UIPosts uiPosts = (UIPosts) uiForm.getSibling(UIPosts.class) ;
       uiPosts.setUIPostsData(uiTopics.getForum() , topic) ;
           
       UIForumPortlet forumPortlet = (UIForumPortlet)uiForm.getAncestorOfType(UIForumPortlet.class);
       forumPortlet.addHistoryElement(uiForm);      
       forumPortlet.setRenderedComponent(UIPosts.class);
       ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
		}
  }
  
}