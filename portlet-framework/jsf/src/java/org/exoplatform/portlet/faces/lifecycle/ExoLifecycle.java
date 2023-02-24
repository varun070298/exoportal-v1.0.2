package org.exoplatform.portlet.faces.lifecycle;

import java.util.*;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;
import com.sun.faces.lifecycle.* ;

import org.exoplatform.portlet.faces.context.* ;

public class ExoLifecycle extends Lifecycle {
	private static Log log_ = LogUtil.getLog("org.exoplatform.portlet.faces.faces");
	
  private List listeners;
  private Phase phases[] = {
     null, new RestoreViewPhase(), new ApplyRequestValuesPhase(), 
     new ProcessValidationsPhase(), new UpdateModelValuesPhase(), 
     new InvokeApplicationPhase()
  };
  private Phase response;

  public ExoLifecycle() {
    listeners = new ArrayList();
    response = new RenderResponsePhase();
  }

  public void execute(FacesContext context) throws FacesException {
    //log_.debug("##########################Call excute in lifecycle");
    if(context == null) throw new NullPointerException("Faces context cannot be null");
    FacesPortletContextImpl contextImpl = (FacesPortletContextImpl) context ;
    boolean portletRenderPhase = contextImpl.isPortletRenderPhase() ;
    for(int i = 1; i < phases.length; i++) {
      if(context.getRenderResponse() || context.getResponseComplete()) {
        break;
      }
      //log_.debug("phase: " + phases[i]);
      phase((PhaseId)PhaseId.VALUES.get(i), phases[i], context);
      if(reload((PhaseId)PhaseId.VALUES.get(i), context)) {
        context.renderResponse();
      }
      if(portletRenderPhase) return ;
    }
    //log_.debug("##########################End Call excute in lifecycle");
  }

  public void render(FacesContext context) throws FacesException {
    if(context == null) throw new NullPointerException("Faces context cannot be null");
    FacesPortletContextImpl contextImpl = (FacesPortletContextImpl) context ;
    if (!contextImpl.isPortletRenderPhase()) return ;
    if(!context.getResponseComplete()) {
      phase(PhaseId.RENDER_RESPONSE, response, context);
    }
  }

  public void addPhaseListener(PhaseListener listener) {
    if(listener == null)
      throw new NullPointerException("Faces Context cannot be null");
    synchronized(listeners) {
      listeners.add(listener);
    }
  }

  public PhaseListener[] getPhaseListeners() {
    List list = listeners;
    PhaseListener results[] = new PhaseListener[listeners.size()];
    return (PhaseListener[])listeners.toArray(results);
  }

  public void removePhaseListener(PhaseListener listener) {
    if(listener == null) throw new NullPointerException("Faces Context cannot be null");
    synchronized(listeners) {
      listeners.remove(listener);
    }
  }

  private void phase(PhaseId phaseId, Phase phase, FacesContext context) throws FacesException {
    synchronized(listeners) {
      if(listeners.size() > 0) {
        PhaseEvent event = new PhaseEvent(context, phaseId, this);
        for(int i = 0; i < listeners.size(); i++) {
          PhaseListener listener = (PhaseListener)listeners.get(i);
          if(phaseId.equals(listener.getPhaseId()) || PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
            listener.beforePhase(event);
          }
        }

      }
    }

    if(!skipping(phaseId, context)) phase.execute(context);
    synchronized(listeners) {
      if(listeners.size() > 0) {
        PhaseEvent event = new PhaseEvent(context, phaseId, this);
        for(int i = listeners.size() - 1; i >= 0; i--) {
          PhaseListener listener = (PhaseListener)listeners.get(i);
          if(phaseId.equals(listener.getPhaseId()) || PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
            listener.afterPhase(event);
          }
        }

      }
    }
  }

  private boolean reload(PhaseId phaseId, FacesContext context) {
    if(!phaseId.equals(PhaseId.RESTORE_VIEW)) return false;
    if(!(context.getExternalContext().getRequest() instanceof HttpServletRequest)) {
      log_.debug("request instance is not HttpServletRequest ");
      return false;
    }

    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
    String method = request.getMethod();
    log_.debug("method is  " + method);
    if("GET".equals(method)) {
      Iterator names = context.getExternalContext().getRequestParameterNames();
      if(names.hasNext()) return false;
    }
    return !"POST".equals(method) && !"PUT".equals(method);
  }

  private boolean skipping(PhaseId phaseId, FacesContext context) {
    if(context.getResponseComplete()) return true;
    return context.getRenderResponse() && !phaseId.equals(PhaseId.RENDER_RESPONSE);
  }

}
