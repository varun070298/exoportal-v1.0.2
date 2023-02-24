/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.portlets.communication.forum.component;

import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import org.exoplatform.Constants;
import org.exoplatform.faces.FacesUtil;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.portlets.communication.forum.ForumACL;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.communication.forum.ForumServiceContainer;
import org.exoplatform.services.grammar.wiki.WikiEngineService;
import org.exoplatform.services.indexing.IndexingService;
import org.exoplatform.services.organization.OrganizationService;
/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class UIForumPortlet extends UIPortlet {
  
  private Stack history;

  public UIForumPortlet(ForumServiceContainer container,
                        OrganizationService orgService,
                        IndexingService iservice,
                        WikiEngineService weService) throws Exception {
    setId("UIForumPortlet");
    setRendererType("ChildrenRenderer");      
    history = new Stack();
    PortletPreferences prefs = FacesUtil.getPortletPreferences();
    String forumOwner = prefs.getValue("forum-owner",  Constants.DEFAUL_PORTAL_OWNER);
    String remoteUser = 
      FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    ForumACL acl = new ForumACL(remoteUser);
    if (remoteUser == null)  remoteUser = "anonymous";
    ForumService service = container.findForumService(forumOwner);    

    List children = getChildren();
    
    UIToolbarPanel toolbarPanel = new UIToolbarPanel();
    toolbarPanel.setRendered(true);
    children.add(toolbarPanel);    
    
    UIViewCategories uiViewCategories = new UIViewCategories(service);
    uiViewCategories.setRendered(true);
    children.add(uiViewCategories);

    UITopics uiTopics = new UITopics(orgService, service, acl);
    uiTopics.setRendered(false);
    children.add(uiTopics);

    UIPosts uiPosts = new UIPosts(weService, orgService, service, acl);
    uiPosts.setRendered(false);
    children.add(uiPosts);

    UIPostForm uiPostForm = new UIPostForm(service);
    uiPostForm.setRendered(false);
    children.add(uiPostForm);

    UIForumSearcher panel = new UIForumSearcher(service, iservice);
    panel.setRendered(false);
    children.add(panel);
    
    UIWatchForm uiWatchForm = new UIWatchForm(service) ;
    uiWatchForm.setRendered(false) ;
    children.add(uiWatchForm) ;
  }
  
  public UIComponent getRenderedComponent(){
    Collection children = getChildren();
    for (Iterator iter = children.iterator(); iter.hasNext();) {
      UIComponent element = (UIComponent) iter.next();
      if(!(element instanceof UIToolbarPanel) && element.isRendered())
        return element;
    }
    return null;
  }
  
  public void addHistoryElement(UIComponent component){
    history.add(component);
  }
  
  public void undo(){    
    if(history.size() > 0)
      setRenderedComponent(((UIComponent)history.pop()).getId());
  }

  public void processDecodes(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap();
    String uicomponent = (String) paramMap.get(UICOMPONENT);
    UIExoComponent uiComp = findRenderedComponentById(uicomponent);
    if (uiComp != null) {
      uiComp.processDecodes(context);
      return;
    }
    //user click back button
    uiComp = findComponentById(uicomponent);
    if (uiComp.canDecodeInvalidState()) {
      uiComp.processDecodes(context);
    }
  }

}