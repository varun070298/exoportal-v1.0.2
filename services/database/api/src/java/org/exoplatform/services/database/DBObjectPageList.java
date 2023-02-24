/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database;

import java.util.List;
import org.exoplatform.commons.utils.PageList;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 21, 2004
 * @version $Id: DBObjectPageList.java,v 1.1 2004/10/22 14:22:27 tuan08 Exp $
 */
public class DBObjectPageList extends PageList {
 
  private String findQuery_ ;
  private String countQuery_ ;
  private HibernateService service_ ;
  
  public DBObjectPageList(HibernateService service, Class objectType) throws Exception {
    super(20) ;
    service_ = service ;
    findQuery_ = "from o in class " + objectType.getName() ;
    countQuery_ = "select count(o) from " + objectType.getName()  + " o" ;
    Session session = service_.openSession() ;
    List l  = session.find(countQuery_) ;
    Integer count = (Integer) l.get(0) ;
    setAvailablePage(count.intValue()) ;
  }
  
  public DBObjectPageList(HibernateService service, ObjectQuery  oq) throws Exception {
    super(20) ;
    service_ = service ;
    findQuery_ = oq.getHibernateQuery() ;
    countQuery_ = oq.getHibernateCountQuery() ;
    Session session = service_.openSession() ;
    List l  = session.find(countQuery_) ;
    Integer count = (Integer) l.get(0) ;
    setAvailablePage(count.intValue()) ;
  }
  
  public DBObjectPageList(HibernateService service, int pageSize,
                          String query, String countQuery) throws Exception {
    super(pageSize) ;
    service_ = service ;
    findQuery_ =  query ;
    countQuery_ =  countQuery ;
    Session session = service_.openSession() ;
    List l  = session.find(countQuery_) ;
    Integer count = (Integer) l.get(0) ;
    setAvailablePage(count.intValue()) ;
  }
  
  protected void populateCurrentPage(int page) throws Exception  {
    Session session = service_.openSession() ;
    Query query = session.createQuery(findQuery_);
    int from = getFrom() ;
    query.setFirstResult(from);
    query.setMaxResults(getTo()- from) ;
    currentListPage_ = query.list() ;
  }
  
  public List getAll() throws Exception  { 
    Session session = service_.openSession() ;
    Query query = session.createQuery(findQuery_);
    return query.list() ;
  }
}