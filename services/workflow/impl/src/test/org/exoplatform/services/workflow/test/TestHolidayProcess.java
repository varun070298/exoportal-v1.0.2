/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.test;

import org.exoplatform.services.exception.ExoServiceException;
import org.exoplatform.services.workflow.WorkflowExecutionService;
import org.jbpm.ExecutionException;
import org.jbpm.JpdlException;
import org.jbpm.model.definition.Definition;
import org.jbpm.model.execution.Token;
import org.jbpm.model.log.InvocationLog;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 10 mai 2004
 */
public class TestHolidayProcess extends BaseTest{

  private static final String PROCESS_FILE = "holidays.xml";

  public TestHolidayProcess(String name) {
    super(name);
  }

  protected String getDescription() {
    return "test holiday process";
  }

  public void setUp()  {
    super.setUp();
    try {
      deployProcess(PROCESS_FILE, null);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JpdlException e) {
      e.printStackTrace();
    } catch (ExoServiceException e) {
      e.printStackTrace();
    }
  }

  public void testProcessRegistration() {
    WorkflowExecutionService executionService = workflowServiceContainer.
        createWorkflowExecutionService();
    Definition definition = (Definition) executionService.getAllDefinitions().get(0);
    assertEquals("holiday process", definition.getName());
    assertEquals(1, definition.getVersion().intValue());
    assertEquals("request", definition.getStartState().getName());

    Collection latestDefinitions = executionService.getLatestDefinitions();
    definition = (Definition) latestDefinitions.iterator().next();
    assertEquals("holiday process", definition.getName());
    assertEquals(1, definition.getVersion().intValue());
    assertEquals("request", definition.getStartState().getName());
    executionService.close();
  }

  public void testProcessStart() throws ExecutionException {
    WorkflowExecutionService executionService = workflowServiceContainer.
        createWorkflowExecutionService();
    Collection latestDefinitions = executionService.getLatestDefinitions();
    Definition definition = (Definition) latestDefinitions.iterator().next();
    Map variables = new HashMap();
    Date start =  new Date();
    variables.put("start.date", start);
    variables.put("end.date", new Date(start.getTime() + 24*3600));

    InvocationLog iL = executionService.startProcessInstance("benj", definition.getId(), variables);
    assertEquals("benj", iL.getActorId());

    Token token = (Token) executionService.getTaskList("bossOfBenj", null).iterator().next();
    assertNotNull(token);
    assertEquals("bossOfBenj", token.getActorId());
    assertEquals("evaluation", token.getState().getName());
    iL = executionService.endOfState("bossOfBenj", token.getId(), variables, "approve");

    token = (Token) executionService.getTaskList("bossOfBenj", null).iterator().next();
    assertNotNull(token);
    assertEquals("bossOfBenj", token.getActorId());
    assertEquals("assign replacement", token.getState().getName());
    executionService.endOfState("bossOfBenj", token.getId(), variables);

    token = (Token) executionService.getTaskList("Hrofbenj", null).iterator().next();
    assertNotNull(token);
    assertEquals("Hrofbenj", token.getActorId());
    assertEquals("hr acknowledgement", token.getState().getName());
    executionService.endOfState("Hrofbenj", token.getId(), variables);
    executionService.close();
  }

}
