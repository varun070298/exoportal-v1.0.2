package org.exoplatform.services.portletcontainer.event;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 00:05:49
 */
public interface MessageEvent extends Event{

  public static final int MESSAGE_RECEIVED = 0;

	public PortletMessage getMessage();

}
