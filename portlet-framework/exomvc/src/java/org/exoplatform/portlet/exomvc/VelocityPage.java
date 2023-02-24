/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;

import java.io.Writer;
import javax.portlet.* ;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.exoplatform.portlet.exomvc.config.Configuration;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 7, 2004
 * @version $Id$
 */
public class VelocityPage extends Page {
  private String template_ ;
  
  public String getTemplate() { return template_ ; }
  public void setTemplate(String s) { template_ = s ; }
  
  public void render(RenderRequest req, RenderResponse res)  throws Exception {
    Configuration config =  getConfiguration() ;
    String pageURL = getPageURL(res) ;
    Writer writer = res.getWriter() ; 
    Context ctx =  (Context)req.getAttribute(VELOCITY_CONTEXT);
    if(ctx == null)   ctx = new VelocityContext() ;
    ctx.put(PAGE_URL , pageURL) ;
    Template tmpl = config.getVelocityResourceManager().getTemplate(template_) ;
    tmpl.merge(ctx, writer) ;
  }
  
  public void setVelocityContext(PortletRequest req, Context ctx) {
    req.setAttribute(VELOCITY_CONTEXT, ctx) ;
  }
  
  public Context getVelocityContext(PortletRequest req) {
    return (Context)req.getAttribute(VELOCITY_CONTEXT) ;
  }
}