/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor.jvm;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 8, 2004
 * @version $Id$
 */
public class J2EEServerInfo {
  private String serverName_ ;
  private String serverHome_ ;
  protected String sharedLibDirecotry_ ;
  protected String appDeployDirecotry_ ;
  
  public J2EEServerInfo() {
    String catalinaHome = System.getProperty("catalina.home");
    String jbossHome = System.getProperty("jboss.home.dir");
    String jettyHome = System.getProperty("jetty.home");
    String websphereHome = System.getProperty("was.install.root");
    String weblogicHome = System.getProperty("weblogic.Name");
    String standAlone = System.getProperty("maven.exoplatform.dir") ;            
    if (catalinaHome != null) {        
      serverName_= "tomcat" ; serverHome_ = catalinaHome ;
    } else if (jbossHome != null) {
      serverName_= "jboss" ; serverHome_ = jbossHome ;
    } else if (jettyHome != null) {
      serverName_= "tomcat" ; serverHome_ = jettyHome ;
    } else if (websphereHome != null) {
      serverName_= "websphere" ; serverHome_ = websphereHome ;
    } else if (weblogicHome != null) {
      serverName_= "weblogic" ; serverHome_ = weblogicHome ;
    } else if (standAlone != null) {
      serverName_= "standalone" ; serverHome_ = standAlone ;
    } else {
      throw new UnsupportedOperationException("unknown server platform") ;
    }
  }
  
  public String getServerName() { return serverName_ ; }
  
  public String getServerHome() { return serverHome_ ; }
  
  public String getSharedLibDirectory() {  return sharedLibDirecotry_ ;  }
  
  public String getApplicationDeployDirectory() {  return appDeployDirecotry_ ; }
}
