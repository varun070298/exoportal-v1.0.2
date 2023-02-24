/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.* ;
import net.sf.hibernate.Session;
import org.exoplatform.services.communication.message.Account;
import org.exoplatform.services.communication.message.MessageService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.*;
import org.picocontainer.Startable;
import org.exoplatform.container.configuration.*;
import org.exoplatform.container.configuration.ServiceConfiguration;
/**
 * Jul 20, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserEventAccountListener.java,v 1.1 2004/10/30 02:24:13 tuan08 Exp $
 */
public class UserEventAccountListener extends UserEventListener implements Startable {
  private MessageServiceImpl mservice_ ;
  private List accounts_ ;
  
	public UserEventAccountListener(ConfigurationManager cService, 
                                  OrganizationService orgService,
                                  MessageServiceImpl mservice) throws Exception {
    ServiceConfiguration sconf  = cService.getServiceConfiguration(getClass()) ;
    if(sconf.size() == 0) return ;
    accounts_ =  new ArrayList(5) ;
    Iterator i = sconf.values().iterator() ;
    while(i.hasNext()) {
      ObjectParam param = (ObjectParam) i.next() ;
      accounts_.add(param.getObject()) ;
    }
    orgService.addUserEventListener(this) ;
    mservice_ = mservice ;
  }
  
	public void postSave(User user, boolean isNew, XResources xresources) throws Exception {
	  Session session = (Session) xresources.getResource(Session.class) ;
    Iterator i = accounts_.iterator() ;
    while(i.hasNext()) {
      AccountListenerConfig config = (AccountListenerConfig) i.next() ;
      if(MessageService.STANDALONE_PROTOCOL.equals(config.getProtocol())) {
        Account account = createStandaloneAccount(user, config) ;
        mservice_.createAccount(session, account) ;
      } else {
        Account account = createMailAccount(user, config) ;
        mservice_.createAccount(session, account) ;
      }
    }
	}
  
   public void postDelete(User user,  XResources xresources) throws Exception {
     List accounts = mservice_.getAccounts(user.getUserName()) ;
     for(int i = 0; i < accounts.size(); i++) {
       Account account = (Account) accounts.get(i) ;
       mservice_.removeAccount(account) ;
     }
   }
  
	private Account createMailAccount(User user, AccountListenerConfig config){
	  Account account = mservice_.createAccountInstance() ;
	  account.setAccountName(config.getAccountName());
	  account.setOwner(user.getUserName()) ;
    account.setOwnerName(user.getFirstName() + " " + user.getLastName()) ;
    account.setReplyToAddress(user.getUserName() +"@" +config.getAccountName()) ;
	  account.setProtocol(config.getProtocol());
	  account.setProperty(Account.SERVER_SETTING_HOSTNAME, config.getServer());
	  account.setProperty(Account.SERVER_SETTING_USERNAME, user.getUserName());
	  account.setProperty(Account.SERVER_SETTING_PASSWORD, user.getPassword());
	  return account;
	}
  
  private Account createStandaloneAccount(User user, AccountListenerConfig config){
    Account account = mservice_.createAccountInstance() ;
    account.setAccountName(config.getAccountName());
    account.setOwner(user.getUserName()) ;
    account.setOwnerName(user.getFirstName() + " " + user.getLastName()) ;
    account.setReplyToAddress(user.getUserName() +"#" + config.getAccountName()) ;
    account.setProtocol(config.getProtocol());
    return account;
  }
  
  public void start() { }
  public void stop() { }
}