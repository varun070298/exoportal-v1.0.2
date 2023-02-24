/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portlets.content.ContentUtil;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
import org.exoplatform.services.portal.model.Node;
/**
 * Wed, Dec 22, 2003 @ 23:14
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: UIContentConfig.java,v 1.4 2004/11/03 04:24:55 tuan08 Exp $
 */
public class UIContentConfig extends UIExoCommand  {
	
	final static public String[] DEFAULT_VALUES = { "title=", "uri=", "encoding="};
	
	private Map configs_;

  private boolean modificationAllowed_;
	
	public UIContentConfig() {
		setRendererType("ContentConfigRenderer");
		setId("UIContentConfig");
		updateConfigs();
    addActionListener(DeleteActionListener.class, DELETE_ACTION) ;
    addActionListener(EditActionListener.class, "edit") ;
    addActionListener(ModifyActionListener.class, "modify") ;
    addActionListener(NewActionListener.class, "new") ;
	}
	
	private void updateConfigs() {
		ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
		PortletRequest request = (PortletRequest) eContext.getRequest();    
		PortletPreferences prefs = request.getPreferences();
		Enumeration e = prefs.getNames();
		configs_ = new HashMap();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String[] values = prefs.getValues(name, DEFAULT_VALUES);
			ContentConfig contentConfig = new ContentConfig(name, values);
			configs_.put(name, contentConfig);
		}
	}
	
	public Map getAllConfigs() {
		return configs_;
	}
	
	public String getFamily() {
		return "org.exoplatform.portlets.content.display.component.UIContentConfig";
	}
  
  public boolean isModificationAllowed() {
    return modificationAllowed_;
  }  
  
  public void setModificationAllowed(boolean modificationAllowed) {
    modificationAllowed_ = modificationAllowed;
  }
	
	public void saveContentConfig(String action, ContentConfig config) throws Exception {
		FacesContext context = getFacesContext();
		PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
		PortletPreferences prefs = request.getPreferences();
		
		String uri = config.getUri();
		if (action.equals(ContentUtil.EDIT_STATE)) {			
			if (uri == null) {
				uri = "";
				config.setUri(uri);
			}
		} else if (action.equals(ContentUtil.MODIFY_STATE)) {
      if(uri == null || "".equals(uri)){
        ExoPortal portal = (ExoPortal)SessionContainer.getComponent(ExoPortal.class) ;
        Node selectedNode = portal.getSelectedNode();
        uri = selectedNode.getUri();
        if("/".equals(uri)){
          uri = "/" + selectedNode.getName();          
        }        
      } else {
        if(!uri.startsWith("/"))
          uri = "/" + uri;
      }
			ContentUtil.storeContent(uri, config.getContent());
		}
		
		String name = config.getName();
		String[] tmp = new String[3];
		tmp[0] = "title=" + config.getTitle();
		tmp[1] = "uri=" + uri;
		tmp[2] = "encoding=" + config.getEncoding();
		
		prefs.setValues(name, tmp);
		prefs.store();
		updateConfigs();
	}
  
	static public class DeleteActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIContentConfig uiConfig = (UIContentConfig) event.getComponent() ;
			String name =  event.getParameter("name") ;
			//if (!"default".equals(name)) {
				//uiConfig.configs_.remove(name);
				PortletRequest request = 
          (PortletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				PortletPreferences prefs = request.getPreferences();
				prefs.reset(name);
				prefs.store();
        uiConfig.updateConfigs();
			//}
		}
	}
  
  static public class EditActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIContentConfig uiConfig = (UIContentConfig) event.getComponent() ;
      String name = event.getParameter("name");
      ContentConfig config = (ContentConfig) uiConfig.configs_.get(name);
      UIContentConfigForm uiConfigForm = 
        (UIContentConfigForm)uiConfig.getSibling(UIContentConfigForm.class);
      uiConfigForm.setContentConfig(config);
      uiConfig.setRenderedSibling(UIContentConfigForm.class) ;
    }
  }
  
  static public class NewActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIContentConfig uiConfig = (UIContentConfig) event.getComponent() ;
      UIContentConfigForm uiConfigForm = 
        (UIContentConfigForm)uiConfig.getSibling(UIContentConfigForm.class);
      uiConfigForm.setContentConfig(null);
      uiConfig.setRenderedSibling(UIContentConfigForm.class) ;
    }
  }
  
  static public class ModifyActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIContentConfig uiConfig = (UIContentConfig) event.getComponent() ;
      String name = event.getParameter("name");
      ContentConfig config = (ContentConfig) uiConfig.configs_.get(name);
      UIContentEditor uiEditor = 
        ( UIContentEditor)uiConfig.getSibling( UIContentEditor.class);
      uiEditor.setContentConfig(config);
      uiConfig.setRenderedSibling(UIContentEditor.class) ;
    }
  }

}