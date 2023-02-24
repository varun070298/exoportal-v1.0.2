/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 12, 2004
 * @version $Id$
 */
public class PageDecorator {
  PageDecorator nextDecorator_ ;
  
  public void decorate(Page page, RenderRequest req, RenderResponse res) throws Exception {
    render(page, req, res) ;
  }
  
  protected void render(Page page, RenderRequest req, RenderResponse res) throws Exception {
    if(nextDecorator_ != null) {
      nextDecorator_.decorate(page, req, res) ;
    } else {
      page.render(req, res) ;
    }
  }
  
  final public PageDecorator addPageDecorator(PageDecorator decorator) {
    if(decorator.nextDecorator_ != null) {
      throw new RuntimeException("Expect a new page decorator instance, not a chain of decorator");
    }
    if(nextDecorator_ == null) {
      nextDecorator_ = decorator ;
    } else {
      nextDecorator_.addPageDecorator(decorator) ;
    }
    return this ;
  } 
}
