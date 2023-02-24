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
 * Time: 00:14:49
 */
public class DefaultPortletMessage implements PortletMessage{

	private String message;

	public DefaultPortletMessage(){
	}

	public DefaultPortletMessage(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
