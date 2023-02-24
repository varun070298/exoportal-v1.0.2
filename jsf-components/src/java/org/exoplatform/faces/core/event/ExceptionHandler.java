/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.event;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * Jun 3, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
abstract public class ExceptionHandler {
 
  public ExceptionHandler() {
    
  }
  
  abstract public boolean canHandleError(Throwable error) ;
  
  abstract public void handle(ExoActionEvent action, Throwable error) ;
  
  protected String getStackTrace(Throwable t, int numberOfLine) {
    StackTraceElement[] elements = t.getStackTrace() ;
    if(numberOfLine > elements.length) numberOfLine = elements.length ; 
    StringBuffer b = new StringBuffer() ;
    b.append(t.getMessage()).append("\n") ;
    for(int i = 0; i < numberOfLine; i++) {
      b.append(elements[i].toString()).append("\n") ;
    }
    return b.toString() ;
  }
  
  protected String getResource(ResourceBundle res , String key) {
    try {
      return res.getString(key) ;
    } catch (MissingResourceException ex) {   }
    return null; 
  }
}