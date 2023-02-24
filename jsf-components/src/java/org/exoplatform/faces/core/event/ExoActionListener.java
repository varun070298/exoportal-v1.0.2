/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.* ;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.exoplatform.Constants;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.InformationProvider;

/**
 * Apr 17, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoActionListener.java,v 1.15 2004/10/21 15:24:26 tuan08 Exp $
 **/
abstract public class ExoActionListener implements ActionListener {
  private static List exceptionHandlers_ ;
  static {
    exceptionHandlers_ = new ArrayList(10);
    exceptionHandlers_.add(new UniqueObjectExceptionHandler()) ;
    exceptionHandlers_.add(new MessageExceptionHandler()) ;
    exceptionHandlers_.add(new PermissionExceptionHandler()) ;
    exceptionHandlers_.add(new FatalExceptionHandler()) ;
  }
  
  private List interceptors_ ;
  private String action_ ;
  
  public ExoActionListener() {  }
  
  public void init() {  }
  
  public ExoActionListener setActionToListen(String action) {
    action_ = action ;
    return this ;
  }
  
  public String getActionName() { return action_ ; }
  
  final public void processAction(ActionEvent action) throws AbortProcessingException {
    throw new AbortProcessingException("Use processAction(ExoActionEvent action)") ;
  }
  
  public boolean canHandleAction(String action) {
    return action_.equals(action) ;
  }
  
  final public void processAction(ExoActionEvent action) throws AbortProcessingException {
    try {
      //execute interceptor , pre execute
      if(interceptors_ != null) {
        for(int i = 0; i < interceptors_.size(); i++) {
          ActionInterceptor interceptor = (ActionInterceptor) interceptors_.get(i) ;
          interceptor.preExecute(action) ;
        }
      }
      execute(action) ;
      //execute interceptor , post execute
      if(interceptors_ != null) { 
        for(int i = 0; i < interceptors_.size(); i++) {
          ActionInterceptor interceptor = (ActionInterceptor) interceptors_.get(i) ;
          interceptor.preExecute(action) ;
        }
      }
    } catch (Exception ex) {
      ExceptionHandler handler = null; 
      for (int i = 0; i < exceptionHandlers_.size(); i++) {
        ExceptionHandler eh = (ExceptionHandler) exceptionHandlers_.get(i) ;
        if(eh.canHandleError(ex)) {
          handler = eh ;
          break ;
        }
      }
      handler.handle(action, ex) ;
    }
    postExecute(action) ;
  }
  
  protected void postExecute(ExoActionEvent action) {
    FacesContext.getCurrentInstance().renderResponse() ;
  }
  
  abstract public void execute(ExoActionEvent event) throws Exception ;
  
  public ExoActionListener addInterceptor(ActionInterceptor interceptor) {
    if(interceptors_ == null) {
      interceptors_ = new ArrayList(3) ;
    }
    interceptors_.add(interceptor) ;
    return this ;
  }
  
  protected String getResourceBaseName() {
    String baseName = getClass().getName() ;
    int idx = baseName.lastIndexOf('.') ;
    baseName = baseName.substring(idx + 1, baseName.length()) ;
    baseName = baseName.replace('$', '.') ;
    return baseName ;
  }
  
  public ResourceBundle getApplicationResourceBundle() {
    ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext() ;
    if(eContext instanceof PortletExternalContext) {
      PortletExternalContext impl = (PortletExternalContext) eContext ;
      return impl.getApplicationResourceBundle() ;
    }
    return (ResourceBundle)eContext.getRequestMap().get(Constants.APPLICATION_RESOURCE) ;
  }
  
  protected InformationProvider findInformationProvider(UIComponent src) {
    return Util.findInformationProvider(src) ;
  }
}
