/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.jvm.component;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.monitor.jvm.JVMRuntimeInfo;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.text.template.DataHandler;
import org.exoplatform.text.template.ListBeanDataHandler;
import org.exoplatform.text.template.ArrayFormater;
import org.exoplatform.text.template.xhtml.*;
/**
 * May 31, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class UIMemoryPoolInfo extends UIExoCommand {
  private static String SHOW_DETAIL_ACTION = "detail" ;
  private static String SHOW_LIST_ACTION = "list" ;
  
  static private Element  LIST_TEMPLATE = 
    new Table().setCssClass("UIGrid").
      add(new Rows().setShowHeader(true).
          add(new LinkColumn("#{UIMemoryPoolInfo.header.name}", "${name}", "${name}").
              addParameter(ACTION, SHOW_DETAIL_ACTION )).
          add(new Column("#{UIMemoryPoolInfo.header.memory-type}","${type}")).
          add(new Column("#{UIMemoryPoolInfo.header.usage}", "${usage}"))).
      optimize() ;

  static private Element  DETAIL_TEMPLATE  = 
    new Div().
      add(new Properties().
          add("#{UIMemoryPoolInfo.label.name}", "${name}").
          add("#{UIMemoryPoolInfo.label.memory-manager-names}", "${memoryManagerNames}",
              new ArrayFormater(null, "<br/>")).
          add("#{UIMemoryPoolInfo.label.valid}", "${isValid()}").
          
          add("#{UIMemoryPoolInfo.label.memory-type}", "${type}").
          add("#{UIMemoryPoolInfo.label.memory-usage}", "${usage}").
          add("#{UIMemoryPoolInfo.label.memory-peak-usage}", "${peakUsage}").
          
          add("#{UIMemoryPoolInfo.label.usage-threshold}", "${usageThreshold}").
          add("#{UIMemoryPoolInfo.label.usage-threshold-count}", "${usageThresholdCount}").
          add("#{UIMemoryPoolInfo.label.is-usage-threshold-supported}", "${isUsageThresholdSupported()}").
          add("#{UIMemoryPoolInfo.label.is-usage-threshold-exceeded}", "${isUsageThresholdExceeded()}").
          
          add("#{UIMemoryPoolInfo.label.collection-usage-threshold}", "${collectionUsageThreshold}").
          add("#{UIMemoryPoolInfo.label.is-collection-usage-threshold-exceeded}", "${isCollectionUsageThresholdExceeded()}").
          add("#{UIMemoryPoolInfo.label.collection-usage-threshold-count}", "${collectionUsageThresholdCount}").
          add("#{UIMemoryPoolInfo.label.collection-usage}", "${collectionUsage}").
          add("#{UIMemoryPoolInfo.label.is-collection-usage-threshold-supported}", "${isCollectionUsageThresholdSupported()}")).
      add(new Button("#{UIProcesseInfo.button.cancel}").
          addParameter(ACTION, SHOW_LIST_ACTION)).
      optimize();
  
  private Element template_ = LIST_TEMPLATE ;
  private ListBeanDataHandler dataHandler_ ;
  
	public UIMemoryPoolInfo() {
		setRendererType("TemplateRenderer") ;
    List list = 
      (List)RootContainer.getInstance().getComponentInstance(JVMRuntimeInfo.MEMORY_POOL_MXBEANS) ;
    dataHandler_ =  new ListBeanDataHandler(MemoryPoolMXBean.class) ;
    dataHandler_.setBeans(list) ;
    addActionListener(ShowDetailActionListener.class, SHOW_DETAIL_ACTION) ;
    addActionListener(ShowListActionListener.class, SHOW_LIST_ACTION) ;
	}
  
  public DataHandler getDataHandler(Class type) { 
    return dataHandler_  ;
  }
  
  public Element getTemplate() { return template_ ; }
  
  
  static public class ShowDetailActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIMemoryPoolInfo uicomp = (UIMemoryPoolInfo) event.getComponent() ;
      String poolname = event.getParameter(OBJECTID) ; 
      List list = uicomp.dataHandler_.getBeans() ;
      for(int i = 0; i < list.size(); i++) {
        MemoryPoolMXBean bean = (MemoryPoolMXBean) list.get(i) ;
        if(poolname.equals(bean.getName())) {
          uicomp.dataHandler_.setBean(bean) ;
          uicomp.template_ = DETAIL_TEMPLATE ;
          return ;
        }
      }
    }
  }
  
  static public class ShowListActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIMemoryPoolInfo uicomp = (UIMemoryPoolInfo) event.getComponent() ;
      uicomp.template_ = LIST_TEMPLATE ;
    }
  }
}