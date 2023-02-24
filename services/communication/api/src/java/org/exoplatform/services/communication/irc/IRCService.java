/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 7, 2003
 * Time: 7:02:20 PM
 */
package org.exoplatform.services.communication.irc;

public interface IRCService {

  public int getPort();
  public void start();
  public void stop();
  public String getHostAddress();
  public String getHost();
  public boolean isServerStarted();

}
