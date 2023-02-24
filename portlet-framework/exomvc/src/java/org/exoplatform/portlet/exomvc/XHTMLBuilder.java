/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;

import javax.portlet.RenderResponse;
import org.exoplatform.commons.xhtml.BaseXHTMLBuilder;
import org.exoplatform.portlet.exomvc.Page;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 11, 2004
 * @version $Id$
 */
public class XHTMLBuilder extends BaseXHTMLBuilder {
  private String baseURL_ ;
  public XHTMLBuilder(RenderResponse response, Page pageObject) throws Exception {
    super(response.getWriter()) ;
    baseURL_ = pageObject.getPageURL(response) ;
  }
  
  protected String getBaseURL() { return baseURL_; }
}