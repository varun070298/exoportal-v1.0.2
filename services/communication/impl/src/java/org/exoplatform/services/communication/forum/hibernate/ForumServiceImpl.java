/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.Date;
import java.util.List;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.communication.forum.*;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.idgenerator.IDGeneratorService;
import org.exoplatform.services.log.LogUtil;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class ForumServiceImpl implements ForumService {
  static protected Log log_ = LogUtil.getLog("org.exoplatform.services.communication.forum");
  
  public static final String queryCategoriesByOwner =
    "from category in class org.exoplatform.services.communication.forum.hibernate.CategoryImpl " +
    "where category.owner = ? order by category.categoryOrder asc";
  
  public static final String queryForumsByCategory =
    "from forum in class org.exoplatform.services.communication.forum.hibernate.ForumImpl " +
    "where forum.categoryId = ? order by forum.forumOrder asc";
 
  public static final String queryTopicsByForum =
    "from topic in class org.exoplatform.services.communication.forum.hibernate.TopicImpl " +
    "where topic.forumId = ?";
  
  public static final String queryPostsByForum =
    "from post in class org.exoplatform.services.communication.forum.hibernate.PostImpl " +
    "where post.forumId = ?";
  
  public static final String queryPostsByTopic =
    "from post in class org.exoplatform.services.communication.forum.hibernate.PostImpl " +
    "where post.topicId = ?";
 
	private HibernateService hservice_ ;
  private ForumIndexerPluginImpl indexer_ ;
  private IDGeneratorService idService_ ; 
  private String forumServiceOwner_ ;
  private ListenerStack eventListeners_ ;
  private long modifiedTime_ ;

  public ForumServiceImpl(HibernateService service, 
                          ForumIndexerPluginImpl indexer,
                          IDGeneratorService idService, 
                          ListenerStack eventListeners ,
                          String owner) {
    forumServiceOwner_ = owner ;
    hservice_ = service ;
    indexer_ = indexer ;
    idService_ = idService ;
    eventListeners_ = eventListeners ;
  }
  //--------------------------C A T E G O R Y-------------------------------
  public List getCategories() throws Exception {
  	Session  session = hservice_.openSession();
  	List  categories = session.find(queryCategoriesByOwner, forumServiceOwner_, Hibernate.STRING );
  	return categories ;
  }

  public Category getCategory(String id) throws Exception {
    return (Category) hservice_.findOne(CategoryImpl.class, id) ;
  }

  public Category addCategory(Category category) throws Exception {
  	Date now = new Date() ;
  	CategoryImpl impl = (CategoryImpl) category ;
    impl.setId(idService_.generateStringID(category)) ;
  	impl.setOwner(forumServiceOwner_) ;
  	impl.setCreatedDate(now) ;
  	impl.setModifiedBy(forumServiceOwner_) ;
  	impl.setModifiedDate(now) ;
    Session session = hservice_.openSession();
    session.save(impl) ;
    session.flush();
    modifiedTime_ = System.currentTimeMillis() ;
  	return impl ;
  }

  public Category removeCategory(String id) throws Exception {
  	Session session = hservice_.openSession();
    Category category = (Category) session.get(CategoryImpl.class, id) ;
  	session.delete(category);
    XResources resources = new XResources() ;
    resources.addResource(Session.class, session) ;
    ForumEventListener.onDelete(eventListeners_, resources, category) ;
  	session.flush();
    indexer_.removeCategory(category) ;
    modifiedTime_ = System.currentTimeMillis() ;
  	return category ;
  }

  public Category updateCategory(Category category) throws Exception {
    Session session = hservice_.openSession();
    Date now = new Date() ;
    CategoryImpl impl = (CategoryImpl) category ;
    impl.setModifiedDate(now) ;
    session.update(impl);
    XResources resources = new XResources() ;
    resources.addResource(Session.class, session) ;
    ForumEventListener.onSave(eventListeners_, resources, category) ;
    session.flush();
    return category ;
  }

  public Category createCategoryInstance() { return new CategoryImpl(); }

  //--------------------------F O R U M-------------------------------------
  public List getForums(String categoryId) throws Exception {
  	Session session = hservice_.openSession();
  	List forums = session.find(queryForumsByCategory, categoryId, Hibernate.STRING );
  	return forums ;
  }

  public Forum getForum(String id) throws Exception {
  	return (Forum)hservice_.findOne(ForumImpl.class, id);
  }

  public Forum addForum(Category category, Forum forum) throws Exception {
    Session session = hservice_.openSession();
    Date now = new Date() ;
    ForumImpl impl = (ForumImpl) forum ;
    impl.setId(idService_.generateStringID(impl)) ;
    impl.setOwner(forumServiceOwner_) ;
    impl.setCreatedDate(now) ;
    impl.setModifiedBy(forumServiceOwner_) ;
    impl.setModifiedDate(now) ;
    impl.setCategoryId(category.getId()) ;
    session.save(impl);
    XResources resources = new XResources() ;
    resources.addResource(Session.class, session) ;
    ForumEventListener.onSave(eventListeners_, resources, forum) ;
    session.flush() ;
    modifiedTime_ = System.currentTimeMillis() ;
    return forum ;
  }

  public Forum removeForum(String id) throws Exception {
    Session session = hservice_.openSession();
    Forum forum = (Forum) session.get(ForumImpl.class, id) ;
    session.delete(forum);
    XResources resources = new XResources() ;
    resources.addResource(Session.class, session) ;
    ForumEventListener.onDelete(eventListeners_, resources, forum) ;
    session.flush();
    indexer_.removeForum(forum) ;
    modifiedTime_ = System.currentTimeMillis() ;
    return forum ;
  }

  public Forum updateForum(Forum forum) throws Exception {
    Session session = hservice_.openSession();
    Date now = new Date() ;
    ForumImpl impl = (ForumImpl) forum ;
    impl.setModifiedDate(now) ;
    session.update(impl) ;
    XResources resources = new XResources() ;
    resources.addResource(Session.class, session) ;
    ForumEventListener.onSave(eventListeners_, resources, forum) ;
    session.flush() ;
    return forum ;
  }

  public Forum createForumInstance() { return new ForumImpl(); }
  
  //--------------------------T O P I C-------------------------------------
  public PageList getTopics(String forumId) throws Exception {
    ObjectQuery q =  new ObjectQuery(TopicImpl.class).
                     addEQ("forumId", forumId ).setDescOrderBy("lastPostDate");
    return new DBObjectPageList(hservice_, q) ;
  }

  public Topic getTopic(String id) throws Exception {
  	return (Topic) hservice_.findOne(TopicImpl.class, id) ;
  }

  public Topic addTopic(Forum forum, Topic topic) throws Exception {
  	Date now = new Date() ;
  	TopicImpl impl = (TopicImpl) topic ;
  	ForumImpl forumImpl = (ForumImpl) forum ;
    impl.setId(idService_.generateStringID(impl)) ;
  	impl.setCreatedDate(now) ;
  	impl.setModifiedDate(now) ;
  	impl.setLastPostBy(topic.getOwner()) ;
  	impl.setLastPostDate(now) ;
  	impl.setForumId(forumImpl.getId()) ;
  	Session session = hservice_.openSession();
  	session.save(impl);
  	forumImpl.addTopicCount(1) ;
  	session.update(forumImpl);
  	session.flush();
    modifiedTime_ = System.currentTimeMillis() ;
  	return topic ;
  }

  public Topic removeTopic(String id) throws Exception {
  	Session session = hservice_.openSession();
  	TopicImpl topicImpl = (TopicImpl)session.get(TopicImpl.class, id) ;
  	session.delete(topicImpl);
    ForumImpl forumImpl = (ForumImpl) session.get(ForumImpl.class, topicImpl.getForumId()) ;
  	forumImpl.addTopicCount(-1) ;
  	session.update(forumImpl);
  	session.flush();
    indexer_.removeTopic(topicImpl) ;
    modifiedTime_ = System.currentTimeMillis() ;
  	return topicImpl ;
  }

  public Topic updateTopic(Topic topic) throws Exception {
  	Date now = new Date() ;
  	TopicImpl impl = (TopicImpl) topic ;
  	impl.setModifiedDate(now) ;
  	hservice_.update(impl);
  	return topic ;
  }

  public Topic createTopicInstance() { return new TopicImpl() ;}

  //--------------------------P O S T----------------------------------------
  public PageList getPosts(String topicId) throws Exception {
    ObjectQuery q =  new ObjectQuery(PostImpl.class).addEQ("topicId", topicId );
    return new DBObjectPageList(hservice_, q) ;
  }

  public Post getPost(String id) throws Exception {
  	return (Post) hservice_.findOne(PostImpl.class, id);
  }

  public Post addPost(Topic topic, Post post) throws Exception {
  	Date now = new Date() ;
  	PostImpl postImpl = (PostImpl) post ;
    postImpl.setId(idService_.generateStringID(postImpl)) ;
  	postImpl.setCreatedDate(now) ;
  	postImpl.setModifiedDate(now) ;
  	Session session = hservice_.openSession();
  	TopicImpl topicImpl = (TopicImpl) topic;
  	topicImpl.addPostCount(1) ;
  	topicImpl.setLastPostBy(postImpl.getOwner()) ;
  	topicImpl.setLastPostDate(now) ;
  	ForumImpl forumImpl = (ForumImpl)session.get(ForumImpl.class, topicImpl.getForumId());
  	forumImpl.addPostCount(1) ;
  	forumImpl.setLastPostBy(postImpl.getOwner()) ;
  	forumImpl.setLastPostDate(now) ;
    postImpl.setTopicId(topic.getId()) ;
    postImpl.setForumId(topicImpl.getForumId()) ;
  	session.save(postImpl);
  	session.update(forumImpl);
  	session.update(topicImpl);
  	XResources resources = new XResources() ;
  	resources.addResource(Session.class, session) ;
    resources.addResource(Forum.class, forumImpl) ;
    resources.addResource(Topic.class, topicImpl) ;
    ForumEventListener.onSave(eventListeners_, resources, postImpl) ;
  	session.flush() ;
    indexer_.createPost(forumImpl, postImpl) ;
    modifiedTime_ = System.currentTimeMillis() ;
  	return post ;
  }

  public Post removePost(String id) throws Exception {
  	Session session = hservice_.openSession();
  	PostImpl postImpl = (PostImpl) session.get(PostImpl.class, id); 
  	TopicImpl topicImpl = (TopicImpl) session.get(TopicImpl.class, postImpl.getTopicId());
  	session.delete(postImpl);
  	topicImpl.addPostCount(-1) ;
  	session.update(topicImpl);
    ForumImpl forumImpl =  (ForumImpl) session.get(ForumImpl.class, postImpl.getForumId());
  	forumImpl.addPostCount(-1) ;
  	session.update(forumImpl);
  	session.flush();
    indexer_.removePost(postImpl) ;
    modifiedTime_ = System.currentTimeMillis() ;
  	return postImpl ;
  }

  public Post updatePost(Post post) throws Exception {
  	Date now = new Date() ;
  	PostImpl impl = (PostImpl) post ;
  	impl.setModifiedDate(now) ;
  	hservice_.update(impl);
    Forum forum = getForum(impl.getForumId()) ;
    indexer_.updatePost(forum, impl) ;
  	return post ;
  }

  public Post createPostInstance() { return new PostImpl() ; }
  
  //=============================Watcher watcher =============================
  public Watcher createWatcher(Forum forum)  { return new WatcherImpl(forum) ; }
  
  public Watcher createWatcher(Topic topic)  { return new WatcherImpl(topic) ; }
  
  public Watcher getWatcher(Topic topic, String userName) throws Exception  {
    ObjectQuery q = new ObjectQuery(WatcherImpl.class).
                    addEQ("target", WatcherImpl.TOPIC_TARGET).
                    addEQ("topicId", topic.getId()).
                    addEQ("userName", userName) ;
    return (Watcher) hservice_.findOne(q);
  }
  
  public Watcher getWatcher(Forum forum, String userName) throws Exception  {
    ObjectQuery q = new ObjectQuery(WatcherImpl.class).
                    addEQ("target",  WatcherImpl.FORUM_TARGET).
                    addEQ("forumId", forum.getId()).
                    addEQ("userName", userName) ;
    return (Watcher) hservice_.findOne(q);
  }
  
  public void saveWatcher(Watcher watcher) throws Exception {
    WatcherImpl impl  = (WatcherImpl) watcher ;
    if(impl.getId() == null) {
      impl.setId(idService_.generateStringID(impl)) ;
      hservice_.create(impl) ;
    } else {
      hservice_.update(impl);
    }
  }
  
  public void removeWatcher(Watcher watcher) throws Exception {
    hservice_.remove(watcher) ;
  }
  //======================================================================
  public long     getLastModifiedTime() { return modifiedTime_ ; }
}
