/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.impl;

import org.dom4j.Element;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.workflow.Form;
import org.exoplatform.services.workflow.WorkflowExecutionService;
import org.exoplatform.services.workflow.format.DefaultFormat;
import org.jbpm.util.xml.Dom4jHelper;
import org.apache.commons.logging.Log;

import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.Format;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public class FormImpl implements Form{

  private String stateName;
  private List variableFormats;
  private List submitButtons;

  private ResourceBundle resourceBundle;
  private Log log;

  public FormImpl(Long definitionId, Element formElement, WorkflowExecutionService executionService,
                  Locale locale) {

    this.log = ((LogService)PortalContainer.getInstance().getComponentInstanceOfType(LogService.class)).
        getLog("org.exoplatform.services.workflow");
    String formFileName = Dom4jHelper.getElementText(formElement, "resource-bundle", null);

    //manage properties
    String localisedFileName = getLocalisedString(formFileName, locale);
    log.debug("Try to find localised resource : " + localisedFileName);
    byte[] bytes = executionService.getFile(definitionId, localisedFileName);
    if(bytes == null) {
      log.debug("Try to find default resource : " + formFileName + ".properties");
      bytes = executionService.getFile(definitionId, formFileName + ".properties");
    }
    if(bytes != null)
     log.debug("resource bundle found true");
    else
      log.debug("resource bundle found false");

    try {
      resourceBundle = new PropertyResourceBundle(new ByteArrayInputStream(bytes));
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.stateName = Dom4jHelper.getElementText(formElement, "state-name", null);

    initializeVariableFormats(formElement);
    initializeSubmitButtons(formElement);
  }

  private String getLocalisedString(String fileName, Locale locale) {
    return fileName + "_" + locale.getLanguage() + ".properties";
  }

  private void initializeVariableFormats(Element formElement) {
    this.variableFormats = new ArrayList();
    Iterator iter = formElement.elements("variable").iterator();
    while (iter.hasNext()) {
      Element variableElement = (Element) iter.next();

      String variableName =
        Dom4jHelper.getAttribute(variableElement, "name", null);
      String formatClassName =
        Dom4jHelper.getAttribute(variableElement, "format", null);
      Format variableFormat = new DefaultFormat();
      if (formatClassName != null) {
        try {
          variableFormat = (Format) Thread.currentThread().getContextClassLoader().
              loadClass(formatClassName).newInstance();
        } catch (Exception e) {
          //log.error("couldn't instante variable formatter '" + formatClassName + "'", e);
        }
      }
      this.variableFormats.add(new Attribute(variableName, variableFormat));
    }
  }

  private void initializeSubmitButtons(Element formElement) {
    this.submitButtons = new ArrayList();
    Iterator iter = formElement.elements("submitbutton").iterator();
    while (iter.hasNext()) {
      Element submitButtonElement = (Element) iter.next();
      String value = Dom4jHelper.getAttribute(submitButtonElement, "value", null);
      String transitionName = Dom4jHelper.getAttribute(submitButtonElement, "transition-name", null);
      this.submitButtons.add(new Attribute(value, transitionName));
    }
  }

  public List getVariableFormats() {
    return variableFormats;
  }

  public List getSubmitButtons() {
    return submitButtons;
  }

  public String getStateName() {
    return stateName;
  }

  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

}
