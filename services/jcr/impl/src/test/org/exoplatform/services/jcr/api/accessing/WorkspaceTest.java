/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.accessing;


import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juil. 2004
 */
public class WorkspaceTest extends BaseTest {

  public void testGetTicket() {
    assertEquals(workspace.getTicket(), ticket);
  }

  public void testMove() throws RepositoryException, ConstraintViolationException,
      NoSuchNodeTypeException, ItemExistsException {
    Node root;
    try {
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
        fail("exception should have been throwned");
      } catch (RepositoryException e) {
      }
      assertNotNull(ticket.getNodeByAbsPath("/childNode/childNode2").getNode("jcr:content").getProperty("exo:content"));
      workspace.move("/childNode", "/test");
      ticket = repository.login(credentials, WORKSPACE);
      assertNotNull(ticket.getNodeByAbsPath("/test"));
      assertNotNull(ticket.getNodeByAbsPath("/test/childNode2").getNode("jcr:content").getProperty("exo:content"));

      try {
        ticket.getNodeByAbsPath("/childNode");
        fail("exception should have been throwned");
      } catch (RepositoryException e) {
      }
    } finally {
      root = ticket.getRootNode();
      root.remove("test");
      root.remove("childNode");
      root.remove("existNode");
      ticket.save();
    }
  }
}
