/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import org.exoplatform.commons.exception.ExoMessageException;
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
public class MessageExceptionHandler extends ExceptionHandler {
  
	public MessageExceptionHandler() {
  }
  
  public boolean canHandleError(Throwable error) {
    return error instanceof ExoMessageException ;
  }
  
	public void handle(ExoActionEvent action, Throwable error) {
    ExoMessageException ex = (ExoMessageException) error ;
    UIComponent src = action.getComponent() ;
    InformationProvider iprovider = Util.findInformationProvider(src) ;
    if(iprovider != null) {
      ResourceBundle res = Util.getApplicationResourceBundle()  ;
      String message =  ex.getMessage(res) ;
      String stackTrace = null ;
      if(ex.getSeverity() <= ExoMessageException.WARN) {
        stackTrace = ExceptionUtil.getStackTrace(new Exception(), 3) ;
      }
      iprovider.addMessage(new ExoFacesMessage(FacesMessage.SEVERITY_ERROR, message, stackTrace, ex.getArguments())) ;
      if(ex.getSeverity() <= ExoMessageException.WARN) {
        iprovider.setDisplayMessageType(InformationProvider.BODY_MESSAGE_TYPE) ;
      } else  {
        iprovider.setDisplayMessageType(InformationProvider.FOOTER_MESSAGE_TYPE) ;
      }
    } else {
      //System.out.println(ExceptionUtil.getExoStackTrace(error)) ;
    }
	}
}