/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.tags;

import javax.faces.webapp.UIComponentTag;
import org.exoplatform.portal.faces.component.UISinglePage;

/**
 * Date: Aug 11, 2003
 * @author : Mestrallet Benjamin
 * @email:   benjmestrallet@users.sourceforge.net
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PageTag.java,v 1.5 2004/07/02 17:46:40 tuan08 Exp $
 */
public class PageTag extends UIComponentTag {

  public String getComponentType() { return "UISinglePage" ; }

  public String getRendererType() {
    return UISinglePage.DEFAULT_RENDERER_TYPE;
  }
}
