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
public class UIBPDefinitionController extends UICommandNode{

  public UIBPDefinitionController(UIBPDefinition uibpDefinition, UITask uiTask) throws Exception {
    setId("bd-definition-controller") ;
    setName("Business Processes");
    setRendererType("ChildrenRenderer");
    setClazz("UIBPDefinitionController");
    List children = getChildren() ;
    uibpDefinition.setRendered(true) ;
    children.add(uibpDefinition) ;
    uiTask.setIsStart(true);
    uiTask.setRendered(false) ;
    children.add(uiTask) ;
  }

}