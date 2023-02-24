/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.renderer.html.portal;

import java.io.IOException; 
import java.util.ResourceBundle ;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.portal.html.PortalProviderRenderer;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 26, 2005
 * @version $Id$
 */
public class ExoPortalProviderRenderer implements PortalProviderRenderer {
  private static String EN_KEYWORD = "portlet, portal, jsr-168, exo platform, JSF" ;
  
  public void renderTitle(ResponseWriter w, ResourceBundle res) throws IOException  {
    w.write("\n<TITLE>Welcome to eXo Platform</TITLE>") ;
  }
  
  public void renderMeta(ResponseWriter w, ResourceBundle res) throws IOException   {
    w.    write("\n<meta name='keywords' lang='en' content='" + EN_KEYWORD + "' />\n") ;
  }
  
  public void renderLink(ResponseWriter w, ResourceBundle res) throws IOException   {
    
  }
  
  public void renderScript(ResponseWriter w, ResourceBundle res) throws IOException {
    
  }
}