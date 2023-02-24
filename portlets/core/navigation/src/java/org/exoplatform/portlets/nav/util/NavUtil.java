package org.exoplatform.portlets.nav.util;

import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

/**
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2004/08/05 13:11:14 $
 * @author Fahrid Djebbari
 * 
 */

public class NavUtil {
    
	public static PortletContext getPortletContext() {
		return (PortletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
	
	private static PortletRequest getPortletRequest() {
		return (PortletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static ActionResponse getPortletResponse() {
		return (ActionResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}	
	
	private static RenderResponse getRenderResponse() {
		return (RenderResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static PortletURL createActionURL() {
		return getRenderResponse().createActionURL();
	}	
	
	public static PortletURL createRenderURL() {
		return getRenderResponse().createRenderURL();
	}	
	
	public static PortletPreferences getPortletPreferences() {
		return getPortletRequest().getPreferences();	
	}
}