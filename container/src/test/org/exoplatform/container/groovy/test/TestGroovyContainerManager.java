package org.exoplatform.container.groovy.test;

import java.net.URL; 
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.groovy.* ;
import org.exoplatform.test.BasicTestCase;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestGroovyContainerManager extends BasicTestCase {
  
  public void testGroovyContainerManager() throws Exception  {
    RootContainer container = RootContainer.getInstance() ;
    GroovyManagerContainer  gcontainer = 
      (GroovyManagerContainer)container.getComponentInstanceOfType(GroovyManagerContainer.class) ;
    URL  classpath = new URL("file:" + container.getServerEnvironment().getServerHome() + 
                             "/web/portal/src/webapp/WEB-INF/groovy/") ;
    GroovyManager manager = gcontainer.getGroovyManager(classpath) ;
    Object gobject = 
      manager.getGroovyObject("org/exoplatform/container/groovy/test/TestGroovyObject.groovy") ;
    assertTrue("gobject instance is not null " , gobject != null) ;
    System.out.println(manager.getGroovyObjectAsText("org/exoplatform/container/groovy/test/TestGroovyObject.groovy")) ;
    String cpath = System.getProperty("java.class.path") ;
    System.out.println(cpath) ;
	}
}