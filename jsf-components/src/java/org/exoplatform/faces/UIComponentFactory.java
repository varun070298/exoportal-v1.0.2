/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces;

import java.util.* ;
import java.lang.reflect.* ;
import javax.faces.component.UIComponent ;
import javax.faces.context.FacesContext ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.context.*;
import org.exoplatform.faces.core.component.UIExoComponent;
/**
 * Jun 2, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIComponentFactory {
  private ResourceBundle res_ ;
  
  public UIComponentFactory(ResourceBundle res) {
    res_ = res ;
  }
  
  public UIComponentFactory() {
    FacesContext context = FacesContext.getCurrentInstance() ;
    PortletExternalContext econtext = 
      (PortletExternalContext) context.getExternalContext() ;
    res_ = econtext.getApplicationResourceBundle() ;
  }
  
  final public Object createUIComponent(String className) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
    Class clazz = cl.loadClass(className) ;
    return createUIComponent(clazz) ;
  }
  
  final public Object createUIComponent(Class clazz) throws Exception {
    PortalContainer manager = PortalContainer.getInstance()  ;
    return createUIComponent(clazz, manager) ;
  }
  
  private Object createUIComponent(Class clazz, PortalContainer manager) throws Exception {
    Constructor constructor = findBiggestConstructor(clazz) ;
    Class[] parameters = constructor.getParameterTypes() ;
    Object[] args = new Object[parameters.length] ;
    for (int i = 0; i < args.length; i++) {
      if(UIComponent.class.isAssignableFrom(parameters[i])) {
        args[i] = createUIComponent(parameters[i], manager) ;
      } else if(ResourceBundle.class.isAssignableFrom(parameters[i])) {
        args[i] = res_ ;
      } else {
        args[i] = manager.getComponentInstanceOfType(parameters[i]) ;
        if (args[i] == null) {
          throw new Exception("Cannot find the service " + parameters[i].getName()) ;
        }
      }
    }
    Object result = constructor.newInstance(args) ;
    return result ;
  }
  
  private Constructor findBiggestConstructor(Class clazz) {
    Constructor bc = null ;
    Constructor[] constructors = clazz.getConstructors() ;
    for(int i = 0 ; i < constructors.length; i++) {
      if(bc == null) {
        bc = constructors[i] ;
      } else if(bc.getParameterTypes().length < constructors[i].getParameterTypes().length) {
        bc = constructors[i] ;
      }
    }
    return bc  ;
  }
  
  static public UIExoComponent createComponent(Class cl) throws Exception {
    String className = cl.getName() ;
    int idx = className.lastIndexOf('.') ;
    className = className.substring(idx + 1, className.length()) ;
    SessionContainer scontainer = SessionContainer.getInstance() ;
    UIExoComponent uiComponent =  (UIExoComponent)scontainer.createComponent(cl) ;
    uiComponent.setId(className) ;
    return uiComponent ;
  }
}
