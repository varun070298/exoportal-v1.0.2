package org.exoplatform.container.test;

import java.util.* ;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.test.BasicTestCase;

import org.exoplatform.container.mocks.MockService;
import org.exoplatform.container.configuration.*;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestJMXRegistration extends BasicTestCase {

  public void testJMXRegistration() throws Exception  {
    PortalContainer pcontainer = PortalContainer.getInstance() ;
    pcontainer.getComponentInstanceOfType(ConfigurationManager.class) ;
		assertNotNull(pcontainer.getComponentInstanceOfType(MockService.class));
		MBeanServer mbeanServer = pcontainer.getMBeanServer();
		pcontainer.printMBeanServer() ;
		System.out.println("Default domain : " + mbeanServer.getDefaultDomain());
		String name = "org.exoplatform.container.mocks:type=" + MockService.class.getName();
		ObjectName objectName = new ObjectName(name);
		ObjectInstance instance = mbeanServer.getObjectInstance(objectName);
		assertNotNull(instance);
		Object result = mbeanServer.invoke(objectName, "hello", null, null);
		System.out.println("Result : " + result);
    System.out.println("=================ROOT CONTAINER====================");
    RootContainer.getInstance().printMBeanServer() ;
    java.util.List servers = MBeanServerFactory.findMBeanServer(null) ;
    for (int i = 0; i < servers.size() ; i++) {
    	MBeanServer server = (MBeanServer) servers.get(i) ;
      System.out.println("Server with default domain : " + server.getDefaultDomain());
    }
    
    Set names = mbeanServer.queryNames(null, null) ;
    Iterator i = names.iterator() ;
    while (i.hasNext()) {
      ObjectName oname = (ObjectName) i.next() ;
      System.out.println("object name = " + oname.getCanonicalName()) ;
    }
	}
}