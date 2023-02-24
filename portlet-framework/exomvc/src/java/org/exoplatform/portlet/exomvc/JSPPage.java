/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.exoplatform.portlet.exomvc.config.Configuration; 
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 7, 2004
 * @version $Id$
 */
public class JSPPage extends Page {
  private String jspPage_ ;
  
  public String getJSPPage() { return jspPage_ ; }
  public void setJSPPage(String s) { jspPage_ = s ; }
  
  public void render(RenderRequest req, RenderResponse res)  throws Exception {
    Configuration config =  getConfiguration() ;
    String pageURL = getPageURL(res) ;
    req.setAttribute(PAGE_URL, pageURL) ;
    config.getJSPResourceManager().dispatch(jspPage_, req, res) ;
  }
}