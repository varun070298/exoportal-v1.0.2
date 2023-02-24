/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.impl;

import org.jbpm.model.definition.Definition;
import org.jbpm.util.xml.Dom4jHelper;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.exoplatform.services.workflow.*;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public class WorkflowFormsServiceImpl implements WorkflowFormsService {

  /**
   * double nested map : definitionId's --> stateName's --> Form's
   */
  private static Map allForms = new HashMap();

  public void addForms(Definition definition,
                       WorkflowExecutionService executionService,
                       Locale locale) {
    if (!allForms.containsKey(definition.getId())) {
      Map stateNameToForms = new HashMap();
      byte[] bytes = executionService.getFile(definition.getId(), "forms.xml");
      Iterator iter = null;
      try {
        iter = Dom4jHelper.getRootElement(bytes).elements("form").iterator();
      } catch (DocumentException e) {
        e.printStackTrace();
      }
      while (iter.hasNext()) {
        Element formElement = (Element) iter.next();
        Form formConfiguration = new FormImpl(definition.getId(), formElement,
            executionService, locale);
        stateNameToForms.put(formConfiguration.getStateName(),
            formConfiguration);
      }

      allForms.put(definition.getId(), stateNameToForms);
    }
  }

  public Form getForm(Long definitionId, String stateName) {
    Form formConfiguration = null;
    if (definitionId == null)
      throw new NullPointerException("definitionId is null in Form.getForm");
    if (stateName == null)
      throw new NullPointerException("stateName is null in Form.getForm");

    Map stateNameToForms = (Map) allForms.get(definitionId);
    if (stateNameToForms == null)
      throw new IllegalArgumentException("forms for definition '" + definitionId +
          "' were not yet initialised");
    formConfiguration = (Form) stateNameToForms.get(stateName);
    if (stateNameToForms == null)
      throw new IllegalArgumentException("no form was specified for state '"
          + stateName + "' in definition '" + definitionId + "'");

    return formConfiguration;
  }
}
