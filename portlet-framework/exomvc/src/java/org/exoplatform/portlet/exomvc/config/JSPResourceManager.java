/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import java.io.InputStream;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.commons.utils.IOUtil;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 */
public class JSPResourceManager {
  private String reposistoryPath_ ;
  private PortletContext context_ ;
  
  public JSPResourceManager(PortletContext context, String repo ) throws Exception {
    reposistoryPath_ =  repo + "/jsp/" ;
    context_ = context ;
  }
  
  public String getJSPReposistory() { return reposistoryPath_ ; }
  
  public void dispatch(String page, RenderRequest request, RenderResponse response) throws Exception {
    PortletRequestDispatcher rd = context_.getRequestDispatcher(reposistoryPath_ + page) ;
    rd.include(request, response);
  }
  
  public String getResourceAsText(String resource) throws Exception {
    String path = reposistoryPath_ + resource ;
    InputStream is = context_.getResourceAsStream(path) ;
    if(is == null) return "reosurce : " + resource + " is notfound" ;
    return IOUtil.getStreamContentAsString(is) ;
  }
}