/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.exception;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.portlet.exomvc.config.PageConfig;
import org.exoplatform.commons.utils.ExceptionUtil;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class DefaultExceptionHandler implements ExceptionHandler {
  
  public boolean canHandle(Throwable t)  { return true ; }
  
  public void handle(PageConfig config, Throwable t, ActionRequest req, ActionResponse res)  {
    t  = ExceptionUtil.getRootCause(t) ;
    String trace = ExceptionUtil.getExoStackTrace(t) ;
    //System.out.println(trace) ;
  }
  
  public void handle(PageConfig config, Throwable t, RenderRequest req, RenderResponse res)  {
    t  = ExceptionUtil.getRootCause(t) ;
    String trace = ExceptionUtil.getExoStackTrace(t) ;
    //System.out.println(trace) ;
  }
}