/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.exomvc.decorator ;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.config.Configuration ;
import org.exoplatform.portlet.exomvc.PageDecorator;
import org.exoplatform.portlet.exomvc.XHTMLBuilder;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 * This decorator render the common action for every pages
 */
public class ToolbarDecorator extends  PageDecorator {
  public void decorate(Page page, RenderRequest req, RenderResponse res) throws Exception  {
     XHTMLBuilder builder = new XHTMLBuilder(res, page) ; 
     Configuration config = page.getConfiguration() ;
     String pageURL =  config.getDefaultView().getPageObject(config).getPageURL(res);
     builder.
       startDIV().
     	   a(pageURL, "Go Back").
       closeDIV();
     //invoke the next decorator or render the page
     render(page,req, res) ;
  }
}