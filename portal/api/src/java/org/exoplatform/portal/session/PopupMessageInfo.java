/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.session;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 8, 2005
 * @version $Id$
 */
public class PopupMessageInfo {
  private StringBuffer buf_ ;
  
  public void addMessage(String message) {
    if(buf_ == null) buf_ = new StringBuffer() ;
    else buf_.append("\n") ;
    buf_.append(message) ;
  }
  
  public String getPupupMessage() { 
    if(buf_ == null) return null ;
    String s = buf_.toString() ;
    buf_ = null ;
    return s ;
  }
}
