/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.test;

import java.util.List;
import org.exoplatform.commons.utils.PageList ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.forum.*;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.test.BasicTestCase;


/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: ForumServiceTestCase.java,v 1.8 2004/07/29 14:09:47 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
abstract public class ForumServiceTestCase extends BasicTestCase {
  static protected ForumService service_ ;
  static protected ForumServiceContainer container_ ;
  
  public ForumServiceTestCase(String name) {
    super(name);
  }
  
  public void tearDown() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    HibernateService hservice = 
        (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
    hservice.closeSession();
  }
  
  public void testForumService() throws Exception {
    Category cat = createCategory("category", "this is a test") ;
    assertEquals("check category name: ", "category", cat.getCategoryName()) ;
    assertEquals("check category desc: ", "this is a test", cat.getDescription()) ;

    Forum forum = createForum(cat, "forum", "this is a test") ;
    assertEquals("check forum name: ", "forum", forum.getForumName()) ;
    assertEquals("check forum desc: ", "this is a test", forum.getDescription()) ;
    assertEquals("check forum post count: ", 0 , forum.getPostCount()) ;
    assertEquals("check forum topic count: ", 0 , forum.getTopicCount()) ;
    Watcher forumWatcher = service_.createWatcher(forum) ;
    forumWatcher.setUserName("guest") ;
    forumWatcher.setMessageProtocol("smtp") ;
    forumWatcher.setAddress("exo.platform@laposte.net") ;
    service_.saveWatcher(forumWatcher) ;
    forumWatcher = service_.getWatcher(forum, "guest") ;
    assertTrue("expect forum watcher is found", forumWatcher != null) ;

    Topic topic = createTopic(forum, "topic", "this is a test") ;
    assertEquals("check topic : ", "topic", topic.getTopic()) ;
    assertEquals("check topic desc: ", "this is a test", topic.getDescription()) ;
    assertEquals("check topic post  count: ", 0 , topic.getPostCount()) ;
    Watcher topicWatcher = service_.createWatcher(topic) ;
    topicWatcher.setUserName("guest") ;
    topicWatcher.setMessageProtocol("smtp") ;
    topicWatcher.setAddress("exo.platform@laposte.net") ;
    service_.saveWatcher(topicWatcher) ;
    topicWatcher = service_.getWatcher(topic, "guest") ;
    assertTrue("expect topic watcher is found", topicWatcher != null) ;
    
    System.out.println("-------------------------------------------------------");
    
    Post post = createPost(topic, "post", "this is a test") ;
    assertEquals("check post subject : ", "post", post.getSubject()) ;
    assertEquals("check post message : ", "this is a test", post.getMessage()) ;
    
    forum = service_.getForum(forum.getId()) ;// with no cache forum has been updated in db
    topic = service_.getTopic(topic.getId()) ;// with no cache topic has been updated in db
    assertEquals("check forum topic count: ", 1 , forum.getTopicCount()) ;
    assertEquals("check forum post count: ", 1 , forum.getPostCount()) ;
    assertEquals("check topic post  count: ", 1 , topic.getPostCount()) ;
    
    Post post2 = createPost(topic, "post2", "this is a test") ;
    assertEquals("check post subject : ", "post2", post2.getSubject()) ;
    assertEquals("check post message : ", "this is a test", post.getMessage()) ;

    topic = service_.getTopic(topic.getId()) ;// with no cache topic has been updated in db
    forum = service_.getForum(forum.getId()) ;// with no cache forum has been updated in db
    assertEquals("check topic post  count: ", 2 , topic.getPostCount()) ;
    assertEquals("check forum topic count: ", 1 , forum.getTopicCount()) ;
    assertEquals("check forum post count: ", 2 , forum.getPostCount()) ;

    
    cat = createCategory("2nd category", "this is a test") ;
    List postList = service_.getPosts(topic.getId()).getAll() ;
    assertEquals("check number of post in topic: ", 2 , postList.size()) ;
    for (int i = 0; i < postList.size(); i++) {
      Post test = (Post) postList.get(i) ;
    }
    
    runExportData() ;
    
    service_.removePost(post2.getId()) ;
    forum = service_.getForum(forum.getId()) ;// with no cache forum has been updated in db
    topic = service_.getTopic(topic.getId()) ;// with no cache topic has been updated in db
    assertEquals("check forum post count: ", 1 , forum.getPostCount()) ;
    assertEquals("check forum topic count: ", 1 , forum.getTopicCount()) ;
    assertEquals("check topic post  count: ", 1 , topic.getPostCount()) ;
    try {
      service_.removeTopic(topic.getId()) ;
      topic = service_.getTopic(topic.getId()) ;
    } catch (ForumServiceException ex) {
      assertEquals("check topic not found exception ", ex.TOPIC_NOT_FOUND, ex.getErrorCode()) ;
    }

    try {
      service_.removeForum(forum.getId()) ;
      forum = service_.getForum(forum.getId()) ;
    } catch (ForumServiceException ex) {
      assertEquals("check forum not found exception ", ex.FORUM_NOT_FOUND, ex.getErrorCode()) ;
    }

    forum = createForum(cat, "forum", "this is a test") ;
    for (int i = 0; i < 25; i++) {
      createTopic(forum, "topic" + i, "this is a test") ;
    }
    List topicList = service_.getTopics(forum.getId()).getAll() ;
    assertEquals("check number of topics in forum: ", 25 , topicList.size()) ;
    PageList pages = service_.getTopics(forum.getId()) ;
    pages.setPageSize(10) ;
    topicList = pages.getPage(1) ;
    assertEquals("check number of topics in forum: ", 10 , topicList.size()) ;

    try {
      service_.removeCategory(cat.getId()) ;
      cat = service_.getCategory(cat.getId()) ;
    } catch (ForumServiceException ex) {
      assertEquals("check category not found exception ", ForumServiceException.CATEGORY_NOT_FOUND, ex.getErrorCode()) ;
    }
    
    //tearDown() ;
    runImportData() ;
    //make sure that the task thread is finished
    Thread.sleep(1000) ;
  }
 
  private Category createCategory(String name, String desc) throws Exception {
    Category category = service_.createCategoryInstance() ; 
    category.setCategoryName(name) ;
    category.setDescription(desc) ;
    category = service_.addCategory(category) ;
    return category ;
  }
  
  private Forum createForum(Category cat, String name, String desc) throws Exception {
    Forum forum = service_.createForumInstance() ; 
    forum.setForumName(name) ;
    forum.setDescription(desc) ;
    forum = service_.addForum(cat, forum) ;
    return forum ;
  }

  private Topic createTopic(Forum forum, String name, String desc) throws Exception {
    Topic topic = service_.createTopicInstance() ; 
    topic.setOwner("exo") ;
    topic.setModifiedBy("exo") ;
    topic.setTopic(name) ;
    topic.setDescription(desc) ;
    topic = service_.addTopic(forum, topic) ;
    return topic ;
  }

  private Post createPost(Topic topic, String subject, String message) throws Exception {
    Post post = service_.createPostInstance() ; 
    post.setOwner("exo") ;
    post.setModifiedBy("exo") ;
    post.setSubject(subject) ;
    post.setMessage(message) ;
    post = service_.addPost(topic, post) ;
    return post ;
  }
  
  abstract public void runExportData() throws Exception  ;
  abstract  public void runImportData() throws Exception ;
  
  protected String getDescription() {
    return "Test Forum Service" ;
  }
}

