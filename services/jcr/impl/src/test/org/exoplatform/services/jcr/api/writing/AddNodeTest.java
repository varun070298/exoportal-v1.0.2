/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing;


import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 29 juil. 2004
 */
public class AddNodeTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();
  }

  public void testAddNode() throws RepositoryException {
    Node root = ticket.getRootNode();

    try {
      root.addNode("notExisting/dummy");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

    root.addNode("childNode");
    try {
      ticket.save();
      fail("exception should have been thrown");
    } catch (ItemExistsException e) {
    }
    ticket = repository.login(credentials, WORKSPACE);
    try {
      root.addNode("childNode/childNode2/jcr:content/exo:content/arggg");
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }

    Node node = root.getNode("childNode/childNode2");
    try {
      node.addNode("notJcrContentNode");
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
  }

  public void testAddNodeWithNodeType() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNode/childNode2");
    node.addNode("dummy", "nt:base");
    
    try {
      ticket.save();
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    node = root.getNode("childNode");
    node.addNode("dummy", "nt:base");
    try {
      ticket.save();
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    node = root.getNode("childNode");
    try {
      node.addNode("dummy", "nt:dymyNT");
      fail("exception should have been thrown");
    } catch (NoSuchNodeTypeException e) {
    }

  }

  public void testAddMultipleNodesWithUUID() throws RepositoryException {

    Node root = ticket.getRootNode();
    root.addNode("mnode1", "nt:file");
    root.addNode("mnode2", "nt:file");
    root.addNode("mnode3", "nt:file");
    ticket.save();

  }


}
