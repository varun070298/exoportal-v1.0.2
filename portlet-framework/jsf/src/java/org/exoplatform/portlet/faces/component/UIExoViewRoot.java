/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces.component;

import java.util.* ;
import java.io.IOException;
import javax.faces.component.UIViewRoot ;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.UIComponentFactory;
import org.exoplatform.faces.core.component.UIExoComponent;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 9, 2003 2:27:49 PM
 *
 */
public class UIExoViewRoot extends UIViewRoot {
  private boolean isComponentView_ ;
  
  public UIExoViewRoot(String viewId) {
    super() ;
    isComponentView_ = false ;
    setViewId(viewId) ;
    if (viewId.endsWith(".class")) {
      String clazz = viewId.substring(0, viewId.length() - 6) ;
      getChildren().add(createComponent(clazz)) ;
      isComponentView_ = true ;
    }
  }

  public boolean isComponentView() { return isComponentView_ ; }
  
  static public UIComponent createComponent(String clazz) {
    try {
    	UIComponentFactory factory = new UIComponentFactory() ;
      UIComponent component =	(UIComponent) factory.createUIComponent(clazz);
      return component ;
    } catch (Throwable t) {
      t.printStackTrace() ;
    }
    return null ;
  }

  public void renderChildren( FacesContext context) throws IOException {
    Iterator iterator = getChildren().iterator();
    while (iterator.hasNext()) {
      Object o = iterator.next() ;
      if(o instanceof UIExoComponent) {
        UIExoComponent child = (UIExoComponent) o ;
        child.decorate(context) ;
      } else {
        UIComponent child =(UIComponent) o ;
        child.encodeBegin(context);
        child.encodeChildren(context);
        child.encodeEnd(context);
      }
    }
  }
}