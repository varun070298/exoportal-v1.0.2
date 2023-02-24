/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.util.Enumeration;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
/**
 * Wed, Dec 22, 2003 @ 23:14
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIContentPortlet.java,v 1.2 2004/07/26 02:27:18 tuan08 Exp $
 */
public class UIContentPortlet extends UIPortlet {
	
	final static public String[] DEFAULT_VALUES = { "title=", "uri=", "encoding="};
	
	public UIContentPortlet() throws Exception {
		setClazz("UIContentPortlet");
    setRendererType("PyramidTabBarRenderer") ;
		ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();    
		PortletRequest request = (PortletRequest) eContext.getRequest();        
		PortletPreferences prefs = request.getPreferences();
		Enumeration e = prefs.getNames();
		List children = getChildren();
		boolean select = true;
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String[] values = prefs.getValues(name, DEFAULT_VALUES);
			ContentConfig contentConfig = new ContentConfig(name, values);
			UIContentTab uiContentTab = createUIContentTab(contentConfig);
			uiContentTab.setId("id" + Integer.toString(uiContentTab.hashCode()));
			uiContentTab.setRendered(select);
			children.add(uiContentTab);
			select = false;
		}
	}
  
  protected UIContentTab createUIContentTab(ContentConfig contentConfig) throws Exception{
    return new UIContentTab(contentConfig);
  }
}