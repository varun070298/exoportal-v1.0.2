/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.interceptor;

import javax.portlet.*;
import org.exoplatform.portlet.exomvc.config.PageConfig;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public interface Interceptor {
  public void intercept(PageConfig pconfig, ActionRequest req, ActionResponse res) throws Exception ; 
  public void intercept(PageConfig pconfig, RenderRequest req, RenderResponse res) throws Exception ;
}
