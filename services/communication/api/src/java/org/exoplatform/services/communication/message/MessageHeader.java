/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import java.util.Date ;
/**
 * Fri, May 30, 2003 @  
 * @author: Tuan Nguyen
 * @version: $Id: MessageHeader.java,v 1.4 2004/10/13 03:32:21 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public interface MessageHeader {
  
  public static final String ANSWERED_FLAG = "answered";
  public static final String DELETED_FLAG = "deleted";
  public static final String DRAFT_FLAG = "draft";
  public static final String FLAGGED_FLAG = "flagged";
  public static final String RECENT_FLAG = "recent";
  public static final String SEEN_FLAG = "seen";
  public static final String USER_FLAG = "user";  
  
  public static final String[] SUPPORTED_FLAGS = 
  { ANSWERED_FLAG, DELETED_FLAG, DRAFT_FLAG, FLAGGED_FLAG, RECENT_FLAG, SEEN_FLAG,
    USER_FLAG} ;

  public String  getId() ;
  public void    setId(String value) ;

  public  String getFrom() ;
  public  void   setFrom(String value) ;

  public  String getFlags() ;
  public  void   setFlags(String value) ;

  public  String getSubject() ;
  public  void   setSubject(String value) ;

  public Date getReceivedDate() ;
  public void   setReceivedDate(Date value) ;
  
  public boolean  isNew() ;
  
  public boolean hasFlag(String flag);
  public void addFlag(String flag);
  public void removeFlag(String flag);
  public String[] getFlagsAsArray();
}