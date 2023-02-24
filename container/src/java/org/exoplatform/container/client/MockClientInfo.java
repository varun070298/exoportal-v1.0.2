/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.client;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 7, 2005
 * @version $Id$
 */
public class MockClientInfo implements ClientInfo {
  public MockClientInfo() {} 
  
  public String getClientType() {  return "N/A"; }

 
  public String getRemoteUser() {  return "N/A"; }

  public String getIpAddress() {  return "N/A"; }

  public String getClientName() {  return "N/A"; }
  
}
