/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.validator.EmptyFieldValidator ;
import org.exoplatform.services.communication.forum.Category;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumService;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIForumForm.java,v 1.7 2004/10/17 13:46:25 tuan08 Exp $
 */
public class UIForumForm extends UIForm {
  private static String FORUM_NAME = "forumName" , FORUM_DESC = "forumDesc" ,
                        VIEW_FORUM_ROLE = "viewRole", CREATE_TOPIC_ROLE = "createTopic",
                        REPLY_TOPIC_ROLE = "replyTopic", MODERATORS = "moderators" ,
                        FORUM_ORDER = "order"  ;
  
  private ForumService service_ ;
  private Forum forum_ ;
  private String categoryId_ ;
  
  public UIForumForm(ForumService service) throws Exception {
    super("forumForm") ;
    setId("UIForumForm") ;
    service_ = service ;
    
    addContainer("#{UIForumForm.header.title}").
      add(new StringField(FORUM_NAME, "#{UIForumForm.label.name}", "")).
      add(new TextAreaField(FORUM_DESC, "#{UIForumForm.label.description}", "")).
      add(new StringField(VIEW_FORUM_ROLE, "#{UIForumForm.label.view-forum-role}", "")).
      add(new StringField(CREATE_TOPIC_ROLE, "#{UIForumForm.label.create-topic-role}", "")).
      add(new StringField(REPLY_TOPIC_ROLE, "#{UIForumForm.label.reply-topic-role}", "")).
      add(new StringField(MODERATORS, "#{UIForumForm.label.moderators}", "")).
      add(new IntegerField(FORUM_ORDER, "#{UIForumForm.label.forum-order}", 0));
    addButton(new Button("#{UIForumForm.button.save}", SAVE_ACTION)) ;
    addButton(new Button("#{UIForumForm.button.cancel}", CANCEL_ACTION)) ;
    addFieldValidator(EmptyFieldValidator.class) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION)  ;
    addActionListener(SaveActionListener.class, SAVE_ACTION)  ;
  }
  
  public void setForum(String categoryId, Forum forum) {
    forum_ = forum ;
    categoryId_ = categoryId ;
    if (forum == null) {
      resetFields() ;
    } else {
      setStringFieldValue(FORUM_NAME, forum.getForumName()) ;
      setStringFieldValue(FORUM_DESC, forum.getDescription()) ;
      setStringFieldValue(VIEW_FORUM_ROLE, forum.getViewForumRole()) ;
      setStringFieldValue(CREATE_TOPIC_ROLE, forum.getCreateTopicRole()) ;
      setStringFieldValue(REPLY_TOPIC_ROLE, forum.getReplyTopicRole()) ;
      setStringFieldValue(MODERATORS, forum.getModerators()) ;
      setIntegerFieldValue(FORUM_ORDER, forum.getForumOrder()) ;
    }
  }
  
  public boolean canDecodeInvalidState()  { return categoryId_ != null; }
  
  static public class CancelActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIForumForm uiForm = (UIForumForm) event.getComponent() ;
			uiForm.setRenderedSibling(UIAdminViewCategories.class) ;
		}
  }
  
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIForumForm uiForm = (UIForumForm) event.getComponent() ;
			Forum forum = uiForm.forum_ ;
      boolean isNew =  false ;
      if(forum == null) {
      	forum = uiForm.service_.createForumInstance() ;
        isNew = true ;
      }
      forum.setForumName(uiForm.getStringFieldValue(FORUM_NAME)) ;
      forum.setDescription(uiForm.getStringFieldValue(FORUM_DESC)) ;
      forum.setViewForumRole(uiForm.getStringFieldValue(VIEW_FORUM_ROLE)) ;
      forum.setCreateTopicRole(uiForm.getStringFieldValue(CREATE_TOPIC_ROLE)) ;
      forum.setReplyTopicRole(uiForm.getStringFieldValue(REPLY_TOPIC_ROLE)) ;
      forum.setModerators(uiForm.getStringFieldValue(MODERATORS)) ;
      forum.setForumOrder(uiForm.getIntegerFieldValue(FORUM_ORDER)) ;
      if(isNew) {
        Category category = uiForm.service_.getCategory(uiForm.categoryId_) ;
        uiForm.service_.addForum(category , forum) ;
      } else {
        uiForm.service_.updateForum(forum) ;
      }
      UIAdminViewCategories uiCategories =
       	(UIAdminViewCategories) uiForm.getSibling(UIAdminViewCategories.class) ;
      uiCategories.update() ;
			uiForm.setRenderedSibling(UIAdminViewCategories.class) ;
		}
  }
}