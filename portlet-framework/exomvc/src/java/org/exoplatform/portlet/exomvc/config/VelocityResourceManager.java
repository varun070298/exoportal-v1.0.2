/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import java.io.InputStream;
import java.util.Properties;
import javax.portlet.PortletContext;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.exoplatform.commons.utils.IOUtil;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 */
public class VelocityResourceManager {
  private String reposistoryPath_ ;
  private PortletContext context_ ;
  private VelocityEngine vengine_;
 
  public VelocityResourceManager(PortletContext context, String repo ) throws Exception {
    reposistoryPath_ =  repo + "/velocity/" ;
    context_ = context ;
    String path = context.getRealPath(repo + "/velocity") ;
    Properties p = new Properties(); 
    p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, path);
    vengine_ = new VelocityEngine();
    vengine_.init( p );
  }
  
  public String getVelocityReposistory() { return reposistoryPath_ ; }
  
  public VelocityEngine  getVelocityEngine() { return vengine_ ; }
  
  public Template getTemplate(String template) throws Exception { 
    return  vengine_.getTemplate(template) ;
  }
  
  public String getResourceAsText(String resource) throws Exception {
    String path = reposistoryPath_ + resource ;
    InputStream is = context_.getResourceAsStream(path) ;
    if(is == null) return "reosurce : " + resource + " is notfound" ;
    return IOUtil.getStreamContentAsString(is) ;
  }
}