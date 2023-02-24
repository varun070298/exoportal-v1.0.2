/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log.impl;

import java.util.Date ;
import org.exoplatform.services.portal.log.SessionLogDataDescription;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 * @hibernate.class  table="EXO_SESSION_LOG"
 */
public class SessionLogDataDescriptionImpl implements SessionLogDataDescription {
  private String id ;
  private String sessionOwner ;
  private String remoteUser ;
  private String clientName ;
  private String ipAddress ;
  private Date   accessTime ;
  private int duration ;
  private int errorCount;
  private int actionCount ;
  
  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String getId() {  return id; }
  public void   setId(String s) { id = s ; }
  
  /**
   * @hibernate.property
   ***/
  public String getSessionOwner() {return sessionOwner ;} 
  public void   setSessionOwner(String s) { sessionOwner = s ;}
  
  /**
   * @hibernate.property
   ***/
  public String getRemoteUser() {   return remoteUser ; }
  public void setRemoteUser(String user) { remoteUser = user; }

  /**
   * @hibernate.property
   ***/
  public String getClientName() { return clientName ; }
  public void   setClientName(String client) { clientName = client ; }
  
  /**
   * @hibernate.property
   ***/
  public String getIPAddress() {   return ipAddress ; }
  public void setIPAddress(String ip) { ipAddress = ip ;  }

  /**
   * @hibernate.property
   ***/
  public Date getAccessTime() {  return accessTime ; }
  public void setAccessTime(Date date) { accessTime = date ;  }

  /**
   * @hibernate.property
   ***/
  public int getDuration() {  return duration ; }
  public void setDuration(int time) { duration = time ; }
  
  /**
   * @hibernate.property
   ***/
  public int  getErrorCount()  {  return errorCount  ; }
  public void setErrorCount(int number)  { errorCount = number ; }
  
  /**
   * @hibernate.property
   ***/
  public int  getActionCount() { return actionCount ; }
  public void setActionCount(int number) { actionCount = number ; }
}