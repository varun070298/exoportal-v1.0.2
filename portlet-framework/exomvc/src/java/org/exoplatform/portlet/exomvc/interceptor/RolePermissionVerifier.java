/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.interceptor;

import javax.portlet.*;
import org.exoplatform.portlet.exomvc.config.PageConfig;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class RolePermissionVerifier implements Interceptor {
  private String executeRole_ ;
  private String viewRole_ ;
  
  public RolePermissionVerifier(String viewRole, String executeRole) {
    viewRole_ = viewRole ;
    executeRole_ = executeRole ;
  }
  
  public void intercept(PageConfig pconfig, ActionRequest req, ActionResponse res) throws Exception {
    if(executeRole_ == null)  return  ;
    if(req.isUserInRole(executeRole_)) {
      
    }
  }
  
  public void intercept(PageConfig pconfig, RenderRequest req, RenderResponse res) throws Exception {
    if(viewRole_ == null)  return  ;
    if(!req.isUserInRole(viewRole_)) {
      
    }
  }
}