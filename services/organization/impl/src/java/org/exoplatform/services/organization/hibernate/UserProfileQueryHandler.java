/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;

import java.util.Collection;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;

import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileEventListener;
import org.exoplatform.services.organization.impl.UserProfileImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class UserProfileQueryHandler {
  static private UserProfile NOT_FOUND = new UserProfileImpl() ;
  private static final String queryFindUserProfileByName =
    "from u in class org.exoplatform.services.organization.hibernate.UserProfileData " +
    "where u.userName = ?";
  
  private HibernateService service_ ;
  private ExoCache cache_ ;
  private List listeners_ ;

  public UserProfileQueryHandler(HibernateService service, 
                                 CacheService cservice) throws Exception {
    service_ = service ; 
    cache_ =  cservice.getCacheInstance(getClass().getName()) ;
    cache_.setMaxSize(1000);
    listeners_ = new ListenerStack(5) ;
  }

  public void addUserProfileEventListener(UserProfileEventListener listener) {
    listeners_.add(listener) ;
  }
  
  void createUserProfileEntry(UserProfile up, Session session) throws Exception {
  	UserProfileData upd = new UserProfileData() ;
    upd.setUserProfile(up) ; 
    session.save(upd);
    session.flush();
    cache_.remove(up.getUserName()) ;
  }	
  
  public void saveUserProfile(UserProfile profile) throws Exception {
    Session session = service_.openSession();
  	UserProfileData  upd = 
      (UserProfileData)service_.findOne(session, queryFindUserProfileByName, profile.getUserName());
  	upd.setUserProfile(profile) ;
  	session.update(upd);
  	session.flush();
    cache_.put(profile.getUserName(), profile) ;
  }

  public UserProfile removeUserProfile(String userName) throws Exception {
  	Session session = service_.openSession();
  	UserProfileData upd =(UserProfileData)service_.findExactOne(session,queryFindUserProfileByName, userName);
  	session.delete(upd);
  	session.flush();
    cache_.remove(userName) ;
  	return upd.getUserProfile() ;
  }

  public UserProfile findUserProfileByName(String userName) throws Exception {
    UserProfile up = (UserProfile)cache_.get(userName) ;
    if(up != null) { 
      if(NOT_FOUND == up ) return null ;
      return up ;
    }
    Session session = service_.openSession();
  	up = findUserProfileByName(userName, session) ;
    if(up != null) cache_.put(userName, up) ;
    else cache_.put(userName, NOT_FOUND) ;
    return up ;
  }
  
  public UserProfile findUserProfileByName(String userName, Session session) throws Exception {
    UserProfileData  upd = 
    	(UserProfileData)service_.findOne(session, queryFindUserProfileByName, userName);
    if (upd != null) {
      return upd.getUserProfile() ; 
    }
    return null ;
  }

  static void removeUserProfileEntry(String userName, Session session) throws Exception {
  	session.delete(queryFindUserProfileByName, userName, Hibernate.STRING );
  }
  
  public Collection findUserProfiles() throws Exception {
    return null ;
  }
}