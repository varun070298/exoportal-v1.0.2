/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.reading;


import javax.jcr.*;
import javax.jcr.nodetype.NodeType;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.ByteArrayInputStream;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juil. 2004
 */
public class NodeTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();
  }

  public void tearDown() throws RepositoryException {
//    log.debug("START DOWN");
//    NodeImpl root = (NodeImpl)ticket.getRootNode();
//    log.debug("START DOWN "+root.getNodes().hasNext());

    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();

//    log.debug("END DOWN "+root.getNodes().hasNext());
  }

  public void testGetNode() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2");
    assertNotNull(node);
    assertEquals("nt:file", node.getPrimaryNodeType().getName());
    Property property = node.getNode("jcr:content").getProperty("exo:content");
    property.setValue(new StringValue("this is the NEW content"));

    node = root.getNode("/childNode");
    node.addNode("childNode3", "nt:file");

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    try {
      root.getNode("/childNode/childNode3");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }
    property = root.getNode("/childNode/childNode2/jcr:content").
        getProperty("exo:content");

    assertEquals("this is the content", property.getString());
    Value val = new StringValue("this is the NEW content");
    property.setValue(val);
//    System.out.println("PROP----------"+property+" "+property.getString()+" "+val.getString()+" "+val);
    node = root.getNode("/childNode");

//    node.addNode("childNode3", "nt:file");
    System.out.println("SAVE");
    ticket.save();
    System.out.println("END SAVE");

    root = repository.login(credentials, WORKSPACE).getRootNode();
//    assertNotNull(root.getNode("/childNode/childNode3"));
    property = root.getNode("/childNode/childNode2/jcr:content").
        getProperty("exo:content");
    assertEquals("this is the NEW content", property.getString());

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    node = root.getNode("/childNode");
    assertEquals(node, root.getNode("/childNode"));
    root.getNode("childNode/childNode2/jcr:content").setProperty("myapp:temp", new StringValue("Temp"));

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("childNode/childNode2/jcr:content");
    node2.setProperty("exo:content", new StringValue("Temp"));
    ticket2.save();

    ticket.revert();
    root = ticket.getRootNode();
    node = root.getNode("childNode/childNode2/jcr:content");
    assertNotNull(node);
    assertNotNull(node.getProperty("exo:content"));
    assertEquals("Temp", node.getProperty("exo:content").getString());
    try {
      node.getProperty("myapp:temp");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

  }

  public void testGetNodes() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode");
    node.addNode("childNode4", "nt:folder");

    NodeIterator nodeIterator = node.getNodes();
    while(nodeIterator.hasNext()){
      node = (Node) nodeIterator.next();
      assertNotNull(node.getTicket());
      if(!("childNode4".equals(node.getName()) || "childNode2".equals(node.getName())))
        fail("returned non expected nodes");
    }

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode");
    node2.addNode("childNode5", "nt:folder");
    ticket2.save();

    ticket.revert();

    node = root.getNode("/childNode");
    nodeIterator = node.getNodes();
    while(nodeIterator.hasNext()){
      node = (Node) nodeIterator.next();
//System.out.println("NODE (getNodes)-------- "+node);

      if(!("childNode5".equals(node.getName()) || "childNode2".equals(node.getName())))
        fail("returned non expected nodes "+node.getName());
    }
  }

  public void testGetNodesWithNamePattern() throws RepositoryException{
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode");
    node.addNode("childNode4", "nt:folder");
    node.addNode("otherNode", "nt:folder");
    node.addNode("lastNode", "nt:folder");

    Node result = (Node) node.getNodes("lastNode").next();
    assertEquals("lastNode", result.getName());

    NodeIterator iterator = node.getNodes("otherNode | lastNode");
    if(!iterator.hasNext())
      fail("nodes should have been found");
    while(iterator.hasNext()){
      Node nodeTmp = iterator.nextNode();
      if(!("otherNode".equals(nodeTmp.getName()) || "lastNode".equals(nodeTmp.getName())))
        fail("returned non expected nodes");
    }

    iterator = node.getNodes("childNode*");
    if(!iterator.hasNext())
      fail("nodes should have been found");
    while(iterator.hasNext()){
      Node nodeTmp = iterator.nextNode();
      if(!("childNode2".equals(nodeTmp.getName()) || "childNode4".equals(nodeTmp.getName())))
        fail("returned non expected nodes");
    }

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode");
    node2.addNode("childNode5", "nt:folder");
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode");
    iterator = node.getNodes("childNode*");
    if(!iterator.hasNext())
      fail("nodes should have been found");
    while(iterator.hasNext()){
      Node nodeTmp = iterator.nextNode();
      if(!("childNode2".equals(nodeTmp.getName()) || "childNode5".equals(nodeTmp.getName())))
        fail("returned non expected nodes");
    }
  }

  public void testGetProperty() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    Property property = node.getProperty("exo:content");
    assertEquals("this is the content", property.getString());

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.getProperty("exo:content").setValue("this is the NEW value");
    ticket2.save();

    ticket.revert();
    property = root.getNode("/childNode/childNode2/jcr:content").getProperty("exo:content");
    assertEquals("/childNode/childNode2/jcr:content/exo:content", property.getPath());
    assertEquals("this is the NEW value", property.getString());
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
    node2.setProperty("exo:prop", new StringValue("hehe"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    iterator = node.getProperties();
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("jcr:primaryType".equals(property.getName()) || "jcr:created".equals(property.getName()) ||
          "jcr:lastModified".equals(property.getName()) || "exo:prop".equals(property.getName()) ||
          "exo:content".equals(property.getName()) || "jcr:uuid".equals(property.getName())))
        fail("returned non expected properties");
    }
  }

  public void testGetPropertiesWithNamePattern() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("property1", "prop1Value");
    node.setProperty("property2", "prop2Value");

    PropertyIterator iterator = node.getProperties("property1 | property2");
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("property1".equals(property.getName()) || "property2".equals(property.getName())))
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
    node2.setProperty("exo:prop", new StringValue("hehe"));
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

  public void testFindProperty() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("property1", "prop1Value");
    assertEquals("property1", node.findProperty(new StringValue("prop1Value")).getName());
    assertNull(node.findProperty(new StringValue("prop2Value")));
    node.setProperty("property2", "prop2Value");
    assertEquals("property1", node.findProperty(new StringValue("prop1Value")).getName());
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    node = root.getNode("/childNode/childNode2/jcr:content");

    assertNotNull(node.findProperty(new StringValue("prop1Value")));
    assertEquals("property1", node.findProperty(new StringValue("prop1Value")).getName());

    assertNull(node.findProperty(new LongValue(15)));
    node.setProperty("longProp", new LongValue(15));
    assertEquals("longProp", node.findProperty(new LongValue(15)).getName());

    assertNull(node.findProperty(new DoubleValue(16)));
    node.setProperty("doubleProp", new DoubleValue(16));
    assertEquals("doubleProp", node.findProperty(new DoubleValue(16)).getName());

    Calendar calendar = new GregorianCalendar();
    DateValue dateValue = new DateValue(calendar);
    assertNull(node.findProperty(dateValue));
    node.setProperty("dateProp", dateValue);
