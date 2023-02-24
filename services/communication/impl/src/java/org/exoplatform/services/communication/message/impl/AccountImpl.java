/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.services.communication.message.Account;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 * @hibernate.class  table="EXO_MESSAGE_ACCOUNT"
 * @hibernate.cache  usage="read-write"
 */
public class AccountImpl  implements Account {
  private String id_ ;
  private String accountName_ ;
  private String owner_ ;
  private String ownerName_ ;
  private String replyToAddress_ ;
  private String accessRole_ ;
  private String signature_ ;
  private String protocol_ ;
  
  private ExoProperties properties_  ;

  public AccountImpl() {
    properties_ = new ExoProperties(3) ;
  }
  
  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String getId() { return id_ ; }
  public void   setId(String id) { id_ = id ; }
  
  /**
   * @hibernate.property
   **/
  public String getAccountName() { return accountName_ ; } 
  public void   setAccountName(String accountName) {accountName_ = accountName ; } 
  
  /**
   * @hibernate.property
   **/
  public String getOwner() { return owner_ ; } 
  public void   setOwner(String userName) {owner_ = userName ; } 

  /**
   * @hibernate.property
   **/
  public String getOwnerName() { return ownerName_ ; } 
  public void   setOwnerName(String userName) {ownerName_ = userName ; } 
  
  /**
   * @hibernate.property
   **/
  public String getReplyToAddress() { return replyToAddress_ ; }
  public void   setReplyToAddress(String address) { replyToAddress_ = address ; }
  
  /**
   * @hibernate.property
   **/
  public String getSignature() { return signature_ ; }
  public void   setSignature(String signature) { signature_ = signature ; }

  /**
   * @hibernate.property
   **/
  public String getAccessRole()  { return accessRole_ ; } 
  public void   setAccessRole(String role) { accessRole_ = role ; } 
  
  /**
   * @hibernate.property
   **/
  public String getProtocol() { return protocol_ ; } 
  public void   setProtocol(String accountType) {protocol_ = accountType ; } 
  
  /**
   * @hibernate.property
   **/
  public String getPropertiesText() { return properties_.toText() ; } 
  public void   setPropertiesText(String text) {  
    properties_.clear() ;
    properties_.addPropertiesFromText(text) ;
  } 
  
  public ExoProperties getProperties()  { return properties_ ; }
  public void setProperties(ExoProperties props)  { properties_ = props ; }
  
  public String getProperty(String key)  { return properties_.getProperty(key) ; }
  public void   setProperty(String key, String value)  {
    properties_.setProperty(key, value) ;
  }
}