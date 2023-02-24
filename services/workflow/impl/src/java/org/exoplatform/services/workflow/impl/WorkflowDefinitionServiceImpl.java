/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.impl;

import org.exoplatform.services.workflow.WorkflowDefinitionService;
import org.jbpm.DefinitionService;
import org.jbpm.JbpmServiceFactory;
import org.jbpm.JpdlException;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.jar.JarInputStream;
import java.sql.SQLException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 9 mai 2004
 */
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {
  private DefinitionService definitionService;

  public WorkflowDefinitionServiceImpl(JbpmServiceFactory serviceLocator) {    
    definitionService = serviceLocator.getDefinitionService();
  }


  public void deployProcessArchive(JarInputStream processArchiveStream)
      throws JpdlException, IOException {
    definitionService.deployProcessArchive(processArchiveStream);
  }

  public void close() {
    definitionService.close();
  }
}
