/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import org.exoplatform.services.communication.message.*;
import org.picocontainer.Startable;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 6, 2004
 * @version $Id: BaseMessageProtocolPlugin.java,v 1.2 2004/09/17 15:13:17 tuan08 Exp $
 */
abstract public class BaseMessageProtocolPlugin 
	implements MessageProtocolPlugin, Startable {
  protected MessageService mservice_ ;
  
  public BaseMessageProtocolPlugin(MessageService mservice) {
    mservice_ = mservice ;
    mservice.addMessageProtocolPlugin(this) ;
  }
  
  protected void saveSentMessage(Account account, Message message) throws Exception {
    MessageImpl mesageImpl = (MessageImpl) message ;
    Folder folder = mservice_.getFolder(account, MessageService.SENT_FOLDER) ;
    Message backupMessage =  mesageImpl.cloneMessage() ;
    mservice_.createMessage(account, folder, backupMessage) ;
  }
  
  protected String formatEmail(String email) {
    if(email == null  || email.length() == 0) {
      email = "unknown@unknown.host" ;
    }
    return email ;
  }
   
  public void start() {} 
  public void stop() {} 
}