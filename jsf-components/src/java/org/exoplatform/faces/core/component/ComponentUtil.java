/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.ValidatorManager;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Jan 19, 2005
 * @version $Id$
 */
public class ComponentUtil {
  
  static public List findDescendantsOfType(UIComponent comp, Class type) {
    List list = new ArrayList() ;
    findDescendantsOfType(comp, type, list) ;
    return list ;
  }
  
  static private void findDescendantsOfType(UIComponent uiComp, Class type, List list) {
    if(uiComp.getChildCount() == 0) return ;
    List children = uiComp.getChildren() ;
    for(int i = 0; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i) ;
      if(type.isInstance(child)) list.add(child) ;
      findDescendantsOfType(child, type,list) ;
    }
  }
  
  static public UIExoComponent findComponentById(UIExoComponent comp, String id) {
    if(id.equals(comp.getId())) return comp ; 
    List children = comp.getChildren();
    for (int i = 0; i < children.size() ; i++) {
      UIExoComponent uiChild = (UIExoComponent) children.get(i) ;
      UIExoComponent result =  findComponentById(uiChild, id) ;
      if (result != null) return result ;
    }
    return null ;
  }
  
  static public UIExoComponent findRenderedComponentById(UIExoComponent uiComp, String id) {
    if(id.equals(uiComp.getId())) return uiComp ; 
    List children = uiComp.getChildren();
    for (int i = 0; i < children.size() ; i++) {
      UIExoComponent uiChild = (UIExoComponent) children.get(i) ;
      if(uiChild.isRendered()) {
        UIExoComponent result = findRenderedComponentById(uiChild, id) ;
        if (result != null) return result ;
      }
    }
    return null ;
  }
  
  static public Object getChildComponentOfType(UIComponent comp, Class classType) {
    List children = comp.getChildren() ;
    for (int i = 0; i < children.size(); i++) {
      Object o = children.get(i)  ;
      if( classType.isInstance(o)) return o ;
    }
    return null ;
  }  
  
  static public UIComponent getAncestorOfType(UIComponent comp, Class classType) {
    UIComponent parent = comp.getParent() ;
    while(parent != null ) {
      if( classType.isInstance(parent)) return parent ;
      parent = parent.getParent() ;
    }
    return null ;
  }  
  
  static public void setRenderedComponent(UIExoComponent comp, String id) {
    List children = comp.getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if(child.getId().equals(id))  child.setRendered(true);
      else  child.setRendered(false) ;
    }
  }
  
  static public void setRenderedComponent(UIExoComponent comp, Class type) {
    List children = comp.getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child =  (UIComponent)children.get(i);
      if(type.isInstance(child))  child.setRendered(true);
      else   child.setRendered(false) ;
    }
  }
  
  static public void setRenderedSibling(UIExoComponent comp, Class type) {
    List children = comp.getParent().getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child =  (UIComponent)children.get(i);
      if(type.isInstance(child))  child.setRendered(true);
      else   child.setRendered(false) ;
    }
  }
  
  static public UIComponent getSibling(UIComponent comp, Class type) {
    List children = comp.getParent().getChildren() ;
    for(int i = 0 ; i < children.size(); i++) {
      UIComponent child =  (UIComponent)children.get(i);
      if(type.isInstance(child))  return child  ;
    }
    return null ;
  }
  
  static public void addValidator(List list, Class clazz) {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    ValidatorManager manager = 
      (ValidatorManager) pcontainer.getComponentInstanceOfType(ValidatorManager.class) ;
    Validator result = manager.getValidator(clazz) ;
    list.add(result) ;
  }
  
  static public Validator getValidator(Class clazz) {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    ValidatorManager manager = 
      (ValidatorManager) pcontainer.getComponentInstanceOfType(ValidatorManager.class) ;
    return manager.getValidator(clazz) ;
  }
}