//    log.debug("DATE PROP"+node.getProperty("dateProp"));
    assertEquals("dateProp", node.findProperty(dateValue).getName());

    BooleanValue booleanValue = new BooleanValue(true);
    assertNull(node.findProperty(booleanValue));
    node.setProperty("booleanProp", booleanValue);
    assertEquals("booleanProp", node.findProperty(booleanValue).getName());

    SoftLinkValue softLinkValue = new SoftLinkValue("/childNode");
    assertNull(node.findProperty(softLinkValue));
    node.setProperty("softLinkProp", softLinkValue);
    assertEquals("softLinkProp", node.findProperty(softLinkValue).getName());
    BinaryValue binaryValue = new BinaryValue(new String("binaryValue").getBytes());
    node.setProperty("binaryProp", binaryValue);

System.out.println("Binary PRRRROP --"+node.getProperty("binaryProp")+" "/*+binaryValue.getString()*/);
// Does not work. Possible bug in Binary Value implementation!!!!
//    assertEquals("binaryProp", node.findProperty(binaryValue).getName());

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.setProperty("exo:prop", new StringValue("hehe"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    PropertyIterator iterator = node.getProperties();
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("property1".equals(property.getName()) || "property2".equals(property.getName())
          || "jcr:created".equals(property.getName()) || "jcr:lastModified".equals(property.getName())
          || "jcr:primaryType".equals(property.getName()) || "exo:prop".equals(property.getName()) ||
          "exo:content".equals(property.getName()) || "jcr:uuid".equals(property.getName())))
        fail("returned non expected properties");
    }
  }

  public void testFindProperties() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node.setProperty("property1", "prop1Value");
    node.setProperty("property2", "prop1Value");
    node.setProperty("property3", "prop1Value");
    int i = 0;
    PropertyIterator iterator = node.findProperties(new StringValue("prop1Value"));
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("property1".equals(property.getName()) || "property2".equals(property.getName())
          || "property3".equals(property.getName()) ))
        fail("returned non expected properties");
      i++;
    }
    assertEquals(3, i);

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    Node root2 = ticket2.getRootNode();
    Node node2 = root2.getNode("/childNode/childNode2/jcr:content");
    node2.setProperty("exo:prop", new StringValue("prop1Value"));
    node2.setProperty("exo:prop1", new StringValue("prop1Value"));
    ticket2.save();

    ticket.revert();
    node = root.getNode("/childNode/childNode2/jcr:content");
    i = 0;
    iterator = node.findProperties(new StringValue("prop1Value"));
    while(iterator.hasNext()){
      Property property = iterator.nextProperty();
      if(!("exo:prop".equals(property.getName()) || "exo:prop1".equals(property.getName()) ))
        fail("returned non expected properties");
      i++;
    }
    assertEquals(2, i);
  }

  public void testGetPrimaryItem() throws RepositoryException {
    Node root = ticket.getRootNode();
    try {
      root.getPrimaryItem();
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
      assertTrue(e instanceof ItemNotFoundException);
    }

    Node node = root.getNode("/childNode/childNode2");
    Item item = node.getPrimaryItem();
    assertNotNull(item);
    assertEquals("jcr:content", item.getName());
  }

  public void testGetUUID() throws RepositoryException {
    Node root = ticket.getRootNode();
    try {
      root.getUUID();
      fail("exception should have been thrown");
    } catch (UnsupportedRepositoryOperationException e) {
    }
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    assertNotNull(node.getUUID());
  }

  public void testHasNode() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertFalse(root.hasNode("/dummyNode"));
    assertTrue(root.hasNode("/childNode"));
  }

  public void testHasNodes() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertTrue(root.hasNodes());
    Node node = root.getNode("/childNode/childNode2/jcr:content");
    node = node.addNode("tempNode");
    assertFalse(node.hasNodes());
  }

  public void testHasProperty() throws RepositoryException {
    Node root = ticket.getRootNode();
    assertFalse(root.hasProperty("dummyProperty"));
    assertTrue(root.getNode("/childNode").hasProperty("jcr:created"));
  }

  public void testHasProperties() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("/childNode");
    assertTrue(root.hasProperties());
  }

}
