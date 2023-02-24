/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.mocks.jsf;

import java.util.* ;
import java.io.IOException  ;
import java.io.PrintWriter  ;

import javax.faces.render.* ;
import javax.faces.application.* ;
import javax.faces.context.* ;
import javax.faces.event.FacesEvent;
import javax.faces.component.UIViewRoot ;
import javax.servlet.http.* ;

public class MockFacesContext extends FacesContext {
    
  protected MockExternalContext externalContext_;
  protected Locale locale_ = null;
  protected UIViewRoot viewRoot_ = null;
  protected List facesEvents_ = null;
  protected Map facesMessages_ = null;
  protected int maximumSeverity_ = 0;
  protected ResponseStream responseStream_ = null;
  protected ResponseWriter responseWriter_ = null;
  protected boolean renderResponse_ = false;
  protected boolean responseComplete_ = false;
  protected Application application_ ;
  protected boolean renderPhase_ ;
 
  private FacesContext portalFacesContext;

  public MockFacesContext() { 
     externalContext_ = new MockExternalContext();
  }
  
  public MockFacesContext(HttpServletRequest req, HttpServletResponse res ) throws IOException {
    init(req, res) ;
  }
  
  public void init(HttpServletRequest req, HttpServletResponse res) throws IOException {
    renderPhase_ = false ;
    responseWriter_ = null  ;
    externalContext_.init(req, res) ;
    PrintWriter writer = res.getWriter() ;
    responseWriter_ = new MockResponseWriter(writer) ;
    renderResponse_ = false;
  }
  
  public void reset() { externalContext_.reset() ; }
  
  public void release() {
  }

  public ExternalContext getExternalContext() { return externalContext_; }

  public Locale getLocale() {
    if (locale_ == null) {
      locale_ = getExternalContext().getRequestLocale();
    }
    return locale_;
  }

  public void setLocale( Locale locale ) { locale_ = locale; }

  public UIViewRoot getViewRoot() {  return viewRoot_ ;  }
  
  public void setViewRoot(UIViewRoot viewRoot) {  viewRoot_ = viewRoot;  }


  public void addMessage(String clientId, FacesMessage message) {
  }

  public Iterator getMessages() { return null ; }


  public Iterator getMessages(String clientId) { return null ; }

  public Iterator getFacesEvents() { return null ; }

  public void addFacesEvent( FacesEvent event ) {
    if (facesEvents_ == null) facesEvents_ = new ArrayList();
    facesEvents_.add( event );
  }

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
    return null ;
  }

  public RenderKit getRenderKit() { return null ; }

  public Iterator getClientIdsWithMessages() {
    return null ;
  }

  public Application getApplication() {
    if (application_ == null ) application_ = new MockApplication() ;
    return application_  ; 
  }
  
  public void renderResponse() { renderResponse_ = true; }

  public boolean getRenderResponse() { return renderResponse_; }

  public void responseComplete() { responseComplete_ = true; }

  public boolean getResponseComplete() { return responseComplete_; }

  public FacesContext getPortalFacesContext() { return portalFacesContext ; }

  public boolean isPortletRenderPhase() { return renderPhase_ ; }
  
  public static MockFacesContext getMockFacesContextCurrentInstance() {
  	MockFacesContext context = (MockFacesContext)FacesContext.getCurrentInstance() ;
  	if(context == null) {
  		context= new MockFacesContext() ;
  		FacesContext.setCurrentInstance(context) ;
    }
  	return context ;
  }
}
