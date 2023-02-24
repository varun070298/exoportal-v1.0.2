/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.backup.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ValueParam;
import org.exoplatform.services.backup.ExportLogger;
import org.exoplatform.services.backup.ImportExportPlugin;
import org.exoplatform.services.backup.ImportExportService;
import org.exoplatform.services.backup.ImportLogger;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
/**
 * May 27, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class ImportExportServiceImpl implements ImportExportService {
	private String directory_ ;
	private String userDataDir_ ;
  private String userTransformDir_ ;
	private String serviceDataDir_ ;
  private String serviceTransformDir_ ;
	private Log log_ ;
	private ExportLogger exportLogger_ ;
	private ImportLogger importLogger_ ;
	private OrganizationService orgService_ ;
	private HibernateService hservice_ ;
  private Map plugins_ ;
	
  public ImportExportServiceImpl(LogService lservice, 
                                 OrganizationService orgService,
                                 HibernateService hservice,
                                 ConfigurationManager cmanager) throws Exception {
		orgService_ = orgService ;
		hservice_ = hservice ;
		log_ = lservice.getLog("org.exoplatform.services.exporter") ;
		exportLogger_ = new ExportLogger() ;
		importLogger_ = new ImportLogger() ;
    String backupDir = null ;
    ServiceConfiguration sconf = cmanager.getServiceConfiguration(ImportExportService.class) ;
    ValueParam param = sconf.getValueParam("backup.directory");
    if(param != null) backupDir = param.getValue() ;
		if("default".equals(backupDir)) {
		  backupDir = System.getProperty("java.io.tmpdir") ;
    }
		setDirectory(backupDir);
    plugins_ = new HashMap() ;
	}
	
	public Collection getPlugins() { return plugins_.values() ;	}

  public ImportExportPlugin removePlugin(String name) {
    return (ImportExportPlugin) plugins_.remove(name) ;
  }
  
  public void addPlugin(ImportExportPlugin plugin)  {
    plugins_.put(plugin.getName(), plugin) ;
  }
  
	public String getDirectory() { return directory_ ;}
	public void   setDirectory(String s) throws Exception { 
		File dir = new File(s) ;
		if (!dir.exists()) {
			dir.mkdirs() ;
		}
		if(dir.isDirectory()) {
			directory_ = dir.getPath();
			userDataDir_ = directory_ +  File.separator + "user-data" ;
			dir = new File(userDataDir_) ;
			if(!dir.exists())dir.mkdir() ;
      userTransformDir_ = directory_ +  File.separator + "user-transform" ;
      dir = new File(userTransformDir_) ;
      if(!dir.exists())dir.mkdir() ;
			serviceDataDir_ = directory_ +  File.separator + "service-data" ;
			dir = new File(serviceDataDir_) ;
			if(!dir.exists())dir.mkdir() ;
      serviceTransformDir_ = directory_ +  File.separator + "service-transform" ;
      dir = new File(serviceTransformDir_) ;
      if(!dir.exists())dir.mkdir() ;
		} else {
			throw new Exception(s + " is a file. Cannot create an export directory for backup service") ;
		}
	}
	
	public String getUserDataDirectory() { return userDataDir_ ; }
	public String getServiceDataDirectory() { return serviceDataDir_ ; }


	public void exportUserData() throws Exception {
	  PageList plist = orgService_.getUserPageList(50) ;
	  for(int i = 1; i <= plist.getAvailablePage() ; i++) {
	    List list = plist.getPage(i) ;
	    for(int j = 0; j < list.size(); j++) {
	      User user = (User) list.get(j);
	      String userName = user.getUserName() ;
	      exportUserData(userName) ;
	    }
	  }
	}

  public void exportUserData(String username) throws Exception {
    String fileName = userDataDir_ + File.separator + username.trim() + ".zip" ;    
    File file = new File(fileName) ;
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file)) ;
    exportUserData(username, out) ;
    out.close() ;
  }

 
  public void exportUserData(String username, ZipOutputStream out) throws Exception {
    Session session = null ;
    try {
      session = hservice_.openSession() ;
      XResources xresources = new XResources() ;
      xresources.addResource(Session.class, session) ;
      Iterator i = plugins_.values().iterator() ;
      while(i.hasNext()) {
        ImportExportPlugin plugin = (ImportExportPlugin) i.next() ;
        if(!plugin.hasUserData()) continue ;
        plugin.exportUserData(username, xresources, out);
      }
      hservice_.closeSession() ;
      exportLogger_.log(username) ;
    } catch (Exception ex) {
      exportLogger_.log(username, ex) ;
      hservice_.closeSession() ;
      log_.error("Error when exporting the user" + username, ex) ;
      ex.printStackTrace();
    }
  }

  public void importUserData() throws Exception {
    File dir = new File(userDataDir_) ;
    String[] file = dir.list() ;
    for(int i = 0 ; i < file.length; i++) {
      int idx = file[i].indexOf('.') ;
      String userName = file[i].substring(0, idx) ;
      ZipFile zipFile  = new ZipFile(userDataDir_ + "/" + file[i]) ;
      try {
        importUserData(userName, zipFile) ;
      } catch (Exception ex) {
        log_.error("Cannot import the user " + userName, ex) ;
      }
      zipFile.close() ;
    }
  }

  public void importUserData(String username) throws Exception {
    String fileName = userDataDir_ + File.separator + username + ".zip" ;
    ZipFile zipFile  = new ZipFile(new File(fileName)) ;
    importUserData(username, zipFile) ;
    zipFile.close() ; 
  }

  public void importUserData(String username, ZipFile zipFile) throws Exception {
    Session session = null ;
    try {
      session = hservice_.openSession() ;
      XResources xresources = new XResources() ;
      xresources.addResource(Session.class, session) ;
      Iterator i = plugins_.values().iterator() ;
      while(i.hasNext()) {
        ImportExportPlugin plugin = (ImportExportPlugin) i.next() ;
        if(plugin.hasUserData()) {
          plugin.importUserData(username, xresources, zipFile) ;
        }
      }
      session.flush() ;
      hservice_.closeSession() ;
      importLogger_.log(username) ;
    } catch (Exception ex) {
      hservice_.closeSession() ;
      importLogger_.log(username, ex) ;
      log_.error("cannit import user " + username, ex) ;
    }
  }
  
  //--------------------------------------------------------------------------------------
  
  public void exportServiceData() throws Exception {
    Iterator i = plugins_.keySet().iterator() ;
    while(i.hasNext()) {
      String name = (String) i.next();
      exportServiceData(name) ;
    }
  }

  public void exportServiceData(String serviceName) throws Exception {
    ImportExportPlugin plugin = (ImportExportPlugin) plugins_.get(serviceName) ;
    if(!plugin.hasServiceData()) return ;
    String fileName = serviceDataDir_ + File.separator + serviceName + ".zip" ;
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileName)) ;
    exportServiceData(serviceName, out) ;
    out.close() ;
  }

  
  public void exportServiceData(String serviceName, ZipOutputStream out) throws Exception {
    Session session = null ;
    ImportExportPlugin plugin = (ImportExportPlugin) plugins_.get(serviceName) ;
    if(!plugin.hasServiceData()) return ;
    try {
      session = hservice_.openSession() ;
      XResources xresources = new XResources() ;
      xresources.addResource(Session.class, session) ;
      plugin.exportServiceData(xresources, out) ;
      hservice_.closeSession() ;
      exportLogger_.log(serviceName) ;
    } catch (Exception ex) {
      hservice_.closeSession() ;
      exportLogger_.log(serviceName, ex) ;
      log_.error("Cannot export service" + serviceName, ex) ;
    }
  }

  public void importServiceData() throws Exception {
    Iterator i = plugins_.keySet().iterator() ;
    while(i.hasNext()) {
      String name = (String) i.next();
      importServiceData(name) ;
    }
  }

  public void importServiceData(String serviceName) throws Exception {
    ImportExportPlugin plugin = (ImportExportPlugin) plugins_.get(serviceName) ;
    if(!plugin.hasServiceData()) return ;
    String fileName = serviceDataDir_ + File.separator + serviceName + ".zip" ;
    ZipFile zipFile  = new ZipFile(new File(fileName)) ;
    importServiceData(serviceName, zipFile) ;
    zipFile.close() ;
  }

  public void importServiceData(String serviceName, ZipFile zipFile) throws Exception {
    ImportExportPlugin plugin = (ImportExportPlugin) plugins_.get(serviceName) ;
    Session session = null ;
    try {
      session = hservice_.openSession() ;
      XResources xresources = new XResources() ;
      xresources.addResource(Session.class, session) ;
      plugin.importServiceData(xresources, zipFile) ;
      session.flush() ;
      hservice_.closeSession() ;
      importLogger_.log(serviceName) ;
    } catch (Exception ex) {
      importLogger_.log(serviceName, ex) ;
      hservice_.closeSession() ;
      log_.error("Cannot import the service " + serviceName, ex) ;
    }
  }
  //-------------------------------------------------------------------------------
  
  public ExportLogger getExportLogger() { return exportLogger_; }

  public ImportLogger getImportLogger() { return importLogger_ ; }
}