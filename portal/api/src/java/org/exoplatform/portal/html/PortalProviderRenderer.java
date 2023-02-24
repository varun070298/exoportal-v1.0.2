/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.html;

import java.io.IOException ;
import java.util.ResourceBundle ;
import javax.faces.context.ResponseWriter ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 26, 2005
 * @version $Id$
 */
public interface PortalProviderRenderer {
  public void renderTitle(ResponseWriter w, ResourceBundle res) throws IOException  ;
  public void renderMeta(ResponseWriter w, ResourceBundle res) throws IOException  ;
  public void renderLink(ResponseWriter w, ResourceBundle res) throws IOException  ;
  public void renderScript(ResponseWriter w, ResourceBundle res) throws IOException  ;
}
