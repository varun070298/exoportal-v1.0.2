/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import org.exoplatform.services.communication.forum.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 27, 2004
 * @version $Id$
 * @hibernate.class  table="FORUM_WATCHER"
 */
public class WatcherImpl implements Watcher {
  final static public String TOPIC_TARGET =  "topic" ;
  final static public String FORUM_TARGET =  "forum" ;
  
  private String id ;
  private String topicId ;
  private String forumId ;
  private String target ;
  private String userName ;
  private String protocol ;
  private String address ;
  
  public WatcherImpl() { }
  
  public WatcherImpl(Topic topic) {
    TopicImpl impl  = (TopicImpl) topic ;
    target = TOPIC_TARGET ;
    topicId = topic.getId() ;
    forumId =  impl.getId();
  }
  
  public WatcherImpl(Forum forum) {   
    target = FORUM_TARGET ;
    forumId = forum.getId() ;
  }
  
  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String getId() { return id; }
  public void   setId(String s) { id = s ; }
  
  /**
   * @hibernate.property
   **/
  public String getTopicId() { return topicId; }
  public void  setTopicId(String s) { topicId = s ; }
  
  /**
   * @hibernate.property
   **/
  public String getForumId() { return forumId; }
  public void  setForumId(String s) { forumId = s ; }
  
  /**
   * @hibernate.property
   **/
  public String getTarget() { return target; }
  public void  setTarget(String s) { target = s ; }
  
  /**
   * @hibernate.property
   **/
  public String getUserName() {  return userName; }
  public void setUserName(String username) { this.userName = username ; }
  
  /**
   * @hibernate.property
   **/
  public String getMessageProtocol() { return protocol ; } 
  public void   setMessageProtocol(String s) { protocol = s ; }
  
  /**
   * @hibernate.property
   **/
  public String getAddress() {  return address ; }
  public void   setAddress(String email) { this.address = email ;} 
  
}
