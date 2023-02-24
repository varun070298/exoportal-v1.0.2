/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import javax.management.MBeanInfo;import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;


/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIMBean.java,v 1.3 2004/08/01 18:56:11 tuan08 Exp $
 */
public class UIMBean extends UIExoCommand {
  final static public String EXECUTE_ACTION = "execute" ;
  
	private MBeanInfo minfo_ ;
  private ObjectName name_ ;
  private MBeanServer mserver_ ;
  
	public UIMBean() {
		setId("UIMBean") ;
    setClazz("UIMBean") ;
		setRendererType("ViewMBeanRenderer") ;
    addActionListener(ExecuteActionListener.class, EXECUTE_ACTION) ;
	}
 
  public String getFamily() {
    return "org.exoplatform.portlets.jmx.component.UIMBean" ;
  }
  
  public ObjectName getObjectName() { return name_ ; }
  
  public MBeanServer getMBeanServer() { return mserver_ ; }
  
  public MBeanInfo getMBeanInfo() { return minfo_ ; }
  
  public void  setUIMBeanData(MBeanServer server, ObjectName name) throws Exception {
    mserver_ = server ;
    name_ = name ;
    minfo_ = mserver_.getMBeanInfo(name); 
  }
  
  public boolean canExecute( MBeanOperationInfo method) {
     MBeanParameterInfo[] param =  method.getSignature() ;
     for(int i = 0 ; i < param.length; i++) {
     	if(param[i].getType().equals("java.lang.String") ||
         param[i].getType().equals("java.lang.Boolean") ||
         param[i].getType().equals("java.lang.Double") ||
         param[i].getType().equals("java.lang.Float") ||
         param[i].getType().equals("java.lang.Long") ||
         param[i].getType().equals("java.lang.Integer") ||
         param[i].getType().equals(Boolean.TYPE.getName()) ||
         param[i].getType().equals(Double.TYPE.getName()) ||
         param[i].getType().equals(Float.TYPE.getName()) ||
         param[i].getType().equals(Long.TYPE.getName()) ||
         param[i].getType().equals(Integer.TYPE.getName())) {
        continue ; 
      }
      return false ; 
     }
    return true ; 
  }
  
  static public class ExecuteActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIMBean uiMBean = (UIMBean)event.getComponent() ;
      int methodId = Integer.parseInt(event.getParameter("methodId")) ;
      MBeanOperationInfo[] method = uiMBean.minfo_.getOperations();
      for (int i = 0; i < method.length; i++) {
        if(methodId == method[i].hashCode()) {
          UIOperation uiOperation = (UIOperation) uiMBean.getSibling(UIOperation.class) ;
          uiOperation.setOperation(uiMBean.mserver_, uiMBean.name_, method[i]) ;
          uiMBean.setRenderedSibling(UIOperation.class) ;
          return ;
        }
      }
    }
  }
}