/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

/*
 * @author: Tuan Nguyen
 * @version: $Id: ForumServiceException.java,v 1.1.1.1 2004/03/05 21:56:56 benjmestrallet Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class ForumServiceException extends Exception {
  final static public int UNKNOWN_ERROR = 0 ;
  final static public int CATEGORY_NOT_FOUND = 1 ;
  final static public int FORUM_NOT_FOUND = 2 ;
  final static public int TOPIC_NOT_FOUND = 3 ;
  final static public int POST_NOT_FOUND = 4 ;
  
  private int errorCode_ ;

  public ForumServiceException(String s, int errorCode) {
    super(s) ;
    errorCode_ = errorCode ;
  }
  public int getErrorCode() { return errorCode_ ; }
}
