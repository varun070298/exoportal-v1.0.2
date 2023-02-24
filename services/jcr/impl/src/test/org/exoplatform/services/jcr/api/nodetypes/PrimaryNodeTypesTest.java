package org.exoplatform.services.jcr.api.nodetypes;
 

import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.nodetype.NodeType;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.impl.util.EntityCollection;
import javax.jcr.nodetype.PropertyDef;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: PrimaryNodeTypesTest.java,v 1.6 2004/09/03 09:59:42 geaz Exp $
 */

public class PrimaryNodeTypesTest extends BaseTest {

  protected Node root;
  protected Ticket ticket;

  public void testDefault() throws Exception {
    Node node = root.addNode("node1", "nt:default");
    NodeDef def = node.getDefinition();
    NodeType type = node.getPrimaryNodeType();

    assertTrue("have child ", ((EntityCollection) node.getNodes()).size() == 0);
    assertTrue("prop num !=1 ", ((EntityCollection) node.getProperties()).size() == 1);
    assertEquals("Prop not default ", "nt:default", node.getProperty("jcr:primaryType").getString());

    assertEquals("Type!= nt:default", type.getName(), "nt:default");
    assertTrue("typeNodeDefs != 1", type.getChildNodeDefs().length == 1);
    assertTrue("typePropDefs != 3", type.getPropertyDefs().length == 3);

    assertEquals("prop!=jcr:primaryType", "jcr:primaryType", type.getPropertyDefs()[1].getName());
    assertNull("prop0 name not null ", type.getPropertyDefs()[0].getName());
    assertNull("node name != null", type.getChildNodeDefs()[0].getName());

  }

/*
   public void testReferenceable() throws Exception {

      Node node;
      try {
          node = root.addNode("node-h", "mix:referenceable");
          fail("AddNode ConstraintViolationException should be thrown as type is not primary!");
       } catch (ConstraintViolationException e) {}

      node = root.addNode("node-h", "nt:default");

      node.addMixin("mix:referenceable");

      NodeDef def = node.getDefinition();
      NodeType type = node.getPrimaryNodeType();

      assertTrue("have child ", ((EntityCollection)node.getNodes()).size() == 1);
      assertTrue("prop num !=3 ", ((EntityCollection)node.getProperties()).size() == 3);


//      assertEquals("Prop not referenceable ", "mix:referenceable", node.getProperty("jcr:primaryType").toString());

      assertEquals("Type!= mix:referenceable", "mix:referenceable", type.getName() );
//      assertTrue("typeNodeDefs != 1", type.getChildNodeDefs().length == 1);
      assertTrue("typePropDefs != 1", type.getPropertyDefs().length == 1);

      // Property names: [0]=jcr:uuid, [1]=jcr:primaryType
//      assertEquals("prop2!=jcr:primaryType","jcr:primaryType", type.getPropertyDefs()[1].getName());
      assertEquals("prop0 name != uuid ", "jcr:uuid", type.getPropertyDefs()[0].getName());
      // NodeDefs = null
      assertNull("node name != null", type.getChildNodeDefs());

      assertNotNull("Prop not null ", node.getProperty("jcr:uuid").toString());
      log.debug("UUID before <"+node.getProperty("jcr:uuid").toString()+">");
      node.save(false);
      node = root.getNode("node-h");
      log.debug("UUID after <"+node.getProperty("jcr:uuid").toString()+">");
      assertNotNull("Prop not null ", node.getProperty("jcr:uuid").toString());

       // No children
       errFlag = false;
       try {
           node.addNode("not-allowed");
           errFlag = true;
       } catch (ConstraintViolationException e) {}
       if (errFlag)
          fail("AddNode ConstraintViolationException should be thrown!");

       errFlag = false;
       try {
           node.setProperty("not-allowed", "val");
           errFlag = true;
       } catch (ConstraintViolationException e) {}
       if (errFlag)
          fail("SetProp ConstraintViolationException should be thrown!");

       // UUID Read Only
       errFlag = false;
       try {
           node.setProperty("jcr:uuid", "1234");
           errFlag = true;
       } catch (ConstraintViolationException e) {}
       if (errFlag)
          fail("SetProp UUID ConstraintViolationException should be thrown!");
   }

*/

  public void testHierarchyItem() throws Exception {

    Node node = root.addNode("node-hi", "nt:hierarchyItem");
    NodeDef def = node.getDefinition();
    NodeType type = node.getPrimaryNodeType();

    assertTrue("have child ", ((EntityCollection) node.getNodes()).size() == 0);
    assertTrue("prop num !=2 ==" + ((EntityCollection) node.getProperties()).size(), ((EntityCollection) node.getProperties()).size() == 3);

    assertEquals("Prop is not HierarchyItem ", "nt:hierarchyItem", node.getProperty("jcr:primaryType").getString());
    assertEquals("Type!= nt:hierarchyItem", "nt:hierarchyItem", type.getName());

    assertTrue("typePropDefs != 4", type.getPropertyDefs().length == 4);
    // NodeDefs = null
    assertTrue("nodeDefs != 0", type.getChildNodeDefs().length == 0);

    // Property names: [0]=jcr:created, [1]=jcr:lastModified, [2]=jcr:primaryType
    assertEquals("prop2 name !=jcr:primaryType", "jcr:primaryType", type.getPropertyDefs()[2].getName());
    assertEquals("prop1 name != jcr:lastModified ", "jcr:lastModified", type.getPropertyDefs()[1].getName());
    assertEquals("prop0 name != jcr:created", "jcr:created", type.getPropertyDefs()[0].getName());

//    assertNull("Prop created not null ", node.getProperty("jcr:created").getValue());
//    assertNull("Prop modified not null ", node.getProperty("jcr:lastModified").getValue());

//    node.save(false);
    node = root.getNode("node-hi");
    assertNotNull("Prop null ", node.getProperty("jcr:created").toString());
//    assertNull("Prop modified SAVED not null ", node.getProperty("jcr:lastModified").getValue());
  }

