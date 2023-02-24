/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.workflow.component;


import java.util.List;
import org.exoplatform.faces.core.component.UICommandNode;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 mai 2004
 */
public class UITaskListController extends UICommandNode {

  public UITaskListController(UITaskList uiTaskList, UITask uiTask) throws Exception {
    setId("task-list-controller") ;
    setName("Tasks");
    setRendererType("ChildrenRenderer");
    setClazz("UITaskListController");
    List children = getChildren() ;
    uiTaskList.setRendered(true) ;
    children.add(uiTaskList) ;
    uiTask.setIsStart(false);
    uiTask.setRendered(false) ;
    children.add(uiTask) ;
  }

}
