/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.workflow.impl;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.connection.ConnectionProvider;

import javax.sql.DataSource;
import org.exoplatform.commons.Environment;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DatabaseService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class WorkflowConnectionProvider implements ConnectionProvider {
  private boolean autocommit_ ;

  public WorkflowConnectionProvider() {
    int platform = Environment.getInstance().getPlatform()  ;
    autocommit_ = true ;
    if (platform == Environment.JBOSS_PLATFORM) {
      autocommit_ = false  ;
    }
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#configure(java.util.Properties)
   */
  public void configure(Properties props) throws HibernateException {
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#getConnection()
   */
  public Connection getConnection() throws SQLException {
    PortalContainer container = PortalContainer.getInstance();
    DatabaseService dbService = 
      (DatabaseService) container.getComponentInstanceOfType(DatabaseService.class);    
    DataSource ds = dbService.getDataSource(DatabaseService.EXO_WORKFLOW_DATASOURCE_NAME) ;
    Connection conn = ds.getConnection() ;
    conn.setAutoCommit(autocommit_) ;
    return conn ;
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#closeConnection(java.sql.Connection)
   */
  public void closeConnection(Connection conn) throws SQLException {
    conn.close() ;
  }

  public void close() {
  }
}
