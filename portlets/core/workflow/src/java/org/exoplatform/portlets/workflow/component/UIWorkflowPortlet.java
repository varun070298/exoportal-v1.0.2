/***************************************************************************

 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *

 * Please look at license.txt in info directory for more license detail.   *

 **************************************************************************/

package org.exoplatform.portlets.workflow.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;

/**
 * @author: Benjamin Mestrallet
 * @email: benjamin.mestrallet@exoplatform.com
 */

public class UIWorkflowPortlet extends UIPortlet {

  public static final String ANONYMOUS_USER = "anonymous";

  public UIWorkflowPortlet(UIBPDefinitionController uibpDefinitionController,
      UITaskListController uiTaskListController) throws Exception {
    setRendererType("SimpleTabRenderer");
    setClazz("UIWorkflowPortlet");
    List children = getChildren();
    uibpDefinitionController.setRendered(true);
    children.add(uibpDefinitionController);
    uiTaskListController.setRendered(false);
    children.add(uiTaskListController);
  }

}