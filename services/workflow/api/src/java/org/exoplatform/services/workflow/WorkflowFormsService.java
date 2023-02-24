package org.exoplatform.services.workflow;

import org.jbpm.model.definition.Definition;

import java.util.Locale;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public interface WorkflowFormsService {

  public void addForms(Definition definition,
                       WorkflowExecutionService executionService,
                       Locale locale);

  public Form getForm(Long definitionId, String stateName);

}
