/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.processes.holidays;

import org.jbpm.delegation.AssignmentHandler;
import org.jbpm.delegation.AssignmentContext;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 12 mai 2004
 */
public class BossAssignementHandler implements AssignmentHandler{

  public String selectActor(AssignmentContext assignmentContext) {
    //System.out.println("In selectActor of BossAssignementHandler : ");
    //System.out.println("  --> Previous actor : " + assignmentContext.getPreviousActorId());
    if("benj".equals(assignmentContext.getPreviousActorId()) ||
        "bossOfBenj".equals(assignmentContext.getPreviousActorId())){
      //System.out.println("  --> Next actor : bossOfBenj");
      return "bossOfBenj";
    }
    return assignmentContext.getPreviousActorId();
  }
}
