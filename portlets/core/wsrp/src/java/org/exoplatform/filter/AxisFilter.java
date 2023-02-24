/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.database.HibernateServiceContainer;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 26 juil. 2004
 */
public class AxisFilter implements Filter {

  public static final String WSRP_CONTAINER = "portal";  
  private HibernateServiceContainer hserviceContainer_;

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    PortalContainer pcontainer = RootContainer.getInstance().getPortalContainer(WSRP_CONTAINER);                 
    PortalContainer.setInstance(pcontainer);
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {      
      getHibernateServiceContainer(pcontainer).closeAllSessions() ;
    }
  }

  public void destroy() {
  }
  
  private HibernateServiceContainer getHibernateServiceContainer(PortalContainer pcontainer){
    if(hserviceContainer_ == null){
      hserviceContainer_ = (HibernateServiceContainer) pcontainer.getComponentInstanceOfType(
          HibernateServiceContainer.class) ;
    }
    return hserviceContainer_;
  }  

}
