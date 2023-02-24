/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: ExoAuthenticator.java,v 1.1 2004/09/12 22:48:20 tuan08 Exp $
 */
public class ExoAuthenticator extends Authenticator{
  private PasswordAuthentication authentication_ ;
  
  public ExoAuthenticator(String userName , String password) {
    authentication_ = new PasswordAuthentication(userName, password) ;
  }
  
  protected PasswordAuthentication getPasswordAuthentication() {
    return authentication_ ;
  }
}