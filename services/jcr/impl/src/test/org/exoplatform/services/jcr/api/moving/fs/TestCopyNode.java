/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.moving.fs;


import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;

import org.exoplatform.services.jcr.api.moving.CopyNodeTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 8 aout 2004
 */
public class TestCopyNode extends CopyNodeTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("fs1");    
    ticket = repository.login(credentials, "ws");
    workspace = ticket.getWorkspace();
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
/*
    ticket.getRootNode().addNode("toCorrupt", "nt:childNodeDef");
    ticket.save();
    try {
      workspace.copy("/toCorrupt", "/childNode/corrupted", false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
*/
  }

  public void testClone() {

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
/*
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
*/

  }


}
