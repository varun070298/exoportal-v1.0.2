/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import java.util.* ;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.container.client.ClientInfo ;
/**
 * Tue, May 27, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: PortalMonitorService.java,v 1.2 2004/04/29 15:07:27 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class SessionMonitor {
  private SessionMonitorListenerStack listeners_ ;
  private List history_ ;
  private String error_ ;
  private int errorCount_ = 0;
  private int actionCount_ = 0;
  private long startTime_ ;
  private String owner_ ;
  private ClientInfo clientInfo_ = ClientInfo.DEFAULT ;
  
  public SessionMonitor(SessionMonitorListenerStack listeners, String owner) {
    startTime_ = System.currentTimeMillis() ;
    history_ = new LinkedList() ;
    listeners_ = listeners ;
    owner_ = owner ;
  }
  
  public String getSessionOwner() { return owner_ ; }
  
  public void log(ActionData data) {
    if(error_ != null) data.setError(error_) ;
    history_.add(data) ;
    error_ = null ;
    actionCount_++ ;
    listeners_.onLog(this, data) ;
  }
  
  public void error(String errorMessage, Throwable t) {
    String trace = "no trace is available" ;
    if(t != null) trace = ExceptionUtil.getStackTrace(t, 20) ;
    error_ = errorMessage + "\n" + trace  + "\n";
    errorCount_++ ;
    listeners_.onError(this, errorMessage, t) ;
  }
  
  public List getHistory() { return history_ ; }
  
  public List emptyHistory() {     return history_ ; }
  
  public SessionMonitorListenerStack getListeners() { return listeners_ ; }
  
  public int  getErrorCount() { return errorCount_ ; }
  
  public int getActionCount() { return actionCount_ ; }
  
  public String getRemoteUser() { return clientInfo_.getRemoteUser() ; }
  public String getIPAddress() { return  clientInfo_.getIpAddress(); } 
  
  public ClientInfo getClientInfo() { return clientInfo_ ; }
  public void       setClientInfo(ClientInfo ci) { clientInfo_ = ci ; }
  
  public long getAccessTime() { return startTime_ ; }
  public long getLiveTime() { return System.currentTimeMillis() - startTime_ ;}
  public long getLiveTimeInMinute() { return (System.currentTimeMillis() - startTime_)/60000 ;}
  public long getLiveTimeInSecond() { return (System.currentTimeMillis() - startTime_)/1000 ;}
}