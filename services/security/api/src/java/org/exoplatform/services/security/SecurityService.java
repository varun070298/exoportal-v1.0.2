package org.exoplatform.services.security;

import org.apache.commons.logging.Log;
import org.exoplatform.services.exception.ExoServiceException;

import javax.security.auth.Subject;

/**
 * Date: 27 avr. 2004
 * Time: 13:30:38
 */
public interface SecurityService {

  public boolean authenticate(String login, String password) throws Exception;

  public Subject getSubject(String userName);
  public void setUpAndCacheSubject(String userName, Subject value) throws ExoServiceException;
  public void removeSubject(String userName);

  public void addSubjectEvenetListener(SubjectEventListener subjectEventListener);

  public boolean isUserInRole(String userName, String role);
  
  public Log getLog() ;
}
