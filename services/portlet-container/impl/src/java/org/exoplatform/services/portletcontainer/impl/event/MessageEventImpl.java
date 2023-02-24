package org.exoplatform.services.portletcontainer.impl.event;


import javax.portlet.ActionRequest;
import org.exoplatform.services.portletcontainer.event.MessageEvent;
import org.exoplatform.services.portletcontainer.event.PortletMessage;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 01:05:24
 */
public class MessageEventImpl implements MessageEvent{

	private PortletMessage message;
	private ActionRequest request;

	public MessageEventImpl(PortletMessage message, ActionRequest request) {
		this.message = message;
		this.request = request;
	}

	public PortletMessage getMessage() {
		return message;
	}

	public ActionRequest getActionRequest() {
		return request;
	}

}
