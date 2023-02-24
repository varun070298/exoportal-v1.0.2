/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.tags;

import javax.faces.webapp.UIComponentTag;
import org.exoplatform.portal.faces.component.UIPortal;

/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalTag.java,v 1.7 2004/07/02 17:46:40 tuan08 Exp $
 */
public class PortalTag extends UIComponentTag {
  private String style_ ; 

  public String getComponentType() { return "UIPortal"; }

  public String getRendererType() { return UIPortal.DEFAULT_RENDERER_TYPE; }
  
  public String getStyle() { return style_ ;}
  public void   setStyle(String s) { style_ = s ; }
}
