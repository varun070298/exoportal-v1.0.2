/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import org.exoplatform.container.monitor.jvm.v15.MemoryInfo;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.text.template.BeanDataHandler;
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
public class UIMemoryInfo extends UIExoComponentBase {
  static private Element  TEMPLATE  = 
    new Div().
    add(new Properties().
        addHeader("#{UIMemoryInfo.header.memory-info}").
        add("#{UIMemoryInfo.label.heap-memory}", "${heapMemoryUsage}").
        add("#{UIMemoryInfo.label.non-heap-memory}", "${nonHeapMemoryUsage}").
        add("#{UIMemoryInfo.label.object-pending-finalization-count}","${objectPendingFinalizationCount}").
        add("#{UIMemoryInfo.label.is-verbose}","${isVerbose()}").
        setDataHandlerType(MemoryInfo.class)).
    optimize();
  
  private BeanDataHandler minfoDataHandler_ ;
  
	public UIMemoryInfo(MemoryInfo minfo) {
		setRendererType("TemplateRenderer") ;
    minfoDataHandler_ =  new BeanDataHandler(minfo) ;  
	}
  
  public DataHandler getDataHandler(Class type) { 
    return minfoDataHandler_  ;
  }
  
  public Element getTemplate() { return TEMPLATE ; }
}