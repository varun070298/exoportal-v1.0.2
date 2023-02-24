/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing.fs;


import java.io.ByteArrayInputStream;
import java.util.GregorianCalendar;

import javax.jcr.DateValue;
import javax.jcr.LongValue;
import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.ReferenceValue;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.exoplatform.services.jcr.api.writing.AddPropertyTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 30 juil. 2004
 */
public class TestAddProperty extends AddPropertyTest{

  public void setUp() throws Exception {
    super.setUp();
    repository = (Repository) repositoryService.getRepository("fs1");    
    ticket = repository.login(credentials, "ws");
    initRepository();
  }

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.addNode("propertyDefNode", "nt:propertyDef");
    root.addNode("childNodeDefNode", "nt:childNodeDef");
//    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    ticket = repository.login(credentials, WORKSPACE);
    Node root = ticket.getRootNode();
    root.remove("propertyDefNode");
    root.remove("childNodeDefNode");
//    ticket.save();
  }

  public void testSetPropertyNameValue() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("propertyDefNode");

    try {
      node.setProperty("jcr:multiple", new LongValue(20));
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }
    ticket.revert();

    node.setProperty("jcr:defaultValue", new ReferenceValue("/a/b/c"));
    assertEquals(PropertyType.REFERENCE, node.getProperty("jcr:defaultValue").getValue().getType());

//    node.save(false);
//    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("propertyDefNode");
    assertEquals("/a/b/c", node.getProperty("jcr:defaultValue").getString());
  }

  public void testSetPropertyNameValueType() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("propertyDefNode");
    try {
      node.setProperty("jcr:multiple", new StringValue("not"), PropertyType.LONG);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    try {
      node.setProperty("jcr:autoCreate", new DateValue(new GregorianCalendar()), PropertyType.DATE);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }
    ticket.revert();

    node.setProperty("jcr:defaultValue", new StringValue("10"), PropertyType.LONG);
    assertEquals(PropertyType.LONG, node.getProperty("jcr:defaultValue").getValue().getType());
    assertEquals(10, node.getProperty("jcr:defaultValue").getLong());
//    node.save(false);
//    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("propertyDefNode");
    assertEquals(10, node.getProperty("jcr:defaultValue").getLong());
  }

  public void testSetPropertyNameValuesType() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNodeDefNode");
    Value[] values = {new StringValue("not"), new StringValue("in")};
    try {
      node.setProperty("jcr:requiredPrimaryTypes", values, PropertyType.LONG);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }
    try {
      node.setProperty("jcr:onParentVersion", values, PropertyType.STRING);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    node.setProperty("jcr:requiredPrimaryTypes", values, PropertyType.STRING);
//    node.save(false);

//    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("childNodeDefNode");
    assertEquals(2, node.getProperty("jcr:requiredPrimaryTypes").getValues().length);
  }

  public void testSetPropertyNameStringValueType() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("propertyDefNode");
    try {
      node.setProperty("jcr:multiple", "not", PropertyType.LONG);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }
    ticket.revert();

    node.setProperty("jcr:defaultValue", "10", PropertyType.LONG);
    assertEquals(PropertyType.LONG, node.getProperty("jcr:defaultValue").getValue().getType());
    assertEquals(10, node.getProperty("jcr:defaultValue").getLong());
//    node.save(false);
//    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("propertyDefNode");
    assertEquals(10, node.getProperty("jcr:defaultValue").getLong());
  }

  public void testSetPropertyNameStringValuesType() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("childNodeDefNode");
    String[] values = {"not", "in"};
    try {
      node.setProperty("jcr:requiredPrimaryTypes", values, PropertyType.LONG);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }
    try {
      node.setProperty("jcr:onParentVersion", values, PropertyType.STRING);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    node.setProperty("jcr:requiredPrimaryTypes", values, PropertyType.STRING);
//    node.save(false);

//    ticket = repository.login(credentials, WORKSPACE);
    node = ticket.getRootNode().getNode("childNodeDefNode");
    assertEquals(2, node.getProperty("jcr:requiredPrimaryTypes").getValues().length);
  }

  public void testSetPropertyNameTypedValue() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node node = root.getNode("propertyDefNode");

    node.setProperty("jcr:defaultValue", "default");
    node.setProperty("jcr:defaultValue", new ByteArrayInputStream(new String("default").getBytes()));
    node.setProperty("jcr:defaultValue", true);
    node.setProperty("jcr:defaultValue", new GregorianCalendar());
    node.setProperty("jcr:defaultValue", 20D);
    node.setProperty("jcr:defaultValue", 20L);

    try {
      node.setProperty("jcr:multiple", 20D);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {        
    }
  }

}
