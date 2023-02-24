/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.List ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 21, 2004
 * @version $Id$
 */
public class TopicBackup {
  private TopicImpl topic ;
  private List posts ;
  
  public TopicBackup(TopicImpl topic , List posts) {
    this.topic = topic ;
    this.posts = posts ;
  }
  
  
  public List getPosts() {   return posts; }
  public void setPosts(List posts) {   this.posts = posts; }
  
  public TopicImpl getTopic() {   return topic; }
  public void setTopic(TopicImpl topic) {   this.topic = topic; }
}