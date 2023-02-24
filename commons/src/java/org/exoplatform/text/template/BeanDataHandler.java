/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.text.template;

import java.util.* ; 
import net.sf.cglib.reflect.FastClass;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Feb 1, 2005
 * @version $Id$
 */
public class BeanDataHandler implements DataHandler {
  private static Class[] EMPTY_TYPE = {} ;
  private static Object[] EMPTY_ARGS  = {} ;
  private static Map fastClassMap_ = new HashMap(300) ;
  
  private Object bean_ ;
  private FastClass fastClass_ ;
  private Class type_ ;
  
  public BeanDataHandler(Class beanType) {
    type_ = beanType ;
    fastClass_ = getFastClass(beanType) ;
  }
  
  public BeanDataHandler(Object bean) {
    bean_ = bean ;
    type_ = bean.getClass() ;
    fastClass_ = getFastClass(type_);
  }
  
  public Class getDataTypeHandler()  { return type_ ; }
  
  public String getValueAsString(DataBindingValue value)  {
    try {
      Object o = fastClass_.invoke(value.getMethodName(), EMPTY_TYPE, bean_, EMPTY_ARGS) ;
      if(o == null) return "" ;
      return o.toString();
    } catch (Exception ex) {
      return value.getExpression() + " has error: " + ex.getMessage();
    }
  }
  
  public Object getValue(DataBindingValue value) {
    try {
      Object o = fastClass_.invoke(value.getMethodName(), EMPTY_TYPE, bean_, EMPTY_ARGS) ;
      return o;
    } catch (Exception ex) {
      return value.getExpression() + " has error: " + ex.getMessage();
    }
  }
  
  public void setBean(Object bean) {
    bean_ = bean ;
  }
  
  private FastClass getFastClass(Class type) {
    FastClass fc = (FastClass)fastClassMap_.get(type) ;
    if(fc == null) {
      synchronized(fastClassMap_) {
        fc = FastClass.create(type) ;
        fastClassMap_.put(type, fc) ;
      }
    }
    return fc ;
  }
}
