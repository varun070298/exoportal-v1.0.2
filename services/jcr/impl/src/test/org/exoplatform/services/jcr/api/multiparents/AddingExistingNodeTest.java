package org.exoplatform.services.jcr.api.multiparents;
 

import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.util.EntityCollection;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: AddingExistingNodeTest.java,v 1.3 2004/09/16 15:26:54 geaz Exp $
 */

public class AddingExistingNodeTest extends BaseTest {

  protected Node root;


  public void testAddExistingNode() throws Exception {


    Node node = ticket.getRootNode().addNode("testNode", "nt:default");
    Node child = ticket.getRootNode().addNode("childNode", "nt:default");
    child.addMixin("mix:referenceable");

    assertEquals("mix:referenceable", child.getProperty("jcr:mixinType").getString());
    assertTrue(child.hasProperty("jcr:uuid"));

    ticket.save();

    assertTrue(ticket.getNodeByAbsPath("/childNode").isNodeType("mix:referenceable"));

    // /test/childNode if reference to /childNode
    // Test if the nodes are identical
    Node child1 = node.addExistingNode("/childNode");
    assertNotNull(ticket.getNodeByAbsPath("/testNode/childNode"));
    assertTrue("Not identical", ticket.getNodeByAbsPath("/childNode").isIdentical(ticket.getNodeByAbsPath("/testNode/childNode")));
    ticket.save();


    // Test if the nodes are identical after save
    Ticket ticket1 = ticket.impersonate(ticket.getCredentials());
    assertNotNull(ticket1.getNodeByAbsPath("/testNode/childNode"));
    assertTrue("Not identical", ticket1.getNodeByAbsPath("/childNode").isIdentical(ticket1.getNodeByAbsPath("/testNode/childNode")));

    assertEquals(ticket1.getNodeByAbsPath("/childNode").getUUID(),
        ticket1.getNodeByAbsPath("/testNode/childNode").getUUID());

    assertEquals(2, child.getPaths().getSize());

    // Test if property will be assigned to the linked node as well
    child1.setProperty("testProp", "testVal");
    assertNotNull(child.getProperty("testProp"));
    assertEquals("testVal", child.getProperty("testProp").getString());
    assertEquals(2, child.getProperty("testProp").getParents().getSize());

    // Test if linked node will be saved as well
    child.save(false);
    Ticket ticket2 = ticket.impersonate(ticket.getCredentials());
    assertNotNull(ticket2.getNodeByAbsPath("/testNode/childNode").getProperty("testProp"));


    // Test if linked node will be removed as well
    ticket2.getRootNode().remove("/childNode");
    ticket2.save();
    try {
      ticket2.getNodeByAbsPath("/testNode/childNode");
      fail("exception should have been throwned");
    } catch (PathNotFoundException e) {
    }




  }


}
