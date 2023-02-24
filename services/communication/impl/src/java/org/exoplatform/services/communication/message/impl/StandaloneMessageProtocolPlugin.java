/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.Date ;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.services.communication.message.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 6, 2004
 * @version $Id: StandaloneMessageProtocolPlugin.java,v 1.6 2004/11/03 01:23:39 tuan08 Exp $
 */
public class StandaloneMessageProtocolPlugin extends  BaseMessageProtocolPlugin  {
  
  public StandaloneMessageProtocolPlugin(MessageService mservice) {
    super(mservice) ;
  }
  
  public String getProtocol() { return MessageService.STANDALONE_PROTOCOL ; }
  
  public void sendMessage(Account account, Message message) throws Exception {
    String[] tmp = message.getTo().split("#") ;
    if(tmp.length != 2) {
      Object[] args = {message.getTo()} ;
      throw new ExoMessageException("MessageService.invalid-standalone-message-address", args) ;
    }
    String user = tmp[0] ;
    String accountName = tmp[1] ;
    Account receiverAccount  = mservice_.getAccount(user, accountName) ;
    if(receiverAccount == null) {
      Object[] args = {message.getTo()} ;
      throw new ExoMessageException("MessageService.address-not-found", args) ;
    }
    Folder folder = mservice_.getFolder(receiverAccount, MessageService.INBOX_FOLDER) ;
    message.setFrom(formatEmail(account.getReplyToAddress())) ;
    message.setReceivedDate(new Date()) ;
    message.addFlag(MessageHeader.RECENT_FLAG) ;
    mservice_.createMessage(account, folder, message) ;
    saveSentMessage(account, message) ;
  }

  public void synchronize(Account account) throws Exception {
    
  }
}
