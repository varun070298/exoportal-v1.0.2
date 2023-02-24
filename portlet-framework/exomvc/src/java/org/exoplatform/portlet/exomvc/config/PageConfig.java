/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config ;

import java.util.* ;
import javax.portlet.* ;

import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.PageDecorator ;
import org.exoplatform.portlet.exomvc.exception.ExceptionHandler;
import org.exoplatform.portlet.exomvc.interceptor.Interceptor;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
abstract public class PageConfig {
  private String pageName_ ;
  private String description_ ;
  private PageDecorator decorator_ ;
  private List exceptionHandlers_ = new ArrayList(5);
  private List permissionVerifiers_  = new ArrayList(5);
  
  public String getPageName() { return pageName_ ; }
  public PageConfig setPageName(String pageName) { 
    pageName_ = pageName ;
    return this ;
  }
  
  public String getDescription() { return description_ ; }
  public PageConfig setDescription(String desc) {
    description_ = desc ;
    return this ;
  }
  
  public PageDecorator getPageDecorator() { return decorator_; }
  public PageConfig setPageDecorator(PageDecorator decorator) {
    decorator_ = decorator ;
    return this ;
  }
  
  public PageConfig addExceptionHandler(ExceptionHandler handler) {
    exceptionHandlers_.add(handler) ;
    return this ;
  }
  
  public void handle(Throwable t, ActionRequest req, ActionResponse res)  {
    for(int i = 0 ; i < exceptionHandlers_.size(); i++) {
      ExceptionHandler handler = (ExceptionHandler) exceptionHandlers_.get(i) ;
      if(handler.canHandle(t)) {
        handler.handle(this, t, req, res) ;
      }
    }
  }
  
  public void handle(Throwable t, RenderRequest req, RenderResponse res)  {
    for(int i = 0 ; i < exceptionHandlers_.size(); i++) {
      ExceptionHandler handler = (ExceptionHandler) exceptionHandlers_.get(i) ;
      if(handler.canHandle(t)) {
        handler.handle(this, t, req, res) ;
      }
    }
  }
  
  public PageConfig addPermissionVerifier(Interceptor verifier) {
    permissionVerifiers_.add(verifier) ;
    return this ;
  }

  public void checkPermission(ActionRequest req, ActionResponse res) throws Exception {
    for(int i = 0;  i < permissionVerifiers_.size(); i++) {
      Interceptor verifier = (Interceptor) permissionVerifiers_.get(i) ;
      verifier.intercept(this, req, res) ;
    }
  }
  
  public void checkPermission(RenderRequest req, RenderResponse res) throws Exception {
    for(int i = 0;  i < permissionVerifiers_.size(); i++) {
      Interceptor verifier = (Interceptor) permissionVerifiers_.get(i) ;
      verifier.intercept(this, req, res) ;
    }
  }
  
  abstract public  Page getPageObject(Configuration configuration) throws Exception  ;
}