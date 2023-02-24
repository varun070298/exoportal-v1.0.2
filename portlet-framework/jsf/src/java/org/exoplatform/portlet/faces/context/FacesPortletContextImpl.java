/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces.context;


import javax.faces.FactoryFinder;
import javax.faces.render.* ;
import javax.faces.application.* ;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import javax.faces.component.UIViewRoot ;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.faces.context.HtmlResponseWriter;
import org.exoplatform.faces.context.PortletFacesContext;
import org.exoplatform.portlet.faces.application.PortletFacesData; 

import java.util.* ;
import java.io.PrintWriter ;
import java.io.IOException  ;
/**
 * The JSR-127 specification chapter 6.1 outlines the requirements of the faces factory implementation.
 *
 * @author Ove Ranheim (oranheim@users.sourceforge.net)
 * @since Nov 6, 2003 5:29:38 PM
 */
public class FacesPortletContextImpl extends FacesContext implements PortletFacesContext {

  private ExternalContext externalContext_;
  private Locale locale_ = null;
  private UIViewRoot viewRoot_ = null;
  private List p_facesEvents = null;
  private Map facesMessages_ = null;
  private int p_maximumSeverity = 0;
  private ResponseStream responseStream_ = null;
  private ResponseWriter responseWriter_ = null;
  private boolean renderResponse_ = false;
  private boolean responseComplete_ = false;
  private Application application_ ;
  private boolean renderPhase_ ;
 
  private FacesContext portalFacesContext;

  public FacesPortletContextImpl() { throw new UnsupportedOperationException(); }

  public FacesPortletContextImpl(Object config, Object request, Object response ) throws IOException {
    this.portalFacesContext = FacesContext.getCurrentInstance(); 
    renderPhase_ = false ;
    if ( request instanceof RenderRequest ) {
      RenderResponse rr = (RenderResponse) response ;
      PrintWriter writer = rr.getWriter() ;
      /*
      if (portalFacesContext != null) {
      	RenderKit rkit = portalFacesContext.getRenderKit(); 
      	responseWriter_ = rkit.createResponseWriter(writer, null, null) ;
      } else {
      	responseWriter_ = new HtmlResponseWriter(writer) ;
      }
      */
      renderPhase_ = true ;
      responseWriter_ = new HtmlResponseWriter(writer) ;
    }
    externalContext_ = new ExternalContextImpl(config, request, response, renderPhase_);
    FacesContext.setCurrentInstance( this );
  }

  public static void setCurrentInstance( FacesContext context ) {
    FacesContext.setCurrentInstance( context );
  }

  public void release() {
    externalContext_ = null;
    locale_ = null;
    viewRoot_ = null;
    p_facesEvents = null;
    facesMessages_ = null;
    responseStream_ = null;
    responseWriter_ = null;
    application_ = null ;
    FacesContext.setCurrentInstance(portalFacesContext);
  }

  // JSR-127 6.1.1
  public ExternalContext getExternalContext() { return externalContext_; }

  // JSR-127 6.1.2
  public Locale getLocale() {
    if (locale_ == null) {
      locale_ = getExternalContext().getRequestLocale();
    }
    return locale_;
  }

  public void setLocale( Locale locale ) {
    locale_ = locale;
  }

  // JSR-127 6.1.3
  public UIViewRoot getViewRoot() { 
    return viewRoot_ ; 
  }
  public void setViewRoot(UIViewRoot viewRoot) { 
    viewRoot_ = viewRoot; 
  }


  public void addMessage(String clientId, FacesMessage message) {
    if(null == message)
      throw new NullPointerException("message cannot be null");
    if(facesMessages_ == null) facesMessages_ = new HashMap();
    List list = (List) facesMessages_.get(clientId);
    if(list == null) {
      list = new ArrayList();
      facesMessages_.put(clientId, list);
    }
    list.add(message);
  }

  public Iterator getMessages() {
    if(null == facesMessages_) return Collections.EMPTY_LIST.iterator();
    List messages = new ArrayList() ;
    Iterator i = facesMessages_.values().iterator() ;
    while (i.hasNext()) {
      List list = (List) i.next() ;
      for (int j = 0; j < list.size() ; j++) {
        messages.add(list.get(j));
      }
    }
    if(messages.size() > 0) return messages.iterator();
    return Collections.EMPTY_LIST.iterator();
  }


  public Iterator getMessages(String clientId) {
    if(null == facesMessages_) return Collections.EMPTY_LIST.iterator();
    List list = (List) facesMessages_.get(clientId);
    if(list == null) return Collections.EMPTY_LIST.iterator();
    return list.iterator();
  }

  // JSR-127 6.1.5
  public Iterator getFacesEvents() {
    if (p_facesEvents == null) p_facesEvents = new ArrayList();
    return p_facesEvents.iterator();
  }

  // JSR-127 6.1.5
  public void addFacesEvent( FacesEvent event ) {
    if (p_facesEvents == null) p_facesEvents = new ArrayList();
    p_facesEvents.add( event );
  }

  // JSR-127 6.1.6
  public ResponseStream getResponseStream() {
    return responseStream_;
  }

  public void setResponseStream( ResponseStream responseStream ) {
    responseStream_ = responseStream;
  }

  public ResponseWriter getResponseWriter() {
    return responseWriter_;
  }

  public void setResponseWriter( ResponseWriter responseWriter ) {
    responseWriter_ = responseWriter;
  }


  public FacesMessage.Severity getMaximumSeverity() {
    int max = 0;
    FacesMessage.Severity result = null;
    if(null == facesMessages_ ) return null;
    Iterator i = facesMessages_.values().iterator() ;
    while (i.hasNext()) {
      List list = (List) i.next() ;
      for (int j = 0; j < list.size() ; j++) {
        FacesMessage.Severity temp  = ((FacesMessage) list.get(j)).getSeverity();
        if(temp.getOrdinal() > max) result = temp;
        if(result == FacesMessage.SEVERITY_FATAL) return result ;
      }
    }

    return result;
  }

  public RenderKit getRenderKit() {
    UIViewRoot vr = getViewRoot();
    if(vr == null) return null;
    String renderKitId = vr.getRenderKitId();
    if(renderKitId == null) {
      return null;
    }
    RenderKitFactory rkFactory =
    	(RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    return rkFactory.getRenderKit(this, renderKitId);
  }

  public Iterator getClientIdsWithMessages() {
    Iterator result = null;
    if(null == facesMessages_) result = Collections.EMPTY_LIST.iterator();
    else result = facesMessages_.keySet().iterator();
    return result;
  }

  
  public Application getApplication() {
    if(null != application_) {
      return application_ ;
    }
    ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    application_ = aFactory.getApplication();
    return application_;
  }
  // JSR-127 6.1.7
  public void renderResponse() { renderResponse_ = true; }

  public boolean getRenderResponse() { return renderResponse_; }

  public void responseComplete() { responseComplete_ = true; }

  public boolean getResponseComplete() { return responseComplete_; }

  public FacesContext getPortalFacesContext() { return portalFacesContext ; }

  public boolean isPortletRenderPhase() { return renderPhase_ ; }
  
  public void destroy()  {
    PortletFacesData.destroy(this) ;
  }
}
