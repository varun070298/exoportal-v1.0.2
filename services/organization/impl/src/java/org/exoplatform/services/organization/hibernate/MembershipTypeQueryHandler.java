/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;

import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.impl.MembershipTypeImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class MembershipTypeQueryHandler {
  private static final String queryFindMembershipType =
    "from m in class org.exoplatform.services.organization.impl.MembershipTypeImpl, " +
    "where m.name = ? " ;
  private static final String queryFindAllMembershipType =
    "from m in class org.exoplatform.services.organization.impl.MembershipTypeImpl";

  private HibernateService service_ ;

  public MembershipTypeQueryHandler(HibernateService service) {
    service_ = service ; 
  }

  public MembershipType createMembershipType(MembershipType mt) throws Exception {
  	Session session = service_.openSession();
  	Date now = new Date() ;
  	mt.setCreatedDate(now) ;
  	mt.setModifiedDate(now) ;
  	session.save(mt);
  	session.flush();
  	return mt ;
  }
  
  public MembershipType saveMembershipType(MembershipType mt) throws Exception {
  	Session  session = service_.openSession();
  	Date now = new Date() ;
  	mt.setModifiedDate(now) ;
  	session.update(mt);
  	session.flush();
  	return mt ;
  }

  public MembershipType findMembershipType(String name) throws Exception {
  	Session session = service_.openSession();
  	MembershipType m = (MembershipType) service_.findOne(session,queryFindMembershipType, name);
  	return m;
  }

  public MembershipType removeMembershipType(String name) throws Exception {
  	Session session = service_.openSession();
  	MembershipTypeImpl m = (MembershipTypeImpl)session.get(MembershipTypeImpl.class, name);
  	if (m != null) {
  	  session.delete("from m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
                     "where m.membershipType = '" + name + "'") ;
  		session.delete(m);
  		session.flush();
  	}
  	return m;
  }
  
  static void removeMembershipTypeEntry(String name, Session session) throws Exception {
  	session.delete(queryFindMembershipType, name, Hibernate.STRING);
  	session.delete("from m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
                   "where m.membershipType = '" + name + "'") ;
  }

  
  public Collection findMembershipTypes() throws Exception {
  	Session session = service_.openSession();
  	List  memberships = session.find( queryFindAllMembershipType);
  	return memberships;
  }
}