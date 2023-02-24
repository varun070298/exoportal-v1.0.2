package org.exoplatform.services.workflow;


import java.sql.Connection;
import org.exoplatform.services.exception.ExoServiceException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 28 juin 2004
 */
public interface WorkflowServiceContainer {

  public WorkflowDefinitionService createWorkflowDefinitionService();

  public WorkflowExecutionService createWorkflowExecutionService();

}
