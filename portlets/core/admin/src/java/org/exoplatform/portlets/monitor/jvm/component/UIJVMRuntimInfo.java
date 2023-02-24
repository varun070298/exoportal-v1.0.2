/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.text.template.*;
import org.exoplatform.text.template.xhtml.Element;
import org.exoplatform.text.template.xhtml.LongTextPopupFormater;
import org.exoplatform.text.template.xhtml.Properties;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIJVMRuntimInfo extends UIExoComponentBase {
  static private LongTextPopupFormater popupTextFt_ = new LongTextPopupFormater(50);
  static private Element  TEMPLATE  = 
    new Properties().
      add("#{UIJVMRuntimInfo.label.name}","${name}").
      add("#{UIJVMRuntimInfo.label.SpecName}", "${SpecName}").
      add("#{UIJVMRuntimInfo.label.SpecVendor}","${SpecVendor}").
      add("#{UIJVMRuntimInfo.label.SpecVersion}","${SpecVersion}").
      add("#{UIJVMRuntimInfo.label.ManagementSpecVersion}", "${ManagementSpecVersion}").
      
      add("#{UIJVMRuntimInfo.label.VmName}", "${VmName}").
      add("#{UIJVMRuntimInfo.label.VmVendor}", "${VmVendor}").
      add("#{UIJVMRuntimInfo.label.VmVersion}", "${VmVersion}").
         
      add("#{UIJVMRuntimInfo.label.BootClassPathSupported}", "${BootClassPathSupported}", popupTextFt_).
      add("#{UIJVMRuntimInfo.label.BootClassPath}", "${BootClassPath}", popupTextFt_).
      add("#{UIJVMRuntimInfo.label.ClassPath}", "${ClassPath}", popupTextFt_).
      add("#{UIJVMRuntimInfo.label.LibraryPath}", "${LibraryPath}", popupTextFt_).

      add("#{UIJVMRuntimInfo.label.StartTime}", "${StartTime}").
      add("#{UIJVMRuntimInfo.label.Uptime}", "${Uptime}").
      
      add("#{UIJVMRuntimInfo.label.InputArguments}", "${InputArguments}", 
           new ListFormater(null, "<br/>")).
      add("#{UIJVMRuntimInfo.label.SystemProperties}", "${SystemProperties}", 
          new MapFormater(null, "<br/>").setValueFormater(popupTextFt_)).
    optimize();
    
  
  private DataHandler dataHandler_ ;
  
	public UIJVMRuntimInfo(JVMRuntimeInfo osinfo) {
		setRendererType("TemplateRenderer") ;
    dataHandler_ = new BeanDataHandler(osinfo);
	}
  
  public DataHandler getDataHandler(Class type) { return dataHandler_ ; }
  
  public Element getTemplate() { return TEMPLATE ; }
}