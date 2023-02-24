/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
aspect OrderAspect { 
  declare precedence: PortletMonitorAspect, PortletSecurityAspect, PortletCacheAspect,                      PortletContentAspect, PortletFilterAspect;
}

