/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.component;

import org.exoplatform.faces.core.component.UIFileUpload;
import org.exoplatform.faces.core.component.UIPortlet;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.workflow.WorkflowDefinitionService;
import org.exoplatform.services.workflow.WorkflowServiceContainer;
import org.jbpm.JpdlException;

import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarInputStream;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 mai 2004
 */
public class UIWorkflowEdit extends UIPortlet{

	public static String COMPONENT_ID = "workflow-edit";
  private static WorkflowServiceContainer workflowServiceContainer;

  public UIWorkflowEdit(WorkflowServiceContainer serviceContainer,
                        UIFileUpload uiFileUpload) {
    this.setRendererType("ChildrenRenderer");
		this.setId(COMPONENT_ID);
    workflowServiceContainer = serviceContainer;
    List children = getChildren();
    uiFileUpload.setClazz("UIFileUpload");
    uiFileUpload.setNumberOfField(1);
    uiFileUpload.setRendered(true) ;
    children.add(uiFileUpload);

  	uiFileUpload.addActionListener(ProcessUploadListener.class, UIFileUpload.UPLOAD_ACTION) ;
    uiFileUpload.addActionListener(CancelListener.class, UIFileUpload.CANCEL_ACTION) ;
  }
  
  public static class ProcessUploadListener extends ExoActionListener{
    public void execute(ExoActionEvent event) throws Exception {
      UIFileUpload uiFileUpload = (UIFileUpload) event.getComponent();
      Collection processes = uiFileUpload.getUserInputs();
      for (Iterator iterator = processes.iterator(); iterator.hasNext();) {
        UIFileUpload.UserInput userInput = (UIFileUpload.UserInput) iterator.next();
        WorkflowDefinitionService workflowDefinitionService = workflowServiceContainer.
            createWorkflowDefinitionService();
        try {
          JarInputStream iS = new JarInputStream(new ByteArrayInputStream(userInput.getStream()));
          workflowDefinitionService.deployProcessArchive(iS);
        } catch (IOException e) {
          e.printStackTrace();
        } catch (JpdlException e) {
          e.printStackTrace(); 
        } finally {
          workflowDefinitionService.close();
        }
      }
      ActionResponse actionResponse = (ActionResponse) FacesContext.getCurrentInstance().
          getExternalContext().getResponse();
      actionResponse.setPortletMode(PortletMode.VIEW);
    }
  }

  public static class CancelListener extends ExoActionListener{
    public void execute(ExoActionEvent event) throws Exception {
      ActionResponse actionResponse = (ActionResponse) FacesContext.getCurrentInstance().
          getExternalContext().getResponse();
      actionResponse.setPortletMode(PortletMode.VIEW);
    }
  }

}
