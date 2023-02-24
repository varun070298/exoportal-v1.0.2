package org.exoplatform.services.portal.log.impl;

import java.util.Date;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.monitor.SessionMonitor;
import org.exoplatform.container.monitor.SessionMonitorListener;
import org.exoplatform.container.monitor.SessionMonitorListenerStack;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.portal.log.PortalLogService;
import org.exoplatform.services.portal.log.SessionLogData;
import org.exoplatform.services.task.BaseTask;
import org.exoplatform.services.task.TaskService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 1, 2004
 * @version $Id$
 */
public class SessionLogPersister extends SessionMonitorListener{
  private PortalLogService plService_ ;
  private OrganizationService orgService_ ;
  private TaskService tservice_ ;
  private String saveRule_ ;
  
  public SessionLogPersister(SessionMonitorListenerStack stack, 
                             PortalLogService service,
                             OrganizationService orgService,
                             TaskService tservice,
                             ConfigurationManager confService) throws Exception {
    super(stack) ;
    plService_ = service ;
    orgService_ = orgService ;
    tservice_ = tservice ;
    ServiceConfiguration sconf = confService.getServiceConfiguration(getClass()) ;
    saveRule_ = sconf.getValueParam("save.rule").getValue() ;
  }
  
  public void onStop(SessionMonitor monitor) {
    if(monitor.getRemoteUser() != null) {
      tservice_.queueTask(new UpdateUserLastLoginTask(orgService_, monitor.getSessionOwner())) ;
    }
    if("error.session".equals(saveRule_) && monitor.getErrorCount() == 0) return ;
    tservice_.queueTask(new SessionLogPersisterTask(monitor, plService_)) ;
  }
  
  static class SessionLogPersisterTask extends BaseTask {
    SessionMonitor monitor_ ;
    PortalLogService service_ ;
    
    SessionLogPersisterTask(SessionMonitor monitor, PortalLogService service) {
      monitor_ = monitor ;
      service_ = service ;
    }
    
    public void execute() throws Exception {
      SessionLogData logData = new SessionLogDataImpl() ;
      logData.setSessionOwner(monitor_.getSessionOwner()) ;
      logData.setRemoteUser(monitor_.getRemoteUser()) ;
      logData.setClientName(monitor_.getClientInfo().getClientName()) ;
      logData.setIPAddress(monitor_.getIPAddress()) ;
      logData.setAccessTime(new Date(monitor_.getAccessTime())) ;
      logData.setDuration((int)monitor_.getLiveTimeInSecond()) ;
      logData.setActionHistory(monitor_.getHistory()) ;
      logData.setErrorCount(monitor_.getErrorCount()) ;
      logData.setActionCount(monitor_.getActionCount()) ;
      service_.saveSessionLogData(logData) ;
    }

    public String getName() {   return "PersistSessionLog";   }
    
    public String getDescription() { return "saving session log data" ;  }
  }
  
  static class UpdateUserLastLoginTask extends BaseTask {
    private OrganizationService  orgService_ ;
    private String   userName_ ;
    
    UpdateUserLastLoginTask(OrganizationService  orgService, String userName) {
      orgService_ = orgService ;
      userName_ = userName;
    }
    
    public void execute() throws Exception {
      User user = orgService_.findUserByName(userName_) ;
      user.setLastLoginTime(new Date()) ;
      orgService_.saveUser(user) ;
    }

    public String getName() {   return "UpdateUserLastLoginTask";   }
    
    public String getDescription() { return "update the last login time of the user" ;  }
  }
}
