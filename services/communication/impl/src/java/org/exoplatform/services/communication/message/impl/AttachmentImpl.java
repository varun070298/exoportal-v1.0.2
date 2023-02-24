/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.communication.message.impl;

import org.exoplatform.services.communication.message.Attachment;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 sept. 2004
 *
 * @hibernate.class  table="EXO_MESSAGE_ATTACHMENT"
 * @hibernate.cache  usage="read-write"
 */
public class AttachmentImpl implements Attachment{

  private String id;
  private String messageId_;
  private String folderId_ ;
  private String name_;
  private byte[] content_;

  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String getId() { return id; }
  public void setId(String id) {  this.id = id; }

  /**
   * @hibernate.property
   **/
  public String getMessageId() { return messageId_; }
  public void setMessageId(String messageId) { this.messageId_ = messageId; }
  
  /**
   * @hibernate.property
   **/
  public String getFolderId() { return folderId_; }
  public void setFolderId(String folderId) { folderId_ = folderId; }
 
  /**
   * @hibernate.property
   **/
  public String getName() {  return name_; }
  public void setName(String name) { this.name_ = name; }

  /**
   * @hibernate.property  type="binary"
   **/
  public byte[] getContent() {  return content_; }
  public void setContent(byte[] content) {  this.content_ = content; }
  
  public Attachment cloneAttachment() {
    AttachmentImpl newAttachment = new AttachmentImpl() ;
    newAttachment.setName(getName()) ;
    newAttachment.setContent(getContent()) ;
    return newAttachment ;
  }
}