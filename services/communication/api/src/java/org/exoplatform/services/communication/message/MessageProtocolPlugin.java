/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 6, 2004
 * @version $Id: MessageProtocolPlugin.java,v 1.1 2004/09/07 01:33:01 tuan08 Exp $
 */
public interface MessageProtocolPlugin {
  public String getProtocol() ;
  public void sendMessage(Account account, Message message) throws Exception ;
  public void synchronize(Account account) throws Exception ;
}