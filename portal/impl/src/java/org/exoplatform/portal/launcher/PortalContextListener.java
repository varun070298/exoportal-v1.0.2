/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.launcher;

import java.util.Locale ;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.faces.application.ExoPortalViewHandler;
import org.exoplatform.portal.faces.component.UIPortal;
import org.exoplatform.portal.session.ExoPortal;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.resources.ResourceBundleService;
/**
 * Created by The eXo Platform SARL        .
 * Date: Jan 25, 2003
 * Time: 5:25:52 PM
 */
public class PortalContextListener implements ServletContextListener {
  
	protected ServletContext portalServletContext_ = null;

	public void contextInitialized(ServletContextEvent event) {
		portalServletContext_ = event.getServletContext();
    RootContainer rootContainer = RootContainer.getInstance();       
    PortalContainer pcontainer = rootContainer.getPortalContainer(portalServletContext_.getServletContextName()) ;
    if(pcontainer == null){
      pcontainer = rootContainer.createPortalContainer(portalServletContext_);
    }        
    PortalContainer.setInstance(pcontainer) ;
    customizeFaces() ;
    ResourceBundleService rservice =
    ((ResourceBundleService)pcontainer.getComponentInstanceOfType(ResourceBundleService.class)) ;
    rservice.getResourceBundle("locale.custom.custom", Locale.ENGLISH) ;  
    rservice.getResourceBundle("locale.users.default", Locale.ENGLISH) ;
    PortalContainer.setInstance(null) ;
  }

  private void customizeFaces() {
    //add RetoreViewListener to set the correct selected page component
    LifecycleFactory lfactory = 
      (LifecycleFactory) FactoryFinder.getFactory( FactoryFinder.LIFECYCLE_FACTORY );
    Lifecycle lifecycle = lfactory.getLifecycle( LifecycleFactory.DEFAULT_LIFECYCLE );
    lifecycle.addPhaseListener(new RestoreViewListener()) ;
    //recustomize the view handler
    ApplicationFactory appFactory = 
      (ApplicationFactory) FactoryFinder.getFactory( FactoryFinder.APPLICATION_FACTORY);
    Application app = appFactory.getApplication() ;
    ViewHandler impl = app.getViewHandler() ;
    app.setViewHandler(new ExoPortalViewHandler(impl)) ;
  }
  
	public void contextDestroyed(ServletContextEvent event) {
    RootContainer rootContainer = RootContainer.getInstance();
    PortalContainer pcontainer = 
      rootContainer.getPortalContainer(event.getServletContext().getServletContextName());
    if(pcontainer.isStarted())pcontainer.stop() ;
    rootContainer.removePortalContainer(event.getServletContext()) ;
  }
  
  
  public static class RestoreViewListener implements PhaseListener {
    public void afterPhase(PhaseEvent event) {
      SessionContainer scontainer = SessionContainer.getInstance() ;
      UIPortal uiPortal = (UIPortal)scontainer.getComponentInstanceOfType(ExoPortal.class) ;
      if(scontainer.getMonitor().getActionCount() < 1) {
        //hack to invoke processDecodes on the first request. 
        //jsf implementation call renderResponse in RestoreViewPhase on the 
        //first request. So the next phases of the first request won't be executed
        uiPortal.processDecodes(FacesContext.getCurrentInstance()) ;
        return ;
      }
      
      //reset the page component if user change the page by URL
      RequestInfo rinfo = 
        (RequestInfo) scontainer.getComponentInstanceOfType(RequestInfo.class);
      String nodeURI = rinfo.getPageName() ;
      if(nodeURI != null && ! uiPortal.getSelectedNode().getUri().equals(nodeURI)) {
        Node node = uiPortal.getRootNode().findNode(nodeURI) ;
        if(node != null) {
          try {
            uiPortal.setSelectedNode(node) ;
          } catch (Exception ex) {
            LogUtil.getLog(getClass()).error("Error: ", ex) ;
          }
        }
        event.getFacesContext().renderResponse()  ;
      }
    }
    
    public void beforePhase(PhaseEvent event) {
      
    }
    
    public PhaseId getPhaseId() {   return PhaseId.RESTORE_VIEW ;   }
  }
}