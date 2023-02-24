/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.delegation;

import org.jbpm.model.definition.Delegation;
import org.jbpm.delegation.ActionHandler;
import org.jbpm.delegation.ExecutionContext;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 12 mai 2004
 */
public class MockActionHandler implements ActionHandler {
  public void execute(ExecutionContext executionContext) {
    System.out.println("MockActionHandlerCalled");
  }
}
