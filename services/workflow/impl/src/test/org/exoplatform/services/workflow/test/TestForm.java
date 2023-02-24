/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.test;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.DatabaseService;
import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.workflow.Form;
import org.exoplatform.services.workflow.WorkflowExecutionService;
import org.jbpm.JpdlException;
import org.jbpm.JbpmConfiguration;
import org.jbpm.model.definition.Definition;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public class TestForm extends BaseTest{

  private static final String PROCESS_FILE = "processdefinition.xml";

  public TestForm(String name) {
    super(name);
  }

  protected String getDescription() {
    return "test workflow forms";
  }

  public void setUp() {
    super.setUp();
    String[] files = {"forms.xml", "start.properties",
        "evaluation.properties", "hr.properties"};
    try {
      deployProcess(PROCESS_FILE, files);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JpdlException e) {
      e.printStackTrace();
    } catch (ExoServiceException e) {
      e.printStackTrace();
    }

  }

  public void testForms() {
    WorkflowExecutionService executionService = workflowServiceContainer.
        createWorkflowExecutionService();
    Definition definition = (Definition) executionService.getAllDefinitions().get(0);
    workflowFormsService.addForms(definition, executionService, new Locale("en"));
    executionService.close();
    Form form = workflowFormsService.getForm(definition.getId(), "start");
    assertNotNull(form);
    assertEquals("start", form.getStateName());
    ResourceBundle rB = form.getResourceBundle();
    assertEquals("Let's pray", rB.getString("submit"));
    assertEquals("the amount you want your salary to be increased",
        rB.getString("amount.asked.title"));

    List map = form.getSubmitButtons();
    assertTrue(map.isEmpty());

    List variableFormats = form.getVariableFormats();
    assertTrue(!variableFormats.isEmpty());
  }
}
