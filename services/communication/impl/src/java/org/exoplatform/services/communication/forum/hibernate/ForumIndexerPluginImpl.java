/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.List;
import java.util.Date;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.communication.forum.*;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.indexing.BaseIndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 13, 2004
 * @version $Id: ForumIndexerPluginImpl.java,v 1.3 2004/11/01 15:07:50 tuan08 Exp $
 */
public class ForumIndexerPluginImpl extends BaseIndexerPlugin implements ForumIndexerPlugin {
  private HibernateService hservice_ ;
  
  public ForumIndexerPluginImpl(IndexingService iservice,
                                HibernateService hservice) {
    super(iservice) ;
    hservice_ = hservice ;
  }
  
  public Document createDocument(Forum forum, PostImpl post)  throws Exception {
    String identifier =  post.getId() ;
    String title =  post.getSubject() ;
    if(title == null ) title = "-----------No Subject----------" ;
    String textToIndex = post.getMessage();
    if(textToIndex == null) textToIndex = "" ;
    String owner = post.getOwner() ;
    if(owner == null) owner = "" ;
    String desc =  getContentDescription(textToIndex, 200);
    Document doc =   createBaseDocument(identifier, owner, title, desc , 
                                        textToIndex, forum.getViewForumRole())  ;
    doc.add(Field.Text(CATEGORY_ID_FIELD,  forum.getCategoryId())) ;
    doc.add(Field.Text(FORUM_ID_FIELD,  post.getForumId())) ;
    doc.add(Field.Text(TOPIC_ID_FIELD,  post.getTopicId())) ;
    doc.add(Field.Text(POST_ID_FIELD,   post.getId())) ;
    Date date = post.getModifiedDate() ;
    if(date == null || date.getTime() == 0) {
      //System.out.println("date is null for: " + post.getSubject()) ;
    }
    doc.add(Field.Text(DATE_FIELD, DateField.dateToString(post.getModifiedDate()))) ;
    return doc ;
  }
  
  public String getPluginIdentifier() { return IDENTIFIER  ; }
  
  public Object getObject(String user,String objectId) throws Exception {
    return hservice_.findOne(PostImpl.class, objectId);
  }
  
  public String getObjectAsText(String userName, String objectId) throws Exception {
    PostImpl postImpl = (PostImpl) getObject(userName, objectId) ;
    if(postImpl == null)  return UNSYNCHRONIZED_DATABASE ;  
    return postImpl.getMessage()  ;
  }
  
  public String getObjectAsXHTML(String userName, String objectId) throws Exception  {
    PostImpl postImpl = (PostImpl) getObject(userName, objectId) ;
    if(postImpl == null)  return UNSYNCHRONIZED_DATABASE ;  
    return postImpl.getMessage()  ;
  }
  
  public String getObjectAsXML(String userName, String objectId) throws Exception  {
    PostImpl postImpl = (PostImpl) getObject(userName, objectId) ;
    if(postImpl == null)  return UNSYNCHRONIZED_DATABASE ;  
    return postImpl.getMessage()  ;
  }
  
  public void reindex() throws Exception {
    removeIndex() ;
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    ForumServiceContainer container = 
      (ForumServiceContainer)pcontainer.getComponentInstanceOfType(ForumServiceContainer.class) ;
    List forumOwners = container.getForumOwners() ;
    for(int i = 0 ; i < forumOwners.size() ; i++) {
      String owner = (String) forumOwners.get(i) ;
      ForumService service = container.findForumService(owner);
      List categories = service.getCategories() ;
      for(int j = 0; j < categories.size(); j++ ) {
        Category category = (Category) categories.get(j) ;
        List forums = service.getForums(category.getId()) ;
        for(int k = 0; k < forums.size(); k++) {
          Forum forum = (Forum) forums.get(k) ;
          indexForum(service, forum) ;
        }
      }
    }
  }
  
  private void indexForum(ForumService service, Forum forum) throws Exception {
    PageList pages = service.getTopics(forum.getId()) ;
    pages.setPageSize(100) ;
    for(int i = 1; i <= pages.getAvailablePage(); i++) {
      List topics = pages.getPage(i) ;
      for(int j = 0; j < topics.size(); j++) {
        Topic topic = (Topic)  topics.get(j) ;
        List posts = service.getPosts(topic.getId()).getAll() ;
        for(int k = 0; k < posts.size(); k++) {
          PostImpl post = (PostImpl) posts.get(k) ;
          createPost(forum, post)  ;
        }
      }
    }
  }
  
  void removeCategory(Category category) throws Exception {
    Term term = new Term(CATEGORY_ID_FIELD, category.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void removeForum(Forum forum) throws Exception {
    Term term = new Term(FORUM_ID_FIELD, forum.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void removeTopic(Topic topic) throws Exception {
    Term term = new Term(TOPIC_ID_FIELD,  topic.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void removePost(Post post) throws Exception {
    Term term = new Term(IDENTIFIER ,  post.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  void createPost(Forum forum, PostImpl post) throws Exception {
    iservice_.queueIndexDocument(createDocument(forum, post)) ;
  }
  
  void updatePost(Forum forum,  PostImpl post) throws Exception {
    Term term = new Term(IDENTIFIER, post.getId()) ;
    iservice_.queueDeleteDocuments(term) ;
    iservice_.queueIndexDocument(createDocument(forum, post)) ;
  }
}