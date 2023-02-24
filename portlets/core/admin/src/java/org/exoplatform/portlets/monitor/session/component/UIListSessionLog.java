/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.monitor.session.component;

import java.text.SimpleDateFormat;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portal.log.PortalLogService;
import org.exoplatform.services.portal.log.Query;
import org.exoplatform.services.portal.log.SessionLogData;
import org.exoplatform.services.portal.log.SessionLogDataDescription;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 22 juin 2004
 */
public class UIListSessionLog  extends UIGrid  {
  private static SimpleDateFormat ft_ = new SimpleDateFormat("dd/MM/yy HH:mm:ss") ;
  final static public String VIEW_ACTION = "view" ;
  private static Parameter[] VIEW_PARAMS = {new Parameter(ACTION , VIEW_ACTION) } ;

  private boolean adminRole_ ;
  private UIPageListIterator uiPageIterator_ ;
  private PortalLogService service_ ;

  public UIListSessionLog(PortalLogService service) throws Exception {
    setId("UIListSessionLog") ;
    service_ = service ;
    uiPageIterator_ = new UIPageListIterator(new SessionLogDataHandler()) ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    add(new Rows(uiPageIterator_.getPageListDataHandler(), "even", "odd").
        add(new Column("#{UIListSessionLog.header.session-owner}", "sessionOwner")).
        add(new Column("#{UIListSessionLog.header.remote-user}", "remoteUser")).
        add(new Column("#{UIListSessionLog.header.ip-address}", "ipAddress")).
        add(new Column("#{UIListSessionLog.header.client-type}", "clientType")).
        add(new Column("#{UIListSessionLog.header.access-time}", "accessTime")).
        add(new Column("#{UIListSessionLog.header.requests}", "requests")).
        add(new Column("#{UIListSessionLog.header.duration}", "duration")).
        add(new Column("#{UIListSessionLog.header.error-count}", "errorCount")).
        add(new ActionColumn("#{UIListSessionLog.header.action}", OBJECTID).
            add(adminRole_ ,new Button("#{UIListSessionLog.button.view}", VIEW_PARAMS)))) ;
    add(new Row().
        add(new ComponentCell(this, uiPageIterator_).
            addColspan("9").addStyle("text-align: right")));
    addActionListener(ViewActionListener.class, VIEW_ACTION) ;
    update(new Query()) ;
  }

  public void update(Query query) throws Exception {
    uiPageIterator_.setPageList(service_.getSessionLogDatas(query)) ;
  }
  
  static public class SessionLogDataHandler extends PageListDataHandler {
  	private SessionLogDataDescription data_ ;
    
  	public String  getData(String fieldName)   {
      if(OBJECTID.equals(fieldName)) return data_.getId() ;
      if("sessionOwner".equals(fieldName)) return data_.getSessionOwner() ;
      if("remoteUser".equals(fieldName)) return data_.getRemoteUser() ;
      if("ipAddress".equals(fieldName)) return data_.getIPAddress() ;
      if("clientType".equals(fieldName)) return data_.getClientName() ;
      if("accessTime".equals(fieldName)) return ft_.format(data_.getAccessTime()) ;
      if("requests".equals(fieldName)) return  Integer.toString(data_.getActionCount());
      if("duration".equals(fieldName)) return Integer.toString(data_.getDuration());
      if("errorCount".equals(fieldName)) return Integer.toString(data_.getErrorCount());
  		return "" ;
  	}
  	
  	public void setCurrentObject(Object o) { data_ = (SessionLogDataDescription) o; }
  }
  
  static public class ViewActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIListSessionLog uiLog = (UIListSessionLog) event.getSource() ;
      String objectId = event.getParameter(OBJECTID) ;
      SessionLogData data = uiLog.service_.getSessionLogData(objectId) ;
      UIActionHistory uiHistory = (UIActionHistory) uiLog.getSibling(UIActionHistory.class) ;
      uiHistory.setActionHistory(UIListSessionLog.class, data.getActionHistory()) ;
      uiLog.setRenderedSibling(UIActionHistory.class );
    }
  }
}