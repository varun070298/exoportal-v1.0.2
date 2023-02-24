/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.writing.fs;


import java.io.ByteArrayInputStream;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.exoplatform.services.jcr.api.writing.ValueTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 aout 2004
 */
public class TestValue extends ValueTest{

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

  public void testSetValue() throws RepositoryException {
    Node root = ticket.getRootNode();
    Property property = root.getNode("propertyDefNode").getProperty("jcr:defaultValue");

    property.setValue(new StringValue("haha"));
    property.setValue("default");
    property.setValue(new ByteArrayInputStream(new String("default").getBytes()));
    property.setValue(true);
    property.setValue(new GregorianCalendar());
    property.setValue(20D);
    property.setValue(20L);

    Value[] values = {new StringValue("not"), new StringValue("in")};
    try {
      property.setValue(values);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    property = root.getNode("propertyDefNode").getProperty("jcr:multiple");
    try {
      property.setValue(20D);
      fail("exception should have been thrown");
    } catch (ValueFormatException e) {
    }

    property = root.getNode("childNodeDefNode").getProperty("jcr:requiredPrimaryTypes");
    property.setValue(values);

//    root.save(false);

//    ticket = repository.login(credentials, WORKSPACE);
    Node node = ticket.getRootNode().getNode("childNodeDefNode");
    assertEquals(2, node.getProperty("jcr:requiredPrimaryTypes").getValues().length);

  }

}
