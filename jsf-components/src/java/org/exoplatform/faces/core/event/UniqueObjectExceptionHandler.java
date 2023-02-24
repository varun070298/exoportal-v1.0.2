/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import org.exoplatform.commons.exception.UniqueObjectException;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.InformationProvider;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UniqueObjectExceptionHandler extends ExceptionHandler {
  public boolean showStackTrace_ = false ;
  
	public UniqueObjectExceptionHandler() {
  }
  
  public UniqueObjectExceptionHandler setShowStackTrace(boolean b) {
    showStackTrace_  = b ;
    return this ;
  }
  
  public boolean canHandleError(Throwable error) {
    return error instanceof UniqueObjectException ;
  }
  
	public void handle(ExoActionEvent action, Throwable error) {
    UniqueObjectException ex = (UniqueObjectException) error ;
    UIComponent src = action.getComponent() ;
    InformationProvider iprovider = Util.findInformationProvider(src) ;
    if(iprovider != null) {
      ResourceBundle res = Util.getApplicationResourceBundle()  ;
      String messageKey = ex.getMessageKey() ; 
      String message =  this.getResource(res, messageKey) ;
      if(message == null) {
        message = "No explaination is found for the key: " + messageKey ;
      }
      iprovider.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ex.getExceptionDescription())) ;
      iprovider.setDisplayMessageType(InformationProvider.FOOTER_MESSAGE_TYPE) ;
    } 
	}
}