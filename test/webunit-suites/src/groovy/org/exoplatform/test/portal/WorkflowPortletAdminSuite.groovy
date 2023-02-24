/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.test.portal;

import org.exoplatform.test.web.WebUnitSuite;
import org.exoplatform.test.web.unit.ClickURLLinkUnit;
import org.exoplatform.test.web.unit.SubmitFormUnit;
import org.exoplatform.test.web.validator.ExpectLinkWithTextValidator;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 juin 2004
 */
public class WorkflowPortletAdminSuite  extends WebUnitSuite{
  
  public WorkflowPortletAdminSuite(String path) {
    super("WorkflowAdminSuite", "Go to the workflow portlet page and upload process");
    goToPage(path);
    addWebUnit(
        new ClickURLLinkUnit("ClickWorkflowEditMode", "Click on the workflow edit mode of the workflow portlet").
        setURL("portal:componentId=workflow&portal:portletMode=edit").
        setBlockId("workflow")
    );
    addWebUnit(
        new SubmitFormUnit("CancelProcessUploadForm", "Cancel the form fill").
        setFormName("uploadForm").
        setField("op", "cancel")
    );
    addWebUnit(
        new ClickURLLinkUnit("ClickWorkflowEditMode", "Click on the workflow edit mode of the workflow portlet").
        setURL("portal:componentId=workflow&portal:portletMode=edit").
        setBlockId("workflow")
    );
    addWebUnit(
        new SubmitFormUnit("SubmitProcessUploadForm", "Fill the form with the business process archive to upload").
        setFormName("uploadForm").
        setFileField("file-1", "exoplatform.business-process.holiday-1.0.par",
                     getClass().getClassLoader().
                     getResourceAsStream("exoplatform.business-process.holiday-1.0.par")).
        setField("op", "upload").
        addValidator(new ExpectLinkWithTextValidator("Start"))
    );
  }
}
