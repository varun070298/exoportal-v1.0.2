/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.workflow.delegation;

import org.jbpm.delegation.AssignmentHandler;
import org.jbpm.delegation.AssignmentContext;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 13 mai 2004
 */
public class HRAssignementHandler implements AssignmentHandler{

  public String selectActor(AssignmentContext assignmentContext) {
    System.out.println("In selectActor of HRAssignementHandler : ");
    System.out.println("  --> Previous actor : " + assignmentContext.getPreviousActorId());
    if("bossOfBenj".equals(assignmentContext.getPreviousActorId())){
      System.out.println("  --> Next actor : Hrofbenj");
      return "Hrofbenj";
    }
    return "";
  }

}