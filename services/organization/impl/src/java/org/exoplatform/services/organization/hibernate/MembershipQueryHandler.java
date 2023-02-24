/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.MembershipEventListener;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.MembershipImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class MembershipQueryHandler {

  private static final String queryFindMembershipByUserGroupAndType =
    "from m in class org.exoplatform.services.organization.impl.MembershipImpl " +
    "where m.userName = ? " +
    "  and m.groupId = ? " +
    "  and m.type = ? " ;
  
  private static final String queryFindMembershipsByUserAndGroup =
    "from m in class org.exoplatform.services.organization.impl.MembershipImpl " +
    "where m.userName = ? " +
    "  and m.groupId = ? " ;
 
  private static final String queryFindMembershipsByGroup =
    "from m in class org.exoplatform.services.organization.impl.MembershipImpl " +
    "where m.groupId = ? " ;
  
  private static final String queryFindMembership =
    "from m in class org.exoplatform.services.organization.impl.MembershipImpl " +
    "where m.id = ? " ;
  
  private static final String queryFindMembershipsByUser =
    "from  m in class org.exoplatform.services.organization.impl.MembershipImpl " +
    "where m.userName = ? "  ;

  private HibernateService service_ ;
  private List listeners_ ;

  public MembershipQueryHandler(HibernateService service) {
    service_ = service ; 
    listeners_ = new ListenerStack(5) ;
  }

  public void addMembershipEventListener(MembershipEventListener listener) {
    listeners_.add(listener) ;
  }

  public void createMembership(Membership m) throws Exception {
  	Session session = service_.openSession();
  	preSave(m, true, session)  ;
  	session.save(m, IdentifierUtil.generateUUID(m));
  	postSave(m, true, session)  ;
  	session.flush() ;
  }
  
  static public void createMembershipEntries(Collection c, Session session) throws Exception {
  	Iterator i = c.iterator() ;
  	while(i.hasNext()) {
  		Membership impl = (Membership) i.next() ;
  		session.save(impl, impl.getId());
  	}
  }

  public void linkMembership(String userName, Group g, Membership m) throws Exception {
  	Session session = service_.openSession();
    MembershipImpl impl = (MembershipImpl) m;
    User user =(User)service_.findExactOne(session,UserQueryHandler.queryFindUserByName, userName);
    impl.setUserName(user.getUserName());
    impl.setGroupId(g.getId());
  	if(impl.getId() == null) {
  		String id = IdentifierUtil.generateUUID(m) ;
  		preSave(impl, true, session)  ;
  		session.save(impl, id);
      postSave(impl, true,session)  ;
  	}  else {
  		preSave(m, false, session)  ;
  		session.update(m);
  		postSave(m, false,session)  ;
  	}
  	session.flush();
  }

  public void saveMembership(Membership m) throws Exception {
  	Session session = service_.openSession();
  	preSave(m, false, session)  ;
  	session.update(m);
  	postSave(m, false,session)  ;
  	session.flush();
  }

  public Membership removeMembership(String id) throws Exception {
  	Session session = service_.openSession();
  	Membership m =(Membership) service_.findOne(session,queryFindMembership, id);
  	if(m != null) {
  		preDelete(m, session)  ;
  		session.delete(m);
  		postDelete(m, session)  ;
  		session.flush();
  	}
  	return m;
  }

  public Membership findMembershipByUserGroupAndType(String userName, 
                                                     String groupId, 
                                                     String type) throws Exception {
    Session session = service_.openSession();
    Object[] args = new Object[] { userName, groupId , type};
    Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.STRING };
    List memberships = session.find( queryFindMembershipByUserGroupAndType, args, types );
    if(memberships.size() == 0) {
      return null  ;
    } else if(memberships.size() == 1) {
      return (Membership) memberships.get(0) ;
    } else {
      throw new Exception("Expect 0 or 1 membership but found" + memberships.size()) ; 
    }
  }

  public Collection findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
  	Session session = service_.openSession();
  	Object[] args = new Object[] { userName, groupId };
  	Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING };
  	List memberships = session.find(queryFindMembershipsByUserAndGroup, args, types );
  	return memberships;
  }

  public Collection findMembershipsByUser(String userName) throws Exception {
  	Session session = service_.openSession();
  	List memberships = session.find(queryFindMembershipsByUser, userName, Hibernate.STRING );
  	return memberships;
  }
  
  static void removeMembershipEntriesOfUser(String userName, Session session) throws Exception {
  	session.delete(queryFindMembershipsByUser, userName, Hibernate.STRING );
  }
  
  static void removeMembershipEntriesOfGroup(Group group, Session session) throws Exception {
    session.delete(queryFindMembershipsByGroup, group.getId(), Hibernate.STRING );
  }
  
  Collection findMembershipsByUser(String userName, Session session) throws Exception {
  	return session.find( queryFindMembershipsByUser, userName, Hibernate.STRING );
  }

  public Collection findMembershipsByGroup(Group group) throws Exception {
  	Session session = service_.openSession();
  	List memberships = session.find( queryFindMembershipsByGroup, group.getId(), Hibernate.STRING );
  	return memberships;
  }

  public Membership findMembership(String id) throws Exception {
  	Session session = service_.openSession();
  	Membership  membership = (Membership) session.find(queryFindMembership, id, Hibernate.STRING ) ;
  	return membership;
  }

  private void preSave(Membership membership , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i) ;
      listener.preSave(membership, isNew, xresources) ;
    }
  }

  private void postSave(Membership membership , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i) ;
      listener.postSave(membership, isNew, xresources) ;
    }
  }

  private void preDelete(Membership membership , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i) ;
      listener.preDelete(membership, xresources) ;
    }
  }

  private void postDelete(Membership membership , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      MembershipEventListener listener = (MembershipEventListener) listeners_.get(i) ;
      listener.postDelete(membership, xresources) ;
    }
  }
}