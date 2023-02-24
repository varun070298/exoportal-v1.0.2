/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

import java.lang.reflect.Method;
import java.util.* ; 
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class ReflectionBeanDataHandler implements DataHandler {
  private static Object[] EMPTY_ARGS  = {} ;
  private static Map cache_ = new HashMap(300) ;
  
  private Object bean_ ;
  private Map methodMap_ ;
  private Class type_ ;
  
  public ReflectionBeanDataHandler(Class beanType) {
    type_ = beanType ;
    methodMap_ = getMethodMap(beanType) ;
  }
  
  public ReflectionBeanDataHandler(Object bean) {
    bean_ = bean ;
    type_ = bean.getClass() ;
    methodMap_ = getMethodMap(type_) ;
  }
  
  public Class getDataTypeHandler()  { return type_ ; }
  
  public String getValueAsString(DataBindingValue value)  {
    try {
      Method method = (Method)methodMap_.get(value.getMethodName()) ;
      Object o = method.invoke(bean_,  EMPTY_ARGS) ;
      if(o == null) return "" ;
      return o.toString();
    } catch (Exception ex) {
      return value.getExpression() + " has error: " + ex.getMessage();
    }
  }
  
  public Object getValue(DataBindingValue value) {
    try {
      Method method = (Method)methodMap_.get(value.getMethodName()) ;
      return method.invoke(bean_,  EMPTY_ARGS) ;
    } catch (Exception ex) {
      return value.getExpression() + " has error: " + ex.getMessage();
    }
  }
  
  public void setBean(Object bean) {
    bean_ = bean ;
  }
  
  private Map getMethodMap(Class type) {
    Map methodMap = (Map)cache_.get(type) ;
    if(methodMap == null) {
      synchronized(cache_) {
        Method[] methods = type.getMethods() ;
        methodMap = new HashMap() ;
        for(int i = 0 ;  i < methods.length; i++) {
          if(methods[i].getParameterTypes().length == 0) {
           methodMap.put(methods[i].getName(), methods[i]) ; 
          }
        }
        cache_.put(type, methodMap) ;
      }
    }
    return methodMap ;
  }
}
