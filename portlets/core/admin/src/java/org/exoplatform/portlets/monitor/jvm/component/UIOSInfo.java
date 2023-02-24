/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import org.exoplatform.container.monitor.jvm.OperatingSystemInfo;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.text.template.BeanDataHandler;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.xhtml.Element;
import org.exoplatform.text.template.xhtml.Properties;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIOSInfo extends UIExoComponentBase {
  static private Element  TEMPLATE  = 
    new Properties().
      add("#{UIOSInfo.label.name}", "${name}").
      add("#{UIOSInfo.label.version}", "${version}").
      add("#{UIOSInfo.label.available-processors}","${AvailableProcessors}").
      add("#{UIOSInfo.label.achitecture}", "${arch}").
      optimize();
  
  private DataHandler dataHandler_ ;
  
	public UIOSInfo(OperatingSystemInfo osinfo) {
		setRendererType("TemplateRenderer") ;
    dataHandler_ = new BeanDataHandler(osinfo);
	}
  
  public DataHandler getDataHandler(Class type) { return dataHandler_ ; }
  
  public Element getTemplate() { return TEMPLATE ; }
}