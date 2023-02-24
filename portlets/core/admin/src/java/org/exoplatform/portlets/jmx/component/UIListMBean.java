/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.ActionColumn;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.component.model.CollectionDataHandler;
import org.exoplatform.faces.core.component.model.Column;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.component.model.Rows;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portlets.jmx.component.model.MBeanDomain;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIListMBean.java,v 1.2 2004/08/01 04:18:27 tuan08 Exp $
 */
public class UIListMBean extends UIGrid  {
  private static Parameter[] VIEW_ACTION = {new Parameter(ACTION , "view") } ;
	private MBeanServer mserver_ ;
  private MBeanDomain mdomain_;
  private ObjectNameDataHandler dataHandler_ ;
  
	public UIListMBean() {
		setId("UIListMbean") ;
    setClazz("UIListMBean") ;
    dataHandler_ = new ObjectNameDataHandler() ;
    add(new Rows(dataHandler_, "even", "odd").
        add(new Column("#{UIListMBean.header.object-name}", "name")).
        add(new ActionColumn("#{UIListMBean.header.action}", "id").
            add(true ,new Button("#{UIListMBean.button.view}", VIEW_ACTION)))) ;
    addActionListener(SelectMBeanActionListener.class, "view") ;
	}
  
  public void setMBeanServerDomain(MBeanServer mserver, MBeanDomain mdomain) {
  	mserver_ = mserver ;
    mdomain_ = mdomain ;
    dataHandler_.setDatas(mdomain.getMBeanNames()) ;
  }
  
  
  static public class ObjectNameDataHandler extends CollectionDataHandler {
    private MBeanDomain.MBeanDescription desc_  ;
    
    public String  getData(String fieldName)   {
      if("name".equals(fieldName)) return desc_.getObjectName().getCanonicalName();
      if("id".equals(fieldName)) return desc_.getId();
      return null ;
    }
    
    public void setCurrentObject(Object o) { desc_ = (MBeanDomain.MBeanDescription) o; }
  }
  
  static public class SelectMBeanActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIListMBean uiList = (UIListMBean)event.getComponent() ;
      String mbean = event.getParameter("objectId") ;
      ObjectName name = uiList.mdomain_.findMBeanObjectname(mbean) ;
      UIJMXPortlet uiPortlet = (UIJMXPortlet) uiList.getAncestorOfType(UIJMXPortlet.class) ;
      UIMBean uiMBean = (UIMBean) uiPortlet.getChildComponentOfType(UIMBean.class) ;
      uiMBean.setUIMBeanData(uiList.mserver_, name) ;
      uiPortlet.setRenderedComponent(uiMBean.getId()) ;
    } 
  }
} 