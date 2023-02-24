/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading.fs;


import javax.jcr.DoubleValue;
import javax.jcr.LongValue;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import javax.jcr.Ticket;

import org.exoplatform.services.jcr.api.reading.NodeTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 21 juil. 2004
 */
public class TestNode extends NodeTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("fs1");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }
  public void testGetProperties() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode");

    PropertyIterator iterator = node.getProperties();
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("jcr:primaryType".equals(property.getName()) || "jcr:created".equals(property.getName()) ||
          "jcr:lastModified".equals(property.getName())))
        fail("returned non expected nodes");
    }

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.setProperty("exo:content", new StringValue("hehe"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    iterator = node.getProperties();
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("jcr:primaryType".equals(property.getName()) || "jcr:created".equals(property.getName()) ||
          "jcr:lastModified".equals(property.getName()) ||
          "exo:content".equals(property.getName()) || "jcr:uuid".equals(property.getName())))
        fail("returned non expected properties");
    }
  }

  public void testGetPropertiesWithNamePattern() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("exo:content", "prop1Value");
//    node.setProperty("property2", "prop2Value");

    PropertyIterator iterator = node.getProperties("property1 | property2");
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("exo:content".equals(property.getName()) ))
        fail("returned non expected properties");
    }

    iterator = node.getProperties("property1 | jcr:*");
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("property1".equals(property.getName()) || "jcr:primaryType".equals(property.getName())
          || "jcr:created".equals(property.getName()) || "jcr:lastModified".equals(property.getName()) ||
          "exo:content".equals(property.getName()) || "jcr:uuid".equals(property.getName()) ))
        fail("returned non expected properties");
    }

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.setProperty("exo:content", new StringValue("hehe"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    iterator = node.getProperties("exo:* | property1");
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("property1".equals(property.getName()) || "exo:prop".equals(property.getName()) ||
          "exo:content".equals(property.getName()) ))
        fail("returned non expected properties");
    }
  }

  public void testFindProperties() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("exo:content", "prop1Value");
    int i = 0;
    PropertyIterator iterator = node.findProperties(new StringValue("prop1Value"));
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if( !"exo:content".equals(property.getName()) )
        fail("returned non expected properties");
      i++;
    }
    assertEquals(1, i);
  }

  public void testFindProperty() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("exo:content", "prop1Value");
    assertEquals("exo:content", node.findProperty(new StringValue("prop1Value")).getName());
    assertNull(node.findProperty(new StringValue("prop2Value")));
//    node.setProperty("property2", "prop1Value");
//    assertEquals("property1", node.findProperty(new StringValue("prop1Value")).getName());
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    node = root.getNode("/childNode/childNode2/jcr:content");
    assertNotNull(node.findProperty(new StringValue("prop1Value")));
    assertEquals("exo:content", node.findProperty(new StringValue("prop1Value")).getName());

    assertNull(node.findProperty(new LongValue(15)));
    node.setProperty("longProp", new LongValue(15));
    assertEquals("longProp", node.findProperty(new LongValue(15)).getName());

    assertNull(node.findProperty(new DoubleValue(16)));
    node.setProperty("doubleProp", new DoubleValue(16));
    assertEquals("doubleProp", node.findProperty(new DoubleValue(16)).getName());

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.setProperty("exo:content", new StringValue("hehe"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    PropertyIterator iterator = node.getProperties();
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!( "exo:content".equals(property.getName()) 
          || "jcr:created".equals(property.getName()) || "jcr:lastModified".equals(property.getName())
          || "jcr:primaryType".equals(property.getName()) || "jcr:uuid".equals(property.getName())  ) )
        fail("returned non expected properties");
    }
  }

  public void testHasNodes() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertTrue(root.hasNodes());
  }

}
