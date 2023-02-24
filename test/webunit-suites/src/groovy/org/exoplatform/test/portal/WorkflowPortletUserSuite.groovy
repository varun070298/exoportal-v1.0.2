/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.test.portal;

import org.exoplatform.test.web.WebUnitSuite;
import org.exoplatform.test.web.unit.ClickLink;
import org.exoplatform.test.web.unit.ClickLinkWithText;
import org.exoplatform.test.web.unit.ClickURLLinkUnit;
import org.exoplatform.test.web.unit.SubmitFormUnit;
import org.exoplatform.test.web.validator.ExpectLinkWithTextValidator;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 juin 2004
 */
public class WorkflowPortletUserSuite  extends WebUnitSuite {
  
  public WorkflowPortletUserSuite(String path) {
    super("WorkflowAdminSuite", "Go to the workflow portlet page and upload process");
    goToPage(path);
    addWebUnit(
        new ClickLink("StartHolidayProcess", "Start the holiday process").
        setTextLink("Start Process")
    );
    
    addWebUnit(
        new SubmitFormUnit("CancelStartProcessForm", "Cancel the process start form fill").
        setFormName("taskForm").
        setField("op", "prefixcancelProcess").
        addValidator(new ExpectLinkWithTextValidator("Start Process"))
    );
    
    addWebUnit(
        new ClickLinkWithText("StartHolidayProcess", "Start the holiday process").
        setTextLink("Start")
    );
    
    addWebUnit(
        new SubmitFormUnit("StartProcessForm", "Fill and submit the start process form").
        setFormName("taskForm").
        setField("start_date", "01/06/1978").
        setField("end_date", "03/06/1978").
        setField("op", "prefixstartProcess").
        addValidator(new ExpectLinkWithTextValidator("Start"))
    );
    
    addWebUnit(
        new ClickURLLinkUnit("ClickTaskListTab", "Click on the task list tab").
        setURL("op=selectTab&tabId=task-list-controller").
        setBlockId("workflow-demo").
        addValidator(new ExpectLinkWithTextValidator("Manage"))
    );
    
    addWebUnit(
        new ClickLinkWithText("ManageEvaluationTask", "Evaluate the holiday request").
        setTextLink("Manage")
    );
    
    /*      addWebUnit(new SubmitFormUnit("EvaluationForm", "Fill and submit the evaluation holidays task form").
     setFormName("taskForm").
     setField("requester", "exo").
     setField("start_date", "01/06/1978").
     setField("end_date", "03/06/1978").
     setField("op", "approve").
     addValidator(new ExpectTextValidator("assign replacement")).
     addValidator(new ExpectTextValidator("hr acknowledgement")).
     addValidator(new ExpectLinkWithTextValidator("Manage")));
     
     addWebUnit(new ClickLinkWithText("AcknowledgeTheHRTask", "Acknowledge the Human Resource department").
     setTextLink("Manage").
     addValidator(new ExpectTextValidator("HR acknoledgement")));
     
     addWebUnit(new SubmitFormUnit("AcknowledgeTheHRForm", "Fill and submit the acknowledgment replacement task form").
     setFormName("taskForm").
     setField("requester", "exo").
     setField("start_date", "01/06/1978").
     setField("end_date", "03/06/1978").
     setField("op", "prefixendOfSate").
     addValidator(new ExpectTextValidator("assign replacement")).
     addValidator(new ExpectLinkWithTextValidator("Manage")));
     
     addWebUnit(new ClickLinkWithText("ManageAssignReplacementTask", "Manage the replacement task management").
     setTextLink("Manage").
     addValidator(new ExpectTextValidator("Assign replacement")));
     
     addWebUnit(new SubmitFormUnit("AssignReplacementForm", "Fill and submit the assign replacement task form").
     setFormName("taskForm").
     setField("requester", "exo").
     setField("start_date", "01/06/1978").
     setField("end_date", "03/06/1978").
     setField("op", "prefixendOfSate").
     addValidator(new ExpectTextValidator("No task waiting for you...")));
     
     */
  }
}
