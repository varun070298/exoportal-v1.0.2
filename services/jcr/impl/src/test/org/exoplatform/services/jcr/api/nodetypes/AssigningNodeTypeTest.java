package org.exoplatform.services.jcr.api.nodetypes;


import javax.jcr.Node;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;

import org.exoplatform.services.jcr.BaseTest;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: AssigningNodeTypeTest.java,v 1.1 2004/09/10 11:12:24 geaz Exp $
 */

public class AssigningNodeTypeTest extends BaseTest {

  protected Node root;

  public void testAddNode() throws Exception {
  
    Node node = root.addNode("node1", "nt:default");
    NodeType type = node.getPrimaryNodeType();
    assertEquals("nt:default", type.getName());
    try {
      root.addNode("node2", "not:found");
      fail("exception should have been thrown");
    } catch (NoSuchNodeTypeException e) {
    }

 
  }

  public void testAddMixin() throws Exception {
  
    Node node = root.addNode("node1", "nt:default");
    assertEquals(0, node.getMixinNodeTypes().length);

    node.addMixin("mix:referenceable");
    assertEquals(1, node.getMixinNodeTypes().length);

    NodeType type = node.getMixinNodeTypes()[0];
    assertEquals("mix:referenceable", type.getName());

    try {
      node.addMixin("mix:notfound");
      node.save(true);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }


    // Should not add another same or subtype
    node.addMixin("mix:referenceable");
    assertEquals(1, node.getMixinNodeTypes().length);


  }

  public void testIfAddMixinAddsAutocreatedItems() throws Exception {

    Node node = root.addNode("node1", "nt:default");
    node.addMixin("mix:referenceable");
    assertTrue("UUID is null! ",node.hasProperty("jcr:uuid"));

  }


}
