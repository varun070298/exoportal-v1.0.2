/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces;

import java.io.IOException;
import javax.faces.FactoryFinder;
import javax.faces.application.*;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.portlet.*;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.portlet.faces.application.FacesPortletViewHandler;
import org.exoplatform.portlet.faces.application.PortletFacesData;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 3, 2003 3:53:21 PM
 * @version $Id: ExoFacesPortlet.java,v 1.27 2004/11/03 01:21:33 tuan08 Exp $
 */
public class ExoFacesPortlet extends GenericPortlet {

  private static final String COMPONENT_TAG_STACK_ATTR = "javax.faces.webapp.COMPONENT_TAG_STACK";
  private static final String GLOBAL_ID_VIEW = "javax.faces.webapp.GLOBAL_ID_VIEW";
  private static final String CURRENT_VIEW_ROOT = "javax.faces.webapp.CURRENT_VIEW_ROOT";

  private Lifecycle facesLifecycle_ ;

  public void init(PortletConfig config) throws PortletException {
    super.init(config);
    try {
      XHTMLRendererConfiguration.confiure() ;
      XHTMLMPRendererConfiguration.confiure() ;
      LifecycleFactory lfactory = (LifecycleFactory) FactoryFinder.getFactory( FactoryFinder.LIFECYCLE_FACTORY );
      facesLifecycle_ = lfactory.getLifecycle( LifecycleFactory.DEFAULT_LIFECYCLE );
      ApplicationFactory factory = 
        (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
      Application app = factory.getApplication();
      ViewHandler handler = new FacesPortletViewHandler(app.getStateManager());
      app.setViewHandler( handler );
    } catch (Exception ex) {
      getLog().error("Error: ", ex);
    }
  }

  public void render (RenderRequest request, RenderResponse response) throws PortletException, java.io.IOException {
    //====================save portal jsf environment=====================
    Object stack = request.getAttribute(COMPONENT_TAG_STACK_ATTR );
    Object currentViewRoot = request.getAttribute(CURRENT_VIEW_ROOT );
    Object globalIdView = request.getAttribute(GLOBAL_ID_VIEW);
    //do not remove this
    request.removeAttribute(CURRENT_VIEW_ROOT );
    request.removeAttribute(COMPONENT_TAG_STACK_ATTR );
    request.removeAttribute(GLOBAL_ID_VIEW);
    //====================================================================
    WindowState state = request.getWindowState();
    if (!state.equals(WindowState.MINIMIZED)) {
      response.setContentType("text/html") ;
      processFacesLifeCycle(request, response) ;
    }
    //====================restore portal jsf environment=====================
    request.setAttribute(COMPONENT_TAG_STACK_ATTR, stack);
    request.setAttribute(CURRENT_VIEW_ROOT , currentViewRoot);
    request.setAttribute(GLOBAL_ID_VIEW, globalIdView);
    //====================================================================
  }

  public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException  {
    processFacesLifeCycle(request, response) ;
  }

  protected void processFacesLifeCycle(PortletRequest request, PortletResponse response) {
    String lastViewId = null ;
    FacesContext facesContext = null;
    try {
      PortletMode mode = request.getPortletMode() ;
      PortletConfig config = getPortletConfig();
      FacesContextFactory factory = 
        (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY );
      facesContext = factory.getFacesContext( config, request, response, facesLifecycle_ );  
      PortletFacesData data = PortletFacesData.getPortletFacesData(facesContext) ; 
      lastViewId = request.getParameter(".view-id") ;
      if (lastViewId != null) {
        int idx = lastViewId.indexOf("?") ;
        if(idx > 0) lastViewId = lastViewId.substring(0, idx);
      } else {
        lastViewId = data.getLastView(mode) ;
      }
      request.setAttribute("javax.servlet.include.path_info", lastViewId) ;
      
      facesLifecycle_.execute(facesContext);
      facesLifecycle_.render(facesContext);
      data.setLastView(mode, facesContext.getViewRoot().getViewId()) ;
    } catch (Throwable t) {
      t = ExceptionUtil.getRootCause(t) ;
      logError(t, lastViewId) ;
    } finally {
      facesContext.release();
    }
  }

  private void logError(Throwable t,  String lastViewId) {
  	StringBuffer b = new StringBuffer() ;
    b.append("portlet is  " + getPortletConfig().getPortletName()).append("\n") ;
    b.append("Last View Id is  " + lastViewId).append("\n") ;
    b.append("ExoFacesPortlet Error: ") ;
    getLog().error(b.toString(), t) ;
  }
  
  private Log getLog() { return  LogUtil.getLog("org.exoplatform.portlet.faces.faces"); }
}