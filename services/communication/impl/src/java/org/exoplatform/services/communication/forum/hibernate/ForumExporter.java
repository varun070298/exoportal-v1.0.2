/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.exoplatform.xml.object.XMLObject;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 21, 2004
 * @version $Id$
 */
public class ForumExporter {
  private Session session_ ;
  private ZipOutputStream out_ ;
  
  public ForumExporter(Session session, ZipOutputStream out) {
    session_ = session ;
    out_ = out ;
  }
  
  public void export() throws Exception {
    Query q = session_.createQuery(ForumServiceContainerImpl.queryForumOwners);
    List owners = q.list() ;
    for(int i = 0; i < owners.size(); i++ ) {
      String owner = (String) owners.get(i) ;
      export(owner) ;
    }
  }
  
  public void export(String owner) throws Exception {
    List  categories = session_.find(ForumServiceImpl.queryCategoriesByOwner, owner, Hibernate.STRING );
    for(int i = 0; i < categories.size(); i++) {
      CategoryImpl cat = (CategoryImpl) categories.get(i) ;
      exportCategory(owner, cat) ; 
    }
  }
  
  public void exportCategory(String path ,CategoryImpl category) throws Exception {
    String categoryPath = path + "/"  + category.getId() ;
    ZipEntry entry = new ZipEntry(categoryPath + "/" + category.getId() +  ".category") ;
    out_.putNextEntry(entry) ;
    XMLObject xmlobject = new XMLObject(category) ;
    out_.write(xmlobject.toByteArray()) ;
    out_.closeEntry() ;
    List forums = session_.find(ForumServiceImpl.queryForumsByCategory, category.getId(), Hibernate.STRING );
    for(int i = 0; i < forums.size(); i++) {
      ForumImpl forum = (ForumImpl)  forums.get(i) ;
      exportForum(categoryPath ,forum) ;
    }
  }
  
  public void exportForum(String path ,ForumImpl forum) throws Exception {
    String forumPath  =  path + "/"  +forum.getId(); 
    ZipEntry entry = new ZipEntry(forumPath+ "/" + forum.getId() +  ".forum") ;
    out_.putNextEntry(entry) ;
    XMLObject xmlobject = new XMLObject(forum) ;
    out_.write(xmlobject.toByteArray()) ;
    out_.closeEntry() ;
//    String queryTopicsByForum =
//      "from topic in class org.exoplatform.services.forum.hibernate.TopicImpl " +
//      "where topic.forumId like ?";   
//    System.out.println("\n\n"+ forum.getId()+" \n\n");
    List topics = session_.find( 
        ForumServiceImpl.queryTopicsByForum, forum.getId(), Hibernate.STRING );
    for(int i = 0; i < topics.size();  i++) {
      TopicImpl topic = (TopicImpl) topics.get(i) ;
      exportTopic(forumPath, topic) ;
    }
  }
  
  public void exportTopic(String path ,TopicImpl topic) throws Exception {
    ZipEntry entry = new ZipEntry(path + "/" + topic.getId() +  ".topic") ;
    out_.putNextEntry(entry) ;
    List  posts = session_.find(
        ForumServiceImpl.queryPostsByTopic, topic.getId(), Hibernate.STRING) ;
    TopicBackup backup = new TopicBackup(topic, posts) ;
    XMLObject xmlobject = new XMLObject(backup);
    try{
      out_.write( xmlobject.toByteArray()) ;
    }catch( Exception exp){
//      exp.printStackTrace();
//      System.out.println( "topic"+topic.getTopic());
//      for( int i = 0; i < posts.size(); i++){
//        System.out.println(  ((PostImpl)posts.get( i)).getSubject());
//        System.out.println( "---------------------------------------------");
//        System.out.println(  ((PostImpl)posts.get( i)).getMessage());
//      }
     
    }
    out_.closeEntry() ;
  }
}
