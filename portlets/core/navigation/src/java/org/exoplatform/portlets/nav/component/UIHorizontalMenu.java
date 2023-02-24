package org.exoplatform.portlets.nav.component;

import java.util.ResourceBundle;

/**
 * @author Fahrid Djebbari
 * @version $Revision: 1.3 $ $Date: 2004/09/07 13:25:42 $
 */

public class UIHorizontalMenu extends UINavigation {
  public UIHorizontalMenu(ResourceBundle res) throws Exception {
    super(res);
    setId("uihorizontalmenu");
    setRendererType("HorizontalMenuRenderer");
  }

  public String getFamily() {
    return "org.exoplatform.portlets.nav.component.UIHorizontalMenu";
  }
}
