/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.exoplatform.services.database.DatabaseService;
import org.picocontainer.Startable;
import org.exoplatform.commons.Environment;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;

/**
 * Created by The eXo Platform SARL . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 */
public class DatabaseServiceImpl implements DatabaseService, Startable {
  
  private DataSource defaultDS_;
  private Map datasources_;

  public DatabaseServiceImpl(ConfigurationManager confService) throws Exception {
    datasources_ = new HashMap();
    ServiceConfiguration sconf =  confService.getServiceConfiguration(DatabaseService.class);
    DatabaseServiceConfig config = 
      (DatabaseServiceConfig)sconf.getObjectParam("database.service.config").getObject() ;
    List dsConfigs = config.getDatasource() ;
    DataSource datasource = null;
    for (int i = 0; i < dsConfigs.size(); i++) {
      DatabaseServiceConfig.DataSourceConfig dsconfig = 
        (DatabaseServiceConfig.DataSourceConfig) dsConfigs.get(i);
      datasource = findDatasource(dsconfig) ;
      datasources_.put(dsconfig.getName(), datasource);
      if(defaultDS_ == null) defaultDS_ = datasource ;
    }
  }

  public DataSource getDefaultDataSource() {  return defaultDS_; }

  public DataSource getDataSource(String name) { 
    return (DataSource) datasources_.get(name);
  }

  public String getDatabaseType(String dsName) {
    DataSource ds = getDataSource(dsName);
    return getDatabaseType(ds) ;
  }
  
  public String getDatabaseType(DataSource ds) {  
    String dbType = null;
    Connection conn = null;
    try {
      conn = ds.getConnection();
      DatabaseMetaData data = conn.getMetaData();
      String pname = data.getDatabaseProductName().toLowerCase();
      if (pname.indexOf(DatabaseService.HSQL) >= 0) {
        dbType = DatabaseService.HSQL;
      } else if (pname.indexOf(DatabaseService.MYSQL) >= 0) {
        dbType = DatabaseService.MYSQL;
      } else if (pname.indexOf(DatabaseService.DB2) >= 0) {
        dbType = DatabaseService.DB2;
      } else if (pname.indexOf(DatabaseService.ORACLE) >= 0) {
        dbType = DatabaseService.ORACLE;
      } else if (pname.indexOf(DatabaseService.POSTGRESQL) >= 0) {
        dbType = DatabaseService.POSTGRESQL;
      } else if (pname.indexOf(DatabaseService.SQL_SERVER) >= 0) {
        dbType = DatabaseService.SQL_SERVER;
      } else {
        dbType = DatabaseService.UNKNOWN;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception ex) { }
      }
    }
    return dbType; 
  }
  
  private DataSource findDatasource(DatabaseServiceConfig.DataSourceConfig dsconfig) throws Exception {
    DataSource datasource = null;
    Context ctx = null;
    int platform = Environment.getInstance().getPlatform();
    if (platform == Environment.TOMCAT_PLATFORM) {
      ctx = new InitialContext();
      datasource = (javax.sql.DataSource) ctx.lookup("java:comp/env/"  + dsconfig.getJndi());
    } else if (platform == Environment.JETTY_PLATFORM) {
      ctx = new InitialContext();
      datasource = (javax.sql.DataSource) ctx.lookup(dsconfig.getJndi());
    } else if (platform == Environment.WEBSHPERE_PLATFORM) {
      ctx = new InitialContext();
      datasource = (javax.sql.DataSource) ctx.lookup(dsconfig.getJndi());
    } else if (platform == Environment.WEBLOGIC_PLATFORM) {
      ctx = new InitialContext();
      datasource = (javax.sql.DataSource) ctx.lookup(dsconfig.getJndi());
    } else if (platform == Environment.JBOSS_PLATFORM) {
      Properties props = new Properties();
      props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
      props.put(Context.PROVIDER_URL, "jnp://localhost:1099");
      props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
      ctx = new InitialContext(props);
      datasource = (javax.sql.DataSource) ctx.lookup("java:" +dsconfig.getJndi());
    } else {
      ctx = new InitialContext();
      datasource = (javax.sql.DataSource) ctx.lookup("java:comp/env/" + dsconfig.getJndi());
    }
    if(ctx != null) ctx.close() ;
    return datasource ;
  }
  
  public void start() {  }
  public void stop() { }
}
