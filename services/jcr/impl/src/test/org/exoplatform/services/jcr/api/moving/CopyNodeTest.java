/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.moving;


import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 8 ao�t 2004
 */
public class CopyNodeTest extends BaseTest {

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    root.remove("existNode");
    root.remove("test");
    root.remove("test2");
    root.remove("toCorrupt");
    ticket.save();
  }

  public void testCopy() throws RepositoryException {
    try {
      workspace.copy("/dummyNode", "/test", false);
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    root.addNode("existNode", "nt:folder");
    ticket.save();

    try {
      workspace.copy("/childNode", "/existNode", false);
      fail("exception should have been throwned");
    } catch (ItemExistsException e) {
    }

    workspace.copy("/childNode", "/test", false);
    ticket = repository.login(credentials, WORKSPACE);
    assertNotNull(ticket.getNodeByAbsPath("/test"));
    assertNotNull(ticket.getNodeByAbsPath("/childNode"));
    assertNotNull(ticket.getNodeByAbsPath("/test/childNode2"));
    assertNotNull(ticket.getNodeByAbsPath("/test/childNode2").getNode("jcr:content").
        getProperty("exo:content"));

    workspace.copy("/childNode", "/test2", true);
    ticket = repository.login(credentials, WORKSPACE);
    assertNotNull(ticket.getNodeByAbsPath("/test2"));
    assertNotNull(ticket.getNodeByAbsPath("/childNode"));
    try {
      ticket.getNodeByAbsPath("/test2/childNode2");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    ticket.getRootNode().addNode("toCorrupt", "nt:childNodeDef");
    ticket.save();
    try {
      workspace.copy("/toCorrupt", "/childNode/corrupted", false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
  }

  public void testMove() throws RepositoryException {
    Node root;
    try {
      workspace.move("/dummyNode", "/test");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    root.addNode("existNode", "nt:folder");
    ticket.save();

    try {
      workspace.move("/childNode", "/existNode");
      fail("exception should have been thrown");
    } catch (ItemExistsException e) {
    }
    assertNotNull(ticket.getNodeByAbsPath("/childNode/childNode2").getNode("jcr:content").getProperty("exo:content"));
    workspace.move("/childNode", "/test");
    ticket = repository.login(credentials, WORKSPACE);
    assertNotNull(ticket.getNodeByAbsPath("/test"));
    assertNotNull(ticket.getNodeByAbsPath("/test/childNode2").getNode("jcr:content").getProperty("exo:content"));

    try {
      ticket.getNodeByAbsPath("/childNode");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    ticket.getRootNode().addNode("toCorrupt", "nt:childNodeDef");
    ticket.save();

    try {
      workspace.move("/toCorrupt", "/childNode/corrupted");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

    try {
      workspace.move("/toCorrupt", "/test/corrupted");
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }


  }


  public void testClone() throws RepositoryException {


    Node root;
    String uuid = null;

    try {
      workspace.clone("/dummyNode", "/test", "ws2", false);
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");

    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();
    uuid = contentNode.getUUID();


    Ticket ticket2 = repository.login(null, "ws2");
    root = ticket2.getRootNode();
    root.addNode("existNode", "nt:folder").addNode("childNode", "nt:file");
    root.addNode("test", "nt:folder");
    ticket2.save();

    try {
      workspace.clone("/childNode", "/existNode", "ws2", false);
      fail("exception should have been throwned");
    } catch (ItemExistsException e) {
    }

    assertNotNull(ticket.getNodeByAbsPath("/childNode"));

    try {
      workspace.clone("/childNode", "/test1", "ws2", false);

      fail("exception should have been throwned");
    } catch (PathNotFoundException e) {
    }

    workspace.clone("/childNode", "/test", "ws2", false);

    ticket2 = repository.login(null, "ws2");
    assertNotNull(ticket2.getNodeByAbsPath("/test"));
    assertNotNull(ticket.getNodeByAbsPath("/childNode"));

    assertNotNull(ticket2.getNodeByAbsPath("/test/childNode/childNode2"));
    assertNotNull(ticket2.getNodeByAbsPath("/test/childNode/childNode2").getNode("jcr:content").
        getProperty("exo:content"));
    assertEquals(uuid, ticket2.getNodeByAbsPath("/test/childNode/childNode2").getNode("jcr:content").getUUID());

    assertNotNull(ticket.getNodeByUUID(uuid));
    assertNotNull(ticket2.getNodeByUUID(uuid));
    assertTrue(ticket.getNodeByUUID(uuid).isIdentical(ticket2.getNodeByUUID(uuid)));

    ticket2 = repository.login(null, "ws2");
    try {
      ticket2.getNodeByAbsPath("/test/childNode2");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    ticket.getRootNode().addNode("toCorrupt", "nt:childNodeDef");
    ticket.save();
    try {
      workspace.clone("/toCorrupt", "/test", "ws2", false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }

    ticket2 = repository.login(null, "ws2");
    root = ticket2.getRootNode();
    root.remove("existNode");
    root.remove("test");
//    root.remove("test2");
    ticket2.save();


  }
}
