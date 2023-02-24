package org.exoplatform.services.portletcontainer.test.portlet;

import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.event.DefaultPortletMessage;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 19 nov. 2003
 * Time: 00:41:37
 */
public class PortletThatSendsMessage extends GenericPortlet{

	public void init(PortletConfig portletConfig) throws PortletException {
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
					throws PortletException, IOException {
		ExoPortletContext context = (ExoPortletContext) actionRequest.getPortletSession().getPortletContext();
		context.send("PortletThatReceivesMessage",
						new DefaultPortletMessage("message sent"),
						actionRequest);
		actionResponse.setRenderParameter("status", "Everything is ok");
	}

	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
					throws PortletException, IOException {
    renderResponse.setContentType("text/html");        
		ExoPortletContext context = (ExoPortletContext) renderRequest.getPortletSession().getPortletContext();
		context.send("PortletThatReceivesMessage",
						new DefaultPortletMessage("message sent"),
						renderRequest);
		PrintWriter w = renderResponse.getWriter();
    w.println("Everything is ok");
	}

	public void destroy() {
	}
}
