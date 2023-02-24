/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.ResourceBundle ;
import java.io.IOException ;
import javax.faces.context.ResponseWriter ;
import org.exoplatform.faces.core.component.UIGrid;

/**
 * Apr 16, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: HtmlFragment.java,v 1.4 2004/07/01 14:20:50 tuan08 Exp $
 **/
public interface HtmlFragment {     
  public void render(ResponseWriter w, ResourceBundle res, UIGrid uiParent) throws IOException ;
}
