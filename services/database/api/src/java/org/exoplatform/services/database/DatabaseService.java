/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
package org.exoplatform.services.database;

import javax.sql.DataSource;

public interface DatabaseService  {

  public static final String HSQL = "hsql";
  public static final String MYSQL = "mysql";
  public static final String DB2 = "db2";
  public static final String ORACLE = "oracle";
  public static final String POSTGRESQL = "postgresql";
  public static final String SQL_SERVER = "microsoft sql server";
  public static final String UNKNOWN = "unknown";

  public static final String EXO_DATASOURCE_NAME = "ExoDS";
  public static final String EXO_WORKFLOW_DATASOURCE_NAME = "WorkflowDS";

  public DataSource getDefaultDataSource()  ;
  public DataSource getDataSource(String name)  ;
  public String getDatabaseType(String dsName) ;
  public String getDatabaseType(DataSource ds) ;
}
