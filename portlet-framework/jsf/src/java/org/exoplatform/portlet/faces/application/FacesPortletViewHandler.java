/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces.application;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.portlet.faces.component.UIExoViewRoot;
/**
 * @author Ove Ranheim (oranheim@users.sourceforge.net)
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 9, 2003 2:27:49 PM
 *
 */
public class FacesPortletViewHandler extends ViewHandler {
  protected static Log log_ = LogUtil.getLog("org.exoplatform.portlet.faces.faces" );
  
  protected StateManager stateManager_ ;

  public FacesPortletViewHandler(StateManager stateManager) {
    stateManager_ = stateManager ;
  }

  public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
    if(null == context || null == viewToRender)
      throw new NullPointerException("Faces context cannot be null");
    UIExoViewRoot exoViewToRender = (UIExoViewRoot) viewToRender ;
    String viewId = exoViewToRender.getViewId();
    if (exoViewToRender.isComponentView()) {
      //log_.debug(" render component , view id " + viewId) ;
      exoViewToRender.renderChildren(context) ;
    } else {
      //log_.debug(" render jsp file , view id =  " + viewId) ;
      context.getExternalContext().dispatch(viewId);
    }
    //PortletFacesData data = PortletFacesData.getPortletFacesData(context) ;
    //data.saveView(context, exoViewToRender) ;  
  }

  public StateManager getStateManager() { return stateManager_; }

  public UIViewRoot restoreView(FacesContext context, String viewId) {
    if(context == null) 
      throw new NullPointerException("faces context cannot be null");
    PortletFacesData data = PortletFacesData.getPortletFacesData(context) ;
    UIViewRoot viewRoot = data.restoreView(context, viewId) ;
    //viewRoot = getStateManager().restoreView(context, viewId);
    return viewRoot;
  }

  public UIViewRoot createView(FacesContext context, String viewId) {
    UIViewRoot result = null  ;
    if (result == null) {
      result = new UIExoViewRoot(viewId);
      Locale locale = null;
      if(context.getViewRoot() != null) locale = context.getViewRoot().getLocale();
      if(locale == null) {
        locale = calculateLocale(context);
      }
      result.setLocale(locale);
    }
    result.setRenderKitId(calculateRenderKitId(context)) ;
    PortletFacesData data = PortletFacesData.getPortletFacesData(context) ;
    data.saveView(context, result) ;
    return result;
  }

  public String calculateRenderKitId(FacesContext context) {
    if(context == null) throw new NullPointerException("Faces Context is null");
    String result = context.getApplication().getDefaultRenderKitId() ;
    if(null == result ) result = "HTML_BASIC";
    return result;
  }
  
  public Locale calculateLocale(FacesContext context) {
    return context.getExternalContext().getRequestLocale() ;
  }

  public void writeState(FacesContext context) throws IOException {
    /*
    if(getStateManager().isSavingStateInClient(context)) {
      context.getResponseWriter().writeText("com.sun.faces.saveStateFieldMarker", null);
    }
    */
  }

  public String getViewIdPath(FacesContext context, String viewId) {
    if(context == null || viewId == null)
      throw new NullPointerException("faces context or viewId cannot be bull");
    if(viewId.charAt(0) != '/') {
      throw new IllegalArgumentException("expect viewId start with /");
    }
    return viewId;
  }

  public String getResourceURL(FacesContext context, String path) {
    if(path.startsWith("/"))
      return context.getExternalContext().getRequestContextPath() + path;
    return path;
  }

  public String getActionURL(FacesContext context, String viewId) {
    return viewId  ;
  }
}