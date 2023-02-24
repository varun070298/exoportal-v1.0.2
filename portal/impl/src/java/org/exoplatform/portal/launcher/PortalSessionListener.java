/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.launcher;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.SessionContainer ;

/**
 * Created by The eXo Platform SARL        .
 * Date: Jan 25, 2003
 * Time: 5:25:52 PM
 */
public class PortalSessionListener implements HttpSessionListener {
  
  public PortalSessionListener() {
  }
  
  public void sessionCreated(HttpSessionEvent event) {
    
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session = event.getSession() ;
    ServletContext context = session.getServletContext() ;
    PortalContainer pcontainer = 
      RootContainer.getInstance().getPortalContainer(context.getServletContextName()) ;
    SessionContainer scontainer = (SessionContainer)pcontainer.getComponentInstance(session.getId()) ;
    if(scontainer != null)  {
      PortalContainer.setInstance(pcontainer) ;
      pcontainer.unregisterComponent(session.getId()) ;
      scontainer.stop() ;
      PortalContainer.setInstance(null) ;
    }
  }
}