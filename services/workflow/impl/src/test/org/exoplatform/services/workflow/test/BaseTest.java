/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.workflow.*;
import org.exoplatform.test.BasicTestCase;
import org.jbpm.JpdlException;
import org.jbpm.par.ArchiveBuilder;



/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 10 mai 2004
 */
public abstract class BaseTest extends BasicTestCase {

  protected static final String PROCESS_PATH = "file:./src/conf/processes/";
  protected WorkflowServiceContainer workflowServiceContainer;
  protected WorkflowFormsService workflowFormsService;

  public BaseTest(String name) {
    super(name);
  }

  public void setUp() {
    workflowServiceContainer = (WorkflowServiceContainer) PortalContainer.
        getInstance().getComponentInstanceOfType(WorkflowServiceContainer.class);
    workflowFormsService = (WorkflowFormsService) PortalContainer.
        getInstance().getComponentInstanceOfType(WorkflowFormsService.class);
  }

  protected void deployProcess(String process, String[] files) throws IOException, JpdlException,
      ExoServiceException {
    URL url = new URL(PROCESS_PATH + process);
    InputStream is = url.openStream();
    ArchiveBuilder archiveBuilder = new ArchiveBuilder(is);

    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        String file = files[i];
        url = new URL(PROCESS_PATH + file);
        archiveBuilder.add(file, url.openStream());
      }
    }
    WorkflowDefinitionService definitionService = workflowServiceContainer.
        createWorkflowDefinitionService();
    definitionService.deployProcessArchive(archiveBuilder.getJarInputStream());
    definitionService.close();
  }

}
