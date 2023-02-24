package org.exoplatform.portal.faces.application;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.context.HtmlResponseWriter;
import org.exoplatform.portal.faces.component.UIPortal;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portal.session.ExoPortal ;

public class ExoPortalViewHandler extends ViewHandler {
  private ViewHandler implViewHandler_ ;
  
  public ExoPortalViewHandler(ViewHandler impl) {
    implViewHandler_ = impl ;
  }
  
  public Locale calculateLocale(FacesContext context) {
    return implViewHandler_.calculateLocale(context) ;
  }
  
  public String calculateRenderKitId(FacesContext context) {
    return implViewHandler_.calculateRenderKitId(context);
  }
  
  public UIViewRoot createView(FacesContext context, String viewId) {
    UIViewRoot uiRoot = implViewHandler_.createView(context, viewId);
    SessionContainer scontainer = SessionContainer.getInstance() ;
    UIPortal uiPortal = (UIPortal) scontainer.getComponentInstanceOfType(ExoPortal.class) ;
    uiRoot.getChildren().add(uiPortal) ;
    return uiRoot ;
  }
  
  public String getActionURL(FacesContext context, String viewId) {
    return implViewHandler_.getActionURL(context, viewId) ;
  }
  
  public String getResourceURL(FacesContext context, String path) {
    return implViewHandler_.getResourceURL(context, path) ;
  }
  
  
  public  void renderView(FacesContext context, UIViewRoot viewToRender)
  throws IOException, FacesException {
    //implViewHandler_.renderView(context, viewToRender) ;
    HttpServletResponse res = 
      (HttpServletResponse)context.getExternalContext().getResponse() ;
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    appres.getLocaleConfig().setOutput(res) ;
    PrintWriter writer = res.getWriter() ;
    HtmlResponseWriter responseWriter = new HtmlResponseWriter(writer) ;
    context.setResponseWriter(responseWriter) ;
    UIComponent uiPortal = (UIComponent)viewToRender.getChildren().get(0);
    uiPortal.encodeChildren(context) ;
    StateManager smanager = context.getApplication().getStateManager() ;
    smanager.saveSerializedView(context) ;
    responseWriter.flush() ;
  }
  
  public UIViewRoot restoreView(FacesContext context, String viewId) {
    return implViewHandler_.restoreView(context, viewId) ;
  }
  
  public void writeState(FacesContext context) throws IOException {
    implViewHandler_.writeState(context) ;
  }
}
