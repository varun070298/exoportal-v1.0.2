/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.List ;
import javax.faces.context.FacesContext ;
import javax.faces.component.UIViewRoot;
import org.exoplatform.container.SessionContainer ;
import org.exoplatform.faces.core.component.UIFatalError;
import org.exoplatform.services.log.LogUtil;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class FatalExceptionHandler extends ExceptionHandler {
  
  public boolean canHandleError(Throwable error) {
    return true  ;
  }
  
	public void handle(ExoActionEvent action, Throwable error) {
		UIViewRoot uiroot = FacesContext.getCurrentInstance().getViewRoot() ;
		List children = uiroot.getChildren();
		for(int i = 0; i < children.size(); i++) {
			children.remove(i);
		}
		children.add(new UIFatalError(error)) ;
    LogUtil.getLog(getClass()).error("Handle action " + action.getAction(), error) ;
	}
}
