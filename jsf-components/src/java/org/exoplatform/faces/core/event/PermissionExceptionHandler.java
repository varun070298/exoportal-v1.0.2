/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import org.exoplatform.commons.exception.ExoPermissionException;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.Util;
import org.exoplatform.faces.core.component.InformationProvider;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class PermissionExceptionHandler extends ExceptionHandler {
  public boolean showStackTrace_ = false ;
  
	public PermissionExceptionHandler() {
  }
  
  public PermissionExceptionHandler setShowStackTrace(boolean b) {
    showStackTrace_  = b ;
    return this ;
  }
  
  public boolean canHandleError(Throwable error) {
    return error instanceof ExoPermissionException ;
  }
  
	public void handle(ExoActionEvent action, Throwable error) {
    ExoPermissionException ex = (ExoPermissionException) error ;
    UIComponent src = action.getComponent() ;
    InformationProvider iprovider = Util.findInformationProvider(src) ;
    if(iprovider != null) {
      String stackTrace = null ;
      if(showStackTrace_) {
        stackTrace = ExceptionUtil.getStackTrace(new Exception(), 3) ;
      }
      ResourceBundle res = Util.getApplicationResourceBundle()  ;
      String message = ex.getMessage(res) ;
      iprovider.addMessage(new ExoFacesMessage(FacesMessage.SEVERITY_ERROR, message, stackTrace)) ;
      iprovider.setDisplayMessageType(InformationProvider.FOOTER_MESSAGE_TYPE) ;
    } else {
      //System.out.println(ExceptionUtil.getExoStackTrace(error)) ;
    }
	}
}