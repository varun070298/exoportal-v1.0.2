/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import javax.mail.URLName;
import org.exoplatform.services.communication.message.MailService;
import org.exoplatform.services.communication.message.MessageService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 6, 2004
 * @version $Id: POP3MessageProtocolPlugin.java,v 1.1 2004/10/14 23:29:22 tuan08 Exp $
 */
public class POP3MessageProtocolPlugin extends  BaseMailMessageProtocolPlugin  {
  
  public POP3MessageProtocolPlugin(MessageService mservice, MailService mailService) {
    super(mservice, mailService) ;
  }
  
  public String getProtocol() { return MessageService.POP3_PROTOCOL ; }
  
  public URLName getInboxFolderURLName(String host, String userName , String password) {
    return new URLName("pop3", host, 110, "INBOX" , userName, password);
  }
}