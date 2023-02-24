package org.exoplatform.services.jcr.api.nodetypes;


import javax.jcr.*;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.ConstraintViolationException;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.impl.util.EntityCollection;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DiscoveringNodeTypesTest.java,v 1.1 2004/09/10 11:12:24 geaz Exp $
 */

public class DiscoveringNodeTypesTest extends BaseTest {

  protected Node root;

  public void testGetPrimaryNodeType() throws Exception {
  
    Node node = root.addNode("node1", "nt:default");

    NodeType type = node.getPrimaryNodeType();
    assertEquals("nt:default", type.getName());

  }

  public void testGetMixinNodeTypes() throws Exception {
  
    Node node = root.addNode("node1", "nt:default");
    assertEquals(0, node.getMixinNodeTypes().length);

    node.addMixin("mix:referenceable");
    assertEquals(1, node.getMixinNodeTypes().length);

    NodeType type = node.getMixinNodeTypes()[0];
    assertEquals("mix:referenceable", type.getName());

  }

  public void testIsNodeType() throws Exception {
  
    Node node = root.addNode("node1", "nt:default");
    assertFalse(node.isNodeType("mix:referenceable"));
    node.addMixin("mix:referenceable");

    assertTrue(node.isNodeType("nt:default"));
    assertTrue("Not nt:base", node.isNodeType("nt:base"));
    assertTrue(node.isNodeType("mix:referenceable"));

    assertFalse(node.isNodeType("nt:file"));
    assertFalse(node.isNodeType("mix:notfound"));

  }

}
