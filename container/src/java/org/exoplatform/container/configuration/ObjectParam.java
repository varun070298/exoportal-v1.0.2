/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.util.* ;
import org.apache.commons.beanutils.PropertyUtils ;
/**
 * Jul 19, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: ObjectParam.java,v 1.1 2004/10/28 15:35:23 tuan08 Exp $
 */
public class ObjectParam extends Parameter {
  private String type_ ;
  private String package_ ;
	private Object  object_  ;
  private ArrayList properties_ = new ArrayList() ;
  
  public  String getType() { return type_  ;} 
  public  void   setType(String s) {  
    type_ = s; 
    int idx = type_.lastIndexOf(".") ;
    if(idx > 0 ) {
      package_ = type_.substring(0, idx) ;
    }
  }
  
  
  public Object getObject() {
    if(object_ == null) {
      populateBean() ;
    }
    return object_ ;   
  }
  
  public void addProperty(String name , String value)  {
    properties_.add(new Property(name, value)) ;
  }
  
  private void populateBean() { 
    Property prop = null ;
    try {
      Class clazz = Class.forName(type_) ;
      object_ =  clazz.newInstance() ;
      for(int i = 0 ; i < properties_.size() ; i++) {
        prop = (Property) properties_.get(i) ;
        if(prop.name_.endsWith("]")) {
          //arrary or list
          populateBeanInArray(object_ , prop.name_ , prop.value_) ;
        } else {
          Object valueBean = getValue(prop.value_) ;
          PropertyUtils.setProperty(object_, prop.name_, valueBean) ;
        }
      }
    } catch(Throwable ex) {
      if(prop != null) {
        //System.out.println("Exception when try setting the prop.name " + prop.name_ + 
        //                   ", value prop.value " + prop.value_) ;
      }
      ex.printStackTrace() ;
    }
  }
  
  private void populateBeanInArray(Object bean, String name , String value) throws Exception  {
    int idx = name.lastIndexOf("[") ;
    String arrayBeanName = name.substring(0, idx) ;
    int index = Integer.parseInt(name.substring(idx + 1, name.length() - 1)) ;
    Object arrayBean = PropertyUtils.getProperty(bean, arrayBeanName) ;
    if(arrayBean instanceof List) {
      List list = (List) arrayBean ;
      Object valueBean = getValue(value) ;
      if(list.size() == index) {
        list.add(valueBean) ;
      } else {
        list.set(index, valueBean) ;
      }
    } else  if(arrayBean instanceof Collection) {
      Collection c = (Collection) arrayBean ;
      Object valueBean = getValue(value) ;
      c.add(valueBean) ;
    } else {
      Object[] array = (Object[]) arrayBean ;
      array[index] = getValue(value) ;
    }
  }
  
  private  Object getValue(String value) throws Exception {
    if(value.startsWith("#new")) {
      String[] temp = value.split(" ") ;
      String className = temp[1] ;
      if(className.indexOf(".") < 0) {
        className = package_ + "." + className ;
        Class clazz = Class.forName(className) ;
        return clazz.newInstance() ;
      }
    } else if(value.startsWith("#int")) {
      String[] temp = value.split(" ") ;
      value = temp[1].trim() ;
      return new Integer(value) ;
    } else if(value.startsWith("#boolean")) {
      String[] temp = value.split(" ") ;
      value = temp[1].trim() ;
      return new Boolean("true".equals(value)) ;
    }
    return value ;
  }
  
  static class Property {
    String name_ ;
    String value_ ;
    
    public Property(String name, String value) {
      name_ = name;
      value_ = value ;
    }
  }
}