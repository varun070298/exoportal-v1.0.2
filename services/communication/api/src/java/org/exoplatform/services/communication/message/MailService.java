/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import javax.mail.Session;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 13, 2004
 * @version $Id: MailService.java,v 1.1 2004/10/14 23:29:21 tuan08 Exp $
 */
public interface MailService {
  public Session getMailSession()  ;
  public String getOutgoingMailServer() ;
  public void sendMessage(Message message) throws Exception  ;  
}