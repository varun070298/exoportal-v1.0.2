/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import org.exoplatform.faces.core.Util;
/**
 * Wed, Dec 22, 2003 @ 22:50 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ExoActionEvent.java,v 1.9 2004/09/24 03:10:19 tuan08 Exp $
 */
public class ExoActionEvent extends ActionEvent {
  private String action_ ;
  protected  Map params_ ;
  protected ResourceBundle res_ ;

  public ExoActionEvent(UIComponent source, String action) {
    super(source) ;
    action_ = action ;
  }
  
  public ExoActionEvent(UIComponent source, String action, Map params) {
    this(source, action) ;
    params_ = params ;
  }
  
  public String getAction() { return action_ ; }
  
  public void addParameter(String name, String value) {
    if(params_ == null) params_ = new HashMap(5) ;
    params_.put(name, value) ;
  }
  
  public String getParameter(String name) { 
    return (String) params_.get(name) ; 
  }
  
  public String[] getParameterValues(String name) {
    FacesContext context = FacesContext.getCurrentInstance() ;
    Map paramMap = context.getExternalContext().getRequestParameterValuesMap() ;
    return (String[]) paramMap.get(name) ; 
  }
  
  public boolean isAppropriateListener(FacesListener listener) {
  	return ((ExoActionListener) listener).canHandleAction(action_) ;
  }
  
  public Map getParameterMap() { return params_ ; }
  
  public void processListener(FacesListener listener) throws AbortProcessingException {
  	ExoActionListener exoListener = (ExoActionListener) listener ;
  	exoListener.processAction(this) ;
  }
  
  public ResourceBundle getApplicationResourceBundle() {
    if(res_ == null) {
      res_ = Util.getApplicationResourceBundle() ;
    }
    return res_ ;
  }
}