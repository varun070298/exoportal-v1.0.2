/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.impl;

import org.exoplatform.services.workflow.WorkflowExecutionService;
import org.jbpm.Assembler;
import org.jbpm.ExecutionException;
import org.jbpm.ExecutionService;
import org.jbpm.JbpmServiceFactory;
import org.jbpm.model.definition.Definition;
import org.jbpm.model.execution.ProcessInstance;
import org.jbpm.model.execution.Token;
import org.jbpm.model.log.InvocationLog;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 9 mai 2004
 */
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService{
  private ExecutionService executionService;

  public WorkflowExecutionServiceImpl(JbpmServiceFactory  serviceLocator) {    
    executionService = serviceLocator.getExecutionService();
  }

  public void close() {
    executionService.close();
  }

  public List getAllDefinitions() {
    return executionService.getAllDefinitions();
  }

  public List getAllDefinitions(Assembler assembler) {
    return executionService.getAllDefinitions(assembler);
  }

  public Collection getTaskList(String targetActorId, Assembler assembler) {
    return executionService.getTaskList(targetActorId, assembler);
  }

  public Token getToken(Long tokenId, Assembler assembler) {
    return executionService.getToken(tokenId, assembler);
  }

  public ProcessInstance getProcessInstance(Long processInstanceId, Assembler assembler) {
    return executionService.getProcessInstance(processInstanceId, assembler);
  }

  public Map getVariables(Long tokenId) {
    return executionService.getVariables(tokenId);
  }

  public Definition getDefinition(Long definitionId, Assembler assembler) {
    return executionService.getDefinition(definitionId, assembler);
  }

  public Definition getLatestDefinition(String name) {
    return executionService.getLatestDefinition(name);
  }

  public Collection getLatestDefinitions() {
    return executionService.getLatestDefinitions();
  }

  public byte[] getFile(Long processDefinitionId, String fileName) {
    return executionService.getFile(processDefinitionId, fileName);
  }

  public InvocationLog startProcessInstance(String actorId, Long definitionId)
      throws ExecutionException {
    return executionService.startProcessInstance(actorId, definitionId);
  }

  public InvocationLog startProcessInstance(String actorId, Long definitionId, Map variables)
      throws ExecutionException {
    return executionService.startProcessInstance(actorId, definitionId, variables);
  }

  public InvocationLog startProcessInstance(String actorId, Long definitionId,
                                            Map variables, String transitionName)
      throws ExecutionException {
    return executionService.startProcessInstance(actorId, definitionId, variables, transitionName);
  }

  public InvocationLog setVariables(String actorId, Long tokenId, Map variables)
      throws ExecutionException {
    return executionService.setVariables(actorId, tokenId, variables);
  }

  public InvocationLog endOfState(String actorId, Long tokenId)
      throws ExecutionException {
    return executionService.endOfState(actorId, tokenId);
  }

  public InvocationLog endOfState(String actorId, Long tokenId, Map variables)
      throws ExecutionException {
    return executionService.endOfState(actorId, tokenId, variables);
  }

  public InvocationLog endOfState(String actorId, Long tokenId,
                                  Map variables, String transitionName)
      throws ExecutionException {
    return executionService.endOfState(actorId, tokenId, variables, transitionName);
  }

  public InvocationLog cancelProcessInstance(String actorId, Long processInstanceId)
      throws ExecutionException {
    return executionService.cancelProcessInstance(actorId, processInstanceId);
  }

  public InvocationLog cancelToken(String actorId, Long flowId)
      throws ExecutionException {
    return executionService.cancelToken(actorId, flowId);
  }

  public InvocationLog undo(String actorId, Long processInstanceId, Date date)
      throws ExecutionException {
    return executionService.undo(actorId, processInstanceId, date);
  }
}
