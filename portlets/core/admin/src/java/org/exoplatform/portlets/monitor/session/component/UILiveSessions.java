/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.monitor.session.component;

import java.io.IOException;
import java.util.ArrayList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.client.http.HttpClientInfo;
import org.exoplatform.container.monitor.SessionMonitor;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 22 juin 2004
 */
public class UILiveSessions  extends UIGrid  {
  final static public String VIEW_ACTION = "view" ;
  private static Parameter[] VIEW_PARAMS = {new Parameter(ACTION , VIEW_ACTION) } ;

  private SessionContainerDataHandler dataHandler_ ;
  private PortalContainer pcontainer_ ;
  private boolean adminRole_ ;

  public UILiveSessions() throws Exception {
    setId("UILiveSessions") ;
    pcontainer_ = PortalContainer.getInstance() ;
    dataHandler_ = new SessionContainerDataHandler() ;
    dataHandler_.setDatas(new ArrayList()) ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    add(new Rows(dataHandler_, "even", "odd").
        add(new Column("#{UILiveSessions.header.user}", "user")).
        add(new Column("#{UILiveSessions.header.client-name}", "clientName")).
        add(new Column("#{UILiveSessions.header.ip-address}", "ipAddress")).
        add(new Column("#{UILiveSessions.header.request-counter}", "requestCounter")).
        add(new Column("#{UILiveSessions.header.live-time}", "liveTime")).
        add(new Column("#{UILiveSessions.header.context-path}", "path")).
        add(new ActionColumn("#{UILiveSessions.header.action}", "sessionId").
            add(adminRole_ ,new Button("#{UILiveSessions.button.view}", VIEW_PARAMS)))) ;
    addActionListener(ViewActionListener.class, VIEW_ACTION) ;
  }

  static public class SessionContainerDataHandler extends ListDataHandler {
  	private SessionContainer scontainer_ ;
    private HttpClientInfo info ;
    private  SessionMonitor monitor ;
    
  	public String  getData(String fieldName)   {
      if("sessionId".equals(fieldName)) return scontainer_.getId() ;
      if("user".equals(fieldName)) return scontainer_.getOwner() ;
      if(info != null && "clientName".equals(fieldName)) return info.getClientName() ;
      if(info != null && "ipAddress".equals(fieldName)) return info.getIpAddress() ;
      if("requestCounter".equals(fieldName)) return Integer.toString(monitor.getActionCount()) ;
      if("liveTime".equals(fieldName)) return Long.toString(monitor.getLiveTimeInMinute()) ;
      if("path".equals(fieldName)) return "N/A";
  		return "unknown" ;
  	}
  	
  	public void setCurrentObject(Object o) { 
      scontainer_ = (SessionContainer) o;
      info = (HttpClientInfo) scontainer_.getMonitor().getClientInfo() ;
      monitor =  scontainer_.getMonitor() ;
    }
  }
  
  public void encodeBegin(javax.faces.context.FacesContext context) throws IOException {
    dataHandler_.setDatas(pcontainer_.getLiveSessions()) ;
  	super.encodeBegin(context) ;
  }
  
  static public class ViewActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UILiveSessions uiComp  = (UILiveSessions) event.getSource() ;
      UIActionHistory uiMonitor = (UIActionHistory) uiComp.getSibling(UIActionHistory.class) ;
      String sessionId = event.getParameter(OBJECTID) ;
      PortalContainer pcontainer = PortalContainer.getInstance() ;
      SessionContainer scontainer = 
        (SessionContainer) pcontainer.getComponentInstance(sessionId) ;
      uiMonitor.setActionHistory(UILiveSessions.class, scontainer.getMonitor().getHistory()) ;
      uiComp.setRenderedSibling(UIActionHistory.class) ;
    }
  }
}