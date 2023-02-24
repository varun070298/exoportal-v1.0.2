/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import java.util.List;
import org.exoplatform.faces.core.component.UIPortlet;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class UIPortletRegistry extends UIPortlet {

  public UIPortletRegistry(UIPortletCategories uiPortletCategories ,
                            UIPortletCategoryForm uiPortletCategoryForm,
														UIPortletForm uiPortletForm,
														UIPortletList uiPortletList,
														UIPortletRole uiPortletRole) {
    setRendererType("ChildrenRenderer");
    setId("UIPortletRegistry");
    List children = getChildren();
    uiPortletCategories.setRendered(true);
    children.add(uiPortletCategories);
    uiPortletCategoryForm.setRendered(false);
    children.add(uiPortletCategoryForm);
    uiPortletForm.setRendered(false);
    children.add(uiPortletForm);
    uiPortletList.setRendered(false);
    children.add(uiPortletList);
    uiPortletRole.setRendered(false);
    children.add(uiPortletRole);
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletRegistry" ;
  }
}