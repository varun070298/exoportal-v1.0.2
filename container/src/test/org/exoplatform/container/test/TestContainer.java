package org.exoplatform.container.test;

import java.util.List;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.test.BasicTestCase;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestContainer extends BasicTestCase {

  public void testPortalContainer() throws Exception  {
		RootContainer rootContainer = RootContainer.getInstance() ;
    PortalContainer pcontainer = rootContainer.getPortalContainer("portal") ;
    Object parent =  pcontainer.getParent() ;
    assertTrue("Root container should not be null", parent != null) ;
    pcontainer.createSessionContainer("sessioncontainer1", "anon") ;
    pcontainer.createSessionContainer("sessioncontainer2", "anon") ;
    List sessions = pcontainer.getLiveSessions() ; 
    assertEquals("expect 2 session container", 2 , sessions.size()) ;
    //performance test 
    
    int INSERTLOOP = 0 ;
    long start = System.currentTimeMillis() ;
    for(int i = 0 ; i < INSERTLOOP; i++) {
      rootContainer.getPortalContainer("name-" + Integer.toString(i)) ;
    }
    System.out.println("Insert 1000 components " +  (System.currentTimeMillis()  - start ) +"ms") ;
    
    int LOOP = 10000000 ;
    start = System.currentTimeMillis() ;
    for(int i = 0 ; i < LOOP; i++) {
      pcontainer = (PortalContainer)rootContainer.getComponentInstance("portal") ;
      assertTrue("not null", pcontainer != null) ;
    }
    System.out.println("Retrieve compoponent 10M times " +  (System.currentTimeMillis()  - start ) +"ms") ;
    System.out.println("AVG = " +  (System.currentTimeMillis()  - start )/LOOP +"ms") ;
    System.out.println("-------------------------------------------------------------------------") ;
    Object oo = pcontainer.getComponentInstance("org/exoplatform/container/groovy/test/TestGroovyObject.groovy") ;
    System.out.println("++++++++" +oo) ;
    pcontainer.getGroovyManager().reloadGroovyObjects() ;
    pcontainer.getComponentInstance("org/exoplatform/container/groovy/test/TestGroovyObject.groovy") ;
	}
}