/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.tags;

import javax.faces.webapp.UIComponentTag;
import org.exoplatform.portal.faces.component.UIMobilePage;

/**
 * Date: Jun 30, 2004
 * @author : MORON Fran�ois
 * @email:   francois.moron@rd.francetelecom.com
 *
 */
public class MobilePageTag extends UIComponentTag {

  public String getComponentType() { return "UIMobilePage" ; }

  public String getRendererType() {
    return UIMobilePage.DEFAULT_RENDERER_TYPE;
  }
}
