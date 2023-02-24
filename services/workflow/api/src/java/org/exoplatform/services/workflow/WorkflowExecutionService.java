package org.exoplatform.services.workflow;

import org.jbpm.model.definition.Definition;
import org.jbpm.model.execution.ProcessInstance;
import org.jbpm.model.execution.Token;
import org.jbpm.model.log.InvocationLog;
import org.jbpm.Assembler;
import org.jbpm.ExecutionException;

import java.util.Collection;
import java.util.Map;
import java.util.Date;
import java.util.List;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 9 mai 2004
 * <p/>
 * interfaces taken from JBPM
 */
public interface WorkflowExecutionService {

  public void close();

  public List getAllDefinitions();

  public List getAllDefinitions(Assembler assembler);

  public Collection getTaskList(String targetActorId, Assembler assembler);

  public Token getToken(Long tokenId, Assembler assembler);

  public ProcessInstance getProcessInstance(Long processInstanceId, Assembler assembler);

  public Map getVariables(Long tokenId);

  public Definition getDefinition(Long definitionId, Assembler assembler);

  public Definition getLatestDefinition(String name);

  public Collection getLatestDefinitions();

  public byte[] getFile(Long processDefinitionId, String fileName);

  public InvocationLog startProcessInstance(String actorId, Long definitionId)
      throws ExecutionException;

  public InvocationLog startProcessInstance(String actorId, Long definitionId, Map variables)
      throws ExecutionException;

  public InvocationLog startProcessInstance(String actorId, Long definitionId, Map variables, String transitionName)
      throws ExecutionException;

  public InvocationLog setVariables(String actorId, Long tokenId, Map variables)
      throws ExecutionException;

  public InvocationLog endOfState(String actorId, Long tokenId)
      throws ExecutionException;

  public InvocationLog endOfState(String actorId, Long tokenId, Map variables)
      throws ExecutionException;

  public InvocationLog endOfState(String actorId, Long tokenId,
                                  Map variables, String transitionName)
      throws ExecutionException;

  public InvocationLog cancelProcessInstance(String actorId, Long processInstanceId)
      throws ExecutionException;

  public InvocationLog cancelToken(String actorId, Long flowId)
      throws ExecutionException;

  public InvocationLog undo(String actorId, Long processInstanceId, Date date)
      throws ExecutionException;

}
