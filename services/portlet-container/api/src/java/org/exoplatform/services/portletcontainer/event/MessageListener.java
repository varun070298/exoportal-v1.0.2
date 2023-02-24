package org.exoplatform.services.portletcontainer.event;

import javax.portlet.PortletException;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 00:08:25
 */
public interface MessageListener {

	public void messageReceived(MessageEvent event)
					throws PortletException;

}
