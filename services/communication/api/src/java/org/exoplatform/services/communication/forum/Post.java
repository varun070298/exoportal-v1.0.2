/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

import java.util.Date ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface Post {
  public String   getId() ;
  public String   getSubject() ;  
  public void     setSubject(String s) ;  

  public String   getMessage() ;  
  public void     setMessage(String s);  

  public String   getOwner() ; 
  public void     setOwner(String s) ;  
  
  public Date     getCreatedDate() ; 
  public void     setCreatedDate(Date d) ;  

  public String   getModifiedBy() ; 
  public void     setModifiedBy(String s) ;  

  public Date     getModifiedDate() ; 
  public void     setModifiedDate(Date d) ;  

  public String   getRemoteAddr() ; 
  public void     setRemoteAddr(String s) ;  
}
