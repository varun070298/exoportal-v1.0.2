/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.Properties;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.commons.exception.ExoMessageException;
import org.exoplatform.container.configuration.*;
import org.exoplatform.container.configuration.ConfigurationManager;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 13, 2004
 * @version $Id: MailServiceImpl.java,v 1.3 2004/11/03 01:23:39 tuan08 Exp $
 */
public class MailServiceImpl implements MailService {
  private Session mailSession_;
  private String smtpServer_ ;
  
  public MailServiceImpl(ConfigurationManager confService) throws Exception {
    ServiceConfiguration sconf  = confService.getServiceConfiguration(MailService.class) ;
    ValueParam param = (ValueParam)sconf.getParameter("outgoing.mail.server") ;
    smtpServer_ = param.getValue() ;
    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpServer_);
    ExoAuthenticator auth = new ExoAuthenticator("exo.platform", "exo2004") ;
    mailSession_ = Session.getDefaultInstance(props, null);
  }
  
  public Session getMailSession() { return mailSession_ ; }
  
  public String getOutgoingMailServer()  { return smtpServer_ ; }
  
  public void sendMessage(org.exoplatform.services.communication.message.Message message) throws Exception  {
    try {
      MimeMessage mimeMessage = new MimeMessage(mailSession_);
      InternetAddress author = new InternetAddress(message.getFrom());
      mimeMessage.setFrom(author);        
      
      mimeMessage.setFlags(fillFlags(message.getFlagsAsArray()), true);
      String addr = message.getTo() ;
      if(addr != null && addr.length() > 0) {
        mimeMessage.addRecipients(javax.mail.Message.RecipientType.TO, addr);
      }
      addr = message.getCC() ;
      if(addr != null && addr.length() > 0) {
        mimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, addr);
      }
      addr = message.getBCC() ;
      if(addr != null && addr.length() > 0) {
        mimeMessage.addRecipients(javax.mail.Message.RecipientType.BCC, addr);
      }
      mimeMessage.setSubject(message.getSubject());
      mimeMessage.setText(message.getBody());
      Transport.send(mimeMessage);
    } catch (javax.mail.MessagingException ex) {
      ex.printStackTrace();
      Object[] args = {ex.getMessage()} ;
      throw new ExoMessageException("MessageService.send-message-fail", args) ;
    }
  }
  
  private Flags fillFlags(String[] flagsAsArray) {
    Flags flags = new Flags();
    for (int i = 0; i < flagsAsArray.length; i++) {
      String flag = flagsAsArray[i];
      if(MessageHeader.ANSWERED_FLAG.equals(flag)){
        flags.add(Flags.Flag.ANSWERED);
      } else if(MessageHeader.DELETED_FLAG.equals(flag)){
        flags.add(Flags.Flag.DELETED);
      } else if(MessageHeader.DRAFT_FLAG.equals(flag)){
        flags.add(Flags.Flag.DRAFT);      
      } else if(MessageHeader.RECENT_FLAG.equals(flag)){
        flags.add(Flags.Flag.RECENT);
      } else if(MessageHeader.SEEN_FLAG.equals(flag)){
        flags.add(Flags.Flag.SEEN);
      } else if(MessageHeader.USER_FLAG.equals(flag)){
        flags.add(Flags.Flag.USER);
      } else if(MessageHeader.FLAGGED_FLAG.equals(flag)){
        flags.add(Flags.Flag.FLAGGED);        
      } else {
        //user flag
        flags.add(flag);
      }
    }
    return flags;
  }
}
