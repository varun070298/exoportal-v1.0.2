/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.database.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DatabaseService;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.connection.ConnectionProvider;

public class ExoConnectionProvider implements ConnectionProvider {
  private boolean autocommit_ ;
  private String datasourceName_ ;

  public ExoConnectionProvider() {
    autocommit_ = true ;
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#configure(Properties)
   */
  public void configure(Properties props) throws HibernateException {
    datasourceName_ = (String) props.get("hibernate.datasource.name") ;    
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#getConnection()
   */
  public Connection getConnection() throws SQLException {
    PortalContainer container = PortalContainer.getInstance();
    DatabaseService dbService = 
      (DatabaseService) container.getComponentInstanceOfType(DatabaseService.class);    
    DataSource ds = dbService.getDataSource(datasourceName_) ;
    Connection conn = ds.getConnection() ;
    conn.setAutoCommit(autocommit_) ;
    return conn ;
  }

  /**
   * @see net.sf.hibernate.connection.ConnectionProvider#closeConnection(Connection)
   */
  public void closeConnection(Connection conn) throws SQLException {
    conn.close() ;
  }
  
  public void close() {
  }
}