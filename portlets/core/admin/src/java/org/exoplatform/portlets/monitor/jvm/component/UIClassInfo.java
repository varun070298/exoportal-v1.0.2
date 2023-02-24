/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.text.template.DataBindingValue;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.xhtml.Div;
import org.exoplatform.text.template.xhtml.Element;
import org.exoplatform.text.template.xhtml.Properties;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIClassInfo extends UIExoComponentBase {
  static private Element  TEMPLATE  = 
    new Div().
    add(new Properties().
        addHeader("Compiler Info").
        add("#{UIClassInfo.label.compiler-name}", "${name}").
        add("#{UIClassInfo.label.total-compilation-time}", "${totalCompilationTime}").
        add("#{UIClassInfo.label.is-monitor-supported}","${isCompilationTimeMonitoringSupported()}").
        addHeader("Class Loading Info").
        add("#{UIClassInfo.label.loaded-class-count}", "${loadedClassCount}").
        add("#{UIClassInfo.label.total-loaded-class-count}", "${totalLoadedClassCount}").
        add("#{UIClassInfo.label.unloaded-class-count}","${unloadedClassCount}")).
    optimize();
  
  private DataHandler datahandler_ ;
  
	public UIClassInfo(ClassLoadingMXBean classBean, CompilationMXBean compiler) {
		setRendererType("TemplateRenderer") ;
    datahandler_ = new ClassInfoDataHandler(classBean, compiler);
	}
  
  public DataHandler getDataHandler(Class type) { 
    return datahandler_  ;
  }
  
  public Element getTemplate() { return TEMPLATE ; }
  
  static class ClassInfoDataHandler implements DataHandler {
    private ClassLoadingMXBean classBean_;
    private CompilationMXBean compiler_;
    
    public ClassInfoDataHandler(ClassLoadingMXBean classBean, CompilationMXBean compiler) {
      classBean_ = classBean ;
      compiler_ = compiler ;
    }
    
    public Class getDataTypeHandler()  { return ClassInfoDataHandler.class; }
    
    public String getValueAsString(DataBindingValue value) {
      if("getName".equals(value.getMethodName())) return compiler_.getName() ; 
      if("getTotalCompilationTime".equals(value.getMethodName())) 
        return Long.toString(compiler_.getTotalCompilationTime()) ; 
      if("isCompilationTimeMonitoringSupported".equals(value.getMethodName())) 
        return Boolean.toString(compiler_.isCompilationTimeMonitoringSupported()) ;
      
      if("getLoadedClassCount".equals(value.getMethodName())) 
        return Long.toString(classBean_.getLoadedClassCount()) ; 
      if("getTotalLoadedClassCount".equals(value.getMethodName())) 
        return Long.toString(classBean_.getTotalLoadedClassCount()) ; 
      if("getUnloadedClassCount".equals(value.getMethodName())) 
        return Long.toString(classBean_.getUnloadedClassCount()) ; 
      return value.getExpression();
    }
    
    public Object getValue(DataBindingValue value)  {
      throw new RuntimeException("This method is not supported") ;
    }
  }
}