  public void testContent() throws Exception {

    Node node = root.addNode("node-c", "nt:content");
    NodeType type = node.getPrimaryNodeType();

    assertEquals("Type!= nt:content", "nt:content", type.getName());
    assertTrue("typePropDefs != 4", type.getPropertyDefs().length == 4);
    assertTrue("typeNodeDefs != 1", type.getChildNodeDefs().length == 1);

    assertTrue("Props != 2 =" + ((EntityCollection) node.getProperties()).size(), ((EntityCollection) node.getProperties()).size() == 2);
    assertTrue("Nodes != 0", ((EntityCollection) node.getNodes()).size() == 0);

    node.addNode("someNode");
    node.addNode("someNode1");

    node.setProperty("stringProp", "string");
    node.setProperty("boolProp", true);

//    log.debug("NODES-- " + ((EntityCollection) node.getNodes()).getList());

    assertTrue("Props != 4 =" + ((EntityCollection) node.getProperties()).size(), ((EntityCollection) node.getProperties()).size() == 4);
    assertTrue("Nodes != 2, = " + ((EntityCollection) node.getNodes()).size(), ((EntityCollection) node.getNodes()).size() == 2);

  }

  public void testFile() throws Exception {

    Node node = root.addNode("node-f", "nt:file");
    NodeType type = node.getPrimaryNodeType();

    assertEquals("Type!= nt:file", "nt:file", type.getName());
    assertTrue("typePropDefs != 4", type.getPropertyDefs().length == 4);
    assertTrue("typeNodeDefs != 1", type.getChildNodeDefs().length == 1);

    // Property names: [0]=jcr:created, [1]=jcr:lastModified, [2]=jcr:primaryType
    assertEquals("prop2 name !=jcr:primaryType", "jcr:primaryType", type.getPropertyDefs()[2].getName());
    assertEquals("prop1 name != jcr:lastModified ", "jcr:lastModified", type.getPropertyDefs()[1].getName());
    assertEquals("prop0 name != jcr:created", "jcr:created", type.getPropertyDefs()[0].getName());

    assertEquals("node0 name != jcr:content", "jcr:content", type.getChildNodeDefs()[0].getName());

    assertTrue("Nodes != 1", ((EntityCollection) node.getNodes()).size() == 1);

    assertEquals("Primary Item ", "jcr:content", node.getNode("jcr:content").getName());
    assertEquals("Primary Item ", "jcr:content", node.getPrimaryItem().getName());

    try {
      node.addNode("not-allowed");
      fail("AddNode ConstraintViolationException should be thrown!");
    } catch (ConstraintViolationException e) {
    }

    node.setProperty("not-allowed", "val");
    try {
      node.save(false);
      fail("SetProp ConstraintViolationException should be thrown!");
    } catch (RepositoryException e) {
    }

    Node node1 = (Node) node.getPrimaryItem();
    node1.addNode("test");
    node1.setProperty("test", "test");

  }

  public void testFolder() throws Exception {

    Node node = root.addNode("node-fl", "nt:folder");
    NodeType type = node.getPrimaryNodeType();

    assertEquals("Type!= nt:folder", "nt:folder", type.getName());
    assertTrue("typePropDefs != 4", type.getPropertyDefs().length == 4);
    assertTrue("typeNodeDefs != 1", type.getChildNodeDefs().length == 1);

    // Property names: [0]=jcr:created, [1]=jcr:lastModified, [2]=jcr:primaryType
    assertEquals("prop2 name !=jcr:primaryType", "jcr:primaryType", type.getPropertyDefs()[2].getName());
    assertEquals("prop1 name != jcr:lastModified ", "jcr:lastModified", type.getPropertyDefs()[1].getName());
    assertEquals("prop0 name != jcr:created", "jcr:created", type.getPropertyDefs()[0].getName());

    assertNull("node0 name != null", type.getChildNodeDefs()[0].getName());

    node.setProperty("not-allowed", "val");
    try {
      node.save(false);
      fail("SetProp ConstraintViolationException should be thrown!");
    } catch (RepositoryException e) {
    }

    Node node1 = node.addNode("node1");
    assertEquals("Type!= nt:hierarchyItem", "nt:hierarchyItem", node1.getPrimaryNodeType().getName());

  }

  public void testMimeResource() throws Exception {

    Node node = root.addNode("node-mr", "nt:mimeResource");
    NodeType type = node.getPrimaryNodeType();

    assertEquals("Type!=nt:mimeResource", "nt:mimeResource", type.getName());
    PropertyDef[] propDefs = type.getPropertyDefs();

    // 4 + primaryType, mixinType, uuid
    assertTrue("typePropDefs = "+type.getPropertyDefs().length, type.getPropertyDefs().length == 7);
    assertTrue("typeNodeDefs != 0", type.getChildNodeDefs().length == 0);


  }
}
