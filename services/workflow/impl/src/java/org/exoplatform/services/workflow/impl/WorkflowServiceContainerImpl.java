/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.impl;

import org.exoplatform.commons.Environment;
import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.picocontainer.Startable;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.workflow.*;
import org.jbpm.JbpmServiceFactory;
import org.jbpm.JbpmConfiguration;

import javax.sql.DataSource;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 juin 2004
 */
public class WorkflowServiceContainerImpl implements WorkflowServiceContainer, Startable {

  private DataSource workflowDS;

  private static final String DATASOURCE_PROPERTY = "hibernate.connection.datasource";
  private static final String CACHE_PROPERTY = "hibernate.cache.provider_class" ;
  static final String DIALECT_PROPERTY = "hibernate.dialect";
  static final String SHOW_SQL_PROPERTY = "hibernate.show_sql";
  static final String REFLECTION_OPTIMIZER_PROPERTY = "hibernate.cglib.use_reflection_optimizer";
  static final String CONNECTION_PROVIDER_PROPERTY = "hibernate.connection.provider_class";
  
  private JbpmServiceFactory serviceLocator;
  private ServiceConfiguration serviceConfiguration;
  private DatabaseService databaseService;

  public WorkflowServiceContainerImpl(ConfigurationManager conf,
                                      DatabaseService databaseService) throws Exception {
    this.serviceConfiguration = conf.getServiceConfiguration(this.getClass());
    this.databaseService = databaseService;
  }

  private void init(){
    Properties properties = new Properties();
    if (Environment.getInstance().getPlatform() != Environment.STAND_ALONE) {
      String datasource = serviceConfiguration.getValuesParam("datasource").getValue();
      properties.setProperty(DATASOURCE_PROPERTY, datasource);
      String cache = serviceConfiguration.getValuesParam("cache").getValue();
      properties.setProperty(CACHE_PROPERTY, cache);
      String connectionProvider = serviceConfiguration.getValuesParam("connection-provider").getValue();
      properties.setProperty(CONNECTION_PROVIDER_PROPERTY, connectionProvider);
      String dbType = databaseService.getDatabaseType(DatabaseService.EXO_WORKFLOW_DATASOURCE_NAME);
      setDialect(properties, dbType);
    } else {
      properties.setProperty(CACHE_PROPERTY, "org.exoplatform.services.database.impl.ExoCacheProvider");
    }
    this.serviceLocator = new JbpmServiceFactory(new JbpmConfiguration(properties));

  }

  private void setDialect(Properties properties, String dbType) {
    if (DatabaseService.HSQL.equals(dbType)) {
      properties.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.HSQLDialect");
    } else if (DatabaseService.MYSQL.equals(dbType)) {
      properties.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.MySQLDialect");
    } else if (DatabaseService.DB2.equals(dbType)) {
      properties.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.DB2Dialect");
    } else if (DatabaseService.ORACLE.equals(dbType)) {
      properties.setProperty(DIALECT_PROPERTY, "net.sf.hibernate.dialect.Oracle9Dialect");
    }
  }

  public WorkflowDefinitionService createWorkflowDefinitionService() {
    if(serviceLocator == null)
      init();
    return new WorkflowDefinitionServiceImpl(serviceLocator);
  }

  public WorkflowExecutionService createWorkflowExecutionService() {
    if(serviceLocator == null)
      init();
    return new WorkflowExecutionServiceImpl(serviceLocator);
  }

  public void start() {

  }

  public void stop() {
  }

}
