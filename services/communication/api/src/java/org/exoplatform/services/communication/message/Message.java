/*******************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved. * Please look
 * at license.txt in info directory for more license detail. *
 ******************************************************************************/
package org.exoplatform.services.communication.message;

import java.util.List;

/*
 * Fri, May 30, 2003 @ @author: Tuan Nguyen @version: $Id: Message.java,v 1.2
 * 2004/08/27 02:43:29 tuan08 Exp $ @since: 0.0 @email: tuan08@yahoo.com
 */
public interface Message extends MessageHeader {
  static final public String SYNCHRONIZED_FLAG = "exo:synchronized" ;
  static final public String READ_FLAG = "exo:read" ;
  
  public String getTo();

  public void setTo(String value);

  public String getCC();

  public void setCC(String value);

  public String getBCC();

  public void setBCC(String value);

  public String getBody();

  public void setBody(String value);

  public void addAttachment(Attachment attachment) ;
  
  public List getAttachment() ; 
}