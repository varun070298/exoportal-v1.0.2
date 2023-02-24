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

import java.util.List;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 mai 2004
 */
public class UIBPDefinition extends UIExoCommand {
  public static String MANAGE_START = "manageStart";

  private WorkflowServiceContainer service_;

  public UIBPDefinition(WorkflowServiceContainer workflowServiceContainer) {
    setRendererType("BPDefinitionRenderer");
    setId("bp-definition");
    service_ = workflowServiceContainer;
    addActionListener(StartListener.class, MANAGE_START);
  }

  public String getFamily() {
    return "org.exoplatform.portlets.workflow.component.UIBPDefinition";
  }

  public List getAllDefinition()  {
    WorkflowExecutionService workflowExecutionService = null;
    try {
      workflowExecutionService = service_.createWorkflowExecutionService();
      return workflowExecutionService.getAllDefinitions();
    } finally {
      if(workflowExecutionService != null)
        workflowExecutionService.close();
    }

  }

  public static class StartListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      Long processId = new Long(Long.parseLong((String) event.getParameter("process")));
      UITask start = (UITask) ((UIBPDefinitionController) event.getComponent().getParent()).
          getChildComponentOfType(UITask.class);
      start.setIdentification(processId);
      start.updateUITree();
      ((UIBPDefinitionController) event.getComponent().getParent()).
          setRenderedComponent(start.getId());
    }
  }

}
