package org.exoplatform.portlet.faces.application;

import java.util.HashMap ;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.portlet.*;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode ;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portlet.faces.context.ExternalContextImpl;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 1, 2004
 * @version $Id: PortletFacesData.java,v 1.2 2004/11/03 04:24:53 tuan08 Exp $
 */
public class PortletFacesData {
  private static PortletMode CONFIG = new PortletMode("config") ;
  private static PortletMode MONITOR = new PortletMode("monitor") ;
  
  private HashMap views_ ;
  private String lastView_ ;
  private PortletMode lastMode_ ;
  
  public PortletFacesData() {
    views_ = new HashMap(5) ;
  }
  
  public String getLastView(PortletMode mode) throws Exception {
    if(mode.equals(lastMode_) && lastMode_ != null) {
      return lastView_ ;
    }
    ExternalContextImpl impl = 
      (ExternalContextImpl) FacesContext.getCurrentInstance().getExternalContext();
    String view = getDefaultView(mode, impl.getConfig()) ;
    //reinit view mode since user can change the configuration in edit mode 
    //or config mode
    if(PortletMode.VIEW  == mode)  views_.remove(view) ;

    return view ;
  }
  
  public void setLastView(PortletMode mode, String viewId) { 
    lastView_ = viewId ;  
    lastMode_ = mode ;
  }
  
  public void saveView(FacesContext context, UIViewRoot uiViewRoot)  {
    views_.put(uiViewRoot.getViewId(), uiViewRoot) ;
  }
  
  public UIViewRoot restoreView(FacesContext context, String viewId)  {
    return (UIViewRoot) views_.get(viewId) ;
  }
  
  public String getDefaultView(PortletMode mode, PortletConfig config) throws Exception {
    if (PortletMode.VIEW == mode) {
      return  config.getInitParameter("default-view") ;
    } else if (PortletMode.EDIT == mode) {
      return  config.getInitParameter("default-edit") ;
    } else if (PortletMode.HELP == mode) {
      return  config.getInitParameter("default-help") ;
    } else if (CONFIG.equals(mode)) {
      return config.getInitParameter("default-config") ;
    } else if (MONITOR.equals(mode)) {
      return  config.getInitParameter("default-monitor") ;
    } else {
      throw new PortletException("unknown portlet mode: " + mode);
    }
  }
  
  static public PortletFacesData getPortletFacesData(FacesContext context) {
    SessionContainer scontainer = SessionContainer.getInstance() ;
    ExternalContextImpl impl = (ExternalContextImpl) context.getExternalContext();
    ExoWindowID windowId = impl.getWindowID() ;
    String id = "faces:" + windowId.getUniqueID() ;
    PortletFacesData data = (PortletFacesData) scontainer.getComponentInstance(id) ;
    if(data == null) {
      data = new PortletFacesData() ;
      scontainer.registerComponentInstance(id, data) ;
    }
    return data ;
  }
  
  static public void destroy(FacesContext context) {
    SessionContainer scontainer = SessionContainer.getInstance() ;
    ExternalContextImpl impl = (ExternalContextImpl) context.getExternalContext();
    ExoWindowID windowId = impl.getWindowID() ;
    String id = "faces:" + windowId.getUniqueID() ;
    scontainer.unregisterComponent(id) ;
  }
}