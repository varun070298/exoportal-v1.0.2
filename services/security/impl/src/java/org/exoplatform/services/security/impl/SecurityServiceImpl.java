/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.security.impl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.*;
import javax.security.auth.Subject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.security.SecurityService;
import org.exoplatform.services.security.SubjectEventListener;
import org.exoplatform.services.security.jaas.JAASGroup;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 avr. 2004
 */
public class SecurityServiceImpl implements SecurityService {

  private Map subjects;
  private Log log_;
  private OrganizationService orgService_;

  public SecurityServiceImpl(LogService logService,
                             OrganizationService organizationService) {
    log_ = logService.getLog("org.exoplatform.services.security");
    orgService_ = organizationService;
    subjects = new HashMap();
  }

  public boolean authenticate(String login, String password) throws Exception {
    if (password == null || "".equals(password)) {
      log_.debug("password must not be null or empty");
      throw new ExoServiceException("password must not be null or empty");
    }
    return orgService_.authenticate(login, password) ;
  }

  public void setUpAndCacheSubject(String userName, Subject value) throws ExoServiceException {
    log_.debug("setUpAndCacheSubject for user " + userName);
    Set principals = value.getPrincipals();
    principals.add(new UserPrincipalImpl(userName));
    Collection groups = null;
    try {      
      groups = orgService_.findGroupsOfUser(userName);
    } catch (Exception e) {
      log_.error("error occured in findUserRoles of OrganizationService",e);
      throw new ExoServiceException(e);
    }
    Set roles = new HashSet(5);
    for (Iterator iter = groups.iterator(); iter.hasNext();) {
      org.exoplatform.services.organization.Group group = 
        (org.exoplatform.services.organization.Group) iter.next();
      String groupId = group.getId();
      String[] splittedGroupName = StringUtils.split(groupId, "/");
      roles.add(splittedGroupName[0]);
    }
    Group roleGroup = new JAASGroup(JAASGroup.ROLES);
    for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
      String role = (String) iterator.next();
      roleGroup.addMember(new RolePrincipalImpl(role));
      log_.debug("add role : " + role);
    }
    value.getPrincipals().add(roleGroup);
    subjects.put(userName, value);
  }
  
  public boolean isUserInRole(String userName, String role){
    Subject subject = (Subject) subjects.get(userName);
    if(subject == null){
      return false;
    }
    Set roleGroups = subject.getPrincipals(Group.class);
    for (Iterator iter = roleGroups.iterator(); iter.hasNext();) {
      Group roleGroup = (Group) iter.next();
      Enumeration enum = roleGroup.members();
      while (enum.hasMoreElements()) {
        Principal rolePrincipal = (Principal) enum.nextElement();
        if(rolePrincipal.getName().equals(role))
          return true;
      }
    }        
    return false;
  }

  public Subject getSubject(String userName) {
    log_.debug("get subject for user " + userName);
    return (Subject) subjects.get(userName);
  }

  public void removeSubject(String userName) {
    log_.debug("remove subject for user " + userName);
    subjects.remove(userName);
  }

  public void addSubjectEvenetListener(SubjectEventListener subjectEventListener) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
  
  public Log getLog() { return log_ ;  }
}