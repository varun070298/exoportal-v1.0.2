package org.exoplatform.services.jcr;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;
import javax.jcr.Ticket;
import javax.jcr.Workspace;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: BaseTest.java,v 1.14 2004/07/29 10:52:21 benjmestrallet Exp $
 */
public class BaseTest extends TestCase {
  protected Log log;
  protected static String TEMP_PATH = "./temp/fsroot";
  protected static String WORKSPACE = "ws";
  protected Ticket ticket;
  protected Repository repository;
  protected Credentials credentials;
  protected PortalContainer manager;
  protected Workspace workspace;
  protected RepositoryService repositoryService;

  public void setUp() throws Exception {
    try {
      LogService service = (LogService) RootContainer.getInstance().
          getComponentInstanceOfType(LogService.class);
      log = service.getLog("org.exoplatform.services.jcr");
      service.setLogLevel("org.exoplatform.services.jcr", LogService.DEBUG, true); 
      
      PortalContainer servicesManager = PortalContainer.getInstance();
                        
      credentials = new SimpleCredentials("exo", "exo".toCharArray());      
                
      repositoryService = 
        (RepositoryService) servicesManager.getComponentInstanceOfType(RepositoryService.class);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}