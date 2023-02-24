/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 27, 2004
 * @version $Id$
 */
public interface Watcher {
  public String getTarget() ;
  
  public String getUserName() ;
  public void setUserName(String username) ;
  
  public String getMessageProtocol() ;
  public void   setMessageProtocol(String s) ;
  
  public String getAddress() ;
  public void   setAddress(String email) ; 
}
