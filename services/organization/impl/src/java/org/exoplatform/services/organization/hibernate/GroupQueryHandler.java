/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization.hibernate;


import java.util.Collection;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.commons.utils.ListenerStack;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.organization.impl.GroupImpl;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:51:21 PM
 */
public class GroupQueryHandler {
  public static final String queryFindGroupByName =
    "from g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where g.groupName = ? ";
  
  public static final String queryFindGroupById =
    "from g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where g.id = ? ";
  
  public static final String queryFindGroupByParent =
    "from g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where g.parentId = ? ";

  private static final String queryFindRootGroups =
    "from g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where g.parentId is null";
  private static final String queryFindGroupsOfUser =
    "select g " +
    "from g in class org.exoplatform.services.organization.impl.GroupImpl, " +
    "     m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
    "where m.groupId = g.id " +
    "  and m.userName = ?" ;
  
  private static final String queryFindGroupByMembership =
    "select g " +
    "from m in class org.exoplatform.services.organization.impl.MembershipImpl, " +
    "     g in class org.exoplatform.services.organization.impl.GroupImpl " +
    "where m.groupId = g.id " +
    "  and m.userName = ? " +
    "  and m.membershipType = ? ";
  
  private HibernateService service_ ;
  private List listeners_ ;

  public GroupQueryHandler(HibernateService service) {
    service_ = service ; 
    listeners_ = new ListenerStack(5) ;
  }

  public void addGroupEventListener(GroupEventListener listener) {
    listeners_.add(listener) ;
  }

  public void createGroup(Group group) throws Exception {
  	Session session = service_.openSession();
    String groupId =  "/" + group.getGroupName() ;
    Object o = session.get(GroupImpl.class, groupId) ;
    if(o != null) {
      Object[] args = {group.getGroupName()} ;
      throw new UniqueObjectException("OrganizationService.unique-group-exception",  args) ;
    }
  	preSave(group, true, session)  ;
  	session.save(group, "/" + group.getGroupName());
  	postSave(group, true, session)  ;
  	session.flush() ;
  }

  public void addChild(Group parent, Group child) throws Exception {
    Session session = service_.openSession();
    Group parentGroup = (Group) session.get(GroupImpl.class, parent.getId()) ;
    String groupId = parentGroup.getId() + "/" + child.getGroupName() ;
    Object o = session.get(GroupImpl.class, groupId) ;
    if(o != null) {
      Object[] args = {child.getGroupName()} ;
      throw new UniqueObjectException("OrganizationService.unique-group-exception",  args) ;
    }
    GroupImpl childImpl = (GroupImpl) child ; 
    preSave(child, true, session)  ;
    childImpl.setParentId(parentGroup.getId()) ;
    session.save(childImpl, groupId);
    postSave(child, true, session)  ;
    session.flush() ;
  }

  public void saveGroup(Group group) throws Exception {
  	Session session = service_.openSession();
  	preSave(group, false, session)  ;
  	session.update(group);
  	postSave(group, false, session)  ;
  	session.flush();
  }

  public Group removeGroup(Group group) throws Exception {
    Session session = service_.openSession();
    preDelete(group, session)  ;
    session.delete(group);
    session.delete(queryFindGroupByParent, group.getId(), Hibernate.STRING );
    MembershipQueryHandler.removeMembershipEntriesOfGroup(group, session) ;
    postDelete(group, session)  ;
    session.flush();
    return group ;
  }
  
  static void removeGroupEntry(String groupName, Session session) throws Exception {
  	session.delete(queryFindGroupByName, groupName, Hibernate.STRING);
  }

  public Collection findGroupByMembership(String userName, String membershipType) throws Exception {
  	Session session = service_.openSession();
  	Object[] args = new Object[] { userName, membershipType };
  	Type[] types = new Type[] { Hibernate.STRING, Hibernate.STRING };
  	List groups =  session.find(queryFindGroupByMembership, args, types );
  	return groups ;
  }

  public Group findGroupByName(String groupName) throws Exception {
  	Session session = service_.openSession();
  	Group group =(Group)service_.findOne(session,queryFindGroupByName, groupName);
  	return group;
  }
  
  public Group findGroupById(String groupId) throws Exception {
    Session session = service_.openSession();
    Group group =(Group)service_.findOne(session,queryFindGroupById, groupId);
    return group;
  }

  public Collection findGroups(Group parent) throws Exception {
  	Session session = service_.openSession();
  	if (parent == null) {
  		return  session.find( queryFindRootGroups );
  	}
    return  session.find( queryFindGroupByParent, parent.getId(), Hibernate.STRING );
  }
  
  public Collection findGroupsOfUser(String user) throws Exception {
  	Session session = service_.openSession();
  	return   session.find( queryFindGroupsOfUser, user, Hibernate.STRING );
  }

  private void preSave(Group group , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      GroupEventListener listener = (GroupEventListener) listeners_.get(i) ;
      listener.preSave(group, isNew, xresources) ;
    }
  }

  private void postSave(Group group , boolean isNew , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      GroupEventListener listener = (GroupEventListener) listeners_.get(i) ;
      listener.postSave(group, isNew, xresources) ;
    }
  }

  private void preDelete(Group group , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      GroupEventListener listener = (GroupEventListener) listeners_.get(i) ;
      listener.preDelete(group, xresources) ;
    }
  }

  private void postDelete(Group group , Session session) throws Exception {
    XResources xresources = new XResources() ;
    xresources.addResource(Session.class, session) ;
    for (int i = 0 ; i < listeners_.size(); i++) {
      GroupEventListener listener = (GroupEventListener) listeners_.get(i) ;
      listener.postDelete(group, xresources) ;
    }
  }
}