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
public interface SessionLogDataDescription {
  public String getId() ;
 
  public String getSessionOwner() ;
  public void   setSessionOwner(String s) ;
   
  public String getRemoteUser() ;
  public void   setRemoteUser(String user) ;
  
  public String getIPAddress() ;
  public void   setIPAddress(String ip) ;
  
  public String getClientName() ;
  public void   setClientName(String ip) ;
  
  public Date   getAccessTime() ;
  public void   setAccessTime(Date time) ;
  
  public int   getDuration() ;
  public void  setDuration(int time) ;
  
  public int  getActionCount() ;
  public void setActionCount(int number) ;
  
  public int  getErrorCount() ;
  public void setErrorCount(int number) ;
}
