/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.container;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.exoplatform.container.groovy.GroovyManager;
import org.exoplatform.container.jmx.ExoContainerMBean;
import org.exoplatform.container.jmx.MX4JRegistrationException;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jul 18, 2004
 * Time: 12:15:28 AM
 */
public class ExoContainer extends DefaultPicoContainer {
  public ExoContainer(PicoContainer parent) {
    super(parent)  ;
  }
  
  public ExoContainer(ComponentAdapterFactory factory, PicoContainer parent) {
    super(factory, parent)  ;
  }
  
  public MBeanServer getMBeanServer() {  
    throw new UnsupportedOperationException("This container do not support jmx management") ; 
  }
  
  public GroovyManager getGroovyManager() {  
    throw new UnsupportedOperationException("This container do not support jmx management") ; 
  }
  
  /*
  public void registerManageableComponentInstance(Object key , Object instance) {
    manageMBean(key, instance) ;
    registerComponentInstance(key, instance) ;
  }
  
  public void registerManageableComponentImplementation(Class clazz) {
    registerComponent(new MX4JComponentAdapter(clazz, clazz)) ;
  }
  
  public void registerManageableComponentImplementation(Object key, Class clazz) {
    registerComponent(new MX4JComponentAdapter(key, clazz)) ;
  }
  */
  
  public Object createComponent(Class clazz) throws Exception {
    Constructor[] constructors = getSortedConstructors(clazz) ;
    for(int k = 0; k < constructors.length; k++) {
      Constructor constructor =  constructors[k];
      Class[] parameters = constructor.getParameterTypes() ;
      Object[] args = new Object[parameters.length] ;
      boolean satisfied = true ;
      for (int i = 0; i < args.length; i++) {
        args[i] = getComponentInstanceOfType(parameters[i]) ;
        if (args[i] == null) {
          satisfied = false ;
          break ;
        }
      }
      if(satisfied) {
        return constructor.newInstance(args) ;
      }
    }
    throw new Exception("Cannot find a stisfid constructor for " + clazz) ;
  }
  
  private Constructor[] getSortedConstructors(Class clazz) {
    Constructor[] constructors = clazz.getConstructors() ;
    for(int i = 0 ; i < constructors.length; i++) {
      for(int j = i + 1 ; j < constructors.length; j++) {
        if(constructors[i].getParameterTypes().length < constructors[j].getParameterTypes().length) {
          Constructor tmp = constructors[i] ;
          constructors[i] = constructors[j] ;
          constructors[j] = tmp ; 
        }
      }
    }
    return constructors  ;
  }
  
  public void manageMBean(Object key, Object bean)  {
    ObjectName name = null;
    MBeanServer mbeanServer = getMBeanServer() ;
    Object mbean = null;
    try {      
      name = asObjectName(mbeanServer, key);
      mbean = new ExoContainerMBean(bean);
      mbeanServer.registerMBean(mbean, name);
    } catch (InstanceAlreadyExistsException e) {
      try {               
        mbeanServer.unregisterMBean(name);
        mbeanServer.registerMBean(mbean, name);        
      } catch (Exception e1) {
        throw new MX4JRegistrationException("Failed to register MBean '" + name + "' for component '" + key + "', due to " + e.getMessage(), e);
      }             
    } catch (Exception  e) {
      throw new MX4JRegistrationException("Failed to register MBean '" + name + "' for component '" + key + "', due to " + e.getMessage(), e);
    }
    
  }
  
  /**
   * Ensures that the given componentKey is converted to a JMX ObjectName
   * @param componentKey
   * @return an ObjectName based on the given componentKey
   */
  private static ObjectName asObjectName(MBeanServer mserver, Object componentKey) throws MalformedObjectNameException {
    if (componentKey == null) {
      throw new NullPointerException("componentKey cannot be null");
    }
    if (componentKey instanceof ObjectName) {
      return (ObjectName) componentKey;
    }
    if (componentKey instanceof Class) {
      Class clazz = (Class) componentKey;
      String name = clazz.getName() ;
      int idx = name.lastIndexOf(".");
      if(idx > 0) {
       name = name.substring(0, idx) ; 
      }
      return new ObjectName(name + ":type=" + clazz.getName());
    }
    String text = componentKey.toString();
    // Fix, so it works under WebSphere ver. 5
    if (text.indexOf(':') == -1) {
      text = mserver.getDefaultDomain() + ":type=" + text;
    }
    return new ObjectName(text);
  }
  
  public  void printMBeanServer() {
    MBeanServer server = getMBeanServer() ;
    final Set names = server.queryNames(null, null);
    for (final Iterator i = names.iterator(); i.hasNext();) {
      ObjectName name = (ObjectName) i.next();
      try {
        MBeanInfo info = server.getMBeanInfo(name);
        MBeanAttributeInfo[] attrs = info.getAttributes();
        if (attrs == null) continue;
        for (int j = 0; j < attrs.length; j++) {
          if (attrs[j].isReadable()) {
            try {
              Object o = server.getAttribute(name, attrs[j].getName());
            } catch (Exception x) {
              x.printStackTrace();
            }
          }
        }
        MBeanOperationInfo[] methods = info.getOperations();
        for (int j = 0; j < methods.length; j++) {
          MBeanParameterInfo[] params =  methods[j].getSignature() ;
          for(int k = 0 ; k < params.length; k++) {
          }
        }
      } catch (Exception x) {
        //x.printStackTrace(System.err);
      }
    }
  }
}