/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import javax.sql.DataSource;
import java.util.* ;
import org.exoplatform.services.database.DatabaseService;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SARL . Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Date: Jun 14, 2003 Time: 1:12:22 PM
 */
public class InMemoryDatabaseServiceImpl implements DatabaseService, Startable {
  
  private DataSource defaultDS_;
  private Map datasources_ ;

  public InMemoryDatabaseServiceImpl() throws Exception {
    datasources_ = new HashMap() ;
    defaultDS_ = createDataSource(DatabaseService.EXO_DATASOURCE_NAME) ;
    datasources_.put(DatabaseService.EXO_DATASOURCE_NAME, defaultDS_) ;
  }

  public DataSource getDefaultDataSource() {  return defaultDS_; }

  public DataSource getDataSource(String name) {
    DataSource ds = (DataSource) datasources_.get(name) ;
    if(ds == null)  {
      ds = createDataSource(name) ;
      datasources_.put(name, ds) ;
    }
    return ds ; 
  }

  public String getDatabaseType(String dsName) {  return DatabaseService.HSQL ; }
  public String getDatabaseType(DataSource ds) {  return DatabaseService.HSQL ; }
  
  private DataSource createDataSource(String name) {
    org.hsqldb.jdbc.jdbcDataSource ds  = new org.hsqldb.jdbc.jdbcDataSource() ;
    ds.setDatabase("jdbc:hsqldb:mem:" + name) ;
    ds.setUser("sa") ;
    ds.setPassword("") ;
    return ds ;
  }
  
  public void start() {  }
  public void stop() { }
}