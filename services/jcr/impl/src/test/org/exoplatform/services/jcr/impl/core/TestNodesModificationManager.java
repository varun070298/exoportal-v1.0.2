/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

package org.exoplatform.services.jcr.impl.core;


import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.SimpleCredentials;
import javax.jcr.StringValue;

import org.exoplatform.services.jcr.BaseTest;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: TestNodesModificationManager.java,v 1.3 2004/09/16 15:28:52 geaz Exp $
 */
public class TestNodesModificationManager extends BaseTest {


  private NodesModificationManager modificationManager;
  private RepositoryImpl repository;
  private TicketImpl ticket;

  public void setUp() throws Exception {
      super.setUp();
      repository = (RepositoryImpl) repositoryService.getRepository("mock");      
      Credentials cred = new SimpleCredentials("user", "psw".toCharArray());
      ticket = (TicketImpl)repository.login(cred, "ws");
      modificationManager = ticket.getNodesManager(); 

  }


  public void testTicketGetsNodesFromNodesManager() throws Exception {

      ticket.getRootNode().addNode("/testNode");
      assertNotNull(modificationManager.getNodeByPath("/testNode"));
      Node testNode = modificationManager.getNodeByPath("/testNode");
      assertEquals(ticket.getNodeByAbsPath("/testNode"), testNode);
      ticket.save();
      assertEquals(ticket.getNodeByAbsPath("/testNode"), testNode);
//      log.debug(">>>>>>>>>>>"+ticket.getRootNode().getNodes().getSize());

      // just remove
      ticket.getRootNode().remove("testNode");

      ticket.save();
//      log.debug("END SAVE");
      assertNull(modificationManager.getNodeByPath("/testNode"));

  }

  public void testNodeChangesState() throws Exception {

      assertNull(modificationManager.getNodeChange("/testNode"));
      ticket.getRootNode().addNode("/testNode");
      assertEquals(NodeChangeState.ADDED, modificationManager.getNodeChange("/testNode").getState());
      Node testNode = ticket.getNodeByAbsPath("/testNode");
      assertNotNull(testNode);
      testNode.setProperty("testProperty", "testValue");
      assertEquals(NodeChangeState.ADDED, modificationManager.getNodeChange("/testNode").getState());
      ticket.save();
      assertEquals(NodeChangeState.UNCHANGED, modificationManager.getNodeChange("/testNode").getState());
      testNode = ticket.getNodeByAbsPath("/testNode");
      testNode.setProperty("testProperty", "testChangedValue");
      assertEquals(NodeChangeState.UPDATED, modificationManager.getNodeChange("/testNode").getState());
      ticket.getRootNode().remove("testNode");
      assertEquals(NodeChangeState.DELETED, modificationManager.getNodeChange("/testNode").getState());
      ticket.save();
      assertNull(modificationManager.getNodeChange("/testNode"));
  }

  public void testIfPropertiesSaved() throws Exception {
      Node node = ticket.getRootNode().addNode("/testNode");
      assertNotNull(modificationManager.getNodeByPath("/testNode").getProperty("jcr:primaryType"));
      assertEquals(modificationManager.getNodeByPath("/testNode").getProperty("jcr:primaryType").getString(), "nt:default");
      Property prop = node.setProperty("test", "test");
      assertNotNull(modificationManager.getNodeByPath("/testNode").getProperty("test"));
      assertEquals("test", node.getProperty("test").getString());
      prop.setValue("test NEW");
      assertEquals("test NEW", node.getProperty("test").getString());
      StringValue val = new StringValue("this is the NEW content");
      prop.setValue(val);
      assertEquals("this is the NEW content", node.getProperty("test").getString());


      ticket.save();

      log.debug("MODI MAN----"+modificationManager.getNodeByPath("/testNode").getPermanentProperty("test").hashCode());
      log.debug("CONTAINER----"+ticket.getContainer().getNodeByPath("/testNode").getPermanentProperty("test").hashCode());
      log.debug("PROP----"+((NodeImpl)node).getPermanentProperty("test").hashCode());

      node.remove("test");

      assertNull(modificationManager.getNodeByPath("/testNode").getPermanentProperty("test"));
      node = ticket.getNodeByAbsPath("/testNode");
      assertNotNull(ticket.getContainer().getNodeByPath("/testNode").getPermanentProperty("test"));

      ticket.getRootNode().remove("testNode");
      ticket.save();

  }

  public void testChildren() throws Exception {
      Node node = ticket.getRootNode().addNode("/testNode");
      assertEquals(1, modificationManager.getChildren("/").size());

      node.save(false);
      assertEquals(1, modificationManager.getChildren("/").size());
      node = ticket.getNodeByAbsPath("/testNode").addNode("testNode1");
      assertEquals(1, modificationManager.getChildren("/testNode").size());
      node.addNode("node11");
      assertEquals(1, modificationManager.getChildren("/testNode/testNode1").size());
      node.save(false);

      node = ticket.getNodeByAbsPath("/testNode");
      ticket.getRootNode().remove("/testNode");
      node.save(false);

//      for(int i=0; i<modificationManager.getChildren("/").size();i++)
//        log.debug("END SAVE"+ modificationManager.getChildren("/").get(i));

      assertEquals(0, modificationManager.getChildren("/").size());

  }

  public void testReferences() throws Exception {


    //prepare node
     Node node = ticket.getRootNode().addNode("/testNode").addNode("childNode");
     assertNotNull(modificationManager.getNodeByPath("/testNode/childNode"));
     // make node mixin - so reference could be assigned
     node.addMixin("mix:referenceable");
     assertNotNull(modificationManager.getNodeByPath("/testNode/childNode").getProperty("jcr:mixinType"));
     ticket.save();
     node = modificationManager.getNodeByPath("/testNode/childNode");

    //add reference
    modificationManager.addReference(node.getProperty("jcr:uuid").getString(), "/refToChildNode");
    String uuid = node.getProperty("jcr:uuid").getString();
    assertNotNull(modificationManager.getPaths(uuid));
    assertEquals(2, modificationManager.getPaths(uuid).size());

    //commit reference
    NodeImpl node1 = (NodeImpl)modificationManager.getNodeByPath("/refToChildNode");
    assertNotNull(node1);
    modificationManager.commit(node1, false);
    assertEquals(2, modificationManager.getPaths(uuid).size());

    //rollback reference (does not save)
    modificationManager.addReference(node.getProperty("jcr:uuid").getString(), "/tempRefToChildNode");
    NodeImpl node3 = (NodeImpl)modificationManager.getNodeByPath("/tempRefToChildNode");
    assertNotNull(node3);
    // ???
//    modificationManager.rollback(node3);
//    assertNull(node3);

    //get node by reference
    NodeImpl node2 = (NodeImpl)modificationManager.getNodeByPath("/refToChildNode");
    assertNotNull(node2);

    //change node

    //save node

    //get node by reference

    //remove node
  }

}
