/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.component;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.workflow.*;
import org.jbpm.ExecutionException;
import org.jbpm.model.execution.Token;

import javax.faces.context.FacesContext;
import java.text.Format;
import java.text.ParseException;
import java.util.*;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public class UITask extends UISimpleForm {
  public static final String PREFIX = "prefix";
  public static final String START_PROCESS = PREFIX + "startProcess";
  public static final String END_OF_STATE = PREFIX + "endOfState";
  public static final String CANCEL_PROCESS = PREFIX + "cancelProcess";
  public static final String MANAGE_TRANSITION = "manageTransition";

  protected static Log log_ = getLog("org.exoplatform.portal.portlets.workflow");

  private ResourceBundle res;
  private WorkflowFormsService formsService;
  private Form form;
  private UIStringInput[] inputs;
  private boolean isStart;
  private Long identification;
  private WorkflowServiceContainer workflowServiceContainer;

  public UITask(WorkflowServiceContainer workflowServiceContainer,
                WorkflowFormsService workflowFormsService,
                ResourceBundle resourceBundle) throws Exception {
    super("taskForm", "post", null);
    setRendererType("SimpleFormRenderer");
    setClazz("UITask");
    setId("UITask");

    res = resourceBundle;
    this.workflowServiceContainer = workflowServiceContainer;
    this.formsService = workflowFormsService;

    addActionListener(StartListener.class, START_PROCESS);
    addActionListener(EndListener.class, END_OF_STATE);
    addActionListener(TransitionListener.class, MANAGE_TRANSITION);
    addActionListener(CancelListener.class, CANCEL_PROCESS);
  }

  public void updateUITree() {
    clear();
    Map variables = new HashMap();
    WorkflowExecutionService executionService = workflowServiceContainer.createWorkflowExecutionService();
    if (isStart) {
      String startStateName = executionService.
          getDefinition(identification, null).getStartState().getName();
      form = formsService.getForm(identification, startStateName);
    } else {
      Token token = executionService.getToken(identification, null);
      variables = executionService.getVariables(identification);
      form = formsService.getForm(token.getState().getDefinition().getId(),
          token.getState().getName());
    }
    executionService.close();

    add(new HeaderRow().add(new LabelCell(form.getResourceBundle().getString("title")).
        addColspan("2")));

    List attributes = form.getVariableFormats();
    int nbVar = form.getVariableFormats().size();

    inputs = new UIStringInput[nbVar];
    int i = 0;
    for (Iterator iterator = attributes.iterator(); iterator.hasNext(); i++) {
      Form.Attribute attribute = (Form.Attribute) iterator.next();
      log_.debug("variable : " + attribute.getName());
      Object value = variables.get(attribute.getName());
      String text = "";
      if (value != null) {
        Format format = (Format) attribute.getValue();
        text = format.format(value);
      }
      inputs[i] = new UIStringInput(StringUtils.replace(attribute.getName(), ".", "_"),
          text);
      add(new Row().
          add(new LabelCell(form.getResourceBundle().
          getString(attribute.getName() + ".label"))).
          add(new ComponentCell(this, inputs[i])));
    }
    List submitButtons = form.getSubmitButtons();
    ListComponentCell cells = new ListComponentCell();
    if (submitButtons.isEmpty()) {
      if (isStart) {
        cells.add(new FormButton(form.getResourceBundle().
            getString("submit"), START_PROCESS));
      } else {
        cells.add(new FormButton(form.getResourceBundle().
            getString("submit"), END_OF_STATE));
      }
    } else {
      for (Iterator iterator = submitButtons.iterator(); iterator.hasNext();) {
        Form.Attribute attribute = (Form.Attribute) iterator.next();
        cells.add(new FormButton(form.getResourceBundle().
            getString(attribute.getName() + ".submit"), attribute.getName()));
      }
    }
    cells.add(new FormButton("#{UITask.cancel}",
        CANCEL_PROCESS)).addAlign("center");
    add(new Row().add(cells.addColspan("2")));
  }

  public void setIdentification(Long identification)  {
    this.identification = identification;
    if (isStart) {
      WorkflowExecutionService executionService = workflowServiceContainer.createWorkflowExecutionService();
      formsService.addForms(executionService.getDefinition(identification, null),
          executionService, res.getLocale());
      executionService.close();
    }
  }

  public static class StartListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITask uiTask = (UITask) event.getComponent();
      String remoteUser = getRemoteUser();
      Map variables = prepareVariables(uiTask.form, uiTask.inputs);
      WorkflowExecutionService executionService = uiTask.workflowServiceContainer.createWorkflowExecutionService();
      executionService.startProcessInstance(remoteUser,
          uiTask.identification, variables);
      executionService.close();
      ((UIBPDefinitionController) uiTask.getParent()).setRenderedComponent(UIBPDefinition.class);
    }
  }

  public static class EndListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITask uiTask = (UITask) event.getComponent();
      Map variables = prepareVariables(uiTask.form, uiTask.inputs);
      String remoteUser = getRemoteUser();
      try {
        log_.debug("Actor ID : " + remoteUser);
        WorkflowExecutionService executionService = uiTask.workflowServiceContainer.createWorkflowExecutionService();
        executionService.endOfState(remoteUser, uiTask.identification, variables);
        executionService.close();
      } catch (Exception ex) {
        log_.error("Cannot resolve state", ex);
      }
      ((UITaskListController) uiTask.getParent()).
          setRenderedComponent(UITaskList.class);
    }
  }

  public static class CancelListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UITask uiTask = (UITask) event.getComponent();
      if (uiTask.isStart) {
        ((UIBPDefinitionController) uiTask.getParent()).setRenderedComponent(UIBPDefinition.class);
      } else {
        ((UITaskListController) uiTask.getParent()).setRenderedComponent(UITaskList.class);
      }
    }
  }

  public static class TransitionListener extends ExoActionListener {

    public boolean canHandleAction(String action) {
      return !action.startsWith(PREFIX);
    }

    public void execute(ExoActionEvent event) throws Exception {
      UITask uiTask = (UITask) event.getComponent();
      Map variables = prepareVariables(uiTask.form, uiTask.inputs);
      String remoteUser = getRemoteUser();
      List submitButtons = uiTask.form.getSubmitButtons();
      for (Iterator iterator = submitButtons.iterator(); iterator.hasNext();) {
        Form.Attribute attribute = (Form.Attribute) iterator.next();
        if (event.getAction().equals(attribute.getName())) {
          String transition = (String) extractAttribute(submitButtons,
              attribute.getName()).getValue();
          try {
            WorkflowExecutionService executionService = uiTask.workflowServiceContainer.
                createWorkflowExecutionService();
            executionService.endOfState(remoteUser, uiTask.identification,
                variables, transition);
            executionService.close();
          } catch (ExecutionException e) {
            log_.error("Cannot resolve state", e);
          }
        }
      }
      ((UITaskListController) uiTask.getParent()).setRenderedComponent(UITaskList.class);
    }
  }

  private static String getRemoteUser() {
    String remoteUser = FacesContext.getCurrentInstance().
        getExternalContext().getRemoteUser();
    if (remoteUser == null)
      remoteUser = "anonymous";
    return remoteUser;
  }

  private static Map prepareVariables(Form form, UIStringInput[] inputs) {
    Map variables = new HashMap();
    Format format = null;
    for (int i = 0; i < inputs.length; i++) {
      UIStringInput input = inputs[i];
      String name = StringUtils.replace(input.getName(), "_", ".");
      log_.debug("input name : " + name);
      format = (Format) extractAttribute(form.getVariableFormats(), name).getValue();
      String value = input.getValue();
      log_.debug("input value : " + value);
      try {
        variables.put(name, format.parseObject(value));
      } catch (ParseException e) {
        log_.error("can not format object : " + value, e);
      }
    }
    return variables;
  }


  private static Form.Attribute extractAttribute(List list, String name) {
    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
      Form.Attribute attribute = (Form.Attribute) iterator.next();
      if (attribute.getName().equals(name))
        return attribute;
    }
    return null;
  }

  public void setIsStart(boolean b) {
    isStart = b;
  }


}
