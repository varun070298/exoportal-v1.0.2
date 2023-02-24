package org.exoplatform.services.portletcontainer.test.listeners;

import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portletcontainer.event.MessageEvent;
import org.exoplatform.services.portletcontainer.event.DefaultPortletMessage;
import org.exoplatform.services.portletcontainer.event.MessageListener;
import org.apache.commons.logging.Log;

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
 * Time: 02:20:41
 */
public class SimpleMessageListener2 implements MessageListener{

	private LogService logService;
	private Log log;

	public SimpleMessageListener2(LogService logService) {
		this.logService = logService;
		log = logService.getLog("org.exoplatform.portal.container");
	}

	public void messageReceived(MessageEvent messageEvent) throws PortletException {
		DefaultPortletMessage message = (DefaultPortletMessage) messageEvent.getMessage();
		log.debug("Message received in listener 2 : " + message.getMessage());
	}


}
