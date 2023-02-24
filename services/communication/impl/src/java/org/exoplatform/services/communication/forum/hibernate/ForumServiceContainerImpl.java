/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved   *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum.hibernate;

import java.util.List ;
import net.sf.hibernate.Session;
import net.sf.hibernate.Query;
import org.exoplatform.services.cache.*;
import org.exoplatform.services.communication.forum.ForumEventListener;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.ForumServiceContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.idgenerator.IDGeneratorService;
import org.exoplatform.commons.utils.ListenerStack;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class ForumServiceContainerImpl implements ForumServiceContainer {
  private static String[]  MAPPING = 
    {
      "org/exoplatform/services/communication/forum/hibernate/CategoryImpl.hbm.xml" ,
      "org/exoplatform/services/communication/forum/hibernate/ForumImpl.hbm.xml",
      "org/exoplatform/services/communication/forum/hibernate/TopicImpl.hbm.xml" ,
      "org/exoplatform/services/communication/forum/hibernate/PostImpl.hbm.xml",
      "org/exoplatform/services/communication/forum/hibernate/WatcherImpl.hbm.xml" 
    };
  
  public static String queryForumOwners =
    "select distinct c.owner from org.exoplatform.services.communication.forum.hibernate.CategoryImpl as c" ;

  private HibernateService hservice_ ;
  private IDGeneratorService idService_ ;
  private ForumIndexerPluginImpl indexer_ ;
  private ExoCache forumServices_ ;
  private ListenerStack eventlisteners_ ;
  
  public ForumServiceContainerImpl(HibernateService hservice,
                                   IDGeneratorService idService,
                                   CacheService cService, 
                                   ForumIndexerPluginImpl indexer) throws Exception {
    hservice_ = hservice ;
    idService_ = idService ;
    indexer_ = indexer ;
    eventlisteners_ = new ListenerStack(3) ;
    hservice.addMappingFiles(MAPPING) ;
    forumServices_ = cService.getCacheInstance(getClass().getName()) ; 
  }

  public ForumService findForumService(String owner) throws Exception {
    ForumService service = (ForumService) forumServices_.get(owner) ;
    if(service == null) {
      synchronized(forumServices_) {
        service = createForumService(owner) ;
        forumServices_.put(owner, service) ;
      }
    }
    return service  ;
  }

  public ForumService createForumService(String owner) throws Exception {
    ForumServiceImpl service = new ForumServiceImpl(hservice_, indexer_, idService_,
                                                    eventlisteners_, owner) ;
    return service ;
  }
  
  public List getForumOwners() throws Exception {
    Session session = hservice_.openSession();
    Query q = session.createQuery(queryForumOwners);
    return q.list() ;
  }
  
  
  public void addForumEventListener(ForumEventListener listener)  {
    eventlisteners_.add(listener) ;
  }
}