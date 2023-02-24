/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.List;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.communication.forum.* ;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 25, 2004
 * @version $Id$
 */
public class ForumDependencyObjectManager extends ForumEventListener {
  public ForumDependencyObjectManager(ForumServiceContainer container) {
    super(container) ;
  }
  
  public void onDelete(XResources resources, Category category) throws Exception  {  
    Session session = (Session) resources.getResource(Session.class) ;
    List forums = session.find(ForumServiceImpl.queryForumsByCategory, 
                               category.getId(), Hibernate.STRING );
    for (int i = 0; i < forums.size(); i++) {
      Forum forum = (Forum) forums.get(i) ;
      onDelete(resources, forum) ;
    }
    session.delete(ForumServiceImpl.queryForumsByCategory, category.getId(), Hibernate.STRING );
  }
  
  public void onDelete(XResources resources, Forum forum) throws Exception  {
    Session session = (Session) resources.getResource(Session.class) ;
    session.delete(ForumServiceImpl.queryTopicsByForum, forum.getId(), Hibernate.STRING );
    session.delete(ForumServiceImpl.queryPostsByForum, forum.getId(), Hibernate.STRING );
    session.delete("from w in class org.exoplatform.services.communication.forum.hibernate.WatcherImpl " + 
                   "where w.forumId = ?",
                   forum.getId(), Hibernate.STRING );
  }
  
  public void onDelete(XResources resources, Topic topic) throws Exception  {
    Session session = (Session) resources.getResource(Session.class) ;
    session.delete(ForumServiceImpl.queryPostsByTopic, topic.getId(), Hibernate.STRING);
    session.delete("from w in class org.exoplatform.services.communication.forum.hibernate.WatcherImpl " + 
                   "where w.topicId = ?",
                   topic.getId(), Hibernate.STRING );
  }
  
  public void onDelete(XResources resources, Post post) throws Exception  { }
}