/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.ExceptionUtil;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.container.monitor.ActionData;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.services.database.HibernateServiceContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.model.PortalConfig;
/*
 * Fri, May 30, 2003 @
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PublicRequestFilter.java,v 1.28 2004/11/03 01:19:46 tuan08 Exp $
 */
public class PublicRequestFilter implements Filter {
  private Log log_ ;  
  private PortalACL portalACL_ ;
  private String portalName_ ;
  private HibernateServiceContainer hserviceContainer_;
  private PortalConfigService configService_ ;
  
  public void init(FilterConfig filterConfig) {
    portalName_ = filterConfig.getServletContext().getServletContextName() ;
    PortalContainer pcontainer = RootContainer.getInstance().getPortalContainer(portalName_) ;
    if(pcontainer == null){
      pcontainer = RootContainer.getInstance().createPortalContainer(filterConfig.getServletContext());
    }
    PortalContainer.setInstance(pcontainer) ;    
    portalACL_ = (PortalACL) pcontainer.getComponentInstanceOfType(PortalACL.class) ;
    configService_ = 
      (PortalConfigService) pcontainer.getComponentInstanceOfType(PortalConfigService.class) ;
    hserviceContainer_ = 
      (HibernateServiceContainer) pcontainer.getComponentInstanceOfType(HibernateServiceContainer.class) ;
    LogService lservice = (LogService) pcontainer.getComponentInstanceOfType(LogService.class) ; 
    log_  = lservice.getLog("org.exoplatform.portal.filter") ;
    PortalContainer.setInstance(null) ;
  }

  public void doFilter(ServletRequest request, ServletResponse response, 
                       FilterChain chain) throws IOException, ServletException {
    long start = System.currentTimeMillis() ;
    PortalContainer pcontainer = RootContainer.getInstance().getPortalContainer(portalName_) ;
    PortalContainer.setInstance(pcontainer) ;
    HttpServletRequest httpRequest = (HttpServletRequest) request ;
    HttpSession session = httpRequest.getSession() ;     
    SessionContainer scontainer = (SessionContainer)pcontainer.getComponentInstance(session.getId()) ;
    RequestInfo rinfo  = null ;
   
    if(scontainer != null) {
      PortalResources appres = 
        (PortalResources)scontainer.getComponentInstanceOfType(PortalResources.class);
      appres.getLocaleConfig().setInput(httpRequest) ;
      rinfo = (RequestInfo) scontainer.getComponentInstanceOfType(RequestInfo.class);
    } 
    try {
      //if user login , invalidate the session to logout when user move to public link
      String owner  = RequestInfo.getPortalOwner(httpRequest) ;
      if (scontainer == null  ||
          rinfo.getAccessibility() != RequestInfo.PUBLIC_ACCESS ||
          !rinfo.getPortalOwner().equals(owner)) {
        Util.removeAttribute(session) ;
        PortalConfig config = configService_.getPortalConfig(owner) ;
        if(!portalACL_.hasViewPortalPermission(config, null)) {
          HttpServletResponse httpResponse = (HttpServletResponse) response ;
          pcontainer.removeSessionContainer(session.getId()) ;
          httpResponse.sendRedirect(httpRequest.getContextPath() + "/access-error.jsp") ;
          return ;
        } 
        if(scontainer != null) {
          pcontainer.removeSessionContainer(session.getId()) ;
        }
        scontainer = pcontainer.createSessionContainer(session.getId(), owner) ;
        scontainer.getMonitor().setClientInfo(new HttpClientInfo(httpRequest)) ;
        rinfo = (RequestInfo) scontainer.getComponentInstanceOfType(RequestInfo.class);
      } 
      rinfo.init(httpRequest, RequestInfo.PUBLIC_ACCESS) ;
      scontainer.startActionLifcycle(); 
      httpRequest.setAttribute("javax.servlet.include.path_info", rinfo.getViewId()) ;
      chain.doFilter(request, response) ;
    } catch (Throwable ex) {
      ex = ExceptionUtil.getRootCause(ex) ;
      log_.error("Error: ", ex) ;
    } finally { 
      hserviceContainer_.closeAllSessions() ;
      long end = System.currentTimeMillis() ;
      ActionData data = new ActionData(rinfo.getPortalOwner(),
                                       rinfo.getPageName(), 
                                       httpRequest.getMethod(), 
                                       end - start, 
                                       httpRequest.getParameterMap()) ;
      scontainer.getMonitor().log(data) ;
      if(scontainer != null)scontainer.endActionLifcycle() ;
      PortalContainer.setInstance(null) ;
      scontainer.endActionLifcycle(); 
    }
  }
  
  public void destroy() { }
}
