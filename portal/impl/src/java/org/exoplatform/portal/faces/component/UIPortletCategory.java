/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.component;

import org.exoplatform.faces.core.component.*;
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 juin 2004
 */
public class UIPortletCategory extends UIExoCommand {

	protected PortletCategoryData categoryData ;

  public UIPortletCategory() {
    setRendererType("PortletCategoryRenderer");
    setId("UIPortletCategory");
	}

  public String getFamily() {
    return "org.exoplatform.portal.faces.component.UIPortletCategory";
  }

  public PortletCategoryData getPortletCategoryData() {
    return categoryData;
  }

  public void setPortletCategoryData(PortletCategoryData portletCategoryData) {
    this.categoryData = portletCategoryData;
  }
}