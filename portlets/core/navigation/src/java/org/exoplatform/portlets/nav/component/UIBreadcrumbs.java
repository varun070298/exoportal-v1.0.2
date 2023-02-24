package org.exoplatform.portlets.nav.component;

import java.util.ResourceBundle;

import org.exoplatform.faces.user.component.UILanguageSelector;

/**
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/06 03:01:23 $
 * @author Fahrid Djebbari
 *  
 */

public class UIBreadcrumbs extends UINavigation {
  public UIBreadcrumbs(UILanguageSelector languageSelector, ResourceBundle res)
      throws Exception {
    super(res);
    setId("uibreadcrumbs");
    setRendererType("BreadcrumbsRenderer");
    getChildren().add(languageSelector);
  }

  public String getFamily() {
    return "org.exoplatform.portlets.nav.component.UIBreadcrumbs";
  }
}