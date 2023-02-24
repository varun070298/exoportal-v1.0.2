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
 * @version $Id: IMAPMessageProtocolPlugin.java,v 1.6 2004/10/14 23:29:22 tuan08 Exp $
 */
public class IMAPMessageProtocolPlugin extends  BaseMailMessageProtocolPlugin  {
  
  public IMAPMessageProtocolPlugin(MessageService mservice, MailService mailService) {
    super(mservice, mailService) ;
  }
  
  public String getProtocol() { return MessageService.IMAP_PROTOCOL ; }
  
  public URLName getInboxFolderURLName(String host, String userName , String password) {
    return new URLName("imap", host, 143, "INBOX" , userName, password);
  }
}