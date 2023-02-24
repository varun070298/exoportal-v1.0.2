/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.component;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanParameterInfo ;
import javax.management.ObjectName;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;

/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIOperation.java,v 1.2 2004/08/01 18:56:11 tuan08 Exp $
 */
public class UIOperation extends UIExoCommand {
  final static public String EXECUTE_ACTION = "execute" ;
  final static public String[] EMPTY_PARAM = new String[0] ;
  
  private MBeanServer mserver_ ;
  private MBeanOperationInfo operation_ ;
  private ObjectName name_ ;
  private Object result_ ; 
  
	public UIOperation() {
		setId("UIOperation") ;
    setClazz("UIOperation") ;
		setRendererType("OperationRenderer") ;
    addActionListener(ExecuteActionListener.class, EXECUTE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
	}
 
  public String getFamily() {
    return "org.exoplatform.portlets.jmx.component.UIOperation" ;
  }
  
  public MBeanOperationInfo getMBeanOperationInfo() { return operation_; }
  
  public void  setOperation(MBeanServer server, ObjectName name, MBeanOperationInfo operation) {
  	operation_ = operation ;
    mserver_ = server ;
    name_  = name ;
    result_ = null ;
  }
 
  public Object getResult() { return result_ ; }
  
  public void execute(String[] param) throws Exception {
    if(param == null) param = EMPTY_PARAM ;
    MBeanParameterInfo[] paramInfo = operation_.getSignature() ;
    String[] signatures = new String[param.length] ;
    Object[] args =  new Object[param.length] ;
    for(int i = 0 ; i < param.length ; i++) {
      signatures[i] = paramInfo[i].getType() ;
      args[i] = getParameter(param[i],  signatures[i]) ;
    }
  	result_  = mserver_.invoke(name_, operation_.getName(), args, signatures) ;
  }
  
  private Object getParameter(String value, String type) throws Exception {
    if(type.equals("java.lang.String"))  return value ;
    if(type.equals("java.lang.Boolean") || 
       type.equals(Boolean.TYPE.getName()))  return new Boolean(value) ;
    if(type.equals("java.lang.Integer") || 
       type.equals(Integer.TYPE.getName()))  return new Integer(value) ;
    if(type.equals("java.lang.Long") || 
        type.equals(Long.TYPE.getName()))  return new Long(value) ;
    if(type.equals("java.lang.Float") || 
        type.equals(Float.TYPE.getName()))  return new Float(value) ;
    if(type.equals("java.lang.Double") || 
       type.equals(Double.TYPE.getName()))  return new Double(value) ;
    throw new Exception("We do not support parameter type: " + type) ;
  }
  
  static public class ExecuteActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UIOperation uiOperation = (UIOperation) event.getComponent() ;
  		String[] param = event.getParameterValues("parameter") ;  
  		uiOperation.execute(param) ;
  	}
  }
  
  static public class CancelActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIOperation uiOperation = (UIOperation) event.getComponent() ;
      uiOperation.setRenderedSibling(UIMBean.class) ;
    }
  }
}