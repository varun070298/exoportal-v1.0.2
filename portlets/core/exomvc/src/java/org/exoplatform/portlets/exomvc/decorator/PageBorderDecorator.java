/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.exomvc.decorator;

import java.io.Writer ;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.PageDecorator;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 */
public class PageBorderDecorator extends  PageDecorator {
  public void decorate(Page page, RenderRequest req, RenderResponse res) throws Exception  {
    Writer w = res.getWriter() ;
    w.write("<div style='border: 5px solid red'>") ;
    render(page, req, res) ;
    w.write("</div>") ;
  }
}