/*******************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved. * Please look
 * at license.txt in info directory for more license detail. *
 ******************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.*;
import org.exoplatform.services.communication.message.Attachment;
import org.exoplatform.services.communication.message.Message;
/**
 * Fri, May 30, 2003 @ @author: Tuan Nguyen @version: $Id: Message.java,v 1.2
 * 2004/08/27 02:43:29 tuan08 Exp $ @since: 0.0 @email: tuan08@yahoo.com
 * Author : Benjamin Mestrallet
 *          benjamin.mestrallet@exoplatform.com
 * @hibernate.class  table="EXO_MESSAGE"
 * @hibernate.cache  usage="read-write"
 **/
public class MessageImpl extends MessageHeaderImpl implements Message {
  private String  receiver         = "";
  private String  CC               = "";
  private String  BCC              = "";
  private String  body             = "";
  private List    attachments;

  public String getTo() { return receiver;}
  public void setTo(String value) { receiver = value; }
  
  /**
   * @hibernate.property
   **/
  public String getReceiver() { return receiver;}
  public void   setReceiver(String value) { receiver = value; }
  
  /**
   * @hibernate.property
   **/
  public String getCC() {  return CC; }
  public void setCC(String value) {  CC = value; }

  /**
   * @hibernate.property
   **/
  public String getBCC() {  return BCC; }
  public void setBCC(String value) {  BCC = value; }

  /**
   * @hibernate.property
   **/
  public String getBody() {  return body; }
  public void setBody(String value) {  body = value; }

  public void addAttachment(Attachment attachment) {
    if(attachments == null) attachments = new ArrayList() ;
    attachments.add(attachment) ;
  }
  
  public List getAttachment() {  return attachments; }
  
  public MessageImpl cloneMessage() {
    MessageImpl newMessage  = new MessageImpl() ;
    newMessage.setFolderId(getFolderId()) ;
    newMessage.setMailMessageId(getMailMessageId()) ;
    newMessage.setFrom(getFrom()) ;
    newMessage.setTo(getTo());
    newMessage.setCC(getCC()) ;
    newMessage.setBCC(getBCC()) ;
    newMessage.setFlags(getFlags());
    newMessage.setSender(getSender()) ;
    newMessage.setBody(getBody()) ;
    newMessage.setSubject(getSubject()) ;
    newMessage.setReceivedDate(getReceivedDate()) ;
    if(attachments != null) {
      for(int i = 0; i < attachments.size(); i++) {
        AttachmentImpl attachment = (AttachmentImpl) attachments.get(i) ;
        newMessage.addAttachment(attachment.cloneAttachment()) ;
      }
    }
    return newMessage ;
  }
}