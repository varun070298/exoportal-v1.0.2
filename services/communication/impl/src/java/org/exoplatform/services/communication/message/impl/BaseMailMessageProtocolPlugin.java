/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.ArrayList;
import java.util.List;
import javax.mail.URLName ;
import javax.mail.Flags  ;
import org.exoplatform.services.communication.message.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 6, 2004
 * @version $Id: BaseMailMessageProtocolPlugin.java,v 1.1 2004/10/14 23:29:22 tuan08 Exp $
 */
abstract public class BaseMailMessageProtocolPlugin extends  BaseMessageProtocolPlugin  {
  private MailService mailService_ ;
  
  public BaseMailMessageProtocolPlugin(MessageService mservice, MailService mailService) {
    super(mservice) ;
    mailService_ = mailService ;
  }
  
  
  public void sendMessage(Account account,Message message) throws Exception {
    message.setFrom(account.getReplyToAddress()) ;
    mailService_.sendMessage(message) ;
    message.setReceivedDate(new java.util.Date()) ;
    saveSentMessage(account, message) ;
  }

  public void synchronize(Account account) throws Exception {
    String userName = account.getProperty(Account.SERVER_SETTING_USERNAME) ;
    String password = account.getProperty(Account.SERVER_SETTING_PASSWORD) ;
    String hostName = account.getProperty(Account.SERVER_SETTING_HOSTNAME) ;
    //System.out.println("hostname: " + hostName + " username: " + userName + " password: " + password) ;
    URLName url = getInboxFolderURLName(hostName, userName, password);
    javax.mail.Folder remoteFolder = mailService_.getMailSession().getFolder(url);
    remoteFolder.open(javax.mail.Folder.READ_WRITE) ;
    javax.mail.Message[] message = remoteFolder.getMessages() ;
    List messageToUpdate = new ArrayList() ;
    Folder inbox = null ;
    for(int i = 0; i < message.length; i++) {
      Flags flags = message[i].getFlags();
      if(!MailUtil.hasSystemFlag(flags, Flags.Flag.FLAGGED))  {
        Message newMessage = MailUtil.createMessage(message[i]) ;
        if(inbox == null) {
        }
          inbox = mservice_.getFolder(account, MessageService.INBOX_FOLDER) ;
        mservice_.createMessage(account, inbox, newMessage) ;
        messageToUpdate.add(message[i]) ;
      }
    }
    message = new javax.mail.Message[messageToUpdate.size()] ;
    for(int i = 0; i < message.length; i++) {
      message[i] = (javax.mail.Message) messageToUpdate.get(i) ;
    }
    Flags flags = new Flags(Flags.Flag.FLAGGED) ;
    remoteFolder.setFlags(message, flags, true) ;
    remoteFolder.close(true) ;
  }
  
  abstract public URLName getInboxFolderURLName(String host, String userName , String password)  ;
}