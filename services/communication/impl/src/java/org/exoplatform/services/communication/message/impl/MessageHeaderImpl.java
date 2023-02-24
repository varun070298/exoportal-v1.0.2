/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.communication.message.MessageHeader;


/**
 * Fri, May 30, 2003 @ @author: Tuan Nguyen
 * @version: $Id: MessageHeaderImpl.java,v 1.8 2004/10/27 03:11:16 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com Author : Benjamin Mestrallet
 *         benjamin.mestrallet@exoplatform.com
 * 
 * @hibernate.class table="EXO_MESSAGE"
 */
public class MessageHeaderImpl implements MessageHeader {
  private String id_;

  private String folderId_;

  private String mailMessageId_;

  private String from_ = "";

  private String flags_ = "";

  private String subject_ = "";

  private Date receivedDate_;  

  public MessageHeaderImpl() {        
  }

  /**
   * @hibernate.id generator-class="assigned"
   */
  public String getId() {
    return id_;
  }

  public void setId(String value) {
    id_ = value;
  }

  /**
   * @hibernate.property
   */
  public String getFolderId() {
    return folderId_;
  }

  public void setFolderId(String value) {
    folderId_ = value;
  }

  /**
   * @hibernate.property
   */
  public String getMailMessageId() {
    return mailMessageId_;
  }

  public void setMailMessageId(String value) {
    mailMessageId_ = value;
  }

  public String getFrom() {
    return from_;
  }

  public void setFrom(String value) {
    from_ = value;
  }

  /**
   * @hibernate.property
   */
  public String getSender() {
    return from_;
  }

  public void setSender(String value) {
    from_ = value;
  }

  /**
   * @hibernate.property
   */
  public String getFlags() {    
    return flags_;
  }

  public void setFlags(String value) {
    flags_ = value;
  }

  /**
   * @hibernate.property
   */
  public String getSubject() {
    return subject_;
  }

  public void setSubject(String value) {
    subject_ = value;
  }

  /**
   * @hibernate.property
   */
  public Date getReceivedDate() {
    return receivedDate_;
  }

  public void setReceivedDate(Date value) {
    receivedDate_ = value;
  }

  public boolean isNew() {
    return !hasFlag(MessageHeader.SEEN_FLAG);
  }

  public boolean hasFlag(String flag) {
    if(flags_ == null)  return false ;
    return flags_.indexOf(flag) >= 0 ;
  }

  public void addFlag(String flag) {
    if(flags_ == null || flags_.length() == 0) {
      flags_ = flag ;
    } else if(flags_.indexOf(flag) < 0){
      flags_ = "," + flag ;
    }
  }

  public void removeFlag(String flag) {
    String[] flags = StringUtils.split(flags_, ',');
    StringBuffer b = new StringBuffer() ;
    for (int i = 0; i < flags.length; i++) {
      if (!flags[i].equals(flag)) {
        if(i == 0) b.append(flags[i]) ;
        else b.append(',').append(flags[i]) ;
      }
    }
    flags_ = b.toString() ;
  }
  
  public String[] getFlagsAsArray(){
    return StringUtils.split(flags_, ',');
  }
}