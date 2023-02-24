/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc;
import java.io.IOException;
import javax.portlet.*;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.portlet.exomvc.config.Configuration;
import org.exoplatform.portlet.exomvc.config.PageConfig ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Tue, Jun 03, 2003 @
 * Time: 11:26:44 PM
 */

public class MVCPortletFramework extends GenericPortlet {
  final public static PortletMode CONFIG_MODE = new PortletMode("config") ;

  private Configuration configuration_ ;
  
  public void init(PortletConfig config) throws PortletException {
    super.init(config);
    try {
      configuration_ = new Configuration(config) ;  
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
  }
  
  public void render (RenderRequest req, RenderResponse res) throws PortletException, java.io.IOException {
    res.setTitle(getTitle(req));
    WindowState state = req.getWindowState();
    if (!state.equals(WindowState.MINIMIZED)) {
      try {
        res.setContentType("text/html") ;
        PortletMode mode = req.getPortletMode();
        String pageName = req.getParameter(Page.FORWARD_PAGE) ;
        PageConfig pconfig = configuration_.getPageConfig(mode, pageName) ;
        Page page  = pconfig.getPageObject(configuration_)  ;
        PageDecorator decorator = pconfig.getPageDecorator() ;
        if(decorator != null) {
          decorator.decorate(page, req, res) ;
        } else {
          page.render(req, res) ;
        }
      } catch (Throwable t) {
        String trace = ExceptionUtil.getExoStackTrace(t) ;
        res.getWriter().write(trace) ;
      }
    }
  }

  public void processAction(ActionRequest req, ActionResponse res) 
  throws PortletException, IOException  {
    try {
      PortletMode mode = req.getPortletMode();
      String pageName = req.getParameter(Page.PAGE_NAME) ;
      PageConfig pconfig = configuration_.getPageConfig(mode, pageName) ;
      Page page =  pconfig.getPageObject(configuration_)  ;
      page.processAction(req, res) ;
    } catch (Throwable t) {
      String trace = ExceptionUtil.getExoStackTrace(t) ;
      //System.out.println(trace) ;
    }
  }
  
  public void destroy() {
    configuration_.destroy() ;
    super.destroy() ;
  }
}