/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log;

import java.util.Date ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 */
public class Query {
  private String sessionOwner_ ;
  private String remoteUser_ ;
  private String ipAddress_ ;
  private String clientType_ ;
  private Date fromDate_ ;
  private Date toDate_ ;
  private int  error_ = 0;
  
  public String getSessionOwner() { return sessionOwner_ ; }
  public void   setSessionOwner(String s) { sessionOwner_ = s ; }
  
  public String getRemoteUser() { return remoteUser_ ; }
  public void   setRemoteUser(String s) { remoteUser_ = s ; }
  
  public String getIPAddress() { return ipAddress_ ;}
  public void   setIPAddress(String s) { ipAddress_ = s ; }
  
  public String getClientType() { return clientType_ ;}
  public void   setClientType(String s) { clientType_ = s ; }
  
  public Date  getFromDate() { return fromDate_ ; }
  public void setFromDate(Date d) { fromDate_ = d ; }
  
  public Date  getToDate() { return toDate_ ; }
  public void setToDate(Date d) { toDate_ = d ; }
  
  public int getError() { return error_ ; }
  public void setError(int i) { error_ =  i ; }
}
