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
import net.sf.hibernate.type.Type;

import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.Query;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;
import org.exoplatform.services.organization.impl.UserImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class UserQueryHandler {
  public static final String queryFindUserByName =
    "from u in class org.exoplatform.services.organization.impl.UserImpl " +
    "where u.userName = ?";
  
  private static final String queryFindUsersByGroupAndRole =
    "select u " +
    "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
    "     m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
    "     g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where m.user = u " +
    "  and m.group = g " +
    "  and g.groupName = ? " +
    "  and m.role = ? ";

  private HibernateService service_ ;
  private ExoCache cache_ ;
  private List listeners_ ;

  public UserQueryHandler(HibernateService service, 
                          CacheService cservice) throws Exception {
    service_ = service ; 
    cache_ = cservice.getCacheInstance(UserImpl.class.getName()) ;
    cache_.setMaxSize(1000);
    listeners_ = new ListenerStack(5) ;
  }

  final public List getUserEventListeners() {
    return listeners_ ;
  }

  public void addUserEventListener(UserEventListener listener) {
    listeners_.add(listener) ;
  }
  
  public void createUser(User user) throws Exception {
  	Session session = service_.openSession();
    Date date = new Date() ;
    user.setLastLoginTime(date) ;
    user.setCreatedDate(date) ;
  	preSave(user, true, session)  ;
  	session.save(user,  IdentifierUtil.generateUUID(user));
  	postSave(user, true, session)  ;
  	UserProfileData upd = new UserProfileData(user.getUserName()) ;
  	session.save(upd);
  	session.flush() ;
  }

  public void saveUser(User user) throws Exception {
  	Session session = service_.openSession();
  	preSave(user, false, session)  ;
  	session.update(user);
  	postSave(user, false, session)  ;
  	session.flush() ;
    cache_.put(user.getUserName(), user) ;
  }
  
  void createUserEntry(User user, Session session) throws Exception {
     session.save(user);
  }

  public User removeUser(String userName) throws Exception {
  	Session session = service_.openSession();
  	User foundUser = findUserByName(userName, session);
  	preDelete(foundUser , session)  ;
  	session.delete(foundUser);
  	MembershipQueryHandler.removeMembershipEntriesOfUser(userName, session) ;
  	UserProfileQueryHandler.removeUserProfileEntry(userName, session) ;
  	postDelete(foundUser, session)  ;
  	session.flush();
    cache_.remove(userName) ;
  	return foundUser ;
  }
  
  /*This method use with importer/exporter */
  void removeUserEntry(User user, Session session) throws Exception {
    session.delete(user);
    cache_.remove(user.getUserName()) ;
  }

  public User findUserByName(String userName) throws Exception {
    User user = (User)cache_.get(userName) ;
    if(user != null) return user ;
  	Session session = service_.openSession();
  	user = findUserByName(userName, session) ;
    if(user != null)cache_.put(userName, user) ;
  	return user;
  }

  public User findUserByName(String userName, Session session) throws Exception {
    User user = (User) service_.findOne(session, queryFindUserByName, userName);
    return user;
  }

  public PageList getUserPageList(int pageSize)  throws Exception {
    return new DBObjectPageList(service_ ,UserImpl.class) ;
  }
  
  public PageList findUsers(Query q) throws Exception {
    ObjectQuery oq = new ObjectQuery(UserImpl.class);
    oq.addLIKE("userName", q.getUserName()) ;
    oq.addLIKE("firstName", q.getFirstName() ) ;
    oq.addLIKE("lastName", q.getLastName()) ;
    oq.addLIKE("email", q.getEmail()) ;
    oq.addGT("lastLoginTime", q.getFromLoginDate()) ;
    oq.addLT("lastLoginTime", q.getToLoginDate()) ;
    return new DBObjectPageList(service_, oq);
  }

  public PageList findUsersByGroup(String groupId) throws Exception {
    String queryFindUsersInGroup =
        "select u " +
        "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
        "     m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
        "where m.userName = u.userName " +
        "  and m.groupId =  '" + groupId + "'" ; 
    String countUsersInGroup =
      "select count(u) " +
      "from u in class org.exoplatform.services.organization.impl.UserImpl, " +
      "     m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
      "where m.userName = u.userName " +
      "  and m.groupId =  '" + groupId + "'" ; 
  	return new DBObjectPageList(service_, 20,queryFindUsersInGroup, countUsersInGroup ) ;
  }

  public Collection findUsersByGroupAndRole(String groupName, String role) throws Exception {
  	Session session = service_.openSession();
  	Object[] args = new Object[] { groupName, role };
  	Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING };
  	List users = session.find( queryFindUsersByGroupAndRole, args, types );
  	return users;
  }
  
  private void preSave(User user , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      UserEventListener listener = (UserEventListener) listeners_.get(i) ;
      listener.preSave(user, isNew, xresources) ;
    }
  }

  private void postSave(User user , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      UserEventListener listener = (UserEventListener) listeners_.get(i) ;
      listener.postSave(user, isNew, xresources) ;
    }
  }

  private void preDelete(User user , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      UserEventListener listener = (UserEventListener) listeners_.get(i) ;
      listener.preDelete(user, xresources) ;
    }
  }

  private void postDelete(User user , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      UserEventListener listener = (UserEventListener) listeners_.get(i) ;
      listener.postDelete(user, xresources) ;
    }
  }
}