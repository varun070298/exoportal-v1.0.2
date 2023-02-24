/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 25 juil. 2004
 */
public class ItemTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file;
//    if(!root.hasNode("childNode/childNode2"))
       file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
//    else
//        file = root.getNode("childNode/childNode2");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();
  }

  public void testGetAncestor() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");

    Item item = property.getAncestor(0);
    assertEquals(root.getPath(), item.getPath());

    item = property.getAncestor(1);
    assertEquals("/childNode", item.getPath());
    assertEquals("childNode", item.getName());
    assertTrue(item instanceof Node);

    item = property.getAncestor(2);
    assertEquals("/childNode/childNode2", item.getPath());
    assertEquals("childNode2", item.getName());
    assertTrue(item instanceof Node);

    item = property.getAncestor(4);
    assertEquals(property, item);
    assertTrue(item instanceof Property);

    try {
      item = property.getAncestor(5);
      fail("exception should have been thrown");
    } catch (ItemNotFoundException e) {
    }
  }

  public void testGetName() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");

    Item item = property.getAncestor(0);
    assertEquals("", item.getName());

    item = property.getAncestor(1);
    assertEquals("childNode", item.getName());
  }

  public void testGetParent() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    Item item = property.getAncestor(4);
    assertEquals("jcr:content", item.getParent().getName());
  }

  public void testGetPath() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    Item item = property.getAncestor(3);
    assertEquals("/childNode/childNode2/jcr:content", item.getPath());
  }

  public void testGetParents() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    NodeIterator nodeIterator = property.getParents();
    System.out.println("ITRATOR >>>>>>>"+nodeIterator.getSize());
    Node node = nodeIterator.nextNode();
    assertEquals("jcr:content", node.getName());
  }

  public void testGetPaths() throws RepositoryException {
    Node root = ticket.getRootNode();
    System.out.println("UUID >>>>>>>"+root.getNode("childNode/childNode2/jcr:content").getUUID());
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    Item item = property.getAncestor(3);
    StringIterator stringIterator = item.getPaths();
    assertTrue("Size==0", stringIterator.getSize()>0);
    assertTrue("Size==0", item.getPaths().getSize()>0);
//    System.out.println("ITEM >>>>>>>"+stringIterator.nextString());
    assertEquals("/childNode/childNode2/jcr:content", stringIterator.nextString());
  }

  public void testGetDepths() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertEquals(0, root.getDepth());
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    assertEquals(4, property.getDepth());
  }

  public void testGetTicket() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    assertEquals(ticket, property.getTicket());
  }

  public void testIsNode() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertTrue(root.isNode());
    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    assertFalse(property.isNode());
  }

}
