/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.ListBeanDataHandler;
import org.exoplatform.text.template.ArrayFormater;
import org.exoplatform.text.template.xhtml.Column;
import org.exoplatform.text.template.xhtml.Element;
import org.exoplatform.text.template.xhtml.Rows;
import org.exoplatform.text.template.xhtml.Table;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIGCInfo extends UIExoComponentBase {
  static private Element  TEMPLATE = 
    new Table().setCssClass("UIGrid").
      add(new Rows().setShowHeader(true).
          add(new Column("#{UIGCInfo.label.name}", "${name}")).
          add(new Column("#{UIGCInfo.label.is-valid}","${isValid()}")).
          add(new Column("#{UIGCInfo.label.collection-count}","${collectionCount}")).
          add(new Column("#{UIGCInfo.label.collectionTime}","${collectionTime}")).
          add(new Column("#{UIGCInfo.label.memory-pool-names}", "${memoryPoolNames}").
              setFomater(new ArrayFormater(null, ", ")))).
      optimize() ;
  
  private ListBeanDataHandler dataHandler_ ;
  
	public UIGCInfo() {
		setRendererType("TemplateRenderer") ;
    List list = 
      (List)RootContainer.getInstance().getComponentInstance(JVMRuntimeInfo.GARBAGE_COLLECTOR_MXBEANS) ;
    dataHandler_ =  new ListBeanDataHandler(GarbageCollectorMXBean.class) ;
    dataHandler_.setBeans(list) ;
	}
  
  public DataHandler getDataHandler(Class type) { 
    return dataHandler_  ;
  }
  
  public Element getTemplate() { return TEMPLATE ; }
}