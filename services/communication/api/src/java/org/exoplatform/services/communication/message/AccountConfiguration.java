/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import java.util.ArrayList ;
import java.util.List ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class AccountConfiguration  {
  private ArrayList accounts_ ;
  private String userName_ ;
  private String replyTo_ ;

  public AccountConfiguration(String userName) {
    userName_ = userName ;
    accounts_ = new ArrayList() ;
  }
  
  public String getReplyTo() { return replyTo_ ; } 
  public void   setReplyTo(String replyTo) {replyTo_ = replyTo ; } 
  
  public List getAccounts() { return accounts_ ; }
  
  public Account getAccount(String name) {
    for (int i = 0; i < accounts_.size() ; i++) {
      Account account =(Account) accounts_.get(i) ;
      if (name.equals(account.getAccountName())) return account ;
    }
    return null ;
  }

  public Account removeAccount(String name) {
    for (int i = 0; i < accounts_.size() ; i++) {
      Account account =(Account) accounts_.get(i) ;
      if (name.equals(account.getAccountName())) {
        accounts_.remove(i) ;
        return account ;
      }
    }
    return null ;
  }

  public void addAccount(Account account) {
   accounts_.add(account) ;
  }

  public Account getDefaultAccount() {
    if(accounts_.size() > 0) 
      return (Account) accounts_.get(0) ;
    return null; 
  }
}