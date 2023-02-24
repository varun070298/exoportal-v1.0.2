/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

import java.util.List;
import org.exoplatform.commons.utils.PageList ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface ForumService  {
  
  public List     getCategories() throws Exception ;
  public Category getCategory( String id)  throws Exception  ;
  public Category addCategory(Category category) throws Exception ;
  public Category removeCategory( String id) throws Exception ;
  public Category updateCategory(Category cat) throws Exception ;
  public Category createCategoryInstance() ;

  public List     getForums( String categoryId) throws Exception ;
  public Forum    getForum( String id) throws Exception ;
  public Forum    addForum(Category category, Forum forum) throws Exception ;
  public Forum    removeForum( String id) throws Exception ;
  public Forum    updateForum(Forum forum) throws Exception ;
  public Forum    createForumInstance() ;

  public PageList getTopics( String forumId) throws Exception ;
  public Topic    getTopic( String id) throws Exception ;
  public Topic    addTopic(Forum forum, Topic topic) throws Exception ;
  public Topic    removeTopic( String id) throws Exception ;
  public Topic    updateTopic(Topic topic) throws Exception ;
  public Topic    createTopicInstance() ;

  public PageList getPosts( String topicId) throws Exception ;
  public Post     getPost( String id) throws Exception ;
  public Post     addPost(Topic topic, Post post) throws Exception ;
  public Post     removePost(String id) throws Exception ;
  public Post     updatePost(Post post) throws Exception ;
  public Post     createPostInstance() ;
  
  public Watcher createWatcher(Forum forum) throws Exception ;
  public Watcher createWatcher(Topic topic) throws Exception ;
  public Watcher getWatcher(Topic topic, String userName) throws Exception ;
  public Watcher getWatcher(Forum forum, String userName) throws Exception ;
  public void    saveWatcher(Watcher watcher) throws Exception ;
  public void    removeWatcher(Watcher watcher) throws Exception ;
  
  public long     getLastModifiedTime() ;
}
