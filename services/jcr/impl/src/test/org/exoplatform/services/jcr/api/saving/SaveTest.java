/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.saving;


import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.PathNotFoundException;
import javax.jcr.nodetype.ConstraintViolationException;
import org.exoplatform.services.jcr.BaseTest;


/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 ao�t 2004
 */
public class SaveTest extends BaseTest{

  public void testTicketSave() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:propertyDef");
    try {
      ticket.save();
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }

    assertEquals("/childNode/childNode2", root.getNode("childNode/childNode2").getPath());

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    try {
      root.getNode("childNode/childNode2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

    Node node = root.addNode("nodeType", "exo:mockNodeType");
    node.addNode("jcr:propertyDef", "nt:propertyDef").setProperty("jcr:defaultValue", "testString");
    try {
      ticket.save();
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
    node.addNode("jcr:childNodeDef", "nt:childNodeDef");
    ticket.save();
    root.remove("nodeType");
    ticket.save();
  }

  public void testRevert() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.addNode("nodeType", "exo:mockNodeType");
    node.addNode("jcr:propertyDef", "nt:propertyDef");
    ticket.revert();
    try {
      root.getNode("nodeType");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
    ticket.save();
  }

  public void testPropertiesManipThenSave() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.addNode("childNode", "nt:default");
    node.addNode("node2BRem");
    node.setProperty("existingProp", "existingValue");
    node.setProperty("existingProp2", "existingValue2");
    ticket.save();
    node.setProperty("prop", "propValue");
    node.setProperty("existingProp", "existingValueBis");
    node.remove("existingProp2");
    node.remove("node2BRem");
    node.addNode("addedNode");
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("childNode");
    root = ticket.getRootNode();
    try {
      node.getProperty("prop");
    } catch (PathNotFoundException e) {
      e.printStackTrace();
      fail("exception should not be thrown");
    }
    assertEquals("existingValueBis", node.getProperty("existingProp").getString());
    try {
      node.getProperty("existingProp2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
    try {
      node.getNode("node2BRem");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
    node.getNode("addedNode");

//    System.out.println("REMOVE childNode");
    root.remove("childNode");
//    System.out.println("SAVE childNode");
    ticket.save();
//    System.out.println("REMOVED");
  }

  public void testNodeSave() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:propertyDef");
    try {
      root.save(false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }

    assertEquals("/childNode/childNode2", root.getNode("childNode/childNode2").getPath());
    ticket.revert();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();

    try {
      root.getNode("childNode/childNode2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

    Node node = root.addNode("nodeType", "exo:mockNodeType");
    node.addNode("jcr:propertyDef", "nt:propertyDef").setProperty("jcr:defaultValue", "testString");
    try {
      node.save(false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {
    }
    node.addNode("jcr:childNodeDef", "nt:childNodeDef");
    node.save(false);

    node = root.addNode("childNode", "nt:default");
    node.addNode("node2BRem");
    node.setProperty("existingProp", "existingValue");
    node.setProperty("existingProp2", "existingValue2");
    root.save(false);
    node.setProperty("prop", "propValue");
    node.setProperty("existingProp", "existingValueBis");
    node.remove("existingProp2");
    node.remove("node2BRem");
    node.addNode("addedNode");
    node.save(true);

    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("childNode");
    root = ticket.getRootNode();
    try {
      node.getProperty("prop");
    } catch (PathNotFoundException e) {
      e.printStackTrace();
      fail("exception should not be thrown");
    }
    assertEquals("existingValueBis", node.getProperty("existingProp").getString());
    try {
      node.getProperty("existingProp2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
    try {
      node.getNode("node2BRem");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
    try {
      node.getNode("addedNode");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

    root.remove("nodeType");
    root.remove("childNode");
    ticket.save();
  }

  public void testSaveNodeWithNonPersistedParent() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node n = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    try {
      n.save(false);
      fail("exception should have been thrown");
    } catch (ConstraintViolationException e) {      
    }

    root.remove("childNode");
    ticket.save();
  }

}
