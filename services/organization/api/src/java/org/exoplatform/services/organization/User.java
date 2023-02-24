/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 21, 2003
 * Time: 3:22:54 PM
 */
package org.exoplatform.services.organization;

import java.util.Date ;

public interface User {
  
  public String getUserName();
  public void   setUserName(String s) ;

  public String getPassword();
  public void   setPassword(String s) ;
  
  public String getFirstName();
  public void   setFirstName(String s) ;

  public String getLastName();
  public void   setLastName(String s) ;

  public String getFullName();
  public void   setFullName(String s) ;

  public String getEmail();
  public void   setEmail(String s) ;

  public Date   getCreatedDate() ;
  public void   setCreatedDate(Date t) ;
  
  public Date getLastLoginTime() ;
  public void setLastLoginTime(Date t) ;
}
