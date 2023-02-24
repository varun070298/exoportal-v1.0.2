/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.portlet.exomvc.config.Configuration;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 7, 2004
 * @version $Id$
 */
public class Page {
  final static public String VELOCITY_CONTEXT = "mvc.velocity.context" ;
  
  final static public String PAGE_URL = "mvc.page.url" ;
  final static public String PAGE_NAME = "mvc.page.name" ;
  final static public String FORWARD_PAGE = "mvc.page.forward" ;
  
  private Configuration configuration_ ;
  private String pageName_ ;
  
  public String getPageName() { return pageName_ ; }
  public void   setPageName(String s) { pageName_ = s ; }
  
  public void render(RenderRequest req, RenderResponse res)  throws Exception {
    
  }
  
  public void processAction(ActionRequest req, ActionResponse res)  throws Exception {
    
  }
  
  public Configuration getConfiguration()  { return configuration_ ; }
  public void  setConfiguration(Configuration c) { configuration_ = c ;} 
  
  public String getPageURL(RenderResponse res) {
    return res.createActionURL().toString() + "&amp;" + PAGE_NAME + "=" + pageName_;
  }
  
  public void setForwardPage(ActionResponse res, String pageName) {
    res.setRenderParameter(FORWARD_PAGE, pageName) ;
  }
}