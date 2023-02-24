/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.component;

import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.workflow.WorkflowExecutionService;
import org.exoplatform.services.workflow.WorkflowServiceContainer;

import java.util.Collection;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 mai 2004
 */
public class UITaskList extends UIExoCommand {

  protected static Log log_ = getLog("org.exoplatform.portal.portlets.workflow") ;

  private static final String TASK_LIST_RENDERER = "TaskListRenderer";
  public static final String MANAGE_STATE = "manageState";
  private WorkflowServiceContainer workflowServiceContainer;

  public UITaskList(WorkflowServiceContainer workflowServiceContainer) {
    setRendererType(TASK_LIST_RENDERER);
    setId("task-list") ;
    this.workflowServiceContainer = workflowServiceContainer;
    addActionListener(StateListener.class, MANAGE_STATE);
	}

  public String getFamily() {
    return "org.exoplatform.portlets.workflow.component.UITaskList";
  }

  public Collection getTaskList(String actorId)  {
    WorkflowExecutionService executionService = null;
    try {
      executionService = workflowServiceContainer.createWorkflowExecutionService();
      return executionService.getTaskList(actorId, null);
    } finally {
      executionService.close();
    }
  }

  public static class StateListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      Long tokenId = new Long(Long.parseLong((String) event.getParameter("token")));
      log_.debug("Manage state : " + tokenId);
      UITask task = (UITask) ((UITaskListController)event.getComponent().getParent()).
          getChildComponentOfType(UITask.class);
      task.setIdentification(tokenId);
      task.updateUITree();
      ((UITaskListController)event.getComponent().getParent()).
          setRenderedComponent(task.getId());
    }
  }

